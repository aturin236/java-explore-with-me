package ru.practicum.diplom.priv.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.admin.user.repository.UserRepository;
import ru.practicum.diplom.exceptions.RequestForbiddenException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;
import ru.practicum.diplom.priv.event.repository.EventRepository;
import ru.practicum.diplom.priv.request.Request;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.priv.request.dto.RequestDto;
import ru.practicum.diplom.priv.request.dto.RequestMapper;
import ru.practicum.diplom.priv.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDto saveRequest(Long userId, Long eventId) {
        log.debug("Запрос saveRequest userId {}, eventId {}", userId, eventId);

        User user = userRepository.checkAndReturnUserIfExist(userId);
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);

        if (requestRepository.findByRequesterAndEvent(user, event).isPresent()) {
            throw new RequestForbiddenException(
                    String.format("Запрос пользователя %s на событие %s уже добавлен", userId, eventId)
            );
        }

        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new RequestForbiddenException(
                    "Инициатор события не может добавить запрос на участие в своём событии"
            );
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new RequestForbiddenException(
                    "Нельзя участвовать в неопубликованном событии"
            );
        }

        countRequestByEventOrThrowException(event);

        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setStatus(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);

        return RequestMapper.requestToDto(
                requestRepository.save(request)
        );
    }

    @Override
    public List<RequestDto> getRequests(Long userId) {
        log.debug("Запрос getRequests по id {}", userId);
        return RequestMapper.requestToDto(
                requestRepository.findRequestsByRequester(
                        userRepository.checkAndReturnUserIfExist(userId)
                )
        );
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        log.debug("Запрос cancelRequest по userId {}, requestId {}", userId, requestId);
        userRepository.checkAndReturnUserIfExist(userId);
        Request request = requestRepository.checkAndReturnRequestIfExist(requestId);

        if (!Objects.equals(userId, request.getRequester().getId())) {
            throw new RequestForbiddenException(
                    String.format("Реквест %s не создавался пользователем %s", requestId, userId)
            );
        }

        if (request.getStatus() == RequestStatus.CANCELED) {
            throw new RequestForbiddenException("Запрос уже отменен");
        }

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    @Override
    public int countRequestByEventOrThrowException(Event event) {
        int requestCount = requestRepository.countRequestByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (requestCount >= event.getParticipantLimit()) {
            throw new RequestForbiddenException(
                    "Достигнут предел заявок на участие в событии"
            );
        }

        return requestCount;
    }
}
