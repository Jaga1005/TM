package money;

/**
 * Created by Agata Sularz.
 */
public class OutOfMoneyException extends Throwable {
    public OutOfMoneyException(String s) {
        super(s);
    }
}
