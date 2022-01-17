package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface SQLCommands<E> {
    public List<E> getAll() throws SQLException;
    public E getById(int identity) throws SQLException;

    public boolean addEmployee(E entity) throws SQLException;
    public E deleteById(int identity) throws SQLException;
}

