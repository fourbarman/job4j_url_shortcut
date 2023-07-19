# url_shortcut

Convert Your URL to short code.

This project is based on [JDK 17](https://www.oracle.com/java/technologies/javase-downloads.html#JDK17) and use:
- [Maven](https://maven.apache.org/) (v. 3.6.3),
- [Springboot](https://spring.io/projects/spring-boot) (v. 2.7.12),
- [Spring Data](https://spring.io/projects/spring-data) (v. 5.7.3),
- [Lombok](https://projectlombok.org/) (v. 1.18.22),
- [PostgreSQL](https://www.postgresql.org/).

## articles
1. Register Your site in service:\
Request:\
   POST /registration \
{\
"site" : "foo.com"\
}\
\
Response:\
{\
"registration" : true/false, \
"login": "UNIQUE_LOGIN", \
"password" : "UNIQUE_PASSWORD"\
}
2. Login with login and password and receive JWT:\
Request:
   POST /login \
{\
"login": "UNIQUE_LOGIN", \
"password" : "UNIQUE_PASSWORD"\
}\
\
Response JWT in header:\
   Authorization: Bearer xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxx
3. Use received JWT to post Your URLs for getting shortcuts for them:\
Request:\
POST /convert\
   {\
   "url" : "http://foo.com/some/long/url/link" \
   }
\
Response:\
   {\
   "code" : "XXXXXXXX" \
   }
4. Now anyone can use shortcut code for receiving full URL with REDIRECT status (no need JWT):\
Request:\
GET /redirect/XXXXXXXXX \
Response: \
{\
   "url" : "http://foo.com/some/long/url/link" \
}
5. Authorized (with JWT token) clients can get statistics for each posted URL with count of get requests: \
Request: \
   GET /statistic \
Response: \
   {\
   {\
"url" : "foo.com",\
"total" : 0},\
   {\
"url" : "http://foo.com/some/long/url/link", \
   "total" : 103}\
   }\
### Contacts
Feel free for contacting me:
- **Skype**: pankovmv