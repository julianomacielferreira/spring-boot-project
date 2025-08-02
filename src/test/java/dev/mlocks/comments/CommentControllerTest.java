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
package dev.mlocks.comments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    List<Comment> comments = new ArrayList();

    @MockitoBean
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        // create some comments
        this.comments = List.of(
                new Comment(1, 1, "Name 1", "email1@email.com", "This is my first comment", null),
                new Comment(2, 2, "Name 2", "email2@email.com", "This is my second comment", null)
        );
    }

    @Test
    void shouldFindAllComments() throws Exception {

        String jsonResponse = """
                [
                    {
                      "postId": 1,
                      "id": 1,
                      "name": "Name 1",
                      "email": "email1@email.com",
                      "body": "This is my first comment",
                      "version": null
                    },
                    {
                      "postId": 2,
                      "id": 2,
                      "name": "Name 2",
                      "email": "email2@email.com",
                      "body": "This is my second comment",
                      "version": null
                    }
                ]
                """;

        when(this.commentRepository.findAll()).thenReturn(this.comments);

        this.mockMvc.perform(get("/api/comments")).
                andExpect(status().isOk()).
                andExpect(content().json(jsonResponse));
    }

    @Test
    void shouldNotFindCommentWhenGivenInvalidId() throws Exception {

        when(this.commentRepository.findById(999)).thenThrow(CommentNotFoundException.class);

        this.mockMvc.perform(get("/api/comments/999")).
                andExpect(status().isNotFound());
    }

    @Test
    void shouldFindCommentWhenGivenValidId() throws Exception {

        when(this.commentRepository.findById(1)).thenReturn(Optional.of(this.comments.getFirst()));

        Comment comment = this.comments.getFirst();

        String response = this.getJSONFromComment(comment);

        this.mockMvc.perform(get("/api/comments/1")).
                andExpect(status().isOk()).
                andExpect(content().json(response));

    }

    @Test
    void shouldCreateNewCommentWhenCommentIsValid() throws Exception {

        Comment comment = new Comment(3, 3, "Name 3", "email3@email.com", "Comment body 3", null);

        when(this.commentRepository.save(comment)).thenReturn(comment);

        String requestBody = this.getJSONFromComment(comment);

        this.mockMvc.perform(
                post("/api/comments").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateCommentWhenCommentIsInvalid() throws Exception {

        Comment comment = new Comment(4, 4, "", "", "", null);

        when(this.commentRepository.save(comment)).thenReturn(comment);

        String requestBody = this.getJSONFromComment(comment);

        this.mockMvc.perform(
                post("/api/comments").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateWhenGivenValidComment() throws Exception {

        Comment updated = new Comment(1, 1, "Name updated", "email_updated@email.com", "body updated", null);

        when(this.commentRepository.findById(1)).thenReturn(Optional.of(updated));
        when(this.commentRepository.save(updated)).thenReturn(updated);

        String requestBody = this.getJSONFromComment(updated);

        this.mockMvc.perform(
                put("/api/comments/1").
                        contentType("application/json").
                        content(requestBody)
        ).andExpect(status().isOk());
    }

    private String getJSONFromComment(Comment comment) {

        return String.format("""
                            {
                              "postId": %s,
                              "id": %s,
                              "name": "%s",
                              "email": "%s",
                              "body": "%s",
                              "version": null
                            }
                        """,
                comment.postId(),
                comment.id(),
                comment.name(),
                comment.email(),
                comment.body());
    }
}
