package tickets;

import java.io.Serializable;

/**
 * Representation of ticket
 * Created by Agata Sularz.
 */
public class Ticket implements Serializable, Comparable {
    private final String name;
    private final double price;
    private final String kind;
    private final double ID;
    public int count;

    public Ticket(String name, double price, String kind, double id, int count) {
        this.name = name;
        this.price = price;
        this.kind = kind;
        ID = id;
        this.count = count;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (Double.compare(ticket.price, price) != 0) return false;
        if (Double.compare(ticket.ID, ID) != 0) return false;
        if (Double.compare(ticket.count, count) != 0) return false;
        return name.equals(ticket.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ID);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(count);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return name + '-' + price;
    }

    @Override
    public int compareTo(Object o) {
        Ticket t = (Ticket)o;
        int comparePrice = Double.compare(this.getPrice(), t.getPrice());

        return this.kind.compareTo(t.getKind()) !=0 ? ((int)this.ID == (int)t.getID()? comparePrice : Double.compare(this.ID, t.getID())) :  this.kind.compareTo(t.getKind());
    }
}
