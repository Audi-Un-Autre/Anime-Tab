package database;

import java.io.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import org.yaml.snakeyaml.*;

public class DataEntry {

    public static void main(String[] args){
        try{
            getConnection();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private static Auth GetAuth() throws Exception{
        try{
            // Get .yaml config file and parse it
            InputStream file = new FileInputStream(new File("../config.yml"));
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
}

// Class to hold auth data
final class Auth{

    public final String un, pw;
    
    public Auth(String un, String pw){
        this.un = un;
        this.pw = pw;
    }
}
