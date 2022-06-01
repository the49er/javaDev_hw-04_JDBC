package com.goit.javadev.tables.manytomany;

import com.goit.javadev.tables.manytomany.companycustomer.CompanyCustomerKey;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ManyToMany<T> {
    long insertKey(T element) throws SQLException;
    boolean deleteKey(int key1, int key2) throws SQLException;
    Optional<List<T>> getAll();
}
