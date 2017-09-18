package money;

/**
 * Created by Agata Sularz.
 */
public class NotEnoughMoneyException extends Throwable {
    public NotEnoughMoneyException(String s) {
        super(s);
    }
}
