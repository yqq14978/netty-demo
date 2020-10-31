package com.yqq.nettydemo.thrift.server;

import com.yqq.nettydemo.thrift.service.PersonService;
import com.yqq.nettydemo.thrift.service.impl.PersonServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

public class FirstThriftServer {

    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8899);
        THsHaServer.Args args1 = new THsHaServer.Args(serverSocket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        args1.protocolFactory(new TCompactProtocol.Factory());
        args1.transportFactory(new TFramedTransport.Factory());
        args1.processorFactory(new TProcessorFactory(processor));

        TServer tServer = new THsHaServer(args1);

        System.out.println("thrift server started!");

        tServer.serve();
    }
}
