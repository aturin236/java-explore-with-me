package ru.practicum.diplom.priv.request.dto;

import ru.practicum.diplom.priv.request.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    public static RequestDto requestToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .build();
    }

    public static List<RequestDto> requestToDto(Iterable<Request> requests) {
        List<RequestDto> dtos = new ArrayList<>();
        requests.forEach(x -> dtos.add(requestToDto(x)));

        return dtos;
    }
}
