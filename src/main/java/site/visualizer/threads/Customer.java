package site.visualizer.threads;

import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;
import site.visualizer.event.EventType;
import site.visualizer.event.TicketEvent;
import site.visualizer.event.TicketEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Customer extends Thread {
    private final String name;
    private final int ticketCap;
    private final List<Ticket> purchasedTickets = new ArrayList<>();
    private int purchasedCount=0;
    private final TicketEventPublisher publisher;

    private final TicketPool ticketPool;

    public Customer(String name, int ticketCap, TicketPool ticketPool, TicketEventPublisher publisher) {
        this.name = name;
        this.ticketCap = ticketCap;
        this.ticketPool = ticketPool;
        this.publisher = publisher;
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

        System.out.println("\u001B[34m"+name+" bought ticket "+boughtTicket.getId()+" at "+boughtTicket.getProducedTime()+"\u001B[0m");
        TicketEvent newEvent = new TicketEvent(EventType.CONSUMED, boughtTicket, ticketPool.getSize());
        publisher.sendEvent(newEvent);
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
                Thread.sleep(250);
            } catch (NoSuchElementException e) {
                System.out.println("tickets are not added yet");
            } catch (Exception e) {
                System.out.println(name+e.getMessage());
            }
        }
    }
}
