#Build hadoop container image

FROM 192.168.225.210:5000/jdk:1.8.0-aug

MAINTAINER augmentum

#Make base software directory
RUN mkdir -p /usr/local/software
RUN mkdir -p /hadoopProject
RUN mkdir -p /hadoop/tmp
RUN mkdir -p /hadoop/dfs/name
RUN mkdir -p /hadoop/dfs/data

#Download the softwares
ADD http://192.168.225.210:10080/software/hadoop-2.6.2.tar.gz /usr/local/software

#Make hadoop install directory
RUN mkdir -p /usr/lib/hadoop/

RUN cd /usr/local/software && tar -zxvf hadoop-2.6.2.tar.gz

RUN mv /usr/local/software/hadoop-2.6.2/ /usr/lib/hadoop/

# Will be changed after rebuilding the jdk image.
ENV JAVA_HOME /usr/lib/jvm/jdk8/jdk1.8.0_45

ENV HADOOP_HOME /usr/lib/hadoop/hadoop-2.6.2
ENV HADOOP_CONFIG_HOME $HADOOP_HOME/etc/hadoop

ENV PATH $PATH:$JAVA_HOME/bin
ENV PATH $PATH:$HADOOP_HOME/bin
ENV PATH $PATH:$HADOOP_HOME/sbin
ENV PATH $PATH:/hadoopProject

ENV CLASSPATH $CLASSPATH:$JAVA_HOME/jre/lib/rt.jar
ENV CLASSPATH $CLASSPATH:$JAVA_HOME/lib/dt.jar
ENV CLASSPATH $CLASSPATH:$JAVA_HOME/lib/tools.jar
ENV CLASSPATH $CLASSPATH:$HADOOP_HOME/share/hadoop/common/hadoop-common-2.6.2.jar
ENV CLASSPATH $CLASSPATH:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.6.2.jar
ENV CLASSPATH $CLASSPATH:$HADOOP_HOME/share/hadoop/common/lib/commons-cli-1.2.jar

RUN rm -r /usr/local/software