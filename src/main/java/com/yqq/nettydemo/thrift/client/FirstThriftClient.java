package com.yqq.nettydemo.thrift.client;

import com.yqq.nettydemo.thrift.entity.Person;
import com.yqq.nettydemo.thrift.service.PersonService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class FirstThriftClient {

    public static void main(String[] args) {
        TTransport tTransport = new TFastFramedTransport(new TSocket("localhost" , 8899) , 1000);
        TProtocol tProtocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(tProtocol);

        try {
            tTransport.open();

            boolean alive = client.isAlive("yqq");
            System.out.println("yqq is alive —— " + alive);

            Person person = new Person();
            person.setAge(25);
            person.setName("keli");
            person.setSex(false);
            client.savePerson(person);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            tTransport.close();
        }
    }
}
