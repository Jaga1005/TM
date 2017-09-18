package tickets;

import data.DataFileHelper;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Test of ticket state
 * Created by Agata Sularz.
 */
public class TicketStateTest {

    @Test
    public void getPriceOfTicket() throws Exception{
        DataFileHelper.createFilesWithDatasIfNotExist();
        TicketState ts = new TicketState();

        try {
            assertEquals(ts.getPriceOfTicket(1.0), 3.0, 0.0);
            assertEquals(ts.getPriceOfTicket(1.1), 1.95 ,0.0);
            assertEquals(ts.getPriceOfTicket(5.0), 3.0, 0.0);

        } catch (WrongTicketIDException e) {
        }

        Throwable e = null;
        try {
           ts.getPriceOfTicket(0.0);
        } catch (Throwable ex) {
            e = ex;
        }

        assertTrue(e instanceof WrongTicketIDException);


    }

    @Test
    public void isTicketAvailable()throws Exception {
        DataFileHelper.createFilesWithDatasIfNotExist();
        TicketState ts = new TicketState();
        try {
            assertEquals(ts.isTicketAvailable(1.0), true);
            assertEquals(ts.isTicketAvailable(1.0), true);
            ts.getTickets().stream().filter(t->t.getID() == 1.0).findFirst().orElse(null).count=0;
            assertEquals(ts.isTicketAvailable(1.0), false);
        } catch (WrongTicketIDException e) {
        }

        Throwable e = null;
        try {
            ts.isTicketAvailable(0.0);
        } catch (Throwable ex) {
            e = ex;
        }
        assertTrue(e instanceof WrongTicketIDException);

    }

    @Test
    public void hasEnoughTickets() throws Exception {
        DataFileHelper.createFilesWithDatasIfNotExist();
        TicketState ts = new TicketState();

        assertEquals(ts.hasEnoughTickets(1.0, 1), true);
        assertEquals(ts.hasEnoughTickets(1.0, Integer.MAX_VALUE), false);
        assertEquals(ts.hasEnoughTickets(2.0, 2), true);
        assertEquals(ts.hasEnoughTickets(2.0, 5), true);
        ts.getTickets().stream().filter(t-> t.getID() == 2.0).findFirst().orElse(null).count = 2;
        assertEquals(ts.hasEnoughTickets(2.0, 3), false);
    }

}