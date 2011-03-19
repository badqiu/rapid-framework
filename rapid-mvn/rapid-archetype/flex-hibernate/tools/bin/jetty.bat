@echo off
echo [INFO] Use maven jetty-plugin run the project.

cd ..\..\

set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m
call mvn jetty:run -Djetty.port=8080
pause