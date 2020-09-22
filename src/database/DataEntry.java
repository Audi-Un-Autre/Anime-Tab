package database;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.yaml.snakeyaml.*;

public class DataEntry {

    // DB operations allowed to user
    private enum Commands{
        ADD(1), VIEW(2), QUIT(3);
        
        Commands(int option){
            this.option = option;
        }

        private final int option;
        public int getOption(){
            return option;
        }

        public static Commands getCommandName(int command){
            for (Commands c : Commands.values()){
                if (c.getOption() == command){
                    return c;
                }
            }
            System.out.println("Error: Enum not found.");
            return null;
        }

        // Searches through all enums to compare if argument exists in current enum set
        public static boolean contains(int command){
            for (Commands c : Commands.values()){
                if (c.getOption() == command){
                    return true;
                }
            }
            return false;
        }
    }
    
    public static void main(String[] args){
        Commands command = MenuInput();
        
        // If we want to add to or view the database, establish connection first and then call appropriate database functions
        switch(command){
            case ADD:
                Add();
                //System.out.println("Adding to database. Write operations available.");
                break;

            case VIEW:
                View();
                //System.out.println("Viewing database. Read only operations.");
                break;

            case QUIT:
                System.out.println("Exiting . . .");
                System.exit(0);
                break;
            default:
                System.out.println("Something has gone wrong.");
                break;
        }
    }

    private static Commands MenuInput(){
        Scanner in = new Scanner(System.in);
        int input = 0;
        Commands command = null;

        // Main Menu
        System.out.println("Welcome to Anime Tab.");
        System.out.println("\n Anime Tab! Menu:\n");
        System.out.print("1: Add Entry\n2: View All Entries\n3: Quit.\n\nEnter an option . . .");

        // Get valid option input from user
        if (in.hasNextInt()){
            input = in.nextInt();
            if (Commands.contains(input)){
                command = Commands.getCommandName(input);
            } else {
                do{
                    System.out.println("Enter a valid option . . . : ");
                    if (in.hasNextInt()){
                        input = in.nextInt();
                        if (Commands.contains(input)){
                            command = Commands.getCommandName(input);
                        }
                    }
                }while(!Commands.contains(input));
            }
        }
        return command;
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

    private static void Add(){
        Connection connection;
        ArrayList<String> columnNames = new ArrayList<String>();

        // get all columns in table
        try{
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM work");
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 2; i <= columnCount; i++){
                columnNames.add(rsmd.getColumnName(i));
            }
            
        } catch (Exception e){
            System.out.println(e);
        }
        
        System.out.println("\n\nAdding to database. Write operations available.");
        System.out.println("To get started, enter the following information for each prompt below.");
        System.out.print("\n*Note: \n" + 
                        "Both title and title alias cannot be empty at the same time.\n" +
                        "Both author and author alias cannot be empty at the same time.\n" + 
                        "Work type is required.\n");

        // get user input for each column
        ArrayList<String> columnEntries = new ArrayList<String>();
        for (int i = 0; i < columnNames.size(); i++){
            boolean gotInput = false;
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            System.out.print("\nEnter a " + columnNames.get(i) + ": ");

            while (!gotInput){
                try{
                    String input = br.readLine();
                    columnEntries.add(input);
                    gotInput = true;
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }

        Scanner in = new Scanner(System.in);
        if (columnEntries.get(1) != null)
            System.out.print("Data captured. Confirm if you would like to add " + columnEntries.get(1) + " to the list (Y/N): ");
        else
            System.out.print("Data captured. Confirm if you would like to add " + columnEntries.get(2) + " to the list (Y/N): ");

        if (in.next().trim().charAt(0) == 'y'){
            // Create an ID for the new data set
            ArrayList<Integer> existingIds = new ArrayList<Integer>();
            int id;
            int rows = 0;

            
            try{
                connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT work_id FROM work");
                ResultSet rs = statement.executeQuery();

                while(rs.next()){
                    rows++;
                    existingIds.add(rs.getInt(rows));
                }
            } catch (Exception e){
                System.out.println(e);
            }

            do {
                Random rand = new Random();
                id = rand.nextInt(20000);
            }while(existingIds.contains(id));
            
            try{
                final InsertData newData = new InsertData(id, columnEntries);
                connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO work VALUES('"+newData.id+"', '"+newData.title+"', '"+newData.titleAlias+"', '"+newData.author+"', '"+newData.authorAlias+"', '"+newData.year+"', '"+newData.workType+"', '"+newData.language+"', '"+newData.image+"')");
                
                statement.executeUpdate();
                System.out.println("Add successful");
                
            } catch (Exception e){
                System.out.println(e);
            }
        }
        else{
            System.out.println("Exiting program.");
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

final class InsertData{
    int id, year;
    String title, titleAlias, author, authorAlias, workType, language;
    String image;

    public InsertData(){}
    public InsertData(int id, String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, String image){
        this.id = id;
        this.title = title;
        this.titleAlias = titleAlias;
        this.author = author;
        this.authorAlias = authorAlias;
        this.year = year;
        this.workType = workType;
        this.language = language;
        this.image = image;
    }

    public InsertData(int id, ArrayList<String> userEntry){
        this.id = id;
        title = String.valueOf(userEntry.get(0));
        titleAlias = String.valueOf(userEntry.get(1));
        author = String.valueOf(userEntry.get(2));
        authorAlias = String.valueOf(userEntry.get(3));
        year = Integer.parseInt(userEntry.get(4));
        workType = String.valueOf(userEntry.get(5));
        language = String.valueOf(userEntry.get(6));
        image = userEntry.get(7);
    }

    public void setID(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setTitleAlias(String titleAlias){
        this.titleAlias = titleAlias;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setAuthorAlias(String authorAlias){
        this.authorAlias = authorAlias;
    }

    public void setYear(int year){
        this.year = year;
    }

    public void setWorkType(String workType){
        this.workType = workType;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public void setImage(String image){
        this.image = image;
    }
}
