FROM openjdk:17-alpine
COPY target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN apk add --no-cache bash
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["/wait-for-it.sh", "mysql-container:3306", "--"]
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
