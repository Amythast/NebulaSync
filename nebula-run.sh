#!/bin/bash
echo -- -- begin init nebula... -- --

COMPOSE_FILE=./nebula-docs/docker/docker-compose.yml
JAR_DIR=./nebula-docs/docker/jar

echo -- -- stop and remove old docker-compose containers -- --
if docker-compose -f ${COMPOSE_FILE} ps
    then
        docker-compose -f ${COMPOSE_FILE} stop
        docker-compose -f ${COMPOSE_FILE} rm --force
fi

echo -- -- building jar -- --
mvn clean package -Dmaven.test.skip=true

echo -- -- move jar to ${JAR_DIR} -- --
if [ ! -d ${JAR_DIR} ];then
   mkdir -p ${JAR_DIR}
fi

rm -rf ${JAR_DIR}/nebula-authentication*.jar
rm -rf ${JAR_DIR}/nebula-rbac*.jar
rm -rf ${JAR_DIR}/nebula-service-governance*.jar
rm -rf ${JAR_DIR}/nebula-gateway-zuul*.jar

cp ./nebula-authentication/target/nebula-authentication*.jar ${JAR_DIR}
cp ./nebula-rbac/target/nebula-rbac*.jar ${JAR_DIR}
cp ./nebula-service-governance/target/nebula-service-governance*.jar ${JAR_DIR}
cp ./nebula-gateway-zuul/target/nebula-gateway-zuul*.jar ${JAR_DIR}

echo -- -- run docker-compose up -- --
docker-compose -f ${COMPOSE_FILE} up -d --build

docker images|grep none|awk '{print $3 }'|xargs docker rmi
