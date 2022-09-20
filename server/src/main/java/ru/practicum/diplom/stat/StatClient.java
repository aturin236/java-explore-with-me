package ru.practicum.diplom.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.diplom.stat.client.BaseClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {
    public static final String APP_NAME = "explore with me";

    @Autowired
    //public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
    public StatClient(@Value("http://localhost:9091") String serverUrl, RestTemplateBuilder builder) {//todo
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
}
