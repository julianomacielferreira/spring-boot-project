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

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping("")
    List<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Comment> findById(@PathVariable Integer id) {
        return Optional.of(
                this.commentRepository.findById(id).orElseThrow()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    Comment create(@RequestBody @Valid Comment comment) {
        return this.commentRepository.save(comment);
    }

    @PutMapping("/{id}")
    Comment update(@PathVariable Integer id, @RequestBody @Valid Comment comment) {

        Optional<Comment> existing = this.commentRepository.findById(id);

        if (existing.isPresent()) {

            Comment update = new Comment(
                    existing.get().id(),
                    existing.get().postId(),
                    comment.name(),
                    comment.body(),
                    comment.body(),
                    existing.get().version()
            );

            return this.commentRepository.save(update);
        } else {
            throw new CommentNotFoundException("Comment not found");
        }
    }
}
