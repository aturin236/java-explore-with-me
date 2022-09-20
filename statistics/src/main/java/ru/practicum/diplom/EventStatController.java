package ru.practicum.diplom;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.diplom.dto.EventStatDto;
import ru.practicum.diplom.dto.EventStatDtoView;
import ru.practicum.diplom.service.EventStatService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventStatController {
    private final EventStatService eventStatService;

    @PostMapping("/hit")
    public void save(@RequestBody EventStatDto eventStatDto) {
        eventStatService.save(eventStatDto);
    }

    @GetMapping("/stats")
    public List<EventStatDtoView> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> uris,
            @RequestParam Boolean unique
    ) throws UnsupportedEncodingException {
        LocalDateTime startDate = LocalDateTime.parse(
                URLDecoder.decode(start, StandardCharsets.UTF_8.toString())
        );
        LocalDateTime endDate = LocalDateTime.parse(
                URLDecoder.decode(end, StandardCharsets.UTF_8.toString())
        );
        return eventStatService.getEventStats(
                startDate,
                endDate,
                uris,
                unique
        );
    }
}
