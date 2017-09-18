

import data.DataFileHelper;
import ticket_machine.TicketMachine;

import java.io.IOException;

/**
 * Main class
 * Created by Agata Sularz.
 */
public class Main {

    public static void main(String args[]) {
        try {
            DataFileHelper.createFilesWithDatasIfNotExist();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TicketMachine ticketMachine = new TicketMachine();
        ticketMachine.runMachine();
    }
}
