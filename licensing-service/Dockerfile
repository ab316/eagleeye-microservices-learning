FROM openjdk:11
MAINTAINER Abdullah Baig <abdullahbaig456@gmail.com>

ARG JAR_FILE
ARG SERVICE_NAME

WORKDIR /usr/local/${SERVICE_NAME}
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
            "-jar", \
            "app.jar" \
            ]
