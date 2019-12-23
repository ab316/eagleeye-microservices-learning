#!/bin/sh

#NOTE: THIS FILE MUST HAVE UNIX LINE-ENDINGS AS IT WILL RUN IN A LINUX CONTAINER

echo "******************************************************************************************"
echo "Waiting for the eureka server to start at: $EUREKASERVER_HOST:$EUREKASERVER_PORT"
echo "******************************************************************************************"
while ! nc -z "$EUREKASERVER_HOST" "$EUREKASERVER_PORT"; do sleep 3; done
echo ">>>>>>>>>>>>>>>>> Eureka Server has started"
echo ""

echo "******************************************************************************************"
echo "Starting @project.artifactId@"
echo "******************************************************************************************"
java  -Deureka.client.serviceUrl.defaultZone="$EUREKASERVER_URI" \
      -Dspring.profiles.active="$PROFILE" \
      -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
