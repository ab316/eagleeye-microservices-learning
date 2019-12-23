#!/bin/sh

#NOTE: THIS FILE MUST HAVE UNIX LINE-ENDINGS AS IT WILL RUN IN A LINUX CONTAINER

echo "******************************************************************************************"
echo "Waiting for the configuration server to start at: $CONFIGSERVER_HOST:$CONFIGSERVER_PORT"
echo "******************************************************************************************"
while ! nc -z "$CONFIGSERVER_HOST" "$CONFIGSERVER_PORT"; do sleep 3; done
echo ">>>>>>>>>>>>>>>>> Configuration Server has started"


echo "******************************************************************************************"
echo "Waiting for the database server to start at: $DATABASE_HOST:$DATABASE_PORT"
echo "******************************************************************************************"
while ! nc -z "$DATABASE_HOST" "$DATABASE_PORT"; do sleep 3; done
echo ">>>>>>>>>>>>>>>>> Database Server has started"


echo "******************************************************************************************"
echo "Starting @project.artifactId@ with configuration server at: $CONFIGSERVER_URI"
echo "******************************************************************************************"
java  -Dspring.cloud.config.uri="$CONFIGSERVER_URI" \
      -Dspring.profiles.active="$PROFILE" \
      -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
