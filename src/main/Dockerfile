FROM centos:7

RUN yum update -y && \
yum install -y wget && \
yum install -y java-1.8.0-openjdk && \
yum clean all

COPY resources/edentist-0.0.1-SNAPSHOT.jar /opt

ENTRYPOINT java -Djava.net.preferIPv4Stack=true -Dspring.profiles.active=server -jar /opt/edentist-0.0.1-SNAPSHOT.jar