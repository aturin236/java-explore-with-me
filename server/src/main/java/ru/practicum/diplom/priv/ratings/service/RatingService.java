package ru.practicum.diplom.priv.ratings.service;

public interface RatingService {
    void likeEvent(Long userId, Long eventId);

    void dislikeEvent(Long userId, Long eventId);
}
