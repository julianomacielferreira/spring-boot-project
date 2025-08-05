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
package dev.mlocks.util;

import jakarta.validation.Valid;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T, ID, R extends ListCrudRepository<T, ID>, E extends Exception> {

    protected final R repository;
    protected final Class<E> exceptionClass;

    public AbstractController(R repository, Class<E> exceptionClass) {
        this.repository = repository;
        this.exceptionClass = exceptionClass;
    }

    @GetMapping("")
    public List<T> findAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<T> findById(@PathVariable ID id) throws Exception {
        return Optional.of(this.repository.findById(id).orElseThrow(() -> {
            try {
                return exceptionClass.getConstructor(String.class).newInstance("Entity not found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public T create(@RequestBody @Valid T entity) {
        return this.repository.save(entity);
    }

    @PutMapping("/{id}")
    protected abstract T update(@PathVariable ID id, @RequestBody @Valid T entity);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        this.repository.deleteById(id);
    }

    protected void throwException(String message) {
        try {
            throw exceptionClass.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
