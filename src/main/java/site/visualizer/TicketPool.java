package site.visualizer;

import java.util.concurrent.LinkedBlockingQueue;

public class TicketPool {
    private final LinkedBlockingQueue<Ticket> ticketPool;

    TicketPool(int ticketPoolCap) {
        ticketPool = new LinkedBlockingQueue<>(ticketPoolCap);
    }
}
