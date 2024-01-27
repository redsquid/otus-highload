# highload-social

Build & Run:

- $ cd /\<_project dir_\>/
- $ mvn clean install
- $ docker compose -f docker-compose.postgre.yaml up -d
- $ java -jar ./target/highload-social-1.0-SNAPSHOT.jar

---

Postman - коллекция в корне


1. POST **/user** Создание пользователя.

    Request body:
    ```json
       {
           "username": "jonny23",
           "password": "qwerty",
           "account": {
               "firstName": "John",
               "lastName": "Connor",
               "birthDate": "1985-02-28",
               "sex": "MALE",
               "city": "Lod Angeles",
               "interests": "Robots",
               "rank": "100"
           }
       }
   ```
   
    Response body:

    ```userId```


2. GET **/token** Получение токена. Используется Basic - аутентификация. Логин и пароль из тела запроса на создание пользователя.

    Response body:
    
    ```Токен```


3. GET **/user/user-_{userId}_** Получение данных пользователей. В запросе нужно передавать заголовок `Authorization: Bearer {токен}`полученный из **/token**.

