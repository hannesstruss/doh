FROM arm64v8/openjdk:15.0.1-slim-buster

RUN apt-get update && apt-get install -y --no-install-recommends python3 pipenv python3-pip python3-dev zip unzip build-essential

RUN useradd -m doh

RUN ls /home
ENV HOME /home/doh

COPY ./dohkt $HOME/dohkt
RUN chown -R doh: $HOME

WORKDIR $HOME/dohkt

RUN ./gradlew :app:distZip
USER doh
RUN pwd
RUN ls -l

COPY ./analyze-experiment $HOME/analyzer

WORKDIR $HOME/analyzer

RUN pipenv install

CMD /bin/bash
