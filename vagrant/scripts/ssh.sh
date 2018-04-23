# 1. create the local host ssh public and private key
ssh-keygen -t  rsa
# 2. copy the public key to remote host
ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.0.3
# 3. test ssh remote host
ssh 192.168.0.3