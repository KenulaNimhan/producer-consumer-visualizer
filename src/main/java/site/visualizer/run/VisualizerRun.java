package site.visualizer.run;

import org.springframework.stereotype.Service;
import site.visualizer.config.Config;
import site.visualizer.config.Configuration;
import site.visualizer.core.Ticket;
import site.visualizer.core.TicketPool;
import site.visualizer.event.EventType;
import site.visualizer.event.TicketEvent;
import site.visualizer.threads.Customer;
import site.visualizer.threads.Vendor;
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
    private final List<Thread> threadPool = new ArrayList<>();

    public VisualizerRun(TicketEventPublisher publisher, TicketPool ticketPool) {
        this.publisher = publisher;
        this.ticketPool = ticketPool;
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

    public Runnable getProducerRunnable(int quota) {
        return () -> {
            for (int i=0; i<quota; i++) {
                try {
                    // producing and adding ticket to ticket pool
                    Ticket newTicket = new Ticket();
                    ticketPool.addTicket(newTicket);

                    // creating event and publishing it to websocket queue
                    TicketEvent event = new TicketEvent(
                            EventType.PRODUCED,
                            newTicket.getProducedStatement(),
                            ticketPool.getSize()
                    );
                    publisher.sendEvent(event);

                    // small delay to make operations more visible
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public Runnable getConsumerRunnable(int cap) {
        return () -> {
            for (int i=0; i<cap; i++) {
                try {
                    // consuming ticket from ticket pool
                    Ticket ticket = ticketPool.removeTicket();
                    ticket.setBoughtBy(Thread.currentThread().getName());
                    ticket.setBoughtAt(String.valueOf(LocalTime.now()));

                    // creating event and publishing it to websocket queue
                    TicketEvent event = new TicketEvent(
                            EventType.CONSUMED,
                            ticket.getConsumedStatement(),
                            ticketPool.getSize()
                    );
                    publisher.sendEvent(event);

                    // small delay to make operations more visible
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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

        for (int i=0; i<data.getVendorCount(); i++) {
            int vendorID = i+1;
            vendors[i] = new Thread(getProducerRunnable(baseForVendors), "Vendor "+vendorID);
        }


        for (int i=0; i<data.getCustomerCount(); i++) {
            int customerID = i+1;
            customers[i] = new Thread(getConsumerRunnable(data.getCapPerCustomer()), "Customer "+customerID);
        }

        Collections.addAll(threadPool, vendors);
        Collections.addAll(threadPool, customers);
        Collections.shuffle(threadPool);

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
