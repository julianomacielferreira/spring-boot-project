# Spring Boot Project $${\color{red}[in \space progress]}$$

This is a Simple CRUD Rest API project. The initial code is based on the tutorial from [Dan Vega](https://www.youtube.com/@DanVega) called [**Test Driven Development (TDD) in Spring**](https://youtube.com/watch?v=-H5sud1-K5A).
Along the time I will make my own modifications and improvements (i.e, add new features).

![Test Driven Development (TDD) in Spring](./src/main/resources/static/video-thumbnail.jpg)

## Project File Structure

```
.
├── build.gradle
├── compose.yaml
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── LICENSE
├── README.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── dev
    │   │       └── mlocks
    │   │           ├── Application.java
    │   │           ├── comments
    │   │           │   ├── CommentController.java
    │   │           │   ├── CommentDataLoader.java
    │   │           │   ├── Comment.java
    │   │           │   ├── CommentNotFoundException.java
    │   │           │   ├── CommentRepository.java
    │   │           │   └── Comments.java
    │   │           └── posts
    │   │               ├── PostController.java
    │   │               ├── PostDataLoader.java
    │   │               ├── Post.java
    │   │               ├── PostNotFoundException.java
    │   │               ├── PostRepository.java
    │   │               └── Posts.java
    │   └── resources
    │       ├── application.properties
    │       ├── data
    │       │   ├── comments.json
    │       │   └── posts.json
    │       ├── schema.sql
    │       ├── static
    │       │   └── video-thumbnail.jpg
    │       └── templates
    └── test
        └── java
            └── dev
                └── mlocks
                    ├── comments
                    │   └── CommentControllerTest.java
                    └── posts
                        └── PostControllerTest.java

```

---

## Posts Endpoints

- **`GET` /api/posts** (Retrieve all posts)

```bash
$ curl --location 'http://localhost:8080/api/posts'
```
<details>
<summary><b>Response</b></summary>

```json
[
    {
        "id": 1,
        "userId": 1,
        "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto",
        "version": 0
    },
    {
        "id": 2,
        "userId": 1,
        "title": "qui est esse",
        "body": "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla",
        "version": 0
    },
    {
        "id": 3,
        "userId": 1,
        "title": "ea molestias quasi exercitationem repellat qui ipsa sit aut",
        "body": "et iusto sed quo iure\nvoluptatem occaecati omnis eligendi aut ad\nvoluptatem doloribus vel accusantium quis pariatur\nmolestiae porro eius odio et labore et velit aut",
        "version": 0
    }
]
```
</details>

---

- **`POST` /api/posts** (Create a new post)

```bash
$ curl --location 'http://localhost:8080/api/posts' \
--header 'Content-Type: application/json' \
--data '{
    "id": 100,
    "userId": 100,
    "title": "New Title Created",
    "body": "New Body Created",
    "version": 0
}'
```

<details>
<summary><b>Response</b></summary>

```json
{
  "id": 100,
  "userId": 100,
  "title": "New Title Created",
  "body": "New Body Created",
  "version": 1
}
```
</details>

---

- **`PUT` /api/posts/`{id}`** (Update an existing post)

```bash
$ curl --location --request PUT 'http://localhost:8080/api/posts/2' \
--header 'Content-Type: application/json' \
--data '{
    "id": 2,
    "userId": 1,
    "title": "Title Updated",
    "body": "Body Updated",
    "version": 0
}'
```

<details>
<summary><b>Response</b></summary>

```json
{
  "id": 2,
  "userId": 1,
  "title": "Title Updated",
  "body": "Body Updated",
  "version": 1
}
```
</details>

---

- **`DELETE` /api/posts/`{id}`** (Remove an existing post)

```bash
$ curl --location --request DELETE 'http://localhost:8080/api/posts/100'
```

<details>
<summary><b>Response</b></summary>

```json
```
</details>

---

## Comments Endpoints

- **`GET` /api/comments** (Retrieve all comments)

```bash
$ curl --location 'http://localhost:8080/api/comments'
```
<details>
<summary><b>Response</b></summary>

```json
[
      {
        "id": 1,
        "postId": 1,
        "name": "id labore ex et quam laborum",
        "email": "Eliseo@gardner.biz",
        "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium",
        "version": 0
      },
      {
        "id": 2,
        "postId": 1,
        "name": "quo vero reiciendis velit similique earum",
        "email": "Jayne_Kuhic@sydney.com",
        "body": "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et",
        "version": 0
      }
]
```
</details>

---

- **`POST` /api/comments** (Create a new comment)

```bash
$ curl --location 'http://localhost:8080/api/comments' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1010,
    "postId": 1010,
    "name": "New Comment Created",
    "email": "new_email@email.com",
    "body": "New Body Created",
    "version": 0
}'
```

<details>
<summary><b>Response</b></summary>

```json
{
  "id": 101,
  "postId": 101,
  "name": "New Comment Created",
  "email": "new_email@email.com",
  "body": "New Body Created",
  "version": 1
}
```
</details>

---

- **`PUT` /api/comments/`{id}`** (Update an existing comment)

```bash
$ curl --location --request PUT 'http://localhost:8080/api/comments/2' \
--header 'Content-Type: application/json' \
--data '{
    "id": 2,
    "postId": 1,
    "name": "Name Updated",
    "email": "email@email.com",
    "body": "Body Updated",
    "version": 0
}'
```

<details>
<summary><b>Response</b></summary>

```json
{
  "id": 2,
  "postId": 1,
  "name": "Name Updated",
  "email": "Body Updated",
  "body": "Body Updated",
  "version": 1
}
```
</details>

---

- **`DELETE` /api/comments/`{id}`** (Remove an existing comment)

```bash
$ curl --location --request DELETE 'http://localhost:8080/api/comments/100'
```

<details>
<summary><b>Response</b></summary>

```json
```
</details>

---

## Improvements

- [ ] Add other domain objects (albuns, photos and users)
- [ ] Create tests
- [ ] Sanitize input data (maybe using filters ?)
- [ ] Validate constraints for duplicated data (email unique, etc)
- [ ] Improve response error messages
- [ ] Not pass the `id` field in the update body endpoint
- [ ] Deploy as a docker image

## Running the application

@TODO

## References

- [**Spring Boot**](https://spring.io/projects/spring-boot)
- [**MDN Web Docs**](https://developer.mozilla.org/)
- [**IntelliJ IDEA Community Edition**](https://www.jetbrains.com/idea/download/?section=linux)
- [**Docker**](https://www.docker.com/)
- [**{JSON} Placeholder**](https://jsonplaceholder.typicode.com/)