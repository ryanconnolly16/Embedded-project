
package battleship.io;
import battleship.interfaces.*;
import battleship.fleetplacements.Fleet;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

//function to get the fleet from the txt file
public class ShipFileLoader implements ShipLoader {
    
    public ShipFileLoader() {
    }
    @Override
    public List<Fleet.Ship> loadShips(String fileName) {
        List<Fleet.Ship> shipList = new ArrayList<>();
        Fleet.pieces.clear();
        try (InputStream inputStream = Fleet.class.getResourceAsStream("/" + fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            //splits the ship from their size and saves to ship
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int size = Integer.parseInt(parts[1].trim());
                    shipList.add(new Fleet.Ship(name, size));
                    //list of ship names for later use
                    Fleet.pieces.add(name);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("fail");
        }
        
        return shipList;
    }
    
    public static List<Fleet.Ship> loadShipsFromFile(String fileName) {
        ShipFileLoader loader = new ShipFileLoader();
        return loader.loadShips(fileName);
    }
}