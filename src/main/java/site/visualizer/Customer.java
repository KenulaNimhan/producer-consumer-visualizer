package site.visualizer;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Runnable{
    private final String name;
    private final int ticketCap;
    private final List<Ticket> purchasedTickets;
    private int purchasedCount;

    public Customer(String name, int ticketCap) {
        this.name = name;
        this.ticketCap = ticketCap;
        purchasedTickets = new ArrayList<>(ticketCap);
    }

    public boolean hasReachedLimit() {
        return purchasedCount == ticketCap;
    }

    public void buyTicket(Ticket newTicket) throws Exception {
        if (hasReachedLimit()) throw new Exception("cannot buy more tickets.");
        purchasedTickets.addLast(newTicket);
        purchasedCount++;
    }

    public void printBoughtTicketInfo() {
        for (Ticket ticket: purchasedTickets) {
            System.out.println(ticket);
        }
    }

    public void run() {
        Thread.currentThread().setName(name);
    }
}
