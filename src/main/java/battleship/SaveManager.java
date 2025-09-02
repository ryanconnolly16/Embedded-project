
package battleship;

import java.io.*;
import java.nio.file.*;
import java.util.*;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.stream.Stream;

public class SaveManager {
    public static final String DIR = "saves";
    
    
    private static File dir() {
        File d = new File(DIR);
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }
    
    //call after every turn
    public static File writeTurnAutosave(Board p1, Board p2) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        File tmp = new File(dir(), "save_" + timestamp + ".tmp");
        new FileOutput().saveMatch(p1, p2, tmp);
        return tmp;
    }
    
    //when they press x
    public static File keep(File tmp) throws IOException {
        if (tmp == null) throw new IllegalArgumentException("tmp is null");
        String name = tmp.getName();
        if (!name.endsWith(".tmp")) throw new IllegalArgumentException("Not a .tmp: " + name);
        File dst = new File(tmp.getParentFile(), name.substring(0, name.length() - 4) + ".txt");
        if (!tmp.renameTo(dst)) throw new IOException("Rename failed: " + tmp + " -> " + dst);
        return dst;
    }
     
    public static void discard(File tmp) {
        if (tmp != null && tmp.exists()) {
            tmp.delete();
        }
    }
    
    public static ArrayList<String> filenames = new ArrayList<>();
    
    
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
    public static int listSaves() {
        File[] arr = new File(SaveManager.getProjectFolderPath("saves")).listFiles((d, name) -> name.startsWith("save_") && name.endsWith(".txt"));
       
        if (arr != null) {
            for (File file : arr) {
                filenames.add(file.getName());
                
            }
            for(int i = 0; i < filenames.size(); i++){
                System.out.println((i +1) + ". " + filenames.get(i));
            }
            return 1;
        } else {
            System.out.println("No files found or directory doesn't exist");
            return 2;
        }
        
    }
    
    
//    public static Board[] load(File file) throws IOException {
//        return new FileInput().loadMatch(file);
//    }
//    
    // Gets the Path of a specific folder in the project
    public static String getProjectFolderPath(String relativePath) {
        Path folderPath = Paths.get(relativePath);
        
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            System.err.println("Directory '" + relativePath + "' not found or is not a directory");
            return null;
        }
        
        return folderPath.toAbsolutePath().toString();
    }
    
//    
//    public static void readFilesFromParentDir(String directoryPath) {
//        try {
//            Path dir = Paths.get(directoryPath);
//            String parentDirName = dir.getFileName().toString(); // Gets just the directory name
//            
//            System.out.println("Reading files from directory: " + parentDirName);
//            
//            try (Stream<Path> files = Files.walk(dir, 1)) {
//                files.filter(Files::isRegularFile)
//                     .filter(file -> file.toString().endsWith(".txt")) // Only .txt files
//                     .forEach(file -> {
//                         try {
//                             String content = Files.readString(file);
//                             // Show parent dir name, not individual file name
//                             System.out.println("=== Content from " + parentDirName + " ===");
//                             System.out.println(content);
//                             System.out.println(); // Empty line separator
//                         } catch (IOException e) {
//                             System.err.println("Error reading file: " + e.getMessage());
//                         }
//                     });
//            }
//        } catch (IOException e) {
//            System.err.println("Error accessing directory: " + e.getMessage());
//        }
//    }
}
    