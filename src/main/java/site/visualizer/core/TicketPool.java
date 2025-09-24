package site.visualizer.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class of the buffer which contains the tickets
 * buffer is a LinkedBlockingQueue which utilizes offer() and poll() timed waiting methods.
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
     * @throws InterruptedException since offer() is a blocking/waiting method
     */
    public boolean addTicket(Ticket newTicket) throws InterruptedException {
        return ticketPool.offer(newTicket, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * a Ticket is removed from the buffer
     * @return removed Ticket which is added to the customer's purchased ticket list.
     * @throws InterruptedException since poll() is a blocking/waiting method.
     */
    public Ticket removeTicket() throws InterruptedException {
        return ticketPool.poll(200, TimeUnit.MILLISECONDS);
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

    public boolean isEmpty() {
        return ticketPool.isEmpty();
    }
}
