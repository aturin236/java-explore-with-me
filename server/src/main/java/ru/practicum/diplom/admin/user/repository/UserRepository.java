package ru.practicum.diplom.admin.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.diplom.admin.user.User;
import ru.practicum.diplom.exceptions.UserNotFoundException;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    default User checkAndReturnUserIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", id)));
    }

    @Query("select u from User u where u.id in (?1)")
    List<User> findUsersByIds(List<Long> ids, Pageable pageable);
}
