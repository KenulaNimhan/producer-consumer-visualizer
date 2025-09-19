package site.visualizer.core;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final String id;
    private final String producedAt;
    private final String producedBy;
    private String boughtBuy;

    Ticket() {
        id = String.valueOf(UUID.randomUUID());
        producedAt = String.valueOf(LocalDateTime.now());
        producedBy = Thread.currentThread().getName();
    }

    // GETTERS

    public String getProducedTime() {
        return producedAt;
    }

    public String getId() {
        return id;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public String getBoughtBuy() {
        return boughtBuy;
    }

    // SETTERS

    public void setBoughtBuy(String boughtBuy) {
        this.boughtBuy = boughtBuy;
    }
}
