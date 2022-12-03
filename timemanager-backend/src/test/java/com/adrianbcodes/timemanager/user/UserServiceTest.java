package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(new FakeUserRepository());
    }

    @Test
    void getAllUsers() {
        //given
        User user = UserExample.getUser1();
        User user2 = UserExample.getUser2();
        userService.saveUser(user);
        userService.saveUser(user2);
        //when
        var result = userService.getAllUsers();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(user);
        assertThat(result.get(1)).isEqualTo(user2);
    }

    @Test
    void getAllUsers_noUsersInDatabase() {
        //given
        //when
        var result = userService.getAllUsers();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllUsers_onlyUsersWithStatusDeletedInDatabase_returnsEmptyList() {
        //given
        User user = UserExample.getUser1();
        User user2 = UserExample.getUser2();
        userService.saveUser(user);
        userService.saveUser(user2);
        userService.deleteUserById(user.getId());
        userService.deleteUserById(user2.getId());
        //when
        var result = userService.getAllUsers();
        //then
        assertThat(result.size()).isEqualTo(0);

    }

    @Test
    void getAllUsers_ignoresUsersWithStatusDeleted() {
        //given
        User user = UserExample.getUser1();
        User user2 = UserExample.getUser2();
        userService.saveUser(user);
        userService.saveUser(user2);
        userService.deleteUserById(user2.getId());
        //when
        var result = userService.getAllUsers();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(user);
    }

    @Test
    void getUserById() {
        //given
        User user = UserExample.getUser1();
        userService.saveUser(user);
        //when
        var result = userService.getUserById(user.getId());
        //then
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserById_userDoesNotExist_throwsNotFoundException() {
        //given
        //when
        var exception = catchThrowable(() -> userService.getUserById(1L));
        //then
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveUser() {
        //given
        User user = UserExample.getUser1();
        //when
        userService.saveUser(user);
        //then
        assertThat(userService.getUserById(user.getId())).isEqualTo(user);
    }

    @Test
    void deleteUserById() {
        //given
        User user = UserExample.getUser1();
        user.setStatus(StatusEnum.ACTIVE);
        userService.saveUser(user);
        //when
        userService.deleteUserById(user.getId());
        //then
        assertThat(userService.getUserById(user.getId()).getStatus()).isEqualTo(StatusEnum.DELETED);
    }

    @Test
    void deleteUserById_userHasStatusDeleted_throwsAlreadyDeletedException() {
        //given
        User user = UserExample.getUser1();
        user.setStatus(StatusEnum.DELETED);
        userService.saveUser(user);
        //when
        var exception = catchThrowable(() -> userService.deleteUserById(user.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}