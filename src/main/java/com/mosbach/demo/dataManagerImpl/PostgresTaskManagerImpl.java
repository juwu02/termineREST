package com.mosbach.demo.dataManagerImpl;

import com.mosbach.demo.dataManager.TaskManager;
import com.mosbach.demo.model.student.Student;
import com.mosbach.demo.model.task.Task;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PostgresTaskManagerImpl implements TaskManager  {

    String databaseURL = "jdbc:postgresql://ec2-54-209-187-69.compute-1.amazonaws.com:5432/d4gm3fdn3hbq";
    String username = "wumbpgmaqdaodv";
    String password = "aac6fe658e63f0090efab27a4f448170f49e8421c237a1c0eaf787017fbde6ea";
    BasicDataSource basicDataSource;

    static PostgresTaskManagerImpl postgresTaskManager = null;

    private PostgresTaskManagerImpl() {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(databaseURL);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
    }

    static public PostgresTaskManagerImpl getPostgresTaskManagerImpl() {
        if (postgresTaskManager == null)
            postgresTaskManager = new PostgresTaskManagerImpl();
        return postgresTaskManager;
    }


    @Override
    public Collection<Task> getAllTasks(Student student) {

        List<Task> tasks = new ArrayList<>();
        Statement stmt = null;
        Connection connection = null;

        try {
        	connection = basicDataSource.getConnection();
        	stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");
            while (rs.next()) {
                tasks.add(
                        new Task(
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getInt("priority")
                        )
                );  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        try {
        	stmt.close();    
            connection.close();  
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        
        return tasks;
    }

    @Override
    public void addTask(Task task, Student student) {

        Statement stmt = null;
        Connection connection = null;

        try {
        	connection = basicDataSource.getConnection();
        	stmt = connection.createStatement();
            String udapteSQL = "INSERT into tasks (name, description, priority) VALUES (" +
                    "'" + task.getName() + "', " +
                    "'" + task.getDescription() + "', " +
                    "'" + task.getPriority() + "')";

            stmt.executeUpdate(udapteSQL);
            
            stmt.close();
            connection.close();
            } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
        	stmt.close();    
            connection.close();  
        } catch (SQLException e) {
            e.printStackTrace();
        } 

    }

    public void createTableTask() {

        // Be carefull: It deletes data if table already exists.
        //
        Statement stmt = null;
        Connection connection = null;

        try {
        	connection = basicDataSource.getConnection();
        	stmt = connection.createStatement();

            // String dropTable = "DROP TABLE tasks";
            // stmt.executeUpdate(dropTable);

            String createTable = "CREATE TABLE tasks (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "description varchar(250) NOT NULL, " +
                    "priority int NOT NULL)";

            stmt.executeUpdate(createTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
        	stmt.close();    
            connection.close();  
        } catch (SQLException e) {
            e.printStackTrace();
        } 

    }


}
