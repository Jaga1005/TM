package ticket_machine;

import money.Money;
import money.MoneyState;
import money.NotEnoughMoneyException;
import money.OutOfMoneyException;
import tickets.TicketState;
import tickets.WrongTicketIDException;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class of ticket machine
 * Created by Agata Sularz.
 */
public class TicketMachine
{
    private final MoneyState moneyState;
    private final TicketState ticketState;

    public TicketMachine()
    {
        this.moneyState = new MoneyState(false);
        this.ticketState = new TicketState();
    }

    /**
     * Runs ticket machine
     */
    public void runMachine()
    {
        Scanner in = new Scanner(System.in);
        String operation;
        double ticket;
        int numberOfTicket;
        while(true)
        {
            System.out.println(ticketState.toString());
            System.out.println("ATTENTION. All double number write with coma.");
            System.out.print("\nChoose ticket. Write its code (e.g. 1,0 or 2,1): ");
            ticket = in.nextDouble();
            try {
                if(!ticketState.isTicketAvailable(ticket))
                {
                    System.out.println("Ticket is not available. Try again.");
                    continue;
                }
            } catch (WrongTicketIDException e) {
                System.out.print(e.getMessage() + " Try again.");
                continue;
            }
            System.out.println("Continue? YES/NO");
            operation = in.next();
            if(operation.equalsIgnoreCase("no"))
                continue;
            System.out.println("Number of tickets: ");
            numberOfTicket = in.nextInt();
            if(numberOfTicket <= 0)
            {
                System.out.println("Wrong number of tickets. Try again.");
                continue;
            }
            if(!ticketState.hasEnoughTickets(ticket, numberOfTicket))
            {
                System.out.println("There is enough tickets in the machine. Try to buy less.");
                continue;
            }
            System.out.println("Continue? YES/NO");
            operation = in.next();
            if(operation.equalsIgnoreCase("no"))
                continue;

            System.out.print("Please, put money into ticket machine: ");
            MoneyState moneyInMachine = null;
            try
            {
                moneyInMachine = moneyState.tookMoney(ticketState.getPriceOfTicket(ticket), numberOfTicket);
            } catch (WrongTicketIDException e)
            {
                System.out.println("You choose wrong type of ticket! Try again.");
                continue;
            }
            if(moneyInMachine == null)
            {
                System.out.println("Try again.");
                continue;
            }
            ArrayList<Money> moneyToReturn = null;
            try {
                moneyToReturn = moneyState.UpadateState(ticketState.getPriceOfTicket(ticket) * numberOfTicket, moneyInMachine.getAvailableMoney(), moneyInMachine.getActualMoneyState());
            } catch (NotEnoughMoneyException e) {
                System.out.println(e.getMessage());
                System.out.print("Try again.");
                continue;
            } catch (OutOfMoneyException e) {
                System.out.println(e.getMessage());
                System.out.print("Try again.");
                continue;
            } catch (WrongTicketIDException e) {

            }
            ticketState.updateState(ticket ,numberOfTicket);

            System.out.println("Your tickets are printing.");
            if(moneyToReturn.size() == 0)
                System.out.print("It's nothing to return.");
            else
            {
                StringBuilder sb = new StringBuilder("");
                for(Money m: moneyToReturn)
                    sb.append(m.getNominalValue() + " " + moneyState.getCurrency().getCurrencyCode() + " " + m.count + "\n");
                System.out.print("Please, take your rest. \n" + sb.toString());
            }

            System.out.println("\nThank you for your order.");

        }
    }
}
