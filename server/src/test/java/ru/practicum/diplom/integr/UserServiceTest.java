package ru.practicum.diplom.integr;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.diplom.admin.user.dto.UserDto;
import ru.practicum.diplom.admin.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;

    @Test
    void saveAndGetUser() {
        UserDto userDto = UserDto.builder()
                .name("test")
                .email("test@yandex.ru")
                .build();
        userDto = userService.saveUser(userDto);

        assertThat(userDto.getId(), notNullValue());

        List<UserDto> dtoList = userService.getUsers(List.of(userDto.getId()), 0, 10);

        assertThat(dtoList.size(), equalTo(1));
        assertThat(dtoList.get(0).getId(), equalTo(userDto.getId()));
    }
}
