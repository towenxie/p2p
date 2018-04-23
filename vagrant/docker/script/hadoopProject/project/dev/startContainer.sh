#init
imageName='192.168.225.210:5000/hadoop:2.6.2-aug'

sudo mkdir -p /hadoopProject
sudo mkdir -p /hadoop/hadoopHDFS/tmp
sudo mkdir -p /hadoop/hadoopHDFS/dfs/name
sudo mkdir -p /hadoop/hadoopHDFS/dfs/data

sudo docker pull $imageName

# start the hdfs.
sudo docker run --name=hadoopHDFS -v=/hadoopProject/project/dev:/hadoopProject -v=/hadoop/hadoopHDFS/tmp:/hadoop/tmp -v=/hadoop/hadoopHDFS/dfs/name:/hadoop/dfs/name -v=/hadoop/hadoopHDFS/dfs/data:/hadoop/dfs/data -p=9000:9000 -e HADOOP_PROFILE=hdfs --entrypoint=entrypoint.sh -d $imageName

# start the yarn.
sudo docker run --name=hadoopYarn -p=8032:8032 -p=8030:8030 -p=8031:8031 -p=8033:8033 -p=8088:8088 -e HADOOP_PROFILE=yarn --entrypoint=/hadoopProject/entrypoint.sh -d $imageName


