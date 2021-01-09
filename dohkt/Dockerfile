FROM debian:buster-slim

# Prevent installation of openjdk-11-jre failing:
# https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=863199#23
RUN mkdir -p /usr/share/man/man1

RUN apt-get update -qq \
  && apt-get install -y --no-install-recommends \
    openjdk-11-jre \
    unzip \
    imagemagick \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY ./app/build/distributions/app.zip /tmp
RUN unzip /tmp/app.zip -d /tmp/unzipped-app \
  && mv /tmp/unzipped-app/app/* /app \
  && rm -rf /tmp/app.zip /tmp/unzipped-app

# Add raspistill to path
ENV PATH="/opt/vc/bin:${PATH}"

CMD /app/bin/app
