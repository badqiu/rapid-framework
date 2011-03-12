. /etc/artifactory/default
echo "Using ARTIFACTORY_HOME: $ARTIFACTORY_HOME"
export JAVA_OPTS="$JAVA_OPTIONS -Dartifactory.home=$ARTIFACTORY_HOME"
export CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.jasper.runtime.BodyContentImpl.LIMIT_BUFFER=true"
