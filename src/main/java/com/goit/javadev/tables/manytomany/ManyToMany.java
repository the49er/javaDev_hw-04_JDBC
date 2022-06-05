package com.goit.javadev.tables.manytomany;

import java.util.List;
import java.util.Optional;

public interface ManyToMany<T> {
    boolean insertKey(T element);
    boolean deleteKey(int key1, int key2);
    Optional<List<T>> getAll();
    int createKeysFromList(List<T> list);
    int insertKeysFromJsonFile(String jsonFilePath);
}