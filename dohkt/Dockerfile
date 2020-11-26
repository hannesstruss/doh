FROM openjdk:11-jre-slim-buster

RUN apt-get update -qq \
  && apt-get install --no-install-recommends unzip \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY ./app/build/distributions/app.zip /tmp
RUN unzip /tmp/app.zip -d /tmp/unzipped-app \
  && mv /tmp/unzipped-app/app/* /app \
  && rm -rf /tmp/app.zip /tmp/unzipped-app

CMD /app/bin/app
