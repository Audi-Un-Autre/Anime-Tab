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
        Commands command = GetInput();
        
        // If we want to add to or view the database, establish connection first and then call appropriate database functions
        switch(command){
            case ADD:
                System.out.println("User, you have chosen to add an entry to the database.");
                break;

            case VIEW:
                System.out.println("User, you have chosen to view the database.");
                View();
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

    private static Commands GetInput(){
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
        in.close();
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
            //ArrayList<String> array = new ArrayList<String>();
            while(results.next()){
                System.out.println(results.getString("title"));
            }
            connection.close();
        } catch(Exception e) {
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

final class InsertData{
    int id, year;
    String title, titleAlias, author, authorAlias, workType, language;
    File image;

    public InsertData(){}
    public InsertData(String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, File image){
        this.title = title;
        this.titleAlias = titleAlias;
        this.author = author;
        this.authorAlias = authorAlias;
        this.year = year;
        this.workType = workType;
        this.language = language;
        this.image = image;
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

    public void setImage(File image){
        this.image = image;
    }
}
