# url_shortcut

Convert Your URL to short code.

This project is based on [JDK 17](https://www.oracle.com/java/technologies/javase-downloads.html#JDK17) and use:
- [Maven](https://maven.apache.org/) (v. 3.6.3),
- [Springboot](https://spring.io/projects/spring-boot) (v. 2.7.12),
- [Spring Data](https://spring.io/projects/spring-data) (v. 5.7.3),
- [Lombok](https://projectlombok.org/) (v. 1.18.22),
- [PostgreSQL](https://www.postgresql.org/) (v. 13.1),
- [Swagger](https://springdoc.org/v1/) (v. 1.6.14),
- [JWT](https://github.com/auth0/java-jwt) (v. 3.4.0).
---
## articles
1. Register Your site in service and get Your UNIQUE_LOGIN and UNIQUE_PASSWORD:

Request:
```html
POST /registration
{
    "site" : "foo.com"
}
```

Response:
```html
{
    "registration" : true/false, "login": "UNIQUE_LOGIN", "password" : "UNIQUE_PASSWORD"
}
```

2. Login with login and password and receive JWT:\
Request:
```html
POST /login
{
    "login": "UNIQUE_LOGIN", 
    "password" : "UNIQUE_PASSWORD"
}
```
\
Don't forget Your JWT in header:\
   `Authorization: Bearer xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxx`
3. Use received JWT to post Your URLs and get shortcuts for them (URL must start with Your registered site):\
Request:
```html
POST /convert
{
    "url" : "http://foo.com/some/long/url/link"
}
```
Response:
```html
{
    "code" : "XXXXXXXX"
}
```
4. Now anyone can use shortcut code for receiving full URL with REDIRECT status (no need JWT):\
Request:
```html
GET /redirect/XXXXXXXXX
```
Response: \
```html
{
   "url" : "http://foo.com/some/long/url/link"
}
```
5. Authorized (with JWT token) clients can get statistics for each posted URL with count of get requests: \
Request: \
```html
GET /statistic
```
Response: \
   ```html
{ 
    {"url" : "foo.com", "total" : 0},
    {"url" : "http://foo.com/some/long/url/link", "total" : 103} 
}
```
---
### Contacts
Feel free for contacting me:
- **Skype**: pankovmv