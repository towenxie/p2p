# Hadoop for Docker

##Prerequisites
- ubuntu:12.04.5 (docker image)
- hadoop-2.6.2.tar.gz
- jdk-8u45-linux-x64.tar.gz
- deploy scripts

		#download build scripts
		git clone git@git.augmentum.com.cn:knowledgebase/bigdata-and-infrastructure.git

## Build jdk image

Base on the `ubuntu:12.04.5` (docker image)

	# open the jdk dockerfile folder
	$ cd /home/user/bigdata-and-infrastructure/docker/script/dockerfiles/jdk/1.8.0/
	$ ./imageCreation.sh
**Notes**: This jdk image(`192.168.225.210:5000/jdk:1.8.0-aug`) has been built and upload to local docker image registry(`192.168.225.210:5000`). Please refer to the section `Docker Local 
Registry` in `docker_quick_guide.md`.

## Build hadoop image

Base on the `192.168.225.210:5000/jdk:1.8.0-aug` (docker image)

	# open the hadoop dockerfile folder
	$ cd /home/user/bigdata-and-infrastructure/docker/script/dockerfiles/hadoop/2.6.2/
	$ ./imageCreation.sh

**Notes**: This jdk image(`192.168.225.210:5000/hadoop:2.6.2-aug`) has been built and upload to local docker image registry(`192.168.225.210:5000`). Please refer to the section `Docker Local 
Registry` in `docker_quick_guide.md`.

## Deploy Hadoop
Base on the 192.168.225.210:5000/hadoop:2.6.2-aug (docker image)

For example, build three containers: `master`, `slave1`, `slave2`

### Single host Env
#### Build container

	$ cd /home/user/bigdata-and-infrastructure/docker/script/hadoopProject/project/dev/
	$ ./startContainerofSingle.sh

### Multiple host Env

	$ cd ../docker/script/kubernetes/script 
	# create pod
	$ kubecfg -h http://192.168.225.210:8080 -c hadoop-master.json create pods
 	$ kubecfg -h http://192.168.225.210:8080 -c hadoop-slave.json create pods

### Edit hadoop config file
	$ cd /home/user/bigdata-and-infrastructure/docker/script/hadoopProject/project/dev/config/
1. vim hadoop-env.sh

		JAVA_HOME=/usr/lib/jvm/jdk8/jdk1.8.0_45
2. vim masters

		master
3. vim slaves

		master
		slave1
		slave2
4. core-site.xml, hdfs-site.xml, mapred-site.xml, yarn-site.xml

### Config container
1. Get three container ip and edit hosts file.
	
		--$docker exec CID ifconfig 
		--$docker inspect --format='{{.NetworkSettings.IPAddress}}' master
		$ vim /etc/hosts
			172.17.0.2 master
			172.17.0.3 slave1
			172.17.0.4 slave2
2. start ssh.

		$ service ssh start
3. copy config files

		$ cp -r /hadoopProject/config/* $HADOOP_CONFIG_HOME
4. only for start `master` firstly

		$ hadoop namenode -format

### Start & stop hadoop env
Only for `master`,

	#start hadoop
	$ start-all.sh 
	
	#stop hadoop
	$ stop-all.sh

### Test hadoop env

On `master`,

	$ jps
	17785 SecondaryNameNode 
	17436 NameNode 
	17591 DataNode 
	18096 NodeManager 
	17952 ResourceManager

On slave1 and slave2,

	$ jps
	17591 DataNode 
	18096 NodeManager 

Web page:
	
 	Cluster status: http://172.17.0.2:8088 
	HDFS status: http://172.17.0.2:50070 
	Secondary NameNode status: http://172.17.0.2:50090 