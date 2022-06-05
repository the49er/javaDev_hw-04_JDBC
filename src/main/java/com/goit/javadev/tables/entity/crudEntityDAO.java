package com.goit.javadev.tables.entity;

import java.sql.SQLException;
import java.util.List;

public interface crudEntityDAO<T> {
    long insertNewEntity(T element);                     //create
    int insertNewEntities(List<T> element);              //create
    int insertEntitiesFromJsonFile (String jsonFilePath);//create
    boolean updateEntityFieldsById(T element, long id);  //update
    T getEntityById(long id);        //read
    List<T> getAllEntities();                            //read
    int deleteEntitiesFromListById(long[] ids);          //delete
    boolean deleteById(long id);                         //delete
    boolean clearTable();                                //delete
}
