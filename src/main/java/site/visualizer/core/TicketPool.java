package site.visualizer.core;

import site.visualizer.core.produce.Ticket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class of the buffer which contains the tickets
 * buffer is a LinkedBlockingQueue which utilized put() and take() blocking  methods.
 */
public class TicketPool {
    private final LinkedBlockingQueue<Ticket> ticketPool;

    public TicketPool(int ticketPoolCap) {
        ticketPool = new LinkedBlockingQueue<>(ticketPoolCap);
    }

    /**
     * adds a newly produced ticket to the buffer
     * @param newTicket newly produced ticket. the vendor passes the ticket as an argument
     *                  when calling the method.
     * @throws InterruptedException since put() is a blocking method
     */
    public void addTicket(Ticket newTicket) throws InterruptedException {
        ticketPool.put(newTicket);
    }

    /**
     * a Ticket is removed from the buffer
     * @return removed Ticket which is added to the customer's purchased ticket list.
     * @throws InterruptedException since take() is a blocking method.
     */
    public Ticket removeTicket() throws InterruptedException {
        return ticketPool.take();
    }

    /**
     * prints the info of the available tickets in the buffer.
     * it is indicated if the buffer is empty.
     */
    public void printTicketsInPool() {
        if (ticketPool.isEmpty()) System.out.println("no tickets in the buffer");
        for (Ticket ticket: ticketPool) {
            System.out.print(ticket);
        }
    }

    public int getSize() {
        return ticketPool.size();
    }
}
