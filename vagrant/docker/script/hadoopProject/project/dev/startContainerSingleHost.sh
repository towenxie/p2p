#!/bin/bash 
imageName='192.168.225.210:5000/hadoop:2.6.2-aug-2'

sudo mkdir -p /hadoopProject
sudo mkdir -p /hadoop/hadoopHDFS/tmp
sudo mkdir -p /hadoop/hadoopHDFS/dfs/name
sudo mkdir -p /hadoop/hadoopHDFS/dfs/data/master1
sudo mkdir -p /hadoop/hadoopHDFS/dfs/data/slave1
sudo mkdir -p /hadoop/hadoopHDFS/dfs/data/slave2
sudo cp -r ./ /hadoopProject/project

sudo docker pull $imageName

sudo docker run --name=master1 -h=master1 -v=/hadoopProject/project:/hadoopProject -v=/hadoop/hadoopHDFS/tmp:/hadoop/tmp -v=/hadoop/hadoopHDFS/dfs/name:/hadoop/dfs/name -v=/hadoop/hadoopHDFS/dfs/data/master1:/hadoop/dfs/data -p=9000:9000 -p=8088:8088 -p=50070:50070 -p=50090:50090 -p 22 -it -d $imageName bash

sudo docker run --name=slave1 -h=slave1 -v=/hadoopProject/project:/hadoopProject -v=/hadoop/hadoopHDFS/tmp:/hadoop/tmp -v=/hadoop/hadoopHDFS/dfs/name:/hadoop/dfs/name -v=/hadoop/hadoopHDFS/dfs/data/slave1:/hadoop/dfs/data -p 22 -it -d $imageName bash

sudo docker run --name=slave2 -h=slave2 -v=/hadoopProject/project:/hadoopProject -v=/hadoop/hadoopHDFS/tmp:/hadoop/tmp -v=/hadoop/hadoopHDFS/dfs/name:/hadoop/dfs/name -v=/hadoop/hadoopHDFS/dfs/data/slave2:/hadoop/dfs/data -p 22 -it -d $imageName bash

sh ./initIps.sh

sudo docker exec slave1 sh /hadoopProject/initSlaveContainer.sh
sudo docker exec slave2 sh /hadoopProject/initSlaveContainer.sh
sudo docker exec master1 sh /hadoopProject/initMasterContainer.sh
sudo docker exec master1 sh /hadoopProject/entrypoint.sh
#docker exec -it master1 bash

echo "deploy successful"
