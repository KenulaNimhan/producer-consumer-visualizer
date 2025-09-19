package site.visualizer.threads;

import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;

import java.util.ArrayList;
import java.util.List;

public class Vendor implements Runnable {
    private final String name;
    private final List<Ticket> producedTickets = new ArrayList<>();
    private final int quota;

    Vendor(String name, int quota) {
        this.name = name;
        this.quota = quota;
    }

    public void produceTicket(TicketPool ticketPool) {
        producedTickets.add(ticketPool.addTicket());
    }

    public void printProducedTicketInfo() {
        for (Ticket ticket: producedTickets) System.out.println(ticket);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
    }
}
