package develop.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final MockMvc mockMvc;

    private void addUser() throws Exception {
        String jsonUser = "{\"login\":\"dolore\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(jsonUser).contentType("application/json"));
    }

    @Test
    public void userValidationTest() throws Exception {
        String testJsonUser = "{\"login\":\"   \",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(testJsonUser).contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
        testJsonUser = "{\"login\":\"dol ore\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(testJsonUser).contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
        testJsonUser = "{\"login\":\"dolore\",\"name\":\"NickName\",\"email\":\"\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(testJsonUser).contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
        testJsonUser = "{\"login\":\"dolore\",\"name\":\"NickName\",\"email\":\"mailmail.ru@\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(testJsonUser).contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
        LocalDate date = LocalDate.now().plusDays(1);
        testJsonUser = "{\"login\":\"dolore\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"" + date + "\"}";
        this.mockMvc.perform(post("/users").content(testJsonUser).contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updateUserTest() throws Exception {
        addUser();
        String jsonUser = "{\"id\":\"1\",\"login\":\"dalaran\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(put("/users")
                        .content(jsonUser)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        jsonUser = "{\"id\":\"99999\",\"login\":\"dalaran\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(put("/users")
                        .content(jsonUser).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getUsersTest() throws Exception {
        addUser();
        this.mockMvc.perform(get("/users").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUserTest() throws Exception {
        addUser();
        this.mockMvc.perform(get("/users/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/users/9999").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addFriendTest() throws Exception {
        addUser();
        String testUser = "{\"login\":\"DALARAN\",\"name\":\"NickName\",\"email\":\"mailll@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users")
                        .content(testUser).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(put("/users/1/friends/2").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(put("/users/1/friends/9999").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteFriendTest() throws Exception {
        for (int i = 1; i < 15; i++) {
            this.mockMvc.perform(delete("/users/1/friends/" + i).contentType("application/json"))
                    .andReturn();
        }
        addUser();
        String testUser = "{\"login\":\"DALwARrAN\",\"name\":\"NickName\",\"email\":\"maiwdlrll@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users")
                        .content(testUser).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(delete("/users/1/friends/2").contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getFriendsTest() throws Exception {
        addUser();
        this.mockMvc.perform(get("/users/1/friends").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/users/9999/friends").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getCommonFriendsTest() throws Exception {
        addUser();
        String testUser = "{\"login\":\"DALArRrAN\",\"name\":\"NickName\",\"email\":\"maeiflrll@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users")
                        .content(testUser).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/users/1/friends/common/2")
                        .content(testUser).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/users/1/friends/common/9999")
                        .content(testUser).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
