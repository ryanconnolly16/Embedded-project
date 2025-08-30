
package battleship;

import java.io.File;
import java.io.IOException;
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
    
    public static File writeTurnAutosave(Board p1, Board p2) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        File tmp = new File(dir(), "save_" + timestamp + ".tmp");
        new FileOutput().saveMatch(p1, p2, tmp);
        return tmp;
    }
    
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
    
    public static File[] listSaves() {
        File[] arr = dir().listFiles((d, name) -> name.startsWith("save_") && name.endsWith(".txt"));
        if (arr == null) {
            return new File[0];
        }
        
        java.util.Arrays.sort(arr, (a, b) -> b.getName().compareTo(a.getName()));
        return arr;
    }
    
    public static Board[] load(File file) throws IOException {
        return new FileInput().loadMatch(file);
    }
}
