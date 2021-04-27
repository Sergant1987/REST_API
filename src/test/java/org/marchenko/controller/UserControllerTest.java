package org.marchenko.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marchenko.exception.CreateException;
import org.marchenko.exception.NotFoundException;
import org.marchenko.model.User;
import org.marchenko.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUsersWhenNameNullThenGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "user1", "phone1"),
                new User(2L, "user2", "phone2")
        );

        Mockito.when(userService.findAllUsers(Mockito.anyInt(), Mockito.anyInt())).thenReturn(users);

        mvc.perform(MockMvcRequestBuilders.get("/users").content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", contains(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", contains("user1", "user2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].phone", contains("phone1", "phone2")));

        Mockito.verify(userService).findAllUsers(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(userService, Mockito.never()).findUsersByName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testGetUsersWhenNameNotNullThenGetMatchUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User(2L, "user2", "phone2"),
                new User(22L, "user22", "phone22")
        );
        String partOfName = "2";

        Mockito.when(userService.findUsersByName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(users);

        mvc.perform(MockMvcRequestBuilders.get("/users")
                .param("name", partOfName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", contains(2,22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", contains("user2","user22")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].phone", contains("phone2","phone22")));

        Mockito.verify(userService, Mockito.never()).findAllUsers(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(userService).findUsersByName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void createUserWhenUserNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.createUser(user.getName(), user.getPhone())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/users")
                .param("name", user.getName())
                .param("phone", user.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(user.getPhone()));

        Mockito.verify(userService).createUser(user.getName(), user.getPhone());
    }

    @Test
    public void createUserWhenUserExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.createUser(user.getName(), user.getPhone())).thenThrow(CreateException.class);

        mvc.perform(MockMvcRequestBuilders.post("/users")
                .param("name", user.getName())
                .param("phone", user.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(userService).createUser(user.getName(), user.getPhone());
    }

    @Test
    public void testDeleteUserWhenUserIsExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.deleteUser(user.getId())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.delete("/users/{user_id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).deleteUser(user.getId());
    }

    @Test
    public void testDeleteUserWhenUserIsNotExist() throws Exception {
        Mockito.when(userService.deleteUser(Mockito.anyLong())).thenThrow(NotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.delete("/users/{user_id}", Mockito.anyLong()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(userService).deleteUser(Mockito.anyLong());
    }

    @Test
    public void findUserByIdWhenUserExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.findUserById(user.getId())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(user.getPhone()));

        Mockito.verify(userService).findUserById(Mockito.anyLong());
    }

    @Test
    public void findUserByIdWhenUserMotExist() throws Exception {
        Mockito.when(userService.findUserById(Mockito.anyLong())).thenThrow(NotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}", Mockito.anyLong()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(userService).findUserById(Mockito.anyLong());
    }

    @Test
    public void updateUserWhenUserExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.updateUser(user.getId(), user.getName(), user.getPhone())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.patch("/users/{user_id}", user.getId())
                .param("name", user.getName())
                .param("phone", user.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(user.getPhone()));

        Mockito.verify(userService).updateUser(user.getId(), user.getName(), user.getPhone());
    }

    @Test
    public void updateUserWhenUserNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Mockito.when(userService.updateUser(user.getId(), user.getName(), user.getPhone())).thenThrow(NotFoundException.class);

        mvc.perform(
                MockMvcRequestBuilders.patch("/users/{user_id}", user.getId())
                        .param("name", user.getName())
                        .param("phone", user.getPhone())
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(userService).updateUser(user.getId(), user.getName(), user.getPhone());
    }

}

