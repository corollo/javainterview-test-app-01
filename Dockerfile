FROM java:8

# Author
MAINTAINER luigi.corollo

# config volume to log access by local machine
VOLUME ./src/main/docker/javaexercise/logs /usr/local/javaexercise/logs

# set the working dir
WORKDIR /

# Copy run.sh in the image
COPY ./src/main/docker/javaexercise/run.sh /run.sh
RUN chmod 755 /run.sh

# add the jar-with-dependencies in the image
ADD ./target/javaexercise-jar-with-dependencies.jar /usr/local/javaexercise/javaexercise-jar-with-dependencies.jar
