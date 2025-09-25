package site.visualizer.core;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger count = new AtomicInteger();

    private final String id, producedAt, producedBy;
    private String boughtBy, boughtAt;

    public Ticket() {
        id = String.valueOf(count.incrementAndGet());
        producedAt = String.valueOf(LocalTime.now());
        producedBy = Thread.currentThread().getName();
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

    public String getProducedAt() {
        return producedAt;
    }

    public String getBoughtBy() {
        return boughtBy;
    }

    public String getBoughtAt() {
        return boughtAt;
    }

    // SETTERS

    /**
     * this method is used for setting the customer's name in the ticket after purchase.
     * @param boughtBy name of the customer thread.
     */
    public void setBoughtBy(String boughtBy) {
        this.boughtBy = boughtBy;
    }

    public void setBoughtAt(String boughtAt) {
        this.boughtAt = boughtAt;
    }

    // OTHER METHODS

    /**
     * @return a string containing details including producer thread name and produced time.
     */
    public String getProducedStatement() {
        return String.format("%s produced ticket %s at %s", producedBy, id, producedAt);
    }

    /**
     * @return a string containing details including consumer thread name and consumed time.
     */
    public String getConsumedStatement() {
        return String.format("%s bought ticket %s at %s", boughtBy, id, boughtAt);
    }

    /**
     * prints a detailed view of the ticket
     * details include who produced the ticket, and at what time.
     * @return details of ticket
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
