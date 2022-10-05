package ru.practicum.diplom.rating;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.diplom.priv.ratings.RatingController;
import ru.practicum.diplom.priv.ratings.service.RatingService;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingController.class)
@AutoConfigureMockMvc
public class RatingControllerTest {
    @MockBean
    private RatingService ratingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void likeEvent() throws Exception {
        Mockito.doNothing().when(ratingService).likeEvent(Mockito.anyLong(), Mockito.anyLong());

        mvc.perform(patch("/users/1/events/2/like")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void dislikeEvent() throws Exception {
        Mockito.doNothing().when(ratingService).likeEvent(Mockito.anyLong(), Mockito.anyLong());

        mvc.perform(patch("/users/1/events/2/dislike")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
