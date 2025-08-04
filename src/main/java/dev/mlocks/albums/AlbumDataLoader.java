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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AlbumDataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumDataLoader.class);
    private final ObjectMapper objectMapper;
    private final AlbumRepository albumRepository;

    public AlbumDataLoader(ObjectMapper objectMapper, AlbumRepository albumRepository) {
        this.objectMapper = objectMapper;
        this.albumRepository = albumRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (albumRepository.count() == 0) {
            String ALBUM_JSON = "/data/albums.json";

            LOGGER.info("Loading albums into database from JSON: {}", ALBUM_JSON);

            try (InputStream inputStream = TypeReference.class.getResourceAsStream(ALBUM_JSON)) {
                Albums response = objectMapper.readValue(inputStream, Albums.class);
                albumRepository.saveAll(response.albums());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data");
            }
        }
    }
}
