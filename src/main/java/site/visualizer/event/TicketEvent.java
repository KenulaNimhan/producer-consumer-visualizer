package site.visualizer.event;

import site.visualizer.core.produce.Ticket;

public class TicketEvent {
    private final EventType eventType;
    private final Ticket ticket;
    private final int poolSize;

    public TicketEvent(EventType eventType, Ticket ticket, int poolSize) {
        this.eventType = eventType;
        this.ticket = ticket;
        this.poolSize = poolSize;
    }
}
