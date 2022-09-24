package ru.practicum.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.diplom.model.EventStat;

public interface EventStatRepository extends JpaRepository<EventStat, Long> {
}
