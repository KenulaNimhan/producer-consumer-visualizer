package site.visualizer.threads;

import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Customer extends Thread{
    private final String name;
    private final int ticketCap;
    private final List<Ticket> purchasedTickets;
    private int purchasedCount=0;

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

    /**
     * removes a ticket from the buffer and adds it to the customer's purchased list.
     * @throws Exception if cap is reached and is not allowed to buy any more tickets.
     */
    public void buyTicket() throws Exception {
        if (hasReachedLimit()) throw new Exception("cannot buy more tickets.");
        var boughtTicket = ticketPool.removeTicket();
        boughtTicket.setBoughtBuy(Thread.currentThread().getName());
        purchasedTickets.addLast(boughtTicket);
        purchasedCount++;

        System.out.println(name+" bought ticket "+boughtTicket.getId()+" at "+boughtTicket.getProducedTime());
    }

    public void printBoughtTicketInfo() {
        System.out.println("tickets bought by "+name);
        System.out.println(purchasedCount);
        for (Ticket ticket: purchasedTickets) System.out.print(ticket);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
        while (!hasReachedLimit()) {
            try {
                buyTicket();
            } catch (NoSuchElementException e) {
                System.out.println("tickets are not added yet");
            } catch (Exception e) {
                System.out.println(name+e.getMessage());
            }
        }
    }
}
