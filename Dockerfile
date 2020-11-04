FROM openjdk:8

RUN apt-get update && apt-get install -y python3 pipenv python3-pip

COPY ./dohkt/app/build/distributions/app.zip /tmp/app.zip
RUN mkdir -p ~/doh
RUN unzip -d ~/doh /tmp/app.zip

RUN mkdir -p ~/analyzer
COPY ./analyze-experiment /root/analyzer

WORKDIR /root/analyzer

RUN pipenv install

CMD ~/doh/app/bin/app
