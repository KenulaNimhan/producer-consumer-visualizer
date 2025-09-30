package site.visualizer.service;

import org.springframework.stereotype.Service;
import site.visualizer.config.data.Config;
import site.visualizer.config.data.Configuration;
import site.visualizer.core.TicketPool;
import site.visualizer.core.TicketSystemCoordinator;
import site.visualizer.core.event.EventPublisher;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class VisualizerService {
    private final EventPublisher publisher;

    public VisualizerService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * checks if the configuration object only contains values within defined range.
     * @param configuration data sent by the client.
     * @return "ok" if valid.
     */
    public boolean accept(Configuration configuration) {

        Map<Config, Supplier<Integer>> configGetters = Map.of(
                Config.TOTAL_TICKETS, configuration::getTotalTickets,
                Config.BUFFER_CAP, configuration::getBufferCap,
                Config.VENDOR_COUNT, configuration::getVendorCount,
                Config.CUSTOMER_COUNT, configuration::getCustomerCount,
                Config.CAP_PER_CUSTOMER, configuration::getCapPerCustomer,
                Config.RETRIEVAL_RATE, configuration::getRetrievalRate,
                Config.RELEASE_RATE, configuration::getReleaseRate
        );

        for (Config config: Config.values()) {
            var receivedValue = configGetters.get(config).get();
            if (!config.rangeAccepts(receivedValue)) return false;
        }
        return true;
    }

    /**
     * instantiates a run object and then runs the given configuration.
     * @param configuration validated config.
     * @param username session id used to send websocket messages to the correct message queue.
     */
    public void instantiateAndStart(Configuration configuration, String username) {
        // creating ticket pool and coordinator once config is validated and accepted.
        TicketPool ticketPool = new TicketPool(configuration.getBufferCap());
        int totalConsumableAmount = configuration.getCustomerCount() * configuration.getCapPerCustomer();
        TicketSystemCoordinator coordinator = new TicketSystemCoordinator(configuration.getTotalTickets(), totalConsumableAmount);

        VisualizerRun instance = new VisualizerRun(publisher, username, ticketPool, coordinator);
        new Thread(() -> instance.run(configuration)).start();
    }
}
