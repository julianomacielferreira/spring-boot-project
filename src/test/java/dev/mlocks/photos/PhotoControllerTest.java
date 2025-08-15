/*
 * The MIT License
 *
 * Copyright 2025 juliano.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.mlocks.photos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoController.class)
@AutoConfigureMockMvc
public class PhotoControllerTest {

    @Autowired
    MockMvc mockMvc;

    List<Photo> photos = new ArrayList();

    @MockitoBean
    PhotoRepository photoRepository;

    @BeforeEach
    void setUp() {

        // create some photos
        photos = List.of(
                new Photo(1, 1, "Photo 1", "https://via.placeholder.com/600/92c952", "https://via.placeholder.com/150/92c952", null),
                new Photo(2, 2, "Photo 2", "https://via.placeholder.com/600/771796", "https://via.placeholder.com/150/771796", null)
        );
    }

    @Test
    void shouldFindAll() throws Exception {

        String jsonResponse = """
                [
                    {
                     "albumId": 1,
                     "id": 1,
                     "title": "Photo 1",
                     "url": "https://via.placeholder.com/600/92c952",
                     "thumbnailUrl": "https://via.placeholder.com/150/92c952"
                    },
                    {
                     "albumId": 2,
                     "id": 2,
                     "title": "Photo 2",
                     "url": "https://via.placeholder.com/600/771796",
                     "thumbnailUrl": "https://via.placeholder.com/150/771796"
                    }
                ]
                """;

        when(this.photoRepository.findAll()).thenReturn(this.photos);

        this.mockMvc.perform(get("/api/photos")).
                andExpect(status().isOk()).
                andExpect(content().json(jsonResponse));

    }

    @Test
    void shouldFindWhenGivenValidId() throws Exception {

        when(this.photoRepository.findById(1)).thenReturn(Optional.of(this.photos.getFirst()));

        Photo photo = this.photos.getFirst();

        String response = this.getJSONFromPhoto(photo);

        this.mockMvc.perform(get("/api/photos/1")).
                andExpect(status().isOk()).
                andExpect(content().
                        json(response)
                );
    }

    @Test
    void shouldNotFindWhenGivenInvalidId() throws Exception {

        when(this.photoRepository.findById(999)).thenThrow(PhotoNotFoundException.class);

        this.mockMvc.perform(get("/api/photos/999")).
                andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewWhenPhotoIsValid() throws Exception {

        Photo photo = new Photo(3, 3, "Photo 3", "https://via.placeholder.com/600/92c952", "https://via.placeholder.com/150/92c952", null);

        when(this.photoRepository.save(photo)).thenReturn(photo);

        String requestBody = this.getJSONFromPhoto(photo);

        this.mockMvc.perform(
                post("/api/photos").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateWhenPhotoIsInvalid() throws Exception {

        Photo invalid = new Photo(4, 4, "", "", "", null);

        when(this.photoRepository.save(invalid)).thenReturn(invalid);

        String requestBody = this.getJSONFromPhoto(invalid);

        this.mockMvc.perform(
                post("/api/photos").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateWhenGivenValidPhoto() throws Exception {

        Photo updated = new Photo(1, 1, "Title updated", "https://via.placeholder.com/600/24f355", "https://via.placeholder.com/150/24f355", null);

        when(this.photoRepository.findById(1)).thenReturn(Optional.of(updated));
        when(this.photoRepository.save(updated)).thenReturn(updated);

        String requestBody = this.getJSONFromPhoto(updated);

        this.mockMvc.perform(
                put("/api/photos/1").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateWhenGivenInvalidPhoto() throws Exception {

        Photo invalid = new Photo(9999, 9999, "Title not exists", "not exists", "not exists", null);

        String requestBody = this.getJSONFromPhoto(invalid);

        this.mockMvc.perform(
                put("/api/photos/9999").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isNotFound());
    }

    private String getJSONFromPhoto(Photo photo) {

        return String.format("""
                            {
                              "albumId": %s,
                              "id": %s,
                              "title": "%s",
                              "url": "%s",
                              "thumbnailUrl": "%s",
                              "version": null
                            }
                        """,
                photo.albumId(),
                photo.id(),
                photo.title(),
                photo.url(),
                photo.thumbnailUrl(),
                photo.version());
    }

}
