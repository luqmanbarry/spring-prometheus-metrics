#!/bin/bash

APP_HOST=${1}

if [ -z "${APP_HOST}" ] || [ "${APP_HOST}" == " "]; then
  echo "Application host required. Exiting..."
  exit -1;
fi

while true; do

  echo "Invoking / endpoint: "
  curl "${APP_HOST}/"

  echo "\n======================================>\n"
#  sleep 0.5s

  echo "Invoking /todos/add endpoint: "
  curl "${APP_HOST}/todos/add"

  echo "\n======================================>\n"
#  sleep 0.5s

  echo "Invoking /todos endpoint: "
  curl "${APP_HOST}/todos"

  echo "\n======================================>\n"
#  sleep 0.5s

  echo "Listing actuator endpoints : "
  curl "${APP_HOST}/actuator"

  echo "\n======================================>\n"
#  sleep 0.5s

  echo "View application /actuator/info details : "
  curl "${APP_HOST}/actuator/info"

  echo "\n======================================>\n"

  echo "View application JVM based metrics : "
  curl "${APP_HOST}/actuator/metrics"

  echo "\n======================================>\n"
#  sleep 0.5s

  echo "List custom prometheus metrics : "
  curl "${APP_HOST}/actuator/prometheus" | grep "prometheus-metrics"

  echo "\n======================================>\n"

  sleep 0.5s;
done

