@echo off
setlocal

if "%JAVA_HOME%" == "" set _JAVACMD=java.exe
if not exist "%JAVA_HOME%\bin\java.exe" set _JAVACMD=java.exe
if "%_JAVACMD%" == "" set _JAVACMD="%JAVA_HOME%\bin\java.exe"

set CLI_DIR=%~dp0..
set LIB_DIR=%~dp0\..\clilib
set LOGS_DIR=%~dp0\..\logs
set CLASSPATH=.

for %%a in ("%LIB_DIR%\*.*") do call :process %%~nxa
goto :next

:process
set CLASSPATH=%CLASSPATH%;%LIB_DIR%\%1
goto :end

:next

%_JAVACMD% -Dlogback.configurationFile=logback-cli.xml -Dartadmin.logs.dir="%LOGS_DIR%" -cp "%CLASSPATH%" org.artifactory.cli.main.ArtAdmin %*
echo on

@endlocal
:end
