package site.visualizer.core;

import java.util.concurrent.LinkedBlockingQueue;

public class TicketPool {
    private final LinkedBlockingQueue<Ticket> ticketPool;

    TicketPool(int ticketPoolCap) {
        ticketPool = new LinkedBlockingQueue<>(ticketPoolCap);
    }

    public Ticket addTicket() {
        Ticket newTicket = new Ticket();
        ticketPool.add(newTicket);

        return newTicket;
    }

    public Ticket removeTicket() {
        return ticketPool.remove();
    }
}
