#!/bin/sh

curUser=`id -nu`
if [ "$curUser" != "root" ]
then
    echo
    echo -e "\033[31m** ERROR: Only root user can install artifactory on Tomcat and set it as a service\033[0m"
    echo
    exit 1
fi

curDir=`dirname $0`
curDir=`cd $curDir; pwd`

# Finding good artifactory home
ARTIFACTORY_HOME="/opt/artifactory/current"
if [ ! -d "$ARTIFACTORY_HOME" ]; then
    ARTIFACTORY_HOME=`cd $curDir/..; pwd -P`
fi

artHome=""
echo "Where is Artifactory [default $ARTIFACTORY_HOME]?"
read artHome
if [ -n "$artHome" ]; then
    if [ -d "$artHome" ]; then
        ARTIFACTORY_HOME="$artHome"
    else
        echo "\033[31m** ERROR: $artHome is not a folder"
        exit 1
    fi
fi

TOMCAT_HOME="/opt/tomcat/artifactory"
if [ ! -d "$TOMCAT_HOME" ]; then
    TOMCAT_HOME="/opt/tomcat/current"
fi

tomcatHome=""
echo "Where is Tomcat for Artifactory [default $TOMCAT_HOME]?"
read tomcatHome
if [ -n "$tomcatHome" ]; then
    if [ -d "$tomcatHome" ]; then
        TOMCAT_HOME="$tomcatHome"
    fi
fi

if [ ! -d "$TOMCAT_HOME" ]; then
    echo "\033[31m** ERROR: Tomcat home $TOMCAT_HOME is not a folder"
    exit 1
fi

if [ ! -d "$ARTIFACTORY_HOME" ]; then
    echo "\033[31m** ERROR: Artifactory home $ARTIFACTORY_HOME is not a folder"
    exit 1
fi

$curDir/install.sh || ( echo "Could not execute standard install script $curDir/install.sh" && exit 1 )

# Since tomcat 6.0.24 the PID file cannot be created before running catalina.sh. Using /var/run/artifactory folder.
artRunFolder=/var/run/artifactory
if [ ! -d "$artRunFolder" ]; then
    mkdir "$artRunFolder" || ( echo "Could not create /var/run/artifactory" && exit 1 )
fi
chown -R artifactory "$artRunFolder"

echo "" >> /etc/artifactory/default
echo "export TOMCAT_HOME=$TOMCAT_HOME" >> /etc/artifactory/default
echo "export CATALINA_PID=$artRunFolder/artifactory.pid" >> /etc/artifactory/default

tomFiles=$curDir/../misc/tomcat
if [ -e "/etc/init.d/artifactory" ]; then
    cp -f /etc/init.d/artifactory $tomFiles/artifactory.init.backup
fi
cp -f $tomFiles/artifactory /etc/init.d/artifactory
chmod a+x /etc/init.d/artifactory
cp $tomFiles/setenv.sh $TOMCAT_HOME/bin
chmod a+x $TOMCAT_HOME/bin/*
cp $tomFiles/server.xml $TOMCAT_HOME/conf
mkdir -p $TOMCAT_HOME/conf/Catalina/localhost
cp $tomFiles/artifactory.xml $TOMCAT_HOME/conf/Catalina/localhost
chown -R artifactory $TOMCAT_HOME/conf
chown -R artifactory $TOMCAT_HOME/webapps
mv $TOMCAT_HOME/logs $TOMCAT_HOME/logs.orig
ln -s /var/log/artifactory $TOMCAT_HOME/logs
if [ -d $TOMCAT_HOME/work ];then
	chown -R artifactory $TOMCAT_HOME/work
fi
if [ ! -d $TOMCAT_HOME/temp ];then
        mkdir $TOMCAT_HOME/temp  
fi
chown -R artifactory $TOMCAT_HOME/temp	

if [ "x$1" = "xmod_jk" ]; then
    cp $tomFiles/artifactory.conf /etc/httpd/conf.d
fi

echo "All commands executed."
echo "Please check /etc/artifactory, $TOMCAT_HOME and $ARTIFACTORY_HOME folders"
echo "Please check /etc/init.d/artifactory startup script, and /etc/httpd/conf.d/artifactory.conf for mod_jk conf"
echo "To activate artifactory run (httpd if using mod_jk):"
echo "> service artifactory start"
echo "> service httpd reload"
