package ru.practicum.diplom.priv.ratings.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.admin.user.repository.UserRepository;
import ru.practicum.diplom.exceptions.RatingForbiddenException;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.repository.EventRepository;
import ru.practicum.diplom.priv.ratings.model.EventLike;
import ru.practicum.diplom.priv.ratings.model.EventRating;
import ru.practicum.diplom.priv.ratings.repository.EventLikeRepository;
import ru.practicum.diplom.priv.ratings.repository.EventRatingRepository;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.priv.request.repository.RequestRepository;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventLikeRepository eventLikeRepository;
    private final EventRatingRepository eventRatingRepository;
    private final RequestRepository requestRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Override
    public void likeEvent(Long userId, Long eventId) {
        log.debug("Запрос likeEvent с userId - {} и eventId {}", userId, eventId);
        saveRating(userId, eventId, true);
    }

    @Override
    public void dislikeEvent(Long userId, Long eventId) {
        log.debug("Запрос dislikeEvent с userId - {} и eventId {}", userId, eventId);
        saveRating(userId, eventId, false);
    }

    private void saveRating(Long userId, Long eventId, boolean isLike) {
        EventLike eventLike = getVerifiedRating(userId, eventId, isLike);

        eventLikeRepository.save(eventLike);

        updateEventRatings(eventLike);
    }

    /**
     * Метод выполняет валидацию условий перед созданием нового объекта EventLike
     *
     * @param userId  - id пользователя, который лайкает событие
     * @param eventId - id события
     * @param isLike  - признак лайк/дизлайк
     * @return новый entity
     */
    private EventLike getVerifiedRating(Long userId, Long eventId, boolean isLike) {
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);
        User user = userRepository.checkAndReturnUserIfExist(userId);

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new RatingForbiddenException("Нельзя лайкать свое же событие");
        }

        if (requestRepository.findByRequesterAndEventAndStatus(user, event, RequestStatus.CONFIRMED)
                .isEmpty()) {
            throw new RatingForbiddenException("Нельзя лайкать событие, в котором не участвовал");
        }

        EventLike eventLike = eventLikeRepository
                .findByEventAndRequester(event, user)
                .orElseGet(() -> new EventLike(event, user));
        eventLike.setLike(isLike);

        return eventLike;
    }

    /**
     * Метод обновляет таблицу 'event_ratings'
     *
     * @param eventLike - событие, для которого будет пересчет лайков/дизлайков
     */
    private void updateEventRatings(EventLike eventLike) {
        entityManager.flush();

        String sqlQuery = "SELECT " +
                "SUM(CASE " +
                "WHEN is_like THEN 1 ELSE 0 END) as likes_count, " +
                "SUM(CASE " +
                "WHEN is_like THEN 0 ELSE 1 END) as dislikes_count " +
                "FROM public.event_likes " +
                "WHERE event = :eventId " +
                "GROUP BY event";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("eventId", eventLike.getEventId());

        List<EventRating> eventRatings = jdbcTemplate.query(sqlQuery, paramSource,
                (rs, rowNum) -> makeEventRating(rs, eventLike));

        if (eventRatings.size() == 0) {
            EventRating eventRating = eventRatingRepository.findEventRatingByEvent(eventLike.getEvent())
                    .orElseGet(() -> newEventRating(eventLike, 0L, 0L));

            eventRating.setLikesCount(0L);
            eventRating.setDislikesCount(0L);
            eventRatingRepository.save(eventRating);
        } else {
            EventRating eventRatingUpdate = eventRatings.get(0);
            eventRatingRepository.save(eventRatingUpdate);
        }
    }

    private EventRating makeEventRating(ResultSet rs, EventLike eventLike) throws SQLException {
        return newEventRating(
                eventLike,
                rs.getLong("likes_count"),
                rs.getLong("dislikes_count")
        );
    }

    private EventRating newEventRating(EventLike eventLike, long likesCount, long dislikesCount) {
        EventRating eventRating = new EventRating();
        eventRating.setEventId(eventLike.getEventId());
        eventRating.setEvent(eventLike.getEvent());
        eventRating.setLikesCount(likesCount);
        eventRating.setDislikesCount(dislikesCount);

        return eventRating;
    }
}
