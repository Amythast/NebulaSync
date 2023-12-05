#!/bin/bash
echo -- -- begin init nebula eureka... -- --

COMPOSE_FILE=./nebula-docs/docker/docker-compose-zipkin.yml

echo -- -- stop and remove old docker-compose containers -- --
if docker-compose -f ${COMPOSE_FILE} ps
    then
        docker-compose -f ${COMPOSE_FILE} stop
        docker-compose -f ${COMPOSE_FILE} rm --force
fi

echo -- -- run docker-compose up -- --
docker-compose -f ${COMPOSE_FILE} up -d

docker images|grep none|awk '{print $3 }'|xargs docker rmi
