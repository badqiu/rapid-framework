call mvn clean
call mvn archetype:create-from-project

cd target/generated-sources/archetype

call mvn install

cd ../../../


REM -----------------------------------------------
REM mvn archetype:generate -DarchetypeCatalog=local
REM -----------------------------------------------