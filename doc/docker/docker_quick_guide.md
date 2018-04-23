# Docker Quick Guide

##Prerequisites
Docker requires a 64-bit installation regardless of your Ubuntu version. Additionally, your kernel must be 3.10 at minimum. The latest 3.10 minor version or a newer maintained version are also acceptable.

Ubuntu Precise 12.04 (LTS)

1. Update your package manager.

		$ sudo apt-get update
2. Install both the required and optional packages.
	
		$ sudo apt-get install linux-image-generic-lts-trusty
3. Reboot your host.

		$ sudo reboot
## Install Docker

Make sure you have installed the prerequisites for your Ubuntu version. Then, install Docker using the following:

1. Log into your Ubuntu installation as a user with sudo privileges.

2. Update your apt package index

		$ sudo apt-get update
3. Install Docker.

		$ sudo apt-get install docker-engine
4. Start the docker daemon.

		$ sudo service docker start
5. Create the docker group and add your user 

		$ sudo groupadd docker  
		$ sudo usermod -aG docker user
6. Log out and log back in.
5. Verify docker is installed correctly.

		$ docker run hello-world

## Upgrade && Uninstall Docker

**Upgrade**

To install the latest version of Docker with apt-get:

	$ apt-get upgrade docker-engine

**Uninstall**

To uninstall the Docker package:

	$ sudo apt-get purge docker-engine

To uninstall the Docker package and dependencies that are no longer needed:

	$ sudo apt-get autoremove
The above commands will not remove images, containers, volumes, or user created configuration files on your host. If you wish to delete all images, containers, and volumes run the following command:

	$ rm -rf /var/lib/docker
You must delete the user created configuration files manually.


## Docker Local Registry

**Setup registry server**

	# (/data/registry) is new store image path.
	$ docker run -d -p 5000:5000  --restart=always --name registry -v /data/registry:/var/lib/registry -e DOCKER_REGISTRY_CONFIG=/usr/docker/config/registry/config.yml registry:2
	
**Notes**: The docker registry server has been set up in `192.168.225.210`. you can use it directly.

**Configure registry on client**

    # Config the 210 server ip for you client
	$ vim /etc/default/docker
	#add or edit the DOCKER_OPTS
	DOCKER_OPTS="--insecure-registry 192.168.225.210:5000"
	
	$ service docker restart



**How to pull image from hub?**

		$ docker pull ubuntu:12.04

**How to push and pull image to 210 server?**

1. Tag the image so that it points to the registry 
	
		$ docker tag ubuntu:12.04 192.168.225.210:5000/ubuntu:12.04
2. Push it to 210 server

		$ docker push 192.168.225.210:5000/ubuntu:12.04
3. Pull it back from 210 server for group members

		$ docker pull 192.168.225.210:5000/ubuntu:12.04
4. Others.  

		$vim /etc/sysconfig/docker other_args
		other_args='--insecure-registry 192.168.225.210:5000 --graph=/data/docker'  	
**How to show images in 210 server?**

		$ curl -X GET http://192.168.225.210:5000/v2/_catalog
	    {"repositories":["ubuntu"]}
		$ curl -X GET http://192.168.225.210:5000/v2/ubuntu/tags/list
	    {"name":"ubuntu","tags":["12.04"]}
