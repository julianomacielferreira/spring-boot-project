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

import dev.mlocks.util.AbstractController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
class PostController extends AbstractController<Post, Integer, PostRepository, PostNotFoundException> {

    public PostController(PostRepository postRepository) {
        super(postRepository, PostNotFoundException.class);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Integer id, @RequestBody @Valid Post post) {

        Optional<Post> existing = this.repository.findById(id);

        if (existing.isPresent()) {

            Post update = new Post(existing.get().id(), existing.get().userId(), post.title(), post.body(), existing.get().version());

            return this.repository.save(update);
        } else {
            throwException("Post not found.");
            return null; // never reach this point
        }
    }
}
