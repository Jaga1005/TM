package tickets;


import data.DataFileHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Representation of ticket state in ticket machine
 * Created by Agata Sularz.
 */
public class TicketState {
    private final ArrayList<Ticket> tickets;

    public TicketState() {
        this.tickets = DataFileHelper.getTicketState();
        Collections.sort(tickets);
    }

    /**
     * Returns price of ticket
     * @param ID ticket ID
     * @return
     * @throws WrongTicketIDException
     */
    public double getPriceOfTicket(double ID) throws WrongTicketIDException {
       return tickets.stream()
               .filter(t -> t.getID() == ID)
               .findFirst()
               .orElseThrow(()-> new WrongTicketIDException("Wrong number of ticket!"))
               .getPrice();
    }

    /**
     * Getter of list of tickets
     * @return
     */
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Checks ticket is available in ticket machine
     * @param ticket ticket ID
     * @return
     * @throws WrongTicketIDException
     */
    public boolean isTicketAvailable(double ticket) throws WrongTicketIDException {
        return tickets.stream()
                .filter(t-> t.getID() == ticket)
                .findFirst()
                .orElseThrow(()-> new WrongTicketIDException("Wrong type of ticket!"))
                .count > 0;
    }

    /**
     * Updates tickets state and saves it in file
     * @param ticket
     * @param numberOfTicket
     */
    public void updateState(double ticket, int numberOfTicket) {
        tickets.stream()
                .filter(t->(int)t.getID() == (int)ticket)
                .findFirst()
                .orElse(null)
                .count-=numberOfTicket;

        DataFileHelper.updateTicketState(tickets);
    }

    /**
     * Checks available tickets in the machine
     * @param ticket ticket ID
     * @param numberOfTicket number of tickets
     * @return
     */
    public boolean hasEnoughTickets(double ticket, int numberOfTicket) {
        return tickets.stream()
                .filter(t->(int)t.getID() == (int)ticket)
                .findFirst()
                .orElse(null)
                .count-numberOfTicket >= 0;
    }

    @Override
    public String toString() {
        StringBuilder ticketsInString = new StringBuilder("");
        String kind = "";
        for(Ticket t: tickets)
        {
            if(!t.getKind().equals(kind))
            {
                ticketsInString.append(t.getKind() + ":\n");
                kind = t.getKind();
            }
            ticketsInString.append("\t" + t.getID() + " " + t.toString() + "\n");

        }

        return ticketsInString.toString();
    }
}
