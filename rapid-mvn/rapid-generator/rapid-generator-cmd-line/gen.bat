
@IF DEFINED modeCmdExecuted GOTO START
@mode con cols=120 lines=3000
@set modeCmdExecuted="true"

:START
mvn groovy:execute -DexecuteTarget=%1 -DgenInputCmd=%2 -errors