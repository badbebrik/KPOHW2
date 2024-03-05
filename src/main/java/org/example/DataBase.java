package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton
public class DataBase {
    private static volatile Connection connection; // Добавляем volatile для корректной работы double-checked locking

    private DataBase() {
    } // Приватный конструктор, чтобы предотвратить создание экземпляров извне

    public static Connection getInstance() {
        if (connection == null) { // Первая проверка на наличие экземпляра для уменьшения накладных расходов
            synchronized (DataBase.class) {
                if (connection == null) { // Вторая проверка внутри синхронизированного блока
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "24244650Vas");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
