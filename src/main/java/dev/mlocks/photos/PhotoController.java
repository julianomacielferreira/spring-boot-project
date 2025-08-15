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

import dev.mlocks.util.AbstractController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
public class PhotoController extends AbstractController<Photo, Integer, PhotoRepository, PhotoNotFoundException> {

    public PhotoController(PhotoRepository photoRepository) {
        super(photoRepository, PhotoNotFoundException.class);
    }

    @PutMapping("/{id}")
    public Photo update(@PathVariable Integer id, @RequestBody @Valid Photo photo) {

        Optional<Photo> existing = this.repository.findById(id);

        if (existing.isPresent()) {

            Photo update = new Photo(
                    existing.get().id(),
                    existing.get().albumId(),
                    photo.title(),
                    photo.url(),
                    photo.thumbnailUrl(),
                    existing.get().version()
            );

            return this.repository.save(update);
        } else {
            throwException("Photo not found");
            return null;
        }
    }
}
