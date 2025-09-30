package site.visualizer.service;

import site.visualizer.config.data.Configuration;
import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;
import site.visualizer.core.TicketSystemCoordinator;
import site.visualizer.core.event.EventPublisher;
import site.visualizer.core.event.EventType;
import site.visualizer.core.event.TicketEvent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisualizerRun {

    private final EventPublisher publisher;
    private final List<Thread> threadPool = new ArrayList<>();
    private final String username;
    private final TicketPool ticketPool;
    private final TicketSystemCoordinator coordinator;

    public VisualizerRun(
            EventPublisher publisher,
            String username,
            TicketPool ticketPool,
            TicketSystemCoordinator coordinator) {
        this.publisher = publisher;
        this.username = username;
        this.ticketPool = ticketPool;
        this.coordinator = coordinator;

    }

    /**
     * returns the runnable for producers.
     * @param quota - no. of tickets allocated for the producer.
     * @param releaseRate - time in milliseconds to pause the thread.
     */
    public Runnable getProducerRunnable(int quota, int releaseRate) {
        return () -> {
            for (int i=0; i<quota; i++) {
                try {
                    // checking if stop has been initiated during run
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

                    // checking if consumption is complete
                    if (coordinator.isConsumptionDone()) {
                        String msg = Thread.currentThread().getName()+" closes since consumption is complete";
                        System.out.println(msg);
                        publisher.sendEventToUser(username, new TicketEvent(EventType.PRODUCED, msg, ticketPool.getSize()));
                        break;
                    }

                    // producing and adding ticket to ticket pool
                    Ticket newTicket = new Ticket();
                    if (ticketPool.addTicket(newTicket)) {
                        newTicket.assignId(String.valueOf(coordinator.getTotalProduced()));
                        // creating event and publishing it to websocket queue
                        TicketEvent event = new TicketEvent(
                                EventType.PRODUCED,
                                newTicket.getProducedStatement(),
                                ticketPool.getSize()
                        );
                        if (publisher != null) publisher.sendEventToUser(username, event);
                    System.out.println(newTicket.getProducedStatement());

                    // applying release rate of tickets
                    Thread.sleep(releaseRate);
                    } else {
                        i--;
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" interrupted. Shutting down...");
                    break; // exiting loop and then terminate after being interrupted
                }
            }
        };
    }

    /**
     * returns the runnable for the consumers.
     * @param cap - ticket cap per consumer.
     * @param retrievalRate - time in milliseconds to pause the thread
     */
    public Runnable getConsumerRunnable(int cap, int retrievalRate) {
        return () -> {
            for (int i=0; i<cap; i++) {
                try {
                    // checking if stop has been initiated during run
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

                    // checking if production is complete
                    if (coordinator.isProductionDone() && ticketPool.isEmpty()) {
                        String msg = Thread.currentThread().getName()+" closes since production is complete";
                        System.out.println(msg);
                        publisher.sendEventToUser(username, new TicketEvent(EventType.CONSUMED, msg, ticketPool.getSize()));
                        break;
                    }

                    // consuming ticket from ticket pool
                    Ticket ticket = ticketPool.removeTicket();
                    if (ticket == null || !ticket.isIdAssigned()) {
                        // since a ticket remove was unsuccessful, 'i' is decremented
                        i--;
                        continue;
                    }
                    ticket.setBoughtBy(Thread.currentThread().getName());
                    ticket.setBoughtAt(LocalTime.now());
                    coordinator.incrementConsumedCount();
                    // creating event and publishing it to websocket queue
                    TicketEvent event = new TicketEvent(
                            EventType.CONSUMED,
                            ticket.getConsumedStatement(),
                            ticketPool.getSize()
                    );
                    if (publisher != null) publisher.sendEventToUser(username, event);
                    System.out.println(ticket.getConsumedStatement());

                    // applying retrieval rate of tickets
                    Thread.sleep(retrievalRate);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" interrupted. Shutting down...");
                    break; // exiting loop and then terminate after being interrupted
                }
            }
        };
    }

    /**
     * runs the configuration under simulation
     * @param data validated and accepted configuration
     */
    public void run(Configuration data) {

        // clearing old threads
        threadPool.clear();

        Thread[] vendors = new Thread[data.getVendorCount()];
        Thread[] customers = new Thread[data.getCustomerCount()];

        int baseForVendors = data.getTotalTickets() / data.getVendorCount();
        int extraForVendors = data.getTotalTickets() % data.getVendorCount();

        for (int i=1; i<=data.getVendorCount(); i++) {
            int quota = baseForVendors;
            if (i <= extraForVendors) quota++;
            vendors[i-1] = new Thread(getProducerRunnable(quota, data.getReleaseRate()), "Vendor "+i);
        }


        for (int i=1; i<=data.getCustomerCount(); i++) {
            customers[i-1] = new Thread(getConsumerRunnable(data.getCapPerCustomer(), data.getRetrievalRate()), "Customer "+i);
        }

        Collections.addAll(threadPool, vendors);
        Collections.addAll(threadPool, customers);
        Collections.shuffle(threadPool); // mixing vendor and customer threads

        for (Thread thread: threadPool) thread.start();

        try {
            for (Thread thread: threadPool) thread.join();
        } catch (InterruptedException e) {
            System.out.println("error when joining threads");
        }

        publisher.sendEventToUser(username, new TicketEvent(EventType.END, "done", ticketPool.getSize()));
    }
}
