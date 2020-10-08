package database;

public class EntryInfo {
    private int         id;    
    private Integer     year;

    private String      title, titleAlias, 
                        author, authorAlias, 
                        workType, 
                        language,
                        image;

    public EntryInfo(){}

    public EntryInfo(int id, String title, String titleAlias, String author, String authorAlias, Integer year, String workType, String language, String image){
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

    public void setYear(Integer year){
        this.year = year;
    }

    public Integer getYear(){
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

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }
}
