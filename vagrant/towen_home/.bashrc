# .bash_profile

# Get the aliases and functions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi


# User specific environment and startup programs
export TOOLS_HOME=/imsdata/tools
export TOMCAT_HOME=$TOOLS_HOME/apache-tomcat
export CATALINA_HOME=$TOMCAT_HOME
export ANT_HOME=$TOOLS_HOME/apache-ant
export JAVA_HOME=$TOOLS_HOME/java

export TOMCAT_OPTS=${CATALINA_OPTS}

export PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$TOMCAT_HOME/bin:$ANT_HOME/bin
shortHost=`uname -n|cut -d'.' -f1`
export PROMPT="$USER@$shortHost \w> "
export PS1="$PROMPT"
export PAGER='less -is'
export EDITOR=vi





