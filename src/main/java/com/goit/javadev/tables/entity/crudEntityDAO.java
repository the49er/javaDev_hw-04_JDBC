package com.goit.javadev.tables.entity;

import java.util.List;

public interface crudEntityDAO<T, id> {
    void insertNewEntity(T element);            //create
    void insertNewEntities(List<T> element);    //create
    boolean updateEntityFieldsById(long id);    //update
    boolean updateEntityByField(String field);  //update
    T getEntityById(long id);                  //read
    List<T> getAllEntities();                   //read
    void deleteById(long id);                   //delete
    void clearTable();                          //delete
}
