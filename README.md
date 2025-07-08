# Spring Boot Project

This is a Rest API project and the initial code is based on the tutorial from [Dan Vega](https://www.youtube.com/@DanVega) called [**Test Driven Development (TDD) in Spring**](https://youtube.com/watch?v=-H5sud1-K5A).
I have made  make my own modifications and improvements (i.e, add new features).

![Test Driven Development (TDD) in Spring](./src/main/resources/static/video-thumbnail.jpg)

## Project File Structure

```
├── build.gradle
├── compose.yaml
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── HELP.md
├── LICENSE
├── README.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── dev
    │   │       └── mlocks
    │   │           ├── Application.java
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
    │       │   └── posts.json
    │       ├── schema.sql
    │       ├── static
    │       │   ├── project-file-structure.png
    │       │   └── video-thumbnail.jpg
    │       └── templates
    └── test
        └── java
            └── dev
                └── mlocks
                    └── posts
                        └── PostControllerTest.java

```

## Running the application

@TODO

## Endpoints

- Retrieve all posts: **/api/posts**

```bash
$ curl --location 'http://localhost:8080/api/posts'
```

- Response:

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

@TODO

## References

- [**Spring Boot**](https://spring.io/projects/spring-boot)
- [**MDN Web Docs**](https://developer.mozilla.org/)
- [**IntelliJ IDEA Community Edition**](https://www.jetbrains.com/idea/download/?section=linux)
- [**Docker**](https://www.docker.com/)
- [**{JSON} Placeholder**](https://jsonplaceholder.typicode.com/)