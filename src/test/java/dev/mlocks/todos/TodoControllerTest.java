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
package dev.mlocks.todos;

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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    List<Todo> todos = new ArrayList();

    @MockitoBean
    TodoRepository todoRepository;

    @BeforeEach
    void setUp() {

        // create some todos
        todos = List.of(
                new Todo(1, 1, "Todo 1", false, null),
                new Todo(2, 2, "Todo 2", true, null)
        );
    }

    @Test
    void shouldFindAll() throws Exception {

        String jsonResponse = """
                [
                    {
                        "userId": 1,
                        "id": 1,
                        "title": "Todo 1",
                        "completed": false
                    },
                    {
                       "userId": 2,
                       "id": 2,
                       "title": "Todo 2",
                       "completed": true
                    }
                ]
                """;

        when(this.todoRepository.findAll()).thenReturn(this.todos);

        this.mockMvc.perform(get("/api/todos")).
                andExpect(status().isOk()).
                andExpect(content().json(jsonResponse));
    }

    @Test
    void shouldFindWhenGivenValidId() throws Exception {

        when(this.todoRepository.findById(1)).thenReturn(Optional.of(this.todos.getFirst()));

        Todo todo = this.todos.getFirst();

        String response = this.getJSONFromTodo(todo);

        this.mockMvc.perform(get("/api/todos/1")).
                andExpect(status().isOk()).
                andExpect(content().
                        json(response)
                );
    }

    @Test
    void shouldNotFindWhenGivenInvalidId() throws Exception {

        when(this.todoRepository.findById(9999)).thenThrow(TodoNotFoundException.class);

        this.mockMvc.perform(get("/api/todos/9999")).
                andExpect(status().isNotFound());
    }

    private String getJSONFromTodo(Todo todo) {

        return String.format("""
                            {
                              "userId": %s,
                              "id": %s,
                              "title": "%s",
                              "completed": %s,
                              "version": null
                            }
                        """,
                todo.userId(),
                todo.id(),
                todo.title(),
                todo.completed(),
                todo.version());
    }
}
