#!/bin/bash 
sudo apt-get clean
sudo apt-get install vim -y
sudo apt-get install htop -y

# timezone
sudo cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# add user: towen 
sudo useradd towen -m
sudo sh -c "echo 'towen  ALL=(ALL:ALL) ALL' >> /etc/sudoers"
sudo echo towen:towen | sudo chpasswd

# runtime folder
sudo rm -R /imsdata
sudo mkdir -p /imsdata/tools

# data folder
sudo rm -R /shared
sudo mkdir -p /shared

# tomcat
sudo tar zxvf /software/apache-tomcat-8.0.23.tar.gz -C /imsdata/tools
sudo ln -s /imsdata/tools/apache-tomcat-8.0.23 /imsdata/tools/apache-tomcat

# copy misc jar to tomcat
sudo cp /home/towen/images/dev/tools/tomcat/answersSimulationValve-tomcat7.jar /imsdata/tools/apache-tomcat/lib/
sudo cp /home/towen/images/dev/tools/tomcat/mvenvrewrite.jar /imsdata/tools/apache-tomcat/lib/

# java
sudo tar zxvf /software/jdk-8u45-linux-x64.gz -C /imsdata/tools
sudo ln -s /imsdata/tools/jdk1.8.0_45 /imsdata/tools/java


# change owner
sudo chown -R towen:vagrant /imsdata
sudo chown -R towen:vagrant /shared
sudo chown -R towen:vagrant /home/towen

echo "VM initialized!"