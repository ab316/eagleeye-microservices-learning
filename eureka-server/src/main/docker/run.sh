#!/bin/sh

#NOTE: THIS FILE MUST HAVE UNIX LINE-ENDINGS AS IT WILL RUN IN A LINUX CONTAINER

echo "******************************************************************************************"
echo "Starting @project.artifactId@"
echo "******************************************************************************************"
java  -Dspring.profiles.active="$PROFILE" \
      -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
