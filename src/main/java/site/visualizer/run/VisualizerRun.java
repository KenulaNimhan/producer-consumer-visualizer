package site.visualizer.run;

import org.springframework.stereotype.Service;
import site.visualizer.config.Config;
import site.visualizer.config.Configuration;
import site.visualizer.core.TicketPool;
import site.visualizer.core.consume.Customer;
import site.visualizer.core.produce.Vendor;
import site.visualizer.event.TicketEventPublisher;

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
            if (!config.rangeAccepts(receivedValue)) return config+" value is wrong";
        }

        return "ok";
    }

    public void run(Configuration data) throws InterruptedException {
        Vendor[] vendors = new Vendor[data.getVendorCount()];

        for (int i=0; i<data.getVendorCount(); i++) {
            int vendorID = i+1;
            vendors[i] = new Vendor(
                    "vendor "+vendorID,
                    data.getTotalNoOfTickets()/data.getVendorCount(),
                    ticketPool, publisher);
        }

        Customer[] customers = new Customer[data.getCustomerCount()];

        for (int i=0; i<data.getCustomerCount(); i++) {
            int customerID = i+1;
            customers[i] = new Customer(
                    "customer "+customerID,
                    data.getCapPerCustomer(),
                    ticketPool, publisher);
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
