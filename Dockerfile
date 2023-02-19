FROM maven:3.8.6-jdk-11
LABEL maintainer="jugovitskaroly@gmail.com"

WORKDIR /spring-boot-jwt
COPY . .
RUN mvn clean install
CMD mvn spring-boot:run