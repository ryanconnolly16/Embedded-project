package battleship.database;

import java.nio.file.*;
import java.util.Locale;

public final class DbPaths {
    private DbPaths() {}

    public static Path derbyHome() {
        String os = System.getProperty("os.name","").toLowerCase(Locale.ROOT);
        Path base;
        if (os.contains("win")) {
            String lad = System.getenv("LOCALAPPDATA");
            base = (lad != null) ? Path.of(lad) : Path.of(System.getProperty("user.home"), "AppData", "Local");
            base = base.resolve("Battleship").resolve("Derby");
        } else if (os.contains("mac")) {
            base = Path.of(System.getProperty("user.home"), "Battleship", "Derby");
        } else {
            base = Path.of(System.getProperty("user.home"), ".local", "share", "battleship", "derby");
        }
        try { Files.createDirectories(base); } catch (Exception ignored) {}
        return base.toAbsolutePath();
    }
}
