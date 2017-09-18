package money;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of money state
 * Created by Agata Sularz.
 */
public class MoneyStateTest {
    @Test
    public void getAvailableMoney() throws Exception {
        MoneyState ms = new MoneyState(true);
        ms.getActualMoneyState().add(new Money(5.0, 2));
        ms.getActualMoneyState().add(new Money(0.2, 2));
        ms.getActualMoneyState().add(new Money(0.5, 5));
        ms.getActualMoneyState().add(new Money(1.0, 1));

        assertEquals(ms.getAvailableMoney(), 13.9 , 0.0);
        ms.getActualMoneyState().add(new Money(2.0, 5));
        assertEquals(ms.getAvailableMoney(),23.9, 0.0 );
        ms.getActualMoneyState().set(0, new Money(5.0, 5));
        assertEquals(ms.getAvailableMoney(), 38.9, 0.0);
        ms.getActualMoneyState().clear();
        assertEquals(ms.getAvailableMoney(), 0.0, 0.0);
    }


}