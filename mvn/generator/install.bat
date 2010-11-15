mvn clean install -Dmaven.test.skip=true
mvn clean jar:jar source:jar install -Dmaven.test.skip=true