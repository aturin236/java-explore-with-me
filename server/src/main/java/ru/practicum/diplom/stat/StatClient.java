package ru.practicum.diplom.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.diplom.exceptions.EventStatDtoInternalException;
import ru.practicum.diplom.stat.client.BaseClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatClient extends BaseClient {
    public static final String APP_NAME = "explore with me";

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public Object save(EventStatDto eventStatDto) {
        return post("/hit", eventStatDto);
    }

    public Object getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            boolean unique
    ) throws UnsupportedEncodingException {
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(start.toString(), StandardCharsets.UTF_8.toString()),
                "end", URLEncoder.encode(end.toString(), StandardCharsets.UTF_8.toString()),
                "uris", uris,
                "unique", unique
        );

        return get("/stats?start=" + parameters.get("start")
                + "&end=" + parameters.get("end")
                + "&uris=" + String.join(", ", uris)
                + "&unique=" + unique, parameters);
    }

    public Map<Long, Long> getEventViews(List<Long> ids) {
        String suffixUri = "/events/";
        Object object;
        try {
            object = getStats(
                    LocalDateTime.now().minusYears(1),
                    LocalDateTime.now(),
                    ids.stream()
                            .map(x -> suffixUri + x)
                            .collect(Collectors.toList()),
                    true
            );
        } catch (UnsupportedEncodingException e) {
            throw new EventStatDtoInternalException("Не удалось получить данные статистики");
        }

        Map<Long, Long> views = new HashMap<>();
        try {
            ResponseEntity<Object> response = (ResponseEntity<Object>) object;
            List<LinkedHashMap<String, Object>> body = (List<LinkedHashMap<String, Object>>) response.getBody();
            for (LinkedHashMap<String, Object> view : body) {
                String idEventFromUri = ((String) view.get("uri")).replace(suffixUri, "");
                views.put(Long.parseLong(idEventFromUri), (Long) view.get("hits"));
            }
        } catch (Exception e) {
            throw new EventStatDtoInternalException("Не удалось разобрать ответ сервиса статистики");
        }

        return views;
    }
}
