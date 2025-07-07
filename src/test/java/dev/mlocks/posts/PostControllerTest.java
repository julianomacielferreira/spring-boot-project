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
package dev.mlocks.posts;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    List<Post> posts = new ArrayList<>();

    @MockitoBean
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        // create some posts
        this.posts = List.of(
                new Post(1, 1, "Post 1 title", "This is my first post", null),
                new Post(2, 2, "Post 2 title", "This is my second post", null)
        );
    }

    @Test
    void shouldFindAllPosts() throws Exception {

        String jsonResponse = """
                [
                    {
                      "userId": 1,
                      "id": 1,
                      "title": "Post 1 title",
                      "body": "This is my first post",
                      "version": null
                    },
                    {
                      "userId": 2,
                      "id": 2,
                      "title": "Post 2 title",
                      "body": "This is my second post",
                      "version": null
                    }
                ]
                """;

        when(this.postRepository.findAll()).thenReturn(this.posts);

        this.mockMvc.perform(get("/api/posts")).
                andExpect(status().isOk()).
                andExpect(content().json(jsonResponse));
    }

    @Test
    void shouldFindPostWhenGivenValidId() throws Exception {

        when(this.postRepository.findById(1)).thenReturn(Optional.of(this.posts.getFirst()));

        Post post = this.posts.getFirst();

        String requestBody = this.getJSONFromPost(post);

        this.mockMvc.perform(get("/api/posts/1")).
                andExpect(status().isOk()).
                andExpect(content().
                        json(requestBody)
                );
    }

    @Test
    void shouldNotFindPostWhenGivenInvalidId() throws Exception {

        when(this.postRepository.findById(999)).thenThrow(PostNotFoundException.class);

        this.mockMvc.perform(get("/api/posts/999")).
                andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewPostWhenPostIsValid() throws Exception {

        Post post = new Post(3, 3, "Title 3", "This is my third post", null);

        when(this.postRepository.save(post)).thenReturn(post);

        String requestBody = this.getJSONFromPost(post);

        this.mockMvc.perform(
                post("/api/posts").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreatePostWhenPostIsInvalid() throws Exception {

        Post post = new Post(4, 4, "", "", null);

        when(this.postRepository.save(post)).thenReturn(post);

        String requestBody = this.getJSONFromPost(post);

        this.mockMvc.perform(
                post("/api/posts").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePostWhenGivenValidPost() throws Exception {

        Post updated = new Post(1, 1, "Updated Title 1", "Updated Body 1", null);

        when(this.postRepository.findById(1)).thenReturn(Optional.of(updated));
        when(this.postRepository.save(updated)).thenReturn(updated);

        String requestBody = this.getJSONFromPost(updated);

        this.mockMvc.perform(
                put("/api/posts/1").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isOk());


    }

    @Test
    void shouldNotUpdatePostWhenGivenInvalidPost() throws Exception {

        Post invalid = new Post(999, 999, "Title not exists", "Body not exists", null);

        String requestBody = this.getJSONFromPost(invalid);

        this.mockMvc.perform(
                put("/api/posts/999").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isNotFound());

    }

    @Test
    void shouldDeletePostWhenGivenValidId() throws Exception {

        this.mockMvc.perform(
                delete("/api/posts/1")
        ).andExpect(status().isNoContent());

    }

    private String getJSONFromPost(Post post) {

        return String.format("""
                            {
                              "userId": %s,
                              "id": %s,
                              "title": "%s",
                              "body": "%s",
                              "version": null
                            }
                        """,
                post.userId(),
                post.id(),
                post.title(),
                post.body());
    }
}
