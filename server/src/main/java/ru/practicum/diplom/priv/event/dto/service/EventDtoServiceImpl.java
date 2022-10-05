package ru.practicum.diplom.priv.event.dto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.priv.event.dto.EventShortDto;
import ru.practicum.diplom.priv.ratings.model.EventRating;
import ru.practicum.diplom.priv.ratings.repository.EventRatingRepository;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.stat.StatClient;

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
    private final StatClient statClient;
    private final EventRatingRepository eventRatingRepository;

    @Override
    public <T extends EventShortDto> T fillAdditionalInfo(T eventDto) {
        Map<Long, Integer> infos = getConfirmedRequestsInfo(List.of(eventDto.getId()));
        Map<Long, Long> ratings = getEventRating(List.of(eventDto.getId()));
        Map<Long, Long> initiatorRatings = getEventInitiatorRating(List.of(eventDto.getId()));
        Map<Long, Long> views = statClient.getEventViews(List.of(eventDto.getId()));

        return setEventFields(eventDto, infos, views, ratings, initiatorRatings);
    }

    @Override
    public <T extends EventShortDto> List<T> fillAdditionalInfo(List<T> eventDto) {
        List<Long> ids = eventDto.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        Map<Long, Integer> infos = getConfirmedRequestsInfo(ids);
        Map<Long, Long> ratings = getEventRating(ids);
        Map<Long, Long> initiatorRatings = getEventInitiatorRating(ids);
        Map<Long, Long> views = statClient.getEventViews(ids);

        eventDto.forEach(x -> setEventFields(x, infos, views, ratings, initiatorRatings));

        return eventDto;
    }

    private <T extends EventShortDto> T setEventFields(
            T eventDto,
            Map<Long, Integer> infos,
            Map<Long, Long> views,
            Map<Long, Long> ratings,
            Map<Long, Long> initiatorRatings) {
        eventDto.setConfirmedRequests(
                infos.getOrDefault(eventDto.getId(), 0)
        );
        eventDto.setViews(
                views.getOrDefault(eventDto.getId(), 0L)
        );
        eventDto.setEventRating(
                ratings.getOrDefault(eventDto.getId(), 0L)
        );
        eventDto.setEventInitiatorRating(
                initiatorRatings.getOrDefault(eventDto.getInitiator().getId(), 0L)
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

        return infos
                .stream()
                .collect(Collectors.toMap(ConfirmedRequestsInfo::getEventId, ConfirmedRequestsInfo::getCountRequests));
    }

    private ConfirmedRequestsInfo makeConfirmedRequestsInfo(ResultSet rs) throws SQLException {
        ConfirmedRequestsInfo info = new ConfirmedRequestsInfo();
        info.setEventId(rs.getLong("eventId"));
        info.setCountRequests(rs.getInt("countRequests"));

        return info;
    }

    private Map<Long, Long> getEventRating(List<Long> ids) {
        List<EventRating> ratingsList = eventRatingRepository.findEventRatingsByEvents(ids);
        Map<Long, Long> ratings = new HashMap<>();
        ratingsList.forEach(x -> ratings.put(x.getEvent().getId(), x.getLikesCount() - x.getDislikesCount()));
        return ratings;
    }

    private Map<Long, Long> getEventInitiatorRating(List<Long> ids) {
        String sqlQuery = "SELECT ev.initiator as initiator, count(er.likes_count - er.dislikes_count) as rating\n" +
                "from event_ratings er\n" +
                "INNER join events ev ON er.event = ev.id\n" +
                "Where er.event in (:ids)\n" +
                "Group by ev.initiator";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ids", ids);
        List<InitiatorRating> ratingList = jdbcTemplate.query(sqlQuery, paramSource,
                (rs, rowNum) -> makeInitiatorRating(rs));

        Map<Long, Long> ratings = new HashMap<>();
        ratingList.forEach(x -> ratings.put(x.getInitiatorId(), x.getRating()));

        return ratings;
    }

    private InitiatorRating makeInitiatorRating(ResultSet rs) throws SQLException {
        return new InitiatorRating(
                rs.getLong("initiator"),
                rs.getLong("rating")
        );
    }

    static class InitiatorRating {
        private final Long initiatorId;
        private final Long rating;

        public InitiatorRating(Long initiatorId, Long rating) {
            this.initiatorId = initiatorId;
            this.rating = rating;
        }

        public Long getInitiatorId() {
            return initiatorId;
        }

        public Long getRating() {
            return rating;
        }
    }
}
