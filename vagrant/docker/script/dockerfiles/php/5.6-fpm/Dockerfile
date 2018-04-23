#Build container image

FROM 192.168.225.210:5000/ubuntu:latest-dh

MAINTAINER augmentum

# Make base software directory
RUN mkdir -p /usr/local/software

# Download the softwares
ADD http://192.168.225.210:10080/software/sources.list /usr/local/software

RUN cp /etc/apt/sources.list /etc/apt/sources_bak.list
RUN cp /usr/local/software/sources.list /etc/apt/

RUN apt-get update
RUN apt-get install dialog
RUN apt-get install -y python-software-properties
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:ondrej/php5 
RUN apt-get install -y php-pear php5-dev php5-fpm
RUN apt-get install -y libpcre3-dev
RUN apt-get install -y php-apc
RUN apt-get install -y php5-curl
RUN apt-get install -y php5-gd
RUN apt-get install -y php5-mcrypt
RUN php5enmod mcrypt
RUN apt-get install -y php5-memcache
RUN apt-get install -y php5-mysql
RUN apt-get install -y php5-xmlrpc
RUN service php5-fpm start
