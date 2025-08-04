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
package dev.mlocks.albums;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc
public class AlbumControllerTest {

    @Autowired
    MockMvc mockMvc;

    List<Album> albums = new ArrayList();

    @MockitoBean
    AlbumRepository albumRepository;

    @BeforeEach
    void setUp() {

        // create some albums
        this.albums = List.of(
                new Album(1, 1, "First Title", null),
                new Album(2, 2, "Second Title", null)
        );
    }

    @Test
    void shouldFindAll() throws Exception {

        String jsonResponse = """
                [
                     {
                        "userId": 1,
                        "id": 1,
                        "title": "First Title",
                        "version": null
                     },
                     {
                        "userId": 2,
                        "id": 2,
                        "title": "Second Title",
                        "version": null
                     }
                ]
                """;

        when(this.albumRepository.findAll()).thenReturn(this.albums);

        this.mockMvc.perform(get("/api/albums")).
                andExpect(status().isOk()).
                andExpect(content().json(jsonResponse));
    }

    @Test
    void shouldFindWhenGivenValidId() throws Exception {

        when(this.albumRepository.findById(1)).thenReturn(Optional.of(this.albums.getFirst()));

        Album album = this.albums.getFirst();

        String response = this.getJSONFromAlbum(album);

        this.mockMvc.perform(get("/api/albums/1")).
                andExpect(status().isOk()).
                andExpect(content().json(response));
    }

    @Test
    void shouldNotFindWhenGivenInvalidId() throws Exception {

        when(this.albumRepository.findById(999)).thenThrow(AlbumNotFoundException.class);

        this.mockMvc.perform(get("/api/albums/999")).
                andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewWhenAlbumIsValid() throws Exception {

        Album album = new Album(3, 3, "Title 3", null);

        when(this.albumRepository.save(album)).thenReturn(album);

        String requestBody = this.getJSONFromAlbum(album);

        this.mockMvc.perform(
                post("/api/albums").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotUpdateWhenGivenInvalidAlbum() throws Exception {

        Album invalid = new Album(999, 999, "Album not exists", null);

        String requestBody = this.getJSONFromAlbum(invalid);

        this.mockMvc.perform(
                put("/api/albums/999").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isNotFound());
    }

    private String getJSONFromAlbum(Album album) {

        return String.format("""
                            {
                              "userId": %s,
                              "id": %s,
                              "title": "%s",
                              "version": null
                            }
                        """,
                album.userId(),
                album.id(),
                album.title()
        );
    }
}
