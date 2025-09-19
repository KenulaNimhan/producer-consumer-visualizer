package site.visualizer.threads;

import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Runnable{
    private final String name;
    private final int ticketCap;
    private final List<Ticket> purchasedTickets;
    private int purchasedCount;

    private final TicketPool ticketPool;

    public Customer(String name, int ticketCap, TicketPool ticketPool) {
        this.name = name;
        this.ticketCap = ticketCap;
        purchasedTickets = new ArrayList<>(ticketCap);
        this.ticketPool = ticketPool;
    }

    public boolean hasReachedLimit() {
        return purchasedCount == ticketCap;
    }

    public void buyTicket() throws Exception {
        if (hasReachedLimit()) throw new Exception("cannot buy more tickets.");
        var boughtTicket = ticketPool.removeTicket();
        boughtTicket.setBoughtBuy(Thread.currentThread().getName());
        purchasedTickets.addLast(boughtTicket);
        purchasedCount++;
    }

    public void printBoughtTicketInfo() {
        for (Ticket ticket: purchasedTickets) System.out.println(ticket);
    }

    @Override
    public void run() {
        while (!hasReachedLimit()) {
            try {
                buyTicket();
            } catch (Exception e) {
                System.out.println("cannot buy anymore tickets");;
            }
        }
    }
}
