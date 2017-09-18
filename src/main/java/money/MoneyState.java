package money;

import data.DataFileHelper;
import tickets.WrongTicketIDException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Scanner;
import java.util.stream.DoubleStream;

/**
 * Representation of money state in ticket machine
 * Created Agata Sularz.
 */
public class MoneyState {
    private final ArrayList<Money> actualMoneyState;
    private final java.util.Currency currency;

    public MoneyState(boolean clear) {
        currency = DataFileHelper.getCurrency();

        if(!clear)
        {
            actualMoneyState = DataFileHelper.getMoneyState();
            Collections.sort(actualMoneyState);
        }
        else
        {
            actualMoneyState = new ArrayList<>();
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public ArrayList<Money> getActualMoneyState() {
        return actualMoneyState;
    }

    /**
     * Updates state of money in ticket machine and saves changes in file
     * @param amount customer money
     * @param curr available money in ticket machine
     * @param additionalMoney
     * @return
     * @throws NotEnoughMoneyException
     * @throws OutOfMoneyException
     */
    public ArrayList<Money> UpadateState (double amount, double curr, ArrayList<Money> additionalMoney) throws NotEnoughMoneyException, OutOfMoneyException {
        if(amount == curr)
            return new ArrayList<>();
        if(amount > curr)
            throw new NotEnoughMoneyException("Too less money.");

        ArrayList<Money> moneyToReturn = FindMoneyToReturn(curr - amount, additionalMoney);
        DataFileHelper.updateMoneyState(actualMoneyState, currency);
        return moneyToReturn;
    }

    /**
     * Returns sum of available money in ticket machine
     * @return
     */
    public double getAvailableMoney() {
        return actualMoneyState.stream().flatMapToDouble(m-> DoubleStream.of(m.count * m.getNominalValue())).sum();
    }

    /**
     * Finds money to return
     * @param rest rest to return
     * @param additionalMoney customer money in ticket machine
     * @return list of money to return
     * @throws OutOfMoneyException
     */
    private ArrayList<Money> FindMoneyToReturn(double rest, ArrayList<Money> additionalMoney) throws OutOfMoneyException {
        ArrayList<Money> moneyToReturn = new ArrayList<>();
        double actualState = 0;
        int lenght = actualMoneyState.size();
        ArrayList<Money> tmp = (ArrayList<Money>) actualMoneyState.clone();

        addMoney(additionalMoney);

        for(int i=0; i<lenght && actualState!=rest; i++) {
            moneyToReturn.add(0, new Money(actualMoneyState.get(i).getNominalValue(), 0));
            while(actualState + actualMoneyState.get(i).getNominalValue() <= rest && actualMoneyState.get(i).count > 0)
            {
                moneyToReturn.get(0).count++;
                actualState+= actualMoneyState.get(i).getNominalValue();
                actualMoneyState.get(i).count--;
            }
        }

        if(actualState != rest)
        {
            actualMoneyState.clear();
            actualMoneyState.addAll(tmp);
            throw new OutOfMoneyException("Not enought money in ticket machine!");
        }

        for(int i=0; i< moneyToReturn.size(); i++)
            if(moneyToReturn.get(i).count == 0)
                moneyToReturn.remove(i);
        return moneyToReturn;
    }

    /**
     * Adding new money to actual money state
     * @param additionalMoney new money from transaction
     */
    private void addMoney(ArrayList<Money> additionalMoney) {
        Collections.sort(additionalMoney);
        for(Money m: additionalMoney)
        {
            actualMoneyState.stream()
                    .filter(money -> money.getNominalValue() == m.getNominalValue())
                    .findFirst()
                    .orElse(null)
                    .count+=m.count;

        }
    }

    /**
     * It takes money from customer and give rest
     * @param price price of ticket
     * @param numberOfTicket number of tickets
     * @return returns customer rest
     * @throws WrongTicketIDException
     */
    public MoneyState tookMoney(double price , int numberOfTicket) throws WrongTicketIDException {
        MoneyState instance = new MoneyState(true);
        double allPrice = price * numberOfTicket;
        double moneyPut = 0;
        String kindsOfMoney = getAvailableCoins();
        Scanner in = new Scanner(System.in);
        System.out.print("It's " + allPrice + currency.getCurrencyCode() + "\n");
        System.out.println("Please, put your money to the machine. Ticket machine receives:\n" + kindsOfMoney);

        while(true)
        {
              System.out.print("What kind of money would you like to put? ");
              double kindOfMoney = in.nextDouble();
              if(!kindsOfMoney.contains(kindOfMoney + ""))
              {
                  System.out.print("\nWrong kind of money! Try again? YES/NO");
                  String answer = in.nextLine();
                  if(answer.equalsIgnoreCase("NO"))
                      return null;
                  else continue;
              }
              System.out.print("\nHow many coins? ");
              int count = in.nextInt();
              instance.actualMoneyState.add(new Money(kindOfMoney, count));
              moneyPut+= count* kindOfMoney;
              System.out.println("\nIt's " + moneyPut + " " + currency.getCurrencyCode()+ ".");
              if(moneyPut >= allPrice) {
                  System.out.print("It's enought money. Buy a ticket? YES/NO");
                  String answer = in.next();
                  if(answer.equalsIgnoreCase("YES")) {
                      System.out.println("Please, wait for a moment");
                      return instance;
                  }
                  else
                  {
                      System.out.print("\nTook your money and try buying ticket again.");
                      return null;
                  }
              }
              System.out.print("Continue? YES/NO");
              String ans = in.next();
              if(!ans.equalsIgnoreCase("YES"))
              {
                  System.out.println("Took your money and try buying ticket again.");
                  return null;
              }
        }

    }

    /**
     * Returns string with available coins in ticket machine
     * @return
     */
    private String getAvailableCoins()
    {
        StringBuilder sb = new StringBuilder("");
        for(Money m : actualMoneyState)
            sb.append(m.getNominalValue() + " " + currency.getCurrencyCode() + "\n");
        return sb.toString();
    }
}
