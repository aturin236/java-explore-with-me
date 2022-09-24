package ru.practicum.diplom.integr;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.diplom.admin.category.dto.CategoryDto;
import ru.practicum.diplom.admin.category.service.CategoryService;
import ru.practicum.diplom.admin.event.service.EventAdminService;
import ru.practicum.diplom.admin.user.dto.UserDto;
import ru.practicum.diplom.admin.user.service.UserService;
import ru.practicum.diplom.priv.event.Event;
import ru.practicum.diplom.priv.event.dto.EventFullDto;
import ru.practicum.diplom.priv.event.dto.NewEventDto;
import ru.practicum.diplom.priv.event.dto.service.EventDtoService;
import ru.practicum.diplom.priv.event.service.EventService;
import ru.practicum.diplom.priv.request.RequestStatus;
import ru.practicum.diplom.priv.request.dto.RequestDto;
import ru.practicum.diplom.priv.request.service.RequestService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventAndRequestServiceTest {
    private final EntityManager entityManager;
    private final EventService eventService;
    private final EventAdminService eventAdminService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;
    @MockBean
    private final EventDtoService eventDtoService;


    @Test
    void saveEventAndRequest() {
        UserDto userDto = UserDto.builder()
                .name("test")
                .email("test@yandex.ru")
                .build();
        userDto = userService.saveUser(userDto);

        UserDto userDto2 = UserDto.builder()
                .name("test2")
                .email("test2@yandex.ru")
                .build();
        userDto2 = userService.saveUser(userDto2);

        CategoryDto categoryDto = CategoryDto.builder()
                .name("catTest")
                .build();
        categoryDto = categoryService.saveCategory(categoryDto);

        NewEventDto eventDto = new NewEventDto();
        eventDto.setTitle("test");
        eventDto.setDescription("test");
        eventDto.setAnnotation("test");
        eventDto.setEventDate(LocalDateTime.now().plusYears(1));
        eventDto.setPaid(true);
        eventDto.setParticipantLimit(0);
        eventDto.setRequestModeration(true);
        eventDto.setCategory(categoryDto.getId());

        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setTitle("test");
        eventFullDto.setDescription("test");
        eventFullDto.setAnnotation("test");
        eventFullDto.setEventDate(LocalDateTime.now().plusYears(1));
        eventFullDto.setPaid(true);
        eventFullDto.setParticipantLimit(0);
        eventFullDto.setRequestModeration(true);
        eventFullDto.setCategory(categoryDto);

        Mockito.when(eventDtoService.fillAdditionalInfo((EventFullDto) Mockito.any()))
                .thenReturn(eventFullDto);

        eventService.saveEvent(eventDto, userDto.getId());

        TypedQuery<Event> query = entityManager.createQuery("Select e from Event e where e.id = :id", Event.class);
        Event event = query.setParameter("id", 1L).getSingleResult();

        assertThat(event.getDescription(), equalTo(eventDto.getDescription()));

        eventAdminService.publishEvent(1L);

        RequestDto requestDto = requestService.saveRequest(userDto2.getId(), 1L);

        assertThat(requestDto.getId(), notNullValue());
        assertThat(requestDto.getStatus(), equalTo(RequestStatus.PENDING));
    }

}
