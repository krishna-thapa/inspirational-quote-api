# Pull base image
FROM openjdk:11

LABEL developer="Krishna Thapa"
LABEL maintainer="krishna.thapa91@gmail.com"
LABEL version="1.0"
LABEL description="Scala play project to create a backend endpoints for an inspirational quotes using Postgres, Redis and\
elasticsearch as databases. Its purely for the learning purpose only."

# Setup adapted from https://github.com/hseeberger/scala-sbt/blob/master/debian/Dockerfile
RUN \
  apt-get update -q && \
  apt-get upgrade -qq && \
  apt-get install -y git && \
  rm -rf /var/lib/apt/lists/*

# Any RUN command after any ARG is declared, it has that value in it as an environment variable and thus
# invalidates layer cache, so only declaring these ARGs when they're used

ARG SBT_VERSION=1.7.1

RUN \
  curl -fsL "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar xfz - -C /usr/share && \
  chown -R root:root /usr/share/sbt && \
  chmod -R 755 /usr/share/sbt && \
  ln -s /usr/share/sbt/bin/sbt /usr/local/bin/sbt

# Do I need to install Scala also?

# Define working directory and copy all the projects to the container
WORKDIR /inspirational-quote-api
ADD . /inspirational-quote-api

# Run the sbt commands once the docker is run
CMD sbt clean compile run