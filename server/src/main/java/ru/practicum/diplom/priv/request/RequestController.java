package ru.practicum.diplom.priv.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.priv.request.dto.RequestDto;
import ru.practicum.diplom.priv.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto saveRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        return requestService.saveRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return requestService.cancelRequest(userId, requestId);
    }
}
