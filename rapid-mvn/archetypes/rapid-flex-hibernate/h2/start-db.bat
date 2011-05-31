@echo off
call mvn initialize -Pstartdb
@if errorlevel 1 pause