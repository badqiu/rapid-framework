# 异步的IO #

在某些场景，我们需要使用异步的IO提高IO效率。

rapid中有AayncOutputStream及AsyncWriter可以使用.

## AasynOutputStream使用示例 ##
```
//启动异步IO，后台将启动一个线程，进行异步IO数据的写入
BufferedOutputStream output = new BufferedOutputStream(new AsyncOutputStream(new FileOutputStream("c:/debug.log")));

//异步写入数据
output.write("foo".getBytes());

//关闭异步IO，异步线程将停止并close
output.close(); 
```