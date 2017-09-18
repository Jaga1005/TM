package tickets;


/**
 * Created by Agata Sularz.
 */
public class WrongTicketIDException extends Throwable
{
    public WrongTicketIDException(String s)
    {
        super(s);
    }

}
