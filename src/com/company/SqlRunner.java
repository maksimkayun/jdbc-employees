package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlRunner implements SQLCommands<Employee> {
    private Properties props;
    Connection connection;
    public SqlRunner(Properties props) throws SQLException {
        setProps(props);
        createConnection();
    }

    private void createConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test?serverTimezone=UTC", getProps());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM employees");
        ResultSet resultSet = stmt.executeQuery();
        List<Employee> employees = new ArrayList<>();
        while (resultSet.next()) {
            employees.add(new Employee(resultSet.getInt("id"), resultSet.getString("name"),
                    resultSet.getDouble("salary")));
        }
        return employees;
    }

    @Override
    public Employee getById(int identity) throws SQLException {
        PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM employees AS e WHERE e.id = ?");
        // Защита от SQL инъекции
        stmt.setInt(1, identity);

        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next())
            return new Employee(resultSet.getInt("id"),
                    resultSet.getString("name"), resultSet.getDouble("salary"));
        return null;
    }

    @Override
    public boolean addEmployee(Employee employee) throws SQLException {
        PreparedStatement stmt =
                connection.prepareStatement("INSERT INTO employees(name, salary) VALUES(?, ?)");
        stmt.setString(1, employee.getName());
        stmt.setDouble(2, employee.getSalary());
        stmt.execute();

        return true;
    }

    @Override
    public Employee deleteById(int identity) throws SQLException {
        PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM employees AS e WHERE e.id = ?");
        stmt.setInt(1, identity);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Employee employee = new Employee(resultSet.getInt("id"), resultSet.getString("name"),
                    resultSet.getDouble("salary"));
            stmt = connection.prepareStatement("DELETE FROM employees AS e WHERE e.id = ?");
            stmt.setInt(1, identity);
            stmt.execute();
            return employee;
        }
        return null;
    }
}
