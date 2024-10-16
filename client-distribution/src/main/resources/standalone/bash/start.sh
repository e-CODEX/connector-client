#!/bin/sh

#
# This scripts assumes that java executable is on the PATH variable!
#

java -Dloader.path=./lib -cp ./bin/client-starter.jar -Dspring.config.name=connector-client -Dspring.config.location=config/connector-client.properties org.springframework.boot.loader.launch.PropertiesLauncher

