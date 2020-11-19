"# thrift的数据传输格式"

1.TBinaryProtocol - 二进制格式  
2.TCompactProtpcol - 压缩格式  
3.TJSONProtocol - JSON格式  
4.TSimpleJSONProtocol - 提供JSON只写协议，生成的文件很容易通过脚本语言解析  
5.TDebugProtocol - 使用易懂的可读文本格式，以便于测试的时候进行debug

"# thrift的数据传输方式"

1.TSocket - 阻塞式socket  
2.TFramedTransport - 以frame（帧）为单位进行传输，非阻塞式服务器中使用  
3.TFileTransport - 以文件形式进行传输  
4.TMemoryTransport - 将内存用于I/O，java实现是内部使用了简单的ByteArrayOutputStream  
5.TZlibTransport - 使用zlib进行压缩，与其他传输方式联合使用，当前无java实现  

"# thrift支持的服务类型"

1.TSimpleServer - 简单的单线程服务模型，常用于测试  
2.TThreadPoolServer - 多线程服务模型，使用标准的阻塞式IO  
3.TNonblockingServer - 多线程服务模型，使用非阻塞IO（需使用TFramedTransport数据传输方式）  
4.THsHaServer - 引入了线程池去处理，其模型将读写任务放到线程池去处理；Half-sync/Half-async（半同步/半异步）的处理方式，半异步是处理IO事件，半同步是用于handler对rpc的同步处理
   