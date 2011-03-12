#!/bin/sh

if [ -n "$1" ]; then
    ARTIFACTORY_USER=$1
fi
if [ -z "$ARTIFACTORY_USER" ]; then
    ARTIFACTORY_USER=artifactory
fi

echo
echo "Installing artifactory as a Unix service that will run as user ${ARTIFACTORY_USER} "

curUser=`id -nu`
if [ "$curUser" != "root" ]
then
    echo
    echo "\033[31m** ERROR: Only root user can install artifactory as a service\033[0m"
    echo
    exit 1
fi

errorArtHome() {
    echo
    echo "\033[31m** ** ERROR: $1 \033[0m"
    echo
    exit 1
}

if [ "$0" = "." ] || [ "$0" = "source" ]; then
    errorArtHome "Cannot execute script with source $0"
fi

curdir=`dirname $0` || errorArtHome "Cannot find ARTIFACTORY_HOME=$curdir/.."
curdir=`cd $curdir; pwd` || errorArtHome "Cannot finddefau ARTIFACTORY_HOME=$curdir/.."
cd $curdir/.. || errorArtHome "Cannot go to ARTIFACTORY_HOME=$curdir/.."

ARTIFACTORY_HOME=`pwd`
if [ -z "$ARTIFACTORY_HOME" ] || [ "$ARTIFACTORY_HOME" = "/" ]; then
    errorArtHome "ARTIFACTORY_HOME cannot be the root folder"
fi

echo
echo "Installing artifactory with home ${ARTIFACTORY_HOME}"

echo -n "Creating user ${ARTIFACTORY_USER}..."
artifactoryUsername=`id -nu ${ARTIFACTORY_USER}`
if [ "$artifactoryUsername" = "${ARTIFACTORY_USER}" ]; then
    echo -n "already exists..."
else
    echo -n "creating..."
    useradd -m ${ARTIFACTORY_USER}
    if [ ! $? ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo
echo -n "Checking configuration link and files in /etc/artifactory..."
if [ -L ${ARTIFACTORY_HOME}/etc ]; then
    echo -n "already exists, no change..."
else
    echo
    echo -n "Moving configuration dir etc to etc.original"
    mv ${ARTIFACTORY_HOME}/etc ${ARTIFACTORY_HOME}/etc.original && \
    etcOK=true
    if [ ! $etcOK ]; then
       echo
       echo "\033[31m** ERROR\033[0m"
       echo
       exit 1
    fi
    echo "\033[32mDONE\033[0m"
    if [ ! -d /etc/artifactory ]; then
        echo -n "creating dir /etc/artifactory..."
        mkdir -p /etc/artifactory && \
        etcOK=true
    fi
    if [ $etcOK = true ]; then
        echo -n "creating the link and updating dir..."
        ln -s /etc/artifactory etc && \
        cp -Ri ${ARTIFACTORY_HOME}/etc.original/* /etc/artifactory/ && \
        etcOK=true
    fi
    if [ ! $etcOK ]; then
        echo
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo -n "Creating environment file /etc/artifactory/default..."
if [ -e /etc/artifactory/default ]; then
    echo -n "already exists, no change...  "
    echo "\033[33m*** Make sure your default file is up to date ***\033[0m"
else
    # Populating the /etc/artifactory/default with ARTIFACTORY_HOME and ARTIFACTORY_USER
    echo -n "creating..."
    cat ${ARTIFACTORY_HOME}/bin/artifactory.default > /etc/artifactory/default && \
    echo "export ARTIFACTORY_HOME=${ARTIFACTORY_HOME}" >> /etc/artifactory/default && \
    echo "export ARTIFACTORY_USER=${ARTIFACTORY_USER}" >> /etc/artifactory/default && \
    etcDefaultOK=true
    if [ ! $etcDefaultOK ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"
echo "** INFO: Please edit the files in /etc/artifactory to set the correct environment"
echo "Especially /etc/artifactory/default that defines ARTIFACTORY_HOME, JAVA_HOME and JAVA_OPTIONS"
echo
echo -n "Creating link ${ARTIFACTORY_HOME}/logs to /var/log/artifactory..."
if [ -L ${ARTIFACTORY_HOME}/logs ]; then
    echo -n "already a link..."
else
    echo -n "creating..."
    artLogFolder=/var/log/artifactory
    logsOK=false
    if [ ! -d "$artLogFolder" ]; then
        mkdir -p $artLogFolder && \
        logsOK=true
    else
        logsOK=true
    fi
    if $logsOK; then
        if [ -d ${ARTIFACTORY_HOME}/logs ]; then
            mv ${ARTIFACTORY_HOME}/logs ${ARTIFACTORY_HOME}/logs.orig
        fi
        ln -s /var/log/artifactory logs && \
        logsOK=true
    fi
    if [ ! $logsOK ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"

echo
echo -n "Setting file permissions to etc, logs, work, data and backup..."
chown ${ARTIFACTORY_USER} -R /etc/artifactory && \
chmod 755 -R /etc/artifactory && \
chown ${ARTIFACTORY_USER} -R ${ARTIFACTORY_HOME}/logs/ && \
chmod u+w -R ${ARTIFACTORY_HOME}/logs/ && \
mkdir -p ${ARTIFACTORY_HOME}/work/ && \
chown ${ARTIFACTORY_USER} ${ARTIFACTORY_HOME}/work/ && \
chmod u+w -R ${ARTIFACTORY_HOME}/work/ && \
mkdir -p ${ARTIFACTORY_HOME}/backup/ && \
chown ${ARTIFACTORY_USER} ${ARTIFACTORY_HOME}/backup/ && \
chmod u+w ${ARTIFACTORY_HOME}/backup/ && \
mkdir -p ${ARTIFACTORY_HOME}/data/ && \
chown ${ARTIFACTORY_USER} -R ${ARTIFACTORY_HOME}/data/ && \
chmod u+w -R ${ARTIFACTORY_HOME}/data/ && \
permChangeOK=true
if [ ! $permChangeOK ]; then
    echo "\033[31m** ERROR\033[0m"
    echo
    exit 1
fi
echo "\033[32mDONE\033[0m"
echo
echo -n "Copying the init.d/artifactory script..."
if [ -e /etc/init.d/artifactory ]; then
    echo -n "already exists..."
else
    echo -n "copying..."
    cp ${ARTIFACTORY_HOME}/bin/artifactoryctl /etc/init.d/artifactory
    if [ ! $? ]; then
        echo "\033[31m** ERROR\033[0m"
        echo
        exit 1
    fi
fi
echo "\033[32mDONE\033[0m"
echo
# Try update-rc.d for debian/ubuntu else use chkconfig
if [ -x /usr/sbin/update-rc.d ]; then
    echo "Initializing artifactory service with update-rc.d..."
    update-rc.d artifactory defaults && \
    chkconfigOK=true
else
    echo "Initializing artifactory service with chkconfig..."
    chkconfig --add artifactory && \
    chkconfig artifactory on && \
    chkconfig --list artifactory && \
    chkconfigOK=true
fi
if [ ! $chkconfigOK ]; then
    echo "\033[31m** ERROR\033[0m"
    echo
    exit 1
fi
echo "\033[32mDONE\033[0m"
echo
echo "\033[32m************ SUCCESS *****************\033[0m"
echo "Installation of Artifactory completed"
echo "you can now check installation by running:"
echo "> service artifactory check"
echo
echo "Then activate artifactory with:"
echo "> service artifactory start"
echo
