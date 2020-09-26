package database;

import java.io.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.yaml.snakeyaml.*;

public class DataEntry {    
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

    public static ArrayList<EntryInfo> View(String query){
        Connection connection;
        ArrayList<EntryInfo> entries = new ArrayList<EntryInfo>();

        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM work WHERE '"+query+"' IN (title, title_alias, author, author_alias, year, work_type, language)");
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                EntryInfo newFind = new EntryInfo(rs.getString("title"), rs.getString("title_alias"), rs.getString("author"), rs.getString("author_alias"), rs.getInt("year"), rs.getString("work_type"), rs.getString("language"), rs.getBytes("cover"));
                entries.add(newFind);
            }
            connection.close();
            System.out.println("Connection closed.");
        } catch(Exception e) {
            System.out.println(e);
        }
        return entries;
    }

    public static void Add(EntryInfo entry){
        Connection connection;
        ResultSet rs;
        int id;

        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT work_id FROM work");
            rs = statement.executeQuery();

            // Get all existing primary key values
            ArrayList<Integer> existingIDs = new ArrayList<Integer>();
            while(rs.next())
                existingIDs.add(rs.getInt("work_id"));

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
