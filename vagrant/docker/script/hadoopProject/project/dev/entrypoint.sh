#init
case $HADOOP_PROFILE in  
hdfs)  
  sh $HADOOP_HOME/sbin/start-dfs.sh
;;  
yarn)  
  sh $HADOOP_HOME/sbin/start-yarn.sh
;;  
balancer)  
sh $HADOOP_HOME/sbin/start-balancer.sh
;;
esac  
exit 0  