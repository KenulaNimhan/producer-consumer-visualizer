package site.visualizer.core;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketSystemCoordinator {
    private final AtomicInteger totalProduced, totalConsumed;
    private final int totalToProduce, totalToConsume;

    public TicketSystemCoordinator(int totalToProduce, int totalToConsume) {
        totalProduced = new AtomicInteger();
        totalConsumed = new AtomicInteger();
        this.totalToProduce = totalToProduce;
        this.totalToConsume = totalToConsume;
    }

    public void incrementProducedCount() {totalProduced.getAndIncrement();}

    public void incrementConsumedCount() {totalConsumed.getAndIncrement();}

    public boolean isProductionDone() {return totalProduced.get() == totalToProduce;}

    public boolean isConsumptionDone() {return totalConsumed.get() == totalToConsume;}

    public void printResults() {
        System.out.printf("""
                Produced = %s out of %s
                Consumed = %s out of %s
                """,totalProduced, totalToProduce, totalConsumed, totalToConsume);
    }
}
