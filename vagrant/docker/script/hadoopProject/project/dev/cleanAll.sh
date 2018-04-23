# clean all host data
echo "clean all host data"
sudo rm -r /hadoopProject
sudo rm -r /hadoop/hadoopHDFS

# clean all containers
echo "clean all containers"
docker stop master1
docker stop slave1
docker stop slave2
docker rm master1
docker rm slave1
docker rm slave2

echo "clean all successful"