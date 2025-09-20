package site.visualizer;

import site.visualizer.config.Configuration;
import site.visualizer.core.TicketPool;
import site.visualizer.threads.Customer;
import site.visualizer.threads.Vendor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        System.out.print("How many tickets need to be produced: ");
        int totalTickets = scan.nextInt();
        System.out.print("No. of vendors: ");
        int vendorCount = scan.nextInt();
        System.out.print("No. of customers: ");
        int customerCount = scan.nextInt();
        System.out.print("Max no. of tickets a customer can have: ");
        int capPerCustomer = scan.nextInt();
        System.out.print("Capacity of the buffer: ");
        int bufferCap = scan.nextInt();

        Configuration data = new Configuration();
        data.setTotalNoOfTickets(totalTickets);
        data.setVendorCount(vendorCount);
        data.setCustomerCount(customerCount);
        data.setCapPerConsumer(capPerCustomer);
        data.setBufferCap(bufferCap);

        TicketPool ticketPool = new TicketPool(data.getBufferCap());

        Vendor[] vendors = new Vendor[vendorCount];

        for (int i=0; i<vendorCount; i++) {
            vendors[i] = new Vendor("vendor "+i, totalTickets/vendorCount, ticketPool);
        }

        Customer[] customers = new Customer[customerCount];

        for (int i=0; i<customerCount; i++) {
            customers[i] = new Customer("customer "+i, capPerCustomer, ticketPool);
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
