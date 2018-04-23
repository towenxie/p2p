#Build hadoop container image

FROM 192.168.225.210:5000/ubuntu:latest-dh

MAINTAINER augmentum

# Make base software directory
RUN mkdir -p /usr/local/software

# Download the softwares
ADD http://192.168.225.210:10080/software/jdk-8u45-linux-x64.tar.gz /usr/local/software

# Make java install directory
RUN mkdir -p /usr/lib/jvm/jdk8

RUN cd /usr/local/software && tar -zxvf jdk-8u45-linux-x64.tar.gz
RUN mv /usr/local/software/jdk1.8.0_45/ /usr/lib/jvm/jdk8/
 
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk8/jdk1.8.0_45/bin/java 100
RUN update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk8/jdk1.8.0_45/bin/javac 100

ENV JAVA_HOME /usr/lib/jvm/jdk8

RUN apt-get update
RUN apt-get install -y apt-utils 
RUN apt-get install -y vim
RUN apt-get install -y ssh
RUN apt-get clean

USER root

# passwordless ssh
RUN rm -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -q -N "" -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN rm -f /etc/ssh/ssh_host_rsa_key
RUN ssh-keygen -q -N "" -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN rm -f /root/.ssh/id_rsa
RUN ssh-keygen -q -N "" -t rsa -f /root/.ssh/id_rsa
RUN cp /root/.ssh/id_rsa.pub /root/.ssh/authorized_keys

RUN rm -r /usr/local/software
