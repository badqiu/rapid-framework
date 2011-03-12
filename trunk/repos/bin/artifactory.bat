@echo off
title Artifactory
echo.
echo Starting Artifactory...
echo.
echo To stop, press Ctrl+c
setlocal

if "%JAVA_HOME%" == "" set _JAVACMD=java.exe
if not exist "%JAVA_HOME%\bin\java.exe" set _JAVACMD=java.exe
if "%_JAVACMD%" == "" set _JAVACMD="%JAVA_HOME%\bin\java.exe"

set ARTIFACTORY_HOME=%~dp0..
set LIB_DIR=%ARTIFACTORY_HOME%\lib
set CLASSPATH=%ARTIFACTORY_HOME%\artifactory.jar

for %%a in ("%LIB_DIR%\*.*") do call :process %%~nxa
goto :next

:process
set CLASSPATH=%CLASSPATH%;%LIB_DIR%\%1
goto :end

:next

%_JAVACMD% -server -Xmx400m -Djetty.home="%ARTIFACTORY_HOME%" -Dartifactory.home="%ARTIFACTORY_HOME%" -Dfile.encoding=UTF8 -cp "%CLASSPATH%" org.artifactory.standalone.main.Main %*

@endlocal
:end
