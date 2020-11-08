FROM arm64v8/openjdk:15.0.1-slim-buster

RUN apt-get update && apt-get install -y --no-install-recommends python3 pipenv python3-pip zip unzip

COPY ./dohkt/app/build/distributions/app.zip /tmp/app.zip
RUN mkdir -p ~/doh
RUN unzip -d ~/doh /tmp/app.zip

RUN mkdir -p ~/analyzer
COPY ./analyze-experiment /root/analyzer

WORKDIR /root/analyzer

RUN pipenv install

CMD ~/doh/app/bin/app
