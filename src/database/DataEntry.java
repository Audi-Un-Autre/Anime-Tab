package database;

import java.io.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.yaml.snakeyaml.*;

public class DataEntry {

    // DB operations allowed to user
    public enum Commands{
        ADD, VIEW;
    }
    
    public DataEntry(NewEntry entry, Commands command){
        switch(command){
            case ADD:
                Add(entry);
                break;

            case VIEW:
                View();
                break;
        }
    }

    private static Auth GetAuth() throws Exception{
        try{
            // Get .yaml config file and parse it
            String loc = System.getProperty("user.dir");
            InputStream file = new FileInputStream(new File(loc + "/config.yml"));
            Yaml yaml = new Yaml();
            Map<String, String> config = (Map<String, String>) yaml.load(file);
            
            String username = System.getenv(config.get("DB ID"));
            String pw = System.getenv(config.get("DB LI"));
            return new Auth(username, pw);
        
        } catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    private static Connection getConnection() throws Exception{
        
        // Get credentials
        Auth auth = GetAuth();
        
        // Attempt connection to database
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/anime";
            Class.forName(driver);

            Connection connection = DriverManager.getConnection(url, auth.un, auth.pw);
            System.out.println("Connection successful.");
            return connection;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    private static void View(){
        Connection connection;
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT title FROM work");

            ResultSet results = statement.executeQuery();
            while(results.next()){
                System.out.println(results.getString("title"));
            }
            connection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private static void Add(NewEntry entry){
        Connection connection;
        ResultSet rs;
        int id, rows = 0;

        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT work_id FROM work");
            rs = statement.executeQuery();

            // Get all existing primary key values
            ArrayList<Integer> existingIDs = new ArrayList<Integer>();
            while(rs.next()){
                rows++;
                existingIDs.add(rs.getInt(rows));
            }

            // Create random PK for new entry, checking against current PKs
            do{
                Random rand = new Random();
                id = rand.nextInt(20000);
            }while(existingIDs.contains(id));

            // Insert entry into database
            PreparedStatement enterData = connection.prepareStatement("INSERT INTO work VALUES('"+id+"', '"+entry.title+"', '"+entry.titleAlias+"', + '"+entry.author+"', '"+entry.authorAlias+"', '"+entry.year+"', '"+entry.workType+"', '"+entry.language+"', '"+entry.image+"')");
            enterData.executeUpdate();
            System.out.println("NEW ENTRY SUCCESSFUL!");
            connection.close();
            System.out.println("Connection closed.");
        } catch (Exception e){
            System.out.println(e);
        }
    }
}

// Class to hold auth data
final class Auth{

    public final String un, pw;
    
    public Auth(String un, String pw){
        this.un = un;
        this.pw = pw;
    }
}
