master1_ip=`docker inspect --format='{{.NetworkSettings.IPAddress}}' master1`
slave1_ip=`docker inspect --format='{{.NetworkSettings.IPAddress}}' slave1`
slave2_ip=`docker inspect --format='{{.NetworkSettings.IPAddress}}' slave2`

ips=$master1_ip"\tmaster1\n"$slave1_ip"\tslave1\n"$slave2_ip"\tslave2"

echo $ips > /hadoopProject/project/ips
#docker exec master1 cat /etc/hosts

