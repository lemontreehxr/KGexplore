package cn.edu.ruc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClientManager {
    private Connection connection = null;

    public ClientManager(){
        ConfigManager configManager = new ConfigManager("jdbc.properties");

        String DBDrive = configManager.getValue("db.drive");
        String DBUrlBase = configManager.getValue("db.url.base");
        String DBHost = configManager.getValue("db.host");
        String DBName = configManager.getValue("db.database");
        String DBPort = configManager.getValue("db.port");
        String DBUser = configManager.getValue("db.user");
        String DBPassword = configManager.getValue("db.password");
        String DBUrl = DBUrlBase + DBHost + ":" + DBPort + "/" + DBName;
        setConnection(DBDrive, DBUrl, DBUser, DBPassword);
    }

    public Connection getConnection(){
        return connection;
    }

    private void setConnection(String DBDrive, String DBUrl, String DBUser, String DBPassword) {
        try {
            Class.forName(DBDrive).newInstance();
            connection = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            if (connection != null)
                System.out.println("Database connect successfully");
            else
                System.out.println("Database connect unsuccessfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try{
            if (connection != null){
                connection.close();
                System.out.println("Database close successfully");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
