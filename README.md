# Repository Shareit project
![GitHub Actions](https://github.com/Difirton/java-shareit/actions/workflows/api-tests.yml/badge.svg)
![GitHub contributors](https://img.shields.io/github/contributors/Difirton/java-shareit?color=green)

## Quick start
### Requirements

- Java Platform (JDK) 11
- Apache Maven 4.x


While in the directory on the command line, type:

`./mvn package`

`java -jar target/filmorate-0.0.1-SNAPSHOT.jar`

## Quick start with Docker
### Requirements

- Java Platform (JDK) 11
- Apache Maven 4.x
- Docker client


While in the directory on the command line, type:

`./mvn package`

`java -jar target/filmorate-0.0.1-SNAPSHOT.jar`

`docker build -t filmorate:0.0.1`

`docker run -d -p 8080:8080 -t filmorate:0.0.1`

