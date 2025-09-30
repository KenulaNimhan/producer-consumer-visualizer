package site.visualizer.core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss.SSS");

    private final String producedAt, producedBy;
    private String id, boughtBy, boughtAt;

    public Ticket() {
        producedAt = formatter.format(LocalTime.now());
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

    public void setBoughtAt(LocalTime boughtTime) {
        this.boughtAt = formatter.format(boughtTime);
    }

    // OTHER METHODS

    public boolean isIdAssigned() {
        return id != null;
    }

    /**
     * used to assign id only after the ticket is successfully added to the pool.
     */
    public void assignId(String id) {
        this.id = id;
    }

    /**
     * @return a string containing details including producer thread name and produced time.
     */
    public String getProducedStatement() {
        return String.format("%s : %s produced ticket %s", producedAt, producedBy, id);
    }

    /**
     * @return a string containing details including consumer thread name and consumed time.
     */
    public String getConsumedStatement() {
        return String.format("%s %s bought ticket %s", boughtAt, boughtBy, id);
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
