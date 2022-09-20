package ru.practicum.diplom.priv.request.service;

import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto saveRequest(Long userId, Long eventId);

    List<RequestDto> getRequests(Long userId);

    RequestDto cancelRequest(Long userId, Long requestId);

    /**
     * Метод проверяет достигнут ли лимит заявок на участие в событии
     *
     * @param event - событие, для которого получаются подтвержденные заявки
     * @return количество подтвержденных заявок на участие
     */
    int countRequestByEventOrThrowException(Event event);

    boolean isExceededLimitOfRequests(int requestCount, int limit);
}
