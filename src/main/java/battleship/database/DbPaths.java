package battleship.database;

import java.nio.file.*;

public final class DbPaths {
    private DbPaths() {}
    // Gets path to database
    public static Path derbyHome() {
        // Use "db" folder in the project directory 
        Path base = Paths.get("db");
        try { Files.createDirectories(base); } catch (Exception ignored) {}
        return base.toAbsolutePath();
    }
}
