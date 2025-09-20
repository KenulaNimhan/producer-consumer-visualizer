package site.visualizer.threads;

import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;

import java.util.ArrayList;
import java.util.List;

public class Vendor extends Thread {
    private final String name;
    private final List<Ticket> producedTickets = new ArrayList<>();
    private final int quota;
    private final TicketPool ticketPool;

    public Vendor(String name, int quota, TicketPool ticketPool) {
        this.name = name;
        this.quota = quota;
        this.ticketPool = ticketPool;
    }

    public void produceAndAddTicket() throws InterruptedException {
        producedTickets.add(ticketPool.addTicket());
    }

    public void printProducedTicketInfo() {
        System.out.println("tickets produced by "+name);
        for (Ticket ticket: producedTickets) System.out.println(ticket);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
        for (int i=0; i<quota; i++) {
            try {
                produceAndAddTicket();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
