FROM openjdk:11
MAINTAINER Abdullah Baig <abdullahbaig456@gmail.com>

ARG JAR_FILE
ARG SERVICE_NAME

EXPOSE 8080

RUN apt update \
    && apt upgrade -y \
    && apt install -y netcat-openbsd

WORKDIR /usr/local/${SERVICE_NAME}
COPY ${JAR_FILE} ./

COPY run.sh run.sh
RUN chmod +x run.sh

CMD ./run.sh
