package battleship.io;

import battleship.domain.Board;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SaveManager {
    public static final String DIR = "saves";
    private static File dir() {
        File d = new File(DIR);
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }
    
    // saves the current turn to a single temp file (always overwrites)
    public static File writeTurnAutosave(Board p1, Board p2) throws IOException {
        File tmp = new File(dir(), "autosave.tmp"); // fixed name
        new FileOutput().saveMatch(p1, p2, tmp);    // overwrite each turn
        return tmp;
    }

    // when user presses x, save permanent copy with timestamp
    public static File keep(File tmp) throws IOException {
        if (tmp == null) throw new IllegalArgumentException("tmp is null");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        File dst = new File(tmp.getParentFile(), "save_" + timestamp + ".txt");
        
        if (!tmp.renameTo(dst)) throw new IOException("Rename failed: " + tmp + " -> " + dst);
        return dst;
        
    }
    
    //will idscard what has been done
    public static void discard(File tmp) {
        if (tmp != null && tmp.exists()) {
            tmp.delete();
        }
    }
    
    //checks that the file exists and arent empty before opening
    public static int checkfilesexist(){
        File[] arr = new File(SaveManager.getProjectFolderPath("saves")).listFiles((d, name) -> name.startsWith("save_") && name.endsWith(".txt"));
        
        if (arr == null) {
            return 0;
            
        }
        else if (arr.length == 0) {
            return 0;
        }
        else{
            return 1;
        }
    }
    
    //gives list of save files
    public static ArrayList<String> filenames = new ArrayList<>();
    public static int listSaves() {
        File[] arr = new File(SaveManager.getProjectFolderPath("saves")).listFiles((d, name) -> name.startsWith("save_") && name.endsWith(".txt"));
       
        if (arr != null) {
            for (File file : arr) {
                filenames.add(file.getName());
                
            }
            Collections.reverse(filenames);
            for(int i = 0; i < filenames.size(); i++){
                System.out.println((i +1) + ". " + filenames.get(i));
            }
            return 1;
        } else {
            System.out.println("No files found or directory doesn't exist");
            return 2;
        }
        
    }
    //gets the path of a specific folder in the project
    public static String getProjectFolderPath(String relativePath) {
        Path folderPath = Paths.get(relativePath);
        
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            System.err.println("Directory '" + relativePath + "' not found or is not a directory");
            return null;
        }
        
        return folderPath.toAbsolutePath().toString();
    }
    
    
}
    