package com.yqq.nettydemo.thrift.service.impl;

import com.yqq.nettydemo.thrift.entity.Person;
import com.yqq.nettydemo.thrift.exception.PersonException;
import com.yqq.nettydemo.thrift.service.PersonService;
import org.apache.thrift.TException;

public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public boolean isAlive(String name) throws TException {
        return true;
    }

    @Override
    public void savePerson(Person person) throws PersonException, TException {
        System.out.println("save person —— " + person);
    }
}
