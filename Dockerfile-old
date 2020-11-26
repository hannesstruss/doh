FROM arm64v8/openjdk:15.0.1-slim-buster

RUN apt-get update && apt-get install -y --no-install-recommends python3 pipenv python3-pip python3-dev zip unzip build-essential

RUN useradd -m doh

RUN ls /home
ENV HOME /home/doh

# COPY ./dohkt $HOME/dohkt
RUN chown -R doh: $HOME

WORKDIR $HOME/dohkt

USER doh
# RUN ./gradlew :app:distZip

COPY ./analyze-experiment $HOME/analyzer

WORKDIR $HOME/analyzer

RUN uname -a
# RUN pipenv install scikit_image-0.17.2-cp39-cp39-linux_aarch64.whl
# RUN pipenv install

USER root

CMD /bin/bash
