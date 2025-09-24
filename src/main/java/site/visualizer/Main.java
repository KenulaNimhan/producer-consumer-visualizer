package site.visualizer;

import site.visualizer.config.Configuration;
import site.visualizer.config.Configurator;
import site.visualizer.core.TicketPool;
import site.visualizer.core.TicketSystemCoordinator;
import site.visualizer.run.VisualizerRun;

import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        Configurator configurator = new Configurator();
        Configuration data = configurator.configure();

        int totalToConsume = data.getCustomerCount() * data.getCapPerCustomer();
        TicketSystemCoordinator coordinator = new TicketSystemCoordinator(data.getTotalNoOfTickets(), totalToConsume);
        TicketPool ticketPool = new TicketPool(data.getBufferCap());

        VisualizerRun service = new VisualizerRun(ticketPool, coordinator);

        service.run(data);
        coordinator.printResults();

    }
}
