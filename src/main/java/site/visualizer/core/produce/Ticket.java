package site.visualizer.core.produce;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger count = new AtomicInteger();

    private final String id, producedAt, producedBy;
    private String boughtBuy;

    Ticket() {
        id = String.valueOf(count.incrementAndGet());
        producedAt = String.valueOf(LocalTime.now());
        producedBy = Thread.currentThread().getName();
        System.out.println(producedBy+" produced ticket "+id+" at "+producedAt);
    }

    // GETTERS

    public String getProducedTime() {
        return producedAt;
    }

    public String getId() {
        return id;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public String getBoughtBuy() {
        return boughtBuy;
    }

    // SETTERS

    /**
     * this method is used for setting the customer's name in the ticket after purchase.
     * @param boughtBuy name of the customer thread, which is the name of the Customer itself.
     */
    public void setBoughtBuy(String boughtBuy) {
        this.boughtBuy = boughtBuy;
    }

    /**
     * prints a detailed view of the ticket
     * details include who produced the ticket, and at what time.
     * @return
     */
    @Override
    public String toString() {
        return String.format("""
                ______________________________________________
                TICKET %s,
                PRODUCED BY %s,
                PRODUCED AT %s \n""", id, producedBy, producedAt);
    }
}
