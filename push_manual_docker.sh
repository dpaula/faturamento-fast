#!/bin/bash

echo "Iniciando.."
echo "LEMBRE-SE!!! MAVEN CLEAN INSTALL!!!"
echo "Escolha o ambiente (develop, staging, prod):"
# shellcheck disable=SC2162
read env
HASH=$(git rev-parse --short HEAD)
ENV=$env
DATE=$(date +%Y%m%d%H%M%S)


true > .env
# shellcheck disable=SC2059
printf "IMAGE=$ENV-$HASH-$DATE" > .env

#mvn clean install --batch-mode --errors --fail-at-end --show-version
docker-compose build
docker-compose push

rm -rf .env

