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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public abstract class AbstractDataLoader<T, R> implements CommandLineRunner {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected final ObjectMapper objectMapper;
    protected final String jsonFilePath;
    protected final ListCrudRepository<T, Integer> repository;
    protected final Class<R> responseType;

    public AbstractDataLoader(ObjectMapper objectMapper, String jsonFilePath, ListCrudRepository<T, Integer> repository, Class<R> responseType) {
        this.objectMapper = objectMapper;
        this.jsonFilePath = jsonFilePath;
        this.repository = repository;
        this.responseType = responseType;
    }

    @Override
    public void run(String... args) throws Exception {

        if (repository.count() == 0) {
            LOGGER.info("Loading data into the database from JSON: {}", jsonFilePath);

            try (InputStream inputStream = TypeReference.class.getResourceAsStream(jsonFilePath)) {
                R response = objectMapper.readValue(inputStream, responseType);
                loadData(response);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read json data.");
            }
        }
    }

    protected abstract void loadData(R response);
}
