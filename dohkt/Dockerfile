FROM openjdk:11-slim-buster AS builder

RUN apt-get update -qq \
  && apt-get install --no-install-recommends unzip \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /src
COPY . /src

# ENV _JAVA_OPTIONS="-Xmx800m"
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel=true -Dorg.gradle.workers.max=2 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=2 -XX:ParallelGCThreads=2 -Djava.util.concurrent.ForkJoinPool.common.parallelism=2 -XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport"

RUN ./gradlew clean :app:distZip
RUN unzip /src/app/build/distributions/app.zip -d /app

FROM openjdk:11-jre-slim-buster

WORKDIR /app

COPY --from=builder /app /app

CMD ["ls", "/app"] 
