package ru.practicum.diplom.priv.event.dto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.priv.request.RequestStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventDtoServiceImpl implements EventDtoService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public <T extends EventShortDto> T addConfirmedRequests(T eventDto) {
        Map<Long, Integer> infos = getConfirmedRequestsInfo(List.of(eventDto.getId()));

        eventDto.setConfirmedRequests(
                infos.getOrDefault(eventDto.getId(), 0)
        );

        return eventDto;
    }

    @Override
    public <T extends EventShortDto> List<T> addConfirmedRequests(List<T> eventDto) {
        Map<Long, Integer> infos = getConfirmedRequestsInfo(
                eventDto.stream()
                        .map(EventShortDto::getId)
                        .collect(Collectors.toList())
        );
        eventDto.forEach(
                x -> x.setConfirmedRequests(
                        infos.getOrDefault(x.getId(), 0)
                )
        );

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
}
