FROM arm64v8/openjdk:15.0.1-slim-buster

RUN apt-get update && apt-get install -y --no-install-recommends python3 pipenv python3-pip zip unzip

RUN useradd -m doh

USER doh

COPY ./dohkt/app ~/app

WORKDIR ~/app

RUN ./gradlew :app:distZip

RUN mkdir -p ~/analyzer
COPY ./analyze-experiment ~/analyzer

WORKDIR ~/analyzer

RUN pipenv install

CMD /bin/bash
