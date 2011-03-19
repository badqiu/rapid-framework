@echo off
echo [INFO] Use maven eclipse-plugin download jars and generate eclipse project files.
echo [Info] Please add "-Declipse.workspace=<path-to-eclipse-workspace>" at end of mvn command.

cd ..\..\
call mvn eclipse:clean eclipse:eclipse
pause