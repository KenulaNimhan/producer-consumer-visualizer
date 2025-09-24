package site.visualizer.run;

import org.springframework.stereotype.Service;
import site.visualizer.config.Config;
import site.visualizer.config.Configuration;
import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;
import site.visualizer.core.TicketSystemCoordinator;
import site.visualizer.event.EventType;
import site.visualizer.event.TicketEvent;
import site.visualizer.event.TicketEventPublisher;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class VisualizerRun {

    private final TicketEventPublisher publisher;
    private final TicketPool ticketPool;
    private final TicketSystemCoordinator coordinator;

    private final List<Thread> threadPool = new ArrayList<>();

    // used for console level usage. should remove in prod.
    public VisualizerRun(TicketPool ticketPool, TicketSystemCoordinator coordinator) {
        this.publisher = null;
        this.ticketPool = ticketPool;
        this.coordinator = coordinator;
    }

    public VisualizerRun(TicketEventPublisher publisher, TicketPool ticketPool, TicketSystemCoordinator coordinator) {
        this.publisher = publisher;
        this.ticketPool = ticketPool;
        this.coordinator = coordinator;
    }

    /**
     * checks if the configuration object only contains values within defined range.
     * @param configuration data sent by the client
     * @return "ok" if valid.
     */
    public String accept(Configuration configuration) {

        Map<Config, Supplier<Integer>> configGetters = Map.of(
                Config.TOTAL_TICKETS, configuration::getTotalNoOfTickets,
                Config.BUFFER_CAP, configuration::getBufferCap,
                Config.VENDOR_COUNT, configuration::getVendorCount,
                Config.CUSTOMER_COUNT, configuration::getCustomerCount,
                Config.CAP_PER_CUSTOMER, configuration::getCapPerCustomer,
                Config.RETRIEVAL_RATE, configuration::getRetrievalRate,
                Config.RELEASE_RATE, configuration::getReleaseRate
        );

        for (Config config: Config.values()) {
            var receivedValue = configGetters.get(config).get();
            if (!config.rangeAccepts(receivedValue)) return config+" value is invalid";
        }

        return "ok";
    }

    /**
     * returns the runnable for producers.
     * @param quota - no. of tickets allocated for the producer.
     */
    public Runnable getProducerRunnable(int quota) {
        return () -> {
            for (int i=0; i<quota; i++) {
                try {
                    // checking if stop has been initiated during run
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

                    // checking if consumption is complete
                    if (coordinator.isConsumptionDone()) {
                        System.out.println(Thread.currentThread().getName()+" closes since consumption is complete");
                        break;
                    }

                    // producing and adding ticket to ticket pool
                    Ticket newTicket = new Ticket();
                    if (ticketPool.addTicket(newTicket)) {
                        coordinator.incrementProducedCount();
                        // creating event and publishing it to websocket queue
                        TicketEvent event = new TicketEvent(
                                EventType.PRODUCED,
                                newTicket.getProducedStatement(),
                                ticketPool.getSize()
                        );
//                    publisher.sendEvent(event);
                        System.out.println(newTicket.getProducedStatement());

                        // small delay to make operations more visible
                        Thread.sleep(1000);
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
     */
    public Runnable getConsumerRunnable(int cap) {
        return () -> {
            for (int i=0; i<cap; i++) {
                try {
                    // checking if stop has been initiated during run
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

                    // checking if production is complete
                    if (coordinator.isProductionDone() && ticketPool.isEmpty()) {
                        System.out.println(Thread.currentThread().getName()+" closes since production is complete");
                        break;
                    }

                    // consuming ticket from ticket pool
                    Ticket ticket = ticketPool.removeTicket();
                    if (ticket == null) {
                        // since a ticket remove was unsuccessful i is decremented
                        i--;
                        continue;
                    }
                    ticket.setBoughtBy(Thread.currentThread().getName());
                    ticket.setBoughtAt(String.valueOf(LocalTime.now()));
                    coordinator.incrementConsumedCount();
                    // creating event and publishing it to websocket queue
                    TicketEvent event = new TicketEvent(
                            EventType.CONSUMED,
                            ticket.getConsumedStatement(),
                            ticketPool.getSize()
                    );
//                    publisher.sendEvent(event);
                    System.out.println(ticket.getConsumedStatement());

                    // small delay to make operations more visible
                    Thread.sleep(1000);
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
     * @throws InterruptedException if any thread gets interrupted
     */
    public void run(Configuration data) throws InterruptedException {

        Thread[] vendors = new Thread[data.getVendorCount()];
        Thread[] customers = new Thread[data.getCustomerCount()];

        int baseForVendors = data.getTotalNoOfTickets() / data.getVendorCount();
        int extraForVendors = data.getTotalNoOfTickets() % data.getVendorCount();

        for (int i=1; i<=data.getVendorCount(); i++) {
            int quota = baseForVendors;
            if (i <= extraForVendors) quota++;
            vendors[i-1] = new Thread(getProducerRunnable(quota), "Vendor "+i);
        }


        for (int i=1; i<=data.getCustomerCount(); i++) {
            customers[i-1] = new Thread(getConsumerRunnable(data.getCapPerCustomer()), "Customer "+i);
        }

        Collections.addAll(threadPool, vendors);
        Collections.addAll(threadPool, customers);
        Collections.shuffle(threadPool); // mixing vendor and customer threads

        for (Thread thread: threadPool) thread.start();
        for (Thread thread: threadPool) thread.join();
    }

    public void stop() {
        for (Thread thread: threadPool) {
            thread.interrupt();
        }
        threadPool.clear();
    }
}
