FROM openjdk:11
COPY target/MySocialNetworkingSite-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
CMD java -jar ./MySocialNetworkingSite-0.0.1-SNAPSHOT.jar