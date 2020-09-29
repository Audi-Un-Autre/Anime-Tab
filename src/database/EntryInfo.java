package database;

import javafx.scene.image.ImageView;

public class EntryInfo {
    private int         id, 
                        year;

    private String      title, titleAlias, 
                        author, authorAlias, 
                        workType, 
                        language;

    private ImageView   image;
    private byte[]      bimage;

    public EntryInfo(){}
    public EntryInfo(String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, ImageView image){
        this.title = title;
        this.titleAlias = titleAlias;
        this.author = author;
        this.authorAlias = authorAlias;
        this.year = year;
        this.workType = workType;
        this.language = language;
        this.image = image;
    }

    public EntryInfo(int id, String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, ImageView image){
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

    public EntryInfo(String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, byte[] bimage){
        this.title = title;
        this.titleAlias = titleAlias;
        this.author = author;
        this.authorAlias = authorAlias;
        this.year = year;
        this.workType = workType;
        this.language = language;
        this.bimage = bimage;
    }

    public EntryInfo(int id, String title, String titleAlias, String author, String authorAlias, int year, String workType, String language, byte[] bimage){
        this.id = id;
        this.title = title;
        this.titleAlias = titleAlias;
        this.author = author;
        this.authorAlias = authorAlias;
        this.year = year;
        this.workType = workType;
        this.language = language;
        this.bimage = bimage;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setTitleAlias(String titleAlias){
        this.titleAlias = titleAlias;
    }

    public String getTitleAlias(){
        return titleAlias;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthorAlias(String authorAlias){
        this.authorAlias = authorAlias;
    }

    public String getAuthorAlias(){
        return authorAlias;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setWorkType(String workType){
        this.workType = workType;
    }

    public String getWorkType(){
        return workType;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }

    public void setImage(ImageView image){
        this.image = image;
    }

    public ImageView getImage(){
        return image;
    }

    public void setBimage(byte[] bimage){
        this.bimage = bimage;
    }

    public byte[] getBimage(){
        return bimage;
    }
}
