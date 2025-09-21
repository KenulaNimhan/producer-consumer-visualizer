package site.visualizer;

import site.visualizer.config.Configuration;
import site.visualizer.config.Configurator;
import site.visualizer.core.consume.Customer;
import site.visualizer.core.TicketPool;
import site.visualizer.core.produce.Vendor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        Configurator configurator = new Configurator();
        Configuration data = configurator.configure();

        TicketPool ticketPool = new TicketPool(data.getBufferCap());

        Vendor[] vendors = new Vendor[data.getVendorCount()];

        for (int i=0; i<data.getVendorCount(); i++) {
            int vendorID = i+1;
            vendors[i] = new Vendor("vendor "+vendorID, data.getTotalNoOfTickets()/data.getVendorCount(), ticketPool);
        }

        Customer[] customers = new Customer[data.getCustomerCount()];

        for (int i=0; i<data.getCustomerCount(); i++) {
            int customerID = i+1;
            customers[i] = new Customer("customer "+customerID, data.getCapPerCustomer(), ticketPool);
        }

        List<Thread> threads = new ArrayList<>();
        Collections.addAll(threads, vendors);
        Collections.addAll(threads, customers);
        Collections.shuffle(threads);

        for (Thread thread: threads) thread.start();
        for (Thread thread: threads) thread.join();

        ticketPool.printTicketsInPool();

    }
}
