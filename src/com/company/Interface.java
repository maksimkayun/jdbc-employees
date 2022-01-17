package com.company;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;

public class Interface {
    public static Scanner sc = new Scanner(System.in);

    public static Integer menu() {

        StringBuilder sb = new StringBuilder();
        sb.append("""
                1.Добавить работника
                2.Удалить работника
                3.Вывести список работников
                4.Вывести конкретного работника
                0.ВЫХОД
                """);
        System.out.println(sb);

        Integer v = 0;
        try {
            v = Integer.parseInt(sc.nextLine());
            if (v < 0 || v > 4)
                throw new Exception("Incorrect argument");
        } catch (Exception e) {
            System.out.println("Введённое значение некорректно!");
            v = -1;
        }
        return v;
    }

    public static void work(Properties properties) throws SQLException {
        SqlRunner sqlRunner = new SqlRunner(properties);

        boolean exit = false;
        while (!exit) {
            switch (menu()) {
                case 1: {
                    System.out.print("Введите имя: ");
                    String name = sc.nextLine();
                    System.out.print("Введите зарплату: ");
                    Double salary = Double.parseDouble(sc.nextLine());

                    boolean response = sqlRunner.addEmployee(new Employee(name, salary));
                    if (response)
                        System.out.println("Работник успешно добавлен!");
                    else
                        System.out.println("Добавление невозможно!");
                    break;
                }
                case 2: {
                    System.out.print("Введите ID работника для удаления: ");
                    Integer id = Integer.parseInt(sc.nextLine());

                    Employee response = sqlRunner.deleteById(id);
                    if (response != null)
                        System.out.println("Работник успешно удалён!");
                    else {
                        System.out.println("Удаление невозможно!");
                    }
                    break;
                }
                case 3: {
                    List<Employee> employees = sqlRunner.getAll();

                    employees.forEach(new Consumer<Employee>() {
                        @Override
                        public void accept(Employee employee) {
                            System.out.println(employee.toString());
                        }
                    });
                    break;
                }
                case 4: {
                    System.out.print("Введите ID работника, которого хотите увидеть: ");
                    Integer id = Integer.parseInt(sc.nextLine());

                    Employee employee = sqlRunner.getById(id);
                    if (employee == null)
                        System.out.println("Такого работника нет в базе!");
                    else
                        System.out.println(employee.toString());
                    break;
                }
                case -1: {
                    break;
                }
                default: {
                    exit = true;
                    break;
                }
            }
        }
    }
}
