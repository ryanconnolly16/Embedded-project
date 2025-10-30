//package battleship.setup;
//
//import battleship.fleetplacements.*;
//import battleship.domain.Board;
//import battleship.interfaces.*;
//import battleship.ui.*;
//import java.io.IOException;
//
//public class SetUp implements GameSetup {
//
//    public SetUp() {
//    }
//    //asking the user if they want a preset board or make one
//    public void playersetup(Fleet fleet, Board board, String name) throws IOException {
//        System.out.println("\n\n" + name + " Setup:");
//        String answer = InputManager.askPreset();
//
//        if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
//            fleet.preset(fleet, board);
//            System.out.println("\nHere is your board:");
//            System.out.println("\n" + BoardRenderer.renderBoth(board, new DefaultGlyphs()));
//            UiOutput.clearConsole();
//        }
//        else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
//            fleet.userPalcement(fleet, board);
//        }
//        else {
//            System.out.println("Invalid answer, try again.");
//            playersetup(fleet, board, name);
//        }
//    }
//
//    @Override
//    public void setupPlayers() throws IOException {
//        //interface implementation 
//    }
//}