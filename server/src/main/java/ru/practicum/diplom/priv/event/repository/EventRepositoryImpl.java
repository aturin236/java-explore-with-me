package ru.practicum.diplom.priv.event.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.EventState;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final SessionFactory sessionFactory;

    @Override
    public List<Event> findEventsByParam(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch("category", JoinType.LEFT);
        root.fetch("initiator", JoinType.LEFT);
        cq.select(root);

        Predicate predicate = cb.conjunction();

        if (states.size() > 0) {
            Predicate p = root.get("state").in(states);
            predicate = cb.and(predicate, p);
        }

        if (users.size() > 0) {
            Predicate p = root.get("initiator").in(users);
            predicate = cb.and(predicate, p);
        }

        if (categories.size() > 0) {
            Predicate p = root.get("category").in(categories);
            predicate = cb.and(predicate, p);
        }

        Predicate p = cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
        predicate = cb.and(predicate, p);

        p = cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
        predicate = cb.and(predicate, p);

        cq.where(predicate);
        List<Event> results = session.createQuery(cq)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        session.close();

        return results;
    }

    @Override
    public List<Event> findEventsByParam(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch("category", JoinType.LEFT);
        root.fetch("initiator", JoinType.LEFT);
        cq.select(root);

        Predicate predicate = cb.conjunction();

        Predicate p = cb.equal(root.get("state"), EventState.PUBLISHED);
        predicate = cb.and(predicate, p);

        if (text != null) {
            text = "%" + text.toLowerCase() + "%";
            Predicate pAnnotation = cb.like(cb.lower(root.get("annotation")), text);
            Predicate pDescription = cb.like(cb.lower(root.get("description")), text);
            pAnnotation = cb.or(pAnnotation, pDescription);
            predicate = cb.and(predicate, pAnnotation);
        }

        if (rangeStart == null && rangeEnd == null) {
            p = cb.greaterThan(root.get("eventDate"), LocalDateTime.now());
            predicate = cb.and(predicate, p);
        } else {
            if (rangeStart != null) {
                p = cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
                predicate = cb.and(predicate, p);
            }
            if (rangeEnd != null) {
                p = cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
                predicate = cb.and(predicate, p);
            }
        }

        if (categories != null && categories.size() > 0) {
            p = root.get("category").in(categories);
            predicate = cb.and(predicate, p);
        }

        if (paid != null) {
            p = cb.equal(root.get("paid"), paid);
            predicate = cb.and(predicate, p);
        }

        cq.where(predicate);

        cq.orderBy(cb.asc(root.get("eventDate")));

        List<Event> results = session.createQuery(cq).getResultList();

        session.close();

        return results;
    }
}
