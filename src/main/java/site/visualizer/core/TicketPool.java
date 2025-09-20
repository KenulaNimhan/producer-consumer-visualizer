package site.visualizer.core;

import java.util.concurrent.LinkedBlockingQueue;

public class TicketPool {
    private final LinkedBlockingQueue<Ticket> ticketPool;

    public TicketPool(int ticketPoolCap) {
        ticketPool = new LinkedBlockingQueue<>(ticketPoolCap);
    }

    public Ticket addTicket() throws InterruptedException {
        Ticket newTicket = new Ticket();
        ticketPool.put(newTicket);

        return newTicket;
    }

    public Ticket removeTicket() throws InterruptedException {
        return ticketPool.take();
    }

    public void printTicketsInPool() {
        if (ticketPool.isEmpty()) System.out.println("no tickets in the buffer");
        for (Ticket ticket: ticketPool) {
            System.out.print(ticket);
        }
    }
}
