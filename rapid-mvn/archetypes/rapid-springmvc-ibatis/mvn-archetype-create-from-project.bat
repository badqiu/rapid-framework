call mvn eclipse:clean
call mvn clean
call mvn archetype:create-from-project  

cd target/generated-sources/archetype

call mvn install  -DcreateChecksum=true  


cd ../../../
call mvn eclipse:eclipse


REM OK
:end
pause