package com.example.My_Database.Domain.Entity;

import com.example.My_Database.utils.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Slf4j
public class DatabaseManager {
    public static DatabaseManager instance;
    private final HashMap<String, Database> databases = new HashMap<>();

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Collection<Database> list() {
        return databases.values();
    }

    public Database get(String name) {
        return databases.getOrDefault(name, null);
    }

    public Result add(Database db) {
        if (databases.containsKey(db.getName())) {
            return Result.Fail("Duplicate error: database with such name already exists");
        }
        databases.put(db.getName(), db);
        return Result.Success();
    }

    public boolean delete(String name) {
        if (!databases.containsKey(name)) {
            throw new NoSuchElementException("Database with such name does not exist");
        }
        databases.remove(name);
        return true;
    }


}
