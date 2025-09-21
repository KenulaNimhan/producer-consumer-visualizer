package site.visualizer.config;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Configurator {
    private final Scanner scan = new Scanner(System.in);
    private final Configuration configuration = new Configuration();

    private final Map<Config, Consumer<Integer>> actionToSetConfig = Map.of(
            Config.TOTAL_TICKETS, configuration::setTotalNoOfTickets,
            Config.BUFFER_CAP, configuration::setBufferCap,
            Config.VENDOR_COUNT, configuration::setVendorCount,
            Config.CUSTOMER_COUNT, configuration::setCustomerCount,
            Config.CAP_PER_CUSTOMER, configuration::setCapPerCustomer,
            Config.RELEASE_RATE, configuration::setReleaseRate,
            Config.RETRIEVAL_RATE, configuration::setRetrievalRate
    );

    public Configuration configure() {
        for (Config config: Config.values()) {
            var userInput = askFor(config.getPrompt(), config.getRange());
            actionToSetConfig.get(config).accept(userInput);
        }
        return this.configuration;
    }

    private int askFor(String prompt, int[] range) {
        while (true) {
            try {
                System.out.println(prompt);
                System.out.print("( "+range[0]+" - "+range[1]+" ): ");
                var input = scan.nextInt();
                if (input > range[0] && input < range[1]) return input;
                System.out.println("\u001B[31mout of range \u001B[0m");
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31minvalid input \u001B[0m");
                scan.nextLine();
            }
        }
    }
}
