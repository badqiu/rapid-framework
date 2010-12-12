
#modify dos console size
mode con cols=120 lines=3000

mvn groovy:execute -DexecuteTarget=%1 -DgenInputCmd=%2 -errors