package ru.practicum.diplom.rating;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.diplom.admin.category.Category;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.category.dto.CategoryMapper;
import ru.practicum.diplom.admin.event.service.EventAdminService;
import ru.practicum.diplom.admin.user.dto.UserDto;
import ru.practicum.diplom.admin.user.service.UserService;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.service.EventService;
import ru.practicum.diplom.priv.ratings.model.EventLike;
import ru.practicum.diplom.priv.ratings.model.EventRating;
import ru.practicum.diplom.priv.ratings.service.RatingService;
import ru.practicum.diplom.priv.request.service.RequestService;
import ru.practicum.diplom.stat.StatClient;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingServiceTest {
    private final EntityManager entityManager;
    private final RatingService ratingService;
    private final EventService eventService;
    private final UserService userService;
    private final RequestService requestService;
    private final EventAdminService eventAdminService;
    @MockBean
    private final StatClient statClient;

    private UserDto userDto;
    private UserDto userDto2;
    private UserDto userDto3;
    private UserDto userDto4;
    private NewEventDto eventDto;

    @BeforeEach
    void init() {
        userDto = UserDto.builder()
                .name("test")
                .email("test@yandex.ru")
                .build();
        userDto2 = UserDto.builder()
                .name("test2")
                .email("test2@yandex.ru")
                .build();
        userDto3 = UserDto.builder()
                .name("test3")
                .email("test3@yandex.ru")
                .build();
        userDto4 = UserDto.builder()
                .name("test4")
                .email("test4@yandex.ru")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .name("catTest")
                .build();
        Category category = CategoryMapper.toCategory(categoryDto);
        entityManager.persist(category);

        eventDto = new NewEventDto();
        eventDto.setTitle("test");
        eventDto.setDescription("test");
        eventDto.setAnnotation("test");
        eventDto.setEventDate(LocalDateTime.now().plusYears(1));
        eventDto.setPaid(true);
        eventDto.setParticipantLimit(0);
        eventDto.setRequestModeration(true);
        eventDto.setCategory(category.getId());
        eventDto.setRequestModeration(false);

        entityManager.flush();
    }

    @Test
    void getEventLikes() {
        Mockito.when(statClient.getEventViews(Mockito.any())).thenReturn(new HashMap<>());

        userDto = userService.saveUser(userDto);
        userDto2 = userService.saveUser(userDto2);
        userDto3 = userService.saveUser(userDto3);

        EventFullDto eventFullDto = eventService.saveEvent(eventDto, userDto.getId());

        eventAdminService.publishEvent(eventFullDto.getId());
        requestService.saveRequest(userDto2.getId(), eventFullDto.getId());
        ratingService.likeEvent(userDto2.getId(), eventFullDto.getId());

        TypedQuery<EventLike> query = entityManager.createQuery(
                "Select el from EventLike el where el.event.id = :id",
                EventLike.class);
        EventLike eventLike = query.setParameter("id", eventFullDto.getId()).getSingleResult();

        assertThat(eventLike.getId(), notNullValue());
        assertThat(eventLike.isLike(), equalTo(true));
    }

    @Test
    void getEventRating() {
        Mockito.when(statClient.getEventViews(Mockito.any())).thenReturn(new HashMap<>());

        userDto = userService.saveUser(userDto);
        userDto2 = userService.saveUser(userDto2);
        userDto3 = userService.saveUser(userDto3);
        userDto4 = userService.saveUser(userDto4);

        EventFullDto eventFullDto = eventService.saveEvent(eventDto, userDto.getId());
        eventAdminService.publishEvent(eventFullDto.getId());

        requestService.saveRequest(userDto2.getId(), eventFullDto.getId());
        ratingService.likeEvent(userDto2.getId(), eventFullDto.getId());

        requestService.saveRequest(userDto3.getId(), eventFullDto.getId());
        ratingService.likeEvent(userDto3.getId(), eventFullDto.getId());

        requestService.saveRequest(userDto4.getId(), eventFullDto.getId());
        ratingService.dislikeEvent(userDto4.getId(), eventFullDto.getId());

        TypedQuery<EventRating> query = entityManager.createQuery(
                "Select er from EventRating er where er.event.id = :id",
                EventRating.class);
        EventRating eventRating = query.setParameter("id", eventFullDto.getId()).getSingleResult();

        assertThat(eventRating.getId(), notNullValue());
        assertThat(eventRating.getCountLike(), equalTo(2L));
        assertThat(eventRating.getCountDislike(), equalTo(1L));
    }

    /**
     * Тест проверяет добавление сведений о рейтингах события и автора при получении событий
     */
    @Test
    void getRatingsInEvent() {
        Mockito.when(statClient.getEventViews(Mockito.any())).thenReturn(new HashMap<>());

        userDto = userService.saveUser(userDto);
        userDto2 = userService.saveUser(userDto2);
        userDto3 = userService.saveUser(userDto3);
        userDto4 = userService.saveUser(userDto4);

        EventFullDto eventFullDto = eventService.saveEvent(eventDto, userDto.getId());
        eventAdminService.publishEvent(eventFullDto.getId());

        requestService.saveRequest(userDto2.getId(), eventFullDto.getId());
        ratingService.likeEvent(userDto2.getId(), eventFullDto.getId());

        requestService.saveRequest(userDto3.getId(), eventFullDto.getId());
        ratingService.likeEvent(userDto3.getId(), eventFullDto.getId());

        requestService.saveRequest(userDto4.getId(), eventFullDto.getId());
        ratingService.dislikeEvent(userDto4.getId(), eventFullDto.getId());

        EventFullDto eventFullDto1 = eventService.getEvent(userDto.getId(), eventFullDto.getId());

        assertThat(eventFullDto1.getEventRating(), equalTo(1L));
        assertThat(eventFullDto1.getEventInitiatorRating(), equalTo(1L));
    }
}
