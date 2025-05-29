FROM docker.io/library/openjdk:17-alpine

ENV USER_NAME 135

VOLUME /tmp

MAINTAINER portoitapoa

ENV PROFILE=

ENV TZ=America/Sao_Paulo

COPY target/faturamento-fast.jar /opt/app.jar

USER $USER_NAME

WORKDIR /opt

ENTRYPOINT java -Xms64m -Xmx128m -Xss512k -XX:MaxMetaspaceSize=128m -XX:InitialRAMPercentage=25 -XX:MinRAMPercentage=50 -XX:MaxRAMPercentage=80 -Djava.security.egd=file:/dev/./urandom -jar /opt/app.jar $PROFILE

EXPOSE 2179