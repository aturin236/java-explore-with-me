package ru.practicum.diplom.priv.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.category.repository.CategoryRepository;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.admin.user.repository.UserRepository;
import ru.practicum.diplom.exceptions.EventBadRequestException;
import ru.practicum.diplom.exceptions.EventForbiddenException;
import ru.practicum.diplom.exceptions.RequestForbiddenException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.EventMapper;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.dto.UpdateEventDto;
import ru.practicum.diplom.priv.event.dto.service.EventDtoService;
import ru.practicum.diplom.priv.event.repository.EventRepository;
import ru.practicum.diplom.priv.request.Request;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.priv.request.dto.RequestDto;
import ru.practicum.diplom.priv.request.dto.RequestMapper;
import ru.practicum.diplom.priv.request.repository.RequestRepository;
import ru.practicum.diplom.priv.request.service.RequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final RequestService requestService;
    private final EventDtoService eventDtoService;

    @Override
    public EventFullDto saveEvent(NewEventDto eventDto, Long userId) {
        log.debug("???????????? saveEvent ?? title - {} ?? userId {}", eventDto.getTitle(), userId);

        checkTimeOfEvent(eventDto);

        Event event = EventMapper.newEventDtoToEvent(eventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryRepository.checkAndReturnCategoryIfExist(eventDto.getCategory()));
        event.setInitiator(userRepository.checkAndReturnUserIfExist(userId));
        event.setState(EventState.PENDING);

        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(eventRepository.save(event));

        return eventDtoService.fillAdditionalInfo(eventFullDto);
    }

    @Override
    public EventFullDto updateEvent(UpdateEventDto eventDto, Long userId) {
        log.debug("???????????? updateEvent ?? title - {} ?? userId {}", eventDto.getTitle(), userId);

        checkTimeOfEvent(eventDto);

        String error = "???????????? ?????????????????????????? ?????????? ??????????????. ???????????????? %s";
        Event event = getVerifiedEvent(userId, eventDto.getEventId(), error);

        if (event.getState() == EventState.PUBLISHED) {
            throw new EventForbiddenException("?????????????? ?????? ????????????????????????");
        }

        eventDto.setFieldsToEvent(event);
        if (eventDto.getCategory() != null) {
            event.setCategory(
                    categoryRepository.checkAndReturnCategoryIfExist(eventDto.getCategory())
            );
        }

        return eventDtoService.fillAdditionalInfo(
                EventMapper.eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.debug("???????????? getEvent ?? userId - {} ?? eventId {}", userId, eventId);

        String error = "???????????? ???????????????? ?????????? ??????????????. ???????????????? %s";
        Event event = getVerifiedEvent(userId, eventId, error);

        if (event.getState() != EventState.PENDING) {
            throw new EventForbiddenException("?????????????? ???????????? ???????? ?? ?????????????????? PENDING");
        }

        event.setState(EventState.CANCELED);

        return eventDtoService.fillAdditionalInfo(
                EventMapper.eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public List<EventFullDto> getEvents(Long userId, int from, int size) {
        log.debug("???????????? getEvents ?? userId - {}", userId);

        User user = userRepository.checkAndReturnUserIfExist(userId);
        Pageable pageRequest = PageRequest.of(from, size);

        return eventDtoService.fillAdditionalInfo(
                EventMapper.eventToEventFullDto(
                        eventRepository.findEventsByInitiator(user, pageRequest)
                )
        );
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        log.debug("???????????? getEvent ?? userId - {} ?? eventId {}", userId, eventId);

        String error = "???????????? ???????????????? ?????????? ??????????????. ???????????????? %s";

        return eventDtoService.fillAdditionalInfo(
                EventMapper.eventToEventFullDto(
                        getVerifiedEvent(userId, eventId, error)
                )
        );
    }

    @Override
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        log.debug("???????????? getEventRequests ?? userId - {} ?? eventId {}", userId, eventId);

        String error = "???????????? ???????????????? ?????????? ??????????????. ???????????????? %s";

        return RequestMapper.requestToDto(
                requestRepository.findRequestsByEvent(
                        getVerifiedEvent(userId, eventId, error)
                )
        );
    }

    @Override
    public RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId) {
        log.debug("???????????? confirmEventRequest ?? userId - {} ?? eventId {} ?? reqId {}", userId, eventId, reqId);

        Request request = getVerifiedRequest(
                reqId,
                eventId,
                RequestStatus.CONFIRMED,
                "???????????? ?????? ??????????????????????"
        );

        String error = "???????????? ???????????????????????? ???????????? ???? ???????????? ??????????????. ???????????????? ?????????????? %s";
        Event event = getVerifiedEvent(userId, eventId, error);

        int requestCount = requestService.countRequestByEventOrThrowException(event);
        requestCount++;

        if (requestService.isExceededLimitOfRequests(requestCount, event.getParticipantLimit())) {
            rejectOpenRequest(event);
        }

        request.setStatus(RequestStatus.CONFIRMED);

        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId) {
        log.debug("???????????? rejectEventRequest ?? userId - {} ?? eventId {} ?? reqId {}", userId, eventId, reqId);

        String error = "???????????? ???????????????? ???????????? ???? ???????????? ??????????????. ???????????????? ?????????????? %s";
        getVerifiedEvent(userId, eventId, error);

        Request request = getVerifiedRequest(
                reqId,
                eventId,
                RequestStatus.REJECTED,
                "???????????? ?????? ??????????????"
        );

        request.setStatus(RequestStatus.REJECTED);

        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    private void checkTimeOfEvent(UpdateEventDto eventDto) {
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            String eventDate = eventDto.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            throw new EventBadRequestException(
                    String.format("?????????????? ???????????????????? %s. ?????? ?????????? ????????. ?????????? ???? ????????????", eventDate)
            );
        }
    }

    private Event getVerifiedEvent(Long userId, Long eventId, String error) {
        userRepository.checkAndReturnUserIfExist(userId);
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new EventForbiddenException(
                    String.format(error, event.getInitiator().getId())
            );
        }

        return event;
    }

    private Request getVerifiedRequest(Long reqId, Long eventId, RequestStatus status, String error) {
        Request request = requestRepository.checkAndReturnRequestIfExist(reqId);

        if (request.getStatus() == status) {
            throw new RequestForbiddenException(error);
        }

        if (!Objects.equals(request.getEvent().getId(), eventId)) {
            throw new RequestForbiddenException(
                    String.format("?? ???????????? ?????????????? ???????????? ?????????????? - %s", eventId)
            );
        }

        return request;
    }

    private void rejectOpenRequest(Event event) {
        List<Request> requests = requestRepository.findRequestsByEventAndStatus(event, RequestStatus.PENDING);

        requests.forEach(x -> x.setStatus(RequestStatus.REJECTED));

        requestRepository.saveAll(requests);
    }
}
