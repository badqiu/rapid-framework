cd ..\..\

call mvn clean 
call mvn install -Dmaven.test.skip=true

pause