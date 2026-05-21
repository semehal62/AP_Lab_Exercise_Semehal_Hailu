package Note_pad;

import java.lang.classfile.Label;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;

public class notepads_gui extends Application{
    TextArea textpart = new TextArea();
    double fontSize = 14.0;
    @Override
    public void start(Stage primaryStage){
        BorderPane root = new BorderPane();

        MenuBar taskbar = new MenuBar();
        Menu File = new Menu("File");
        MenuItem newfile = new MenuItem("New");
        MenuItem openfile = new MenuItem("Open");
        MenuItem savefile = new MenuItem("Save");
        MenuItem Exitfile = new MenuItem("Exit");

        File.getItems().addAll(newfile,openfile,savefile,Exitfile);
        Menu Edit = new Menu("Edit");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem cut = new MenuItem("Cut");

        Edit.getItems().addAll(copy,paste,cut);
        Menu View = new Menu("View");
        MenuItem zoomin = new MenuItem("Zoomin");
        MenuItem zoomout = new MenuItem("Zoomout");

        View.getItems().addAll(zoomin,zoomout);

        taskbar.getMenus().add(File);
        taskbar.getMenus().add(Edit);
        taskbar.getMenus().add(View);

        root.setTop(taskbar);
        root.setCenter(textpart);

        newfile.setOnAction(e-> textpart.clear());

        openfile.setOnAction(e ->{
            FileChooser fileChooser =  new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if(file != null){
                try{
                    String content = Files.readString(file.toPath());
                    textpart.setText(content);
                }catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }

        });

        savefile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.print(textpart.getText());
                } catch (IOException ex) {
                    System.out.println("Error saving file: " + ex.getMessage());
                }
            }
        });

        Exitfile.setOnAction(e -> primaryStage.close());

    
        copy.setOnAction(e -> textpart.copy());

    
        paste.setOnAction(e -> textpart.paste());

        
        cut.setOnAction(e -> textpart.cut());

        zoomin.setOnAction(e -> {
            fontSize += 2; // Increase size by 2px
            textpart.setStyle("-fx-font-size: " + fontSize + "px;");
        });

        zoomout.setOnAction(e -> {
            if (fontSize > 4) { // Safety check so text doesn't disappear
                fontSize -= 2; 
                textpart.setStyle("-fx-font-size: " + fontSize + "px;");
            }
        });

        Scene scene = new Scene(root,800,600);
        primaryStage.setTitle("Notepad");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] arg){
        launch(arg);
    }
}
