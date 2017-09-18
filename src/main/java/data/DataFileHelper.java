package data;


import money.Money;
import tickets.Ticket;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Scanner;
import java.util.stream.Stream;


/**
 * Support class to read files with data
 * Created by Agata Sularz.
 */
public class DataFileHelper
{
    private static final Path ticketsKinds = Paths.get(".\\datas\\tickets_kinds.txt");
    private static final Path money = Paths.get(".\\datas\\money.txt");


    /**
     * Initialize example data to ticket machine
     * @throws IOException
     */
   public static void createFilesWithDatasIfNotExist() throws IOException
   {
       if(!Files.exists(ticketsKinds))
       {
            Files.createDirectories(Paths.get(".\\datas"));
            Files.createFile(ticketsKinds);
            Files.createFile(money);

            fillFilesWithDatas();
   }
    }

    /**
     * Fills example files with data
     * @throws FileNotFoundException
     */
    private static void fillFilesWithDatas() throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter(ticketsKinds.toFile());
        pw.print("PLN");
        pw.println();
        pw.print("Czasowe");
        pw.println();
        pw.print("30min 3.00 5");
        pw.println();
        pw.print("60min 4.50 5");
        pw.println();
        pw.print("90min 6.00 5");
        pw.println();
        pw.print("24h 11.00 5");
        pw.println();
        pw.print("Jednorazowe");
        pw.println();
        pw.print("Normalne 3.0 5");
        pw.println();
        pw.print("Specjalne 3.20 5");
        pw.close();

        pw = new PrintWriter(money.toFile());
        pw.print("PLN");
        pw.println();
        pw.print("0.10 10");
        pw.println();
        pw.print("0.20 10");
        pw.println();
        pw.print("0.50 10");
        pw.println();
        pw.print("1.00 10");
        pw.println();
        pw.print("2.00 10");
        pw.println();
        pw.print("5.00 5");
        pw.close();

    }

    /**
     * Reading available money from file
     * @return list of available money
     */
    public static ArrayList<Money> getMoneyState()
    {
       final ArrayList<Money> moneyList = new ArrayList<>();

       try(Stream<String> stream = Files.lines(money)) {
            stream.skip(1)
                    .forEach(m -> {
                        String[] datas = m.split(" ");
                        if(datas.length == 2) {
                            moneyList.add(new Money(Double.parseDouble(datas[0]), Integer.parseInt(datas[1])));
                        }
                    });
       } catch (IOException e) {
           e.printStackTrace();
       }

        return moneyList;
    }

    /**
     * Returns currency of money in ticket machine
     * @return currency
     */
    public static java.util.Currency getCurrency()
    {
        Scanner out = null;
        try {
           out = new Scanner((money.toFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Currency currency = Currency.getInstance(out.nextLine());
        out.close();
       return currency;
    }

    /**
     * Saves actual state of money in ticket machine
     * @param actualState of money in ticket machine after one operation of buying tickets
     * @param currency
     */
    public static void updateMoneyState(ArrayList<Money> actualState, Currency currency)
    {
        try {
            PrintWriter pw = new PrintWriter(money.toFile());
            pw.print(currency.getCurrencyCode());
            pw.println();
            for (Money m: actualState)
            {
                pw.print(m.getNominalValue() + " " + m.count);
                pw.println();
            }

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves actual state of tickets in ticket machine
     * @param actualTickets state after one operation of buying
     */
    public static void updateTicketState(ArrayList<Ticket> actualTickets) {
        try {
            PrintWriter pw = new PrintWriter(ticketsKinds.toFile());
            String kind = "";
            for(Ticket ticket: actualTickets) {
                if (!ticket.getName().contains("ulgowe")) {
                    if (kind != ticket.getKind()) {
                        kind = ticket.getKind();
                        pw.print(kind);
                        pw.println();
                    }
                    pw.print(ticket.getName() + " " + ticket.getPrice() + " " + ticket.count);
                    pw.println();
                }
            }

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads available tickets from file
     * @return list of available tickets
     */
    public static ArrayList<Ticket> getTicketState()
    {
        final ArrayList<Ticket> tickets = new ArrayList<>();
        final StringBuilder sb = new StringBuilder("");
        final double[] i = {1.0};
        try(Stream<String>stream = Files.lines(ticketsKinds)) {
            stream.skip(1).forEach(ticket -> {
                String[] datas = ticket.split(" ");
                if(datas.length == 1)
                {
                    sb.delete(0, sb.length());
                    sb.append(datas[0]);
                }
                else if(datas.length>1)
                {
                    tickets.add( new Ticket(datas[0], Double.parseDouble(datas[1]), sb.toString(), i[0] ,Integer.parseInt(datas[2])));
                    i[0]++;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Ticket> ticketsWithRelief = new ArrayList<>();

        for(Ticket t : tickets)
        {
            BigDecimal bd = new BigDecimal(t.getPrice() * 0.65).setScale(2, RoundingMode.HALF_UP);
            ticketsWithRelief.add(new Ticket(t.getName() + " ulgowe", bd.doubleValue(), t.getKind(), t.getID() + 0.1, t.count));
        }
        tickets.addAll(ticketsWithRelief);
        return tickets;
    }
}
