package site.visualizer.core.produce;

import site.visualizer.core.TicketPool;
import site.visualizer.event.EventType;
import site.visualizer.event.TicketEvent;
import site.visualizer.event.TicketEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class Vendor extends Thread {
    private final String name;
    private final List<Ticket> producedTickets = new ArrayList<>();
    private final int quota;
    private final TicketPool ticketPool;
    private final TicketEventPublisher publisher;

    public Vendor(String name, int quota, TicketPool ticketPool, TicketEventPublisher publisher) {
        this.name = name;
        this.quota = quota;
        this.ticketPool = ticketPool;
        this.publisher = publisher;
    }

    /**
     * Produces a new ticket and then adds it to the buffer and also the vendor's produced list.
     * @throws InterruptedException since addTicket uses put() which is a blocking method.
     */
    public void produceAndAddTicket() throws InterruptedException {
        Ticket newTicket = new Ticket();
        ticketPool.addTicket(newTicket);
        producedTickets.add(newTicket);

        System.out.println("\u001B[33m"+name+" produced ticket "+newTicket.getId()+" at "+newTicket.getProducedTime()+"\u001B[0m");
        TicketEvent newEvent = new TicketEvent(EventType.PRODUCED, newTicket, ticketPool.getSize());
        publisher.sendEvent(newEvent);
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
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
