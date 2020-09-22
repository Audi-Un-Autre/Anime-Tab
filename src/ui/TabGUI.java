package ui;

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TabGUI extends JFrame{
    Container container;
    JTextArea previewBox;

    // Input Labels
    JLabel titleLabel;
    JLabel title_aliasLabel;
    JLabel authorLabel;
    JLabel author_aliasLabel;
    JLabel yearLabel;
    JLabel work_typeLabel;
    JLabel languageLabel;
    JLabel imageLabel;

    // Input Fields
    JTextField title;
    JTextField title_alias;
    JTextField author;
    JTextField author_alias;
    JComboBox<Integer> year;
    JComboBox<String> work_type;
    JComboBox<String> language;
    JButton image;
    String imagePath;

    // Submission buttons
    JButton preview;
    JButton addNew;

    // Dropdown inputs
    String workTypes[] = {"Anime", "Manga", "Manhwa", "Comic"};
    String languages[] = {"Japanese", "English", "French", "German", "Spanish"};

    public TabGUI(){
        container = getContentPane();

        setTitle("Anime Tab!");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container.setLayout(null);

        JLabel windowTitle = new JLabel("Add New Entry");
        windowTitle.setFont(new Font("Arial", Font.PLAIN, 30));
        windowTitle.setSize(400, 40);
        windowTitle.setLocation(40, 20);
        container.add(windowTitle);

        // TITLE
        titleLabel = new JLabel("Title ");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        titleLabel.setSize(100, 20);
        titleLabel.setLocation(100, 100);
        container.add(titleLabel);

        title = new JTextField();
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        title.setSize(190, 20);
        title.setLocation(200, 100);
        container.add(title);

        // TITLE ALIAS
        title_aliasLabel = new JLabel("Title Alias ");
        title_aliasLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        title_aliasLabel.setSize(100, 20);
        title_aliasLabel.setLocation(100, 130);
        container.add(title_aliasLabel);

        title_alias = new JTextField();
        title_alias.setFont(new Font("Arial", Font.PLAIN, 20));
        title_alias.setSize(190, 20);
        title_alias.setLocation(200, 130);
        container.add(title_alias);

        // AUTHOR
        authorLabel = new JLabel("Author ");
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        authorLabel.setSize(100, 20);
        authorLabel.setLocation(100, 160);
        container.add(authorLabel);

        author = new JTextField();
        author.setFont(new Font("Arial", Font.PLAIN, 20));
        author.setSize(190, 20);
        author.setLocation(200, 160);
        container.add(author);

        // AUTHOR ALIAS
        author_aliasLabel = new JLabel("Author Alias ");
        author_aliasLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        author_aliasLabel.setSize(130, 20);
        author_aliasLabel.setLocation(100, 190);
        container.add(author_aliasLabel);

        author_alias = new JTextField();
        author_alias.setFont(new Font("Arial", Font.PLAIN, 20));
        author_alias.setSize(190, 20);
        author_alias.setLocation(230, 190);
        container.add(author_alias);

        // YEAR
        yearLabel = new JLabel("Year ");
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        yearLabel.setSize(130, 20);
        yearLabel.setLocation(100, 230);
        container.add(yearLabel);

        year = new JComboBox<Integer>(YearRange());
        year.setFont(new Font("Arial", Font.PLAIN, 20));
        year.setSize(80, 30);
        year.setLocation(190, 230);
        container.add(year);

        // WORK TYPE
        work_typeLabel = new JLabel("Work Type ");
        work_typeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        work_typeLabel.setSize(130, 20);
        work_typeLabel.setLocation(100, 270);
        container.add(work_typeLabel);

        work_type = new JComboBox<String>(workTypes);
        work_type.setFont(new Font("Arial", Font.PLAIN, 20));
        work_type.setSize(150, 30);
        work_type.setLocation(210, 270);
        container.add(work_type);

        // LANGUAGE
        languageLabel = new JLabel("Language ");
        languageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        languageLabel.setSize(130, 20);
        languageLabel.setLocation(100, 310);
        container.add(languageLabel);

        language = new JComboBox<String>(languages);
        language.setFont(new Font("Arial", Font.PLAIN, 20));
        language.setSize(150, 30);
        language.setLocation(210, 310);
        container.add(language);

        // IMAGE
        imageLabel = new JLabel("Image ");
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        imageLabel.setSize(130, 20);
        imageLabel.setLocation(100, 350);
        container.add(imageLabel);

        image = new JButton("Browse . . .");
        image.setSize(100, 30);
        image.setLocation(190, 350);
        image.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser imageFile = new JFileChooser();
                imageFile.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
                imageFile.addChoosableFileFilter(imageFilter);
                int result = imageFile.showSaveDialog(null);
        
                if (result == JFileChooser.APPROVE_OPTION){
                    File selectedFile = imageFile.getSelectedFile();
                    imagePath = selectedFile.getAbsolutePath();
                }
            }
        });
        container.add(image);

        // PREVIEW
        preview = new JButton("Preview Entry");
        preview.setSize(150, 30);
        preview.setLocation(350, 475);
        /*
        preview.addActionListener(new ActionListener()){
            public void actionPerformed(ActionEvent e){
            }
        });*/
        container.add(preview);


        // PREVIEW AREA
        previewBox = new JTextArea();
        previewBox.setFont(new Font("Arial", Font.PLAIN, 20));
        previewBox.setSize(300, 300);
        previewBox.setLocation(500, 100);
        previewBox.setLineWrap(true);
        previewBox.setEditable(false);
        container.add(previewBox);

        setVisible(true);
    }

    private Vector<Integer> YearRange(){
        Vector<Integer> years = new Vector<Integer>();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        for (int i = year; i >= 1900; i--){
            years.add(i);
        }
        return years;
    }
}
