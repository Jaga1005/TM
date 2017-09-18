package money;


/**
 * Representation of money
 * Created by Agata Sularz.
 */
public class Money implements Comparable, Cloneable {
    private final double nominalValue;
    public int count;

    public Money(double nominalValue, int count) {
        this.nominalValue = nominalValue;
        this.count = count;
    }

    /**
     * Getter of nominal value of money
     * @return
     */
    public double getNominalValue() {
        return nominalValue;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.nominalValue, ((Money) o).getNominalValue());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Money(nominalValue, count);
    }

    @Override
    public String toString() {
        return nominalValue + " x " + count;
    }
}
