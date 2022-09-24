package ru.practicum.diplom.priv.ratings;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.diplom.priv.ratings.service.RatingService;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PatchMapping("/like")
    void likeEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        ratingService.likeEvent(userId, eventId);
    }

    @PatchMapping("/dislike")
    void dislikeEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        ratingService.dislikeEvent(userId, eventId);
    }
}
