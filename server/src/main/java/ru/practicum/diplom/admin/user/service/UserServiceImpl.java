package ru.practicum.diplom.admin.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.admin.user.dto.UserDto;
import ru.practicum.diplom.admin.user.dto.UserMapper;
import ru.practicum.diplom.admin.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.debug("Запрос getUsers по userId - {}, from - {}, page - {}", ids, from, size);

        Pageable pageRequest = PageRequest.of(from, size);
        return UserMapper.toUserDto(
                userRepository.findUsersByIds(ids, pageRequest)
        );
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.debug("Запрос saveUser по email - {}", userDto.getEmail());
        return UserMapper.toUserDto(
                userRepository.save(
                        UserMapper.toUser(userDto)
                )
        );
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Запрос deleteUser по id - {}", id);
        User user = userRepository.checkAndReturnUserIfExist(id);
        userRepository.delete(user);
    }
}
