# NutritionistOnline - dietary app

* [General info](#general-info)
* [Technologies](#tech-framework-and-other-dependencies)
* [Building](#building)

## General info

>### Type of service
>This project make it possible to compose a diet according to the user's parameters. Also, user has the opportunity to see the calorie content of products or dishes, contact the administrator in case of questions.

>### What is the purpose of service?
>##### Common:
>- Registration and authorization
>- Login with Google
>- See food calorie content
>- See dishes calorie content
>- Enter own parameters and calculate amount of calories per day and separately for each meal
>- See dishes variants for each meal according own calorie norms
>- Save a file of own diet
>- Chat with admin
>##### Admin:
>- CRUD operations on products, dishes, users (only with user role)
>- Assign new chats and reply to users messages
>##### Super admin:
>- All CRUD operations (users with user and admin roles)
>- Reply to messages in any chat


>### What problem is resolved by this service?
>Mostly people, in pursiut of an ideal figure, who for some reason cannot visit a nutritionist, go on a strict diet, which later has the opposite effect or can lead to various diseases. Application can help choose the correct diet to avoid sudden loss or gain of weight. For people who cannot physically come to a consultation, the app provides a chat consultation with nutritionist.


​

​

## Tech, Framework and other Dependencies



* Java version: **17**

* Gradle version: **7.4**

* SpringBoot version: **3.0.5**

* SpringJDBC version: **6.0.7**

* SpringSecurity version: **6.0.2**

* PostgreSQL version: **42.5.4**

* Flyway Core version: **9.15.0**

* JUnit Jupiter version:  **5.9.0**

* Mockito version: **4.10.0**

* AssertJ Core version: **3.23.1**

* Testcontainers JUnit Jupiter version: **1.17.6**

* Testcontainers PostgreSQL version: **1.17.6**

* Thymeleaf Spring6 version: **3.1.1**

* SpringDoc OpenAPI Starter WebMVC UI version: **2.0.2**

* JavaServlet version: **3.0.1**

* tinylog version: **2.6.0**

​

​

## Building


<p>

<details>

<summary>Instructions for build, run, deploy and test project</summary>

​


build the project

```shell

./gradlew build

```

​

run the project

```shell

./gradlew web-app:bootRun

```

​

run tests.

```shell

./gradlew test

```

​

</details>

</p>

​

​

### Logging agreements

Spring Boot Starter OAuth2 Client version: **3.0.5**

