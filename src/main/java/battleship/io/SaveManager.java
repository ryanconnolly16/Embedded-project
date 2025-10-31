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
    