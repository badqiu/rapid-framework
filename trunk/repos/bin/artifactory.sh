#!/bin/sh

if [ -n "$JAVA_HOME" ]; then
    if [ -x "$JAVA_HOME/bin/java" ]; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        echo "** ERROR: java under JAVA_HOME=$JAVA_HOME cannot be executed"
        exit 1
    fi
else
    JAVACMD=`which java 2> /dev/null`
    if [ -z "$JAVACMD" ]; then
        JAVACMD=java
    fi
fi

# Verify that it is java 5+
javaVersion=`$JAVACMD -version 2>&1 | grep "java version" | egrep -e "1\.[56]"`
if [ -z "$javaVersion" ]; then
    $JAVACMD -version
    echo "** ERROR: The Java of $JAVACMD version is not 1.5 or 1.6"
    exit 1
fi

if [ -z "$ARTIFACTORY_HOME" ]; then
    ARTIFACTORY_HOME=`dirname "$0"`/..
fi

# Verify minimal JVM props are set
hasMinHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xms"`
if [ -z "$hasMinHeapSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -Xms512m"
fi
hasMaxHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xmx"`
if [ -z "$hasMaxHeapSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -Xmx512m"
fi
hasMinPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:PermSize"`
if [ -z "$hasMinPermSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -XX:PermSize=128m"
fi
hasMaxPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:MaxPermSize"`
if [ -z "$hasMaxPermSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -XX:MaxPermSize=128m"
fi
hasMinNewSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:NewSize"`
if [ -z "$hasMinNewSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -XX:NewSize=180m"
fi
hasMaxNewSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:MaxNewSize"`
if [ -z "$hasMaxNewSize" ]; then
    JAVA_OPTIONS="$JAVA_OPTIONS -XX:MaxNewSize=180m"
fi

JAVA_OPTIONS="$JAVA_OPTIONS -server -Djetty.home=$ARTIFACTORY_HOME -Dartifactory.home=$ARTIFACTORY_HOME -Dfile.encoding=UTF8"

LIB_DIR=$ARTIFACTORY_HOME/lib
CLASSPATH=$ARTIFACTORY_HOME/artifactory.jar
# Add all jars under the lib dir to the classpath
for i in `ls $LIB_DIR/*.jar`
do
    CLASSPATH="$CLASSPATH:$i"
done

echo "Runing: exec $JAVACMD$JAVA_OPTIONS -cp \"$CLASSPATH\" org.artifactory.standalone.main.Main $@"
exec "$JAVACMD" $JAVA_OPTIONS -cp "$CLASSPATH" org.artifactory.standalone.main.Main "$@"
