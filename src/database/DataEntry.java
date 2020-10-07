package database;

import java.io.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.yaml.snakeyaml.*;

public class DataEntry {    
    private static Auth GetAuth() throws Exception{
        try{
            // Get .yaml config file and parse it
            String loc = System.getProperty("user.dir");
            InputStream file = new FileInputStream(new File(loc + "/config.yml"));
            Yaml yaml = new Yaml();
            Map<String, String> config = yaml.load(file);
            
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

    public static ArrayList<EntryInfo> View(String query) throws Exception{
        ArrayList<EntryInfo> entries = new ArrayList<EntryInfo>();

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM work WHERE '"+query+"' IN (title, title_alias, author, author_alias, year, work_type, language)");
        ResultSet rs = statement.executeQuery();

        while(rs.next()){
            EntryInfo newFind = new EntryInfo(rs.getInt("work_id"), rs.getString("title"), rs.getString("title_alias"), rs.getString("author"), rs.getString("author_alias"), rs.getInt("year"), rs.getString("work_type"), rs.getString("language"), rs.getString("image"));
            entries.add(newFind);
        }
            
        connection.close();
        System.out.println("Connection closed.");

        return entries;
    }

    public static ArrayList<EntryInfo> ViewInit() throws Exception{
        ArrayList<EntryInfo> entries = new ArrayList<EntryInfo>();

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM work");
        ResultSet rs = statement.executeQuery();

        while(rs.next()){
            EntryInfo newFind = new EntryInfo(rs.getInt("work_id"), rs.getString("title"), rs.getString("title_alias"), rs.getString("author"), rs.getString("author_alias"), rs.getInt("year"), rs.getString("work_type"), rs.getString("language"), rs.getString("image"));
            entries.add(newFind);
        }
            
        connection.close();
        System.out.println("Connection closed.");

        return entries;
    }

    public static void Add(EntryInfo entry) throws Exception{
        Connection connection = getConnection();
        String query = "INSERT INTO work VALUES('"+entry.getId()+"', ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement enterData = connection.prepareStatement(query);
        CheckData(entry, enterData);
        enterData.executeUpdate();
        System.out.println("NEW ENTRY SUCCESSFUL!");
        connection.close();
        System.out.println("Connection closed.");
    }

    public static void ModifyEntry(EntryInfo ei) throws Exception{
        Connection connection = getConnection();
        String query = "UPDATE work SET title = ?, title_alias = ?, author = ?, author_alias = ?, year = ?, work_type = ?, language = ?, image = ? WHERE work_id = '"+ei.getId()+"'";
        PreparedStatement statement = connection.prepareStatement(query);
        CheckData(ei, statement);
        statement.executeUpdate();
        System.out.println("Entry "+ei.getId()+" updated successfully.");
        connection.close();
    }

    public static Integer CreateID() throws Exception{
        int id;

        // Create random PK for new entry, checking against current PKs
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT work_id FROM work");
        ResultSet rs = statement.executeQuery();

        // Get all existing primary key values
        ArrayList<Integer> existingIDs = new ArrayList<Integer>();
        while(rs.next())
            existingIDs.add(rs.getInt("work_id"));

        do{
            Random rand = new Random();
            id = rand.nextInt(20000);
        }while(existingIDs.contains(id));

        return id;
    }

    public static void Delete(EntryInfo entry) throws Exception{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM work WHERE work_id = '"+entry.getId()+"'");
        statement.executeUpdate();
        System.out.println("Entry deleted.");
        connection.close();
    }

    public static void CheckData(EntryInfo ei, PreparedStatement ps) throws Exception{
        if (ei.getTitle().isBlank()) ps.setNull(1, Types.VARCHAR);
        else ps.setString(1, ei.getTitle());

        if (ei.getTitleAlias().isBlank()) ps.setNull(2, Types.VARCHAR);
        else ps.setString(2, ei.getTitleAlias());

        if (ei.getAuthor().isBlank()) ps.setNull(3, Types.VARCHAR);
        else ps.setString(3, ei.getAuthor());
        
        if (ei.getAuthorAlias().isBlank()) ps.setNull(4, Types.VARCHAR);
        else ps.setString(4, ei.getAuthorAlias());

        if ((Integer)ei.getYear() == null) ps.setNull(5, Types.INTEGER);
        else ps.setInt(5, ei.getYear());

        if (ei.getWorkType().isBlank()) ps.setNull(6, Types.VARCHAR);
        else ps.setString(6, ei.getWorkType());

        if (ei.getLanguage().isBlank()) ps.setNull(7, Types.VARCHAR);
        else ps.setString(7, ei.getLanguage());

        if (ei.getImage().isBlank()) ps.setNull(8, Types.VARCHAR);
        else ps.setString(8, ei.getImage());
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
