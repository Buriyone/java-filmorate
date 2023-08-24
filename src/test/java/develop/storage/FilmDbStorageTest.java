package develop.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final MockMvc mockMvc;

    public void addFilm() throws Exception {
        String jsonFilm = "{\"name\":\"Newfilm\",\"releaseDate\":\"1999-04-30\"," +
                "\"description\":\"Newfilmaboutfriends\",\"duration\":120,\"rate\":4," +
                "\"mpa\":{\"id\":3},\"genres\":[{\"id\":1},{\"id\":2},{\"id\":1}]}";
        this.mockMvc.perform(post("/films").content(jsonFilm).contentType("application/json"));
    }

    private void addUser() throws Exception {
        String jsonUser = "{\"login\":\"dolore\",\"name\":\"NickName\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(post("/users").content(jsonUser).contentType("application/json"));
    }

    @Test
    public void updateFilmTest() throws Exception {
        addFilm();
        String jsonFilm = "{\"id\":\"1\",\"name\":\"Newfrilm\",\"releaseDate\":\"1999-04-30\"," +
                "\"description\":\"Newfilmaboutfriends\",\"duration\":120,\"rate\":4," +
                "\"mpa\":{\"id\":3},\"genres\":[{\"id\":1},{\"id\":2}]}";
        this.mockMvc.perform(put("/films")
                        .content(jsonFilm).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        jsonFilm = "{\"id\":\"9999\",\"name\":\"Newfrilm\",\"releaseDate\":\"1999-04-30\"," +
                "\"description\":\"Newfilmaboutfriends\",\"duration\":120,\"rate\":4," +
                "\"mpa\":{\"id\":3},\"genres\":[{\"id\":1},{\"id\":2}]}";
        this.mockMvc.perform(put("/films")
                        .content(jsonFilm).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getFilmsTest() throws Exception {
        addFilm();
        this.mockMvc.perform(get("/films").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getFilmTest() throws Exception {
        addFilm();
        this.mockMvc.perform(get("/films/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/films/9999").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addLikeTest() throws Exception {
        addFilm();
        addUser();
        this.mockMvc.perform(put("/films/1/like/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(put("/films/1/like/999").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void removeLikeTest() throws Exception {
        for (int i = 1; i < 15; i++) {
            this.mockMvc.perform(delete("/films/1/like/" + i).contentType("application/json"))
                    .andReturn();
        }
        addFilm();
        addUser();
        this.mockMvc.perform(delete("/films/1/like/1").contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
        this.mockMvc.perform(put("/films/1/like/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(delete("/films/1/like/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(delete("/films/1/like/999").contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getTopFilmTest() throws Exception {
        addUser();
        addFilm();
        this.mockMvc.perform(put("/films/1/like/1").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        this.mockMvc.perform(get("/films/popular").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
