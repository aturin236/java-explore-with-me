package ru.practicum.diplom.priv.event.dto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.exceptions.EventStatDtoInternalException;
import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.stat.StatClient;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventDtoServiceImpl implements EventDtoService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final StatClient statClient;

    @Override
    public <T extends EventShortDto> T fillAdditionalInfo(T eventDto) {
        Map<Long, Integer> infos = getConfirmedRequestsInfo(List.of(eventDto.getId()));
        Map<Long, Integer> views = getEventViews(List.of(eventDto.getId()));

        eventDto.setConfirmedRequests(
                infos.getOrDefault(eventDto.getId(), 0)
        );

        eventDto.setViews(
                views.getOrDefault(eventDto.getId(), 0)
        );

        return eventDto;
    }

    @Override
    public <T extends EventShortDto> List<T> fillAdditionalInfo(List<T> eventDto) {
        List<Long> ids = eventDto.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        Map<Long, Integer> infos = getConfirmedRequestsInfo(ids);
        Map<Long, Integer> views = getEventViews(ids);

        for (T x : eventDto) {
            x.setConfirmedRequests(
                    infos.getOrDefault(x.getId(), 0)
            );
            x.setViews(
                    views.getOrDefault(x.getId(), 0)
            );
        }

        return eventDto;
    }

    private Map<Long, Integer> getConfirmedRequestsInfo(List<Long> ids) {
        String sqlQuery = "select event as eventId, count(id) as countRequests " +
                "from participation_request pr " +
                "where pr.event in (:eventId) and pr.status = :status " +
                "group by pr.event";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("eventId", ids);
        paramSource.addValue("status", RequestStatus.CONFIRMED.name());

        List<ConfirmedRequestsInfo> infos = jdbcTemplate.query(sqlQuery, paramSource,
                (rs, rowNum) -> makeConfirmedRequestsInfo(rs));
        Map<Long, Integer> infosResult = new HashMap<>();

        infos.forEach(x -> infosResult.put(x.getEventId(), x.getCountRequests()));

        return infosResult;
    }

    private ConfirmedRequestsInfo makeConfirmedRequestsInfo(ResultSet rs) throws SQLException {
        ConfirmedRequestsInfo info = new ConfirmedRequestsInfo();
        info.setEventId(rs.getLong("eventId"));
        info.setCountRequests(rs.getInt("countRequests"));

        return info;
    }

    private Map<Long, Integer> getEventViews(List<Long> ids) {
        String suffixUri = "/events/";
        Object object;
        try {
            object = statClient.getStats(
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

        Map<Long, Integer> views = new HashMap<>();
        try {
            ResponseEntity<Object> response = (ResponseEntity<Object>) object;
            List<LinkedHashMap<String, Object>> body = (List<LinkedHashMap<String, Object>>) response.getBody();
            for (LinkedHashMap<String, Object> view : body) {
                String idEventFromUri = ((String) view.get("uri")).replace(suffixUri, "");
                views.put(Long.parseLong(idEventFromUri), (Integer) view.get("hits"));
            }
        } catch (Exception e) {
            throw new EventStatDtoInternalException("Не удалось разобрать ответ сервиса статистики");
        }

        return views;
    }
}
