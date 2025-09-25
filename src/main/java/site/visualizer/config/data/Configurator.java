package site.visualizer.config.data;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * used for console level usage
 */
public class Configurator {
    private final Scanner scan = new Scanner(System.in);
    private final Configuration configuration = new Configuration();

    private final Map<Config, Consumer<Integer>> configSetters = Map.of(
            Config.TOTAL_TICKETS, configuration::setTotalNoOfTickets,
            Config.BUFFER_CAP, configuration::setBufferCap,
            Config.VENDOR_COUNT, configuration::setVendorCount,
            Config.CUSTOMER_COUNT, configuration::setCustomerCount,
            Config.CAP_PER_CUSTOMER, configuration::setCapPerCustomer,
            Config.RELEASE_RATE, configuration::setReleaseRate,
            Config.RETRIEVAL_RATE, configuration::setRetrievalRate
    );

    /**
     * produces a Configuration object with valid data by asking for each config.
     * @return Configuration
     */
    public Configuration configure() {
        for (Config config: Config.values()) {
            var userInput = askFor(config);
            configSetters.get(config).accept(userInput);
        }
        return this.configuration;
    }

    /**
     * continuously asks for the passed config until a valid input is entered by user.
     * @param config Configuration field
     * @return valid user input
     */
    private int askFor(Config config) {
        while (true) {
            try {
                System.out.println(config.getPrompt());
                System.out.print("( "+config.getRange()[0]+" - "+config.getRange()[1]+" ): ");
                var input = scan.nextInt();
                if (config.rangeAccepts(input)) return input;
                System.out.println("\u001B[31mout of range \u001B[0m");
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31minvalid input \u001B[0m");
                scan.nextLine();
            }
        }
    }
}
