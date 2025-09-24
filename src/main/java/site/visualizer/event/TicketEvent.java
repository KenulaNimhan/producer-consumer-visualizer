package site.visualizer.event;

import site.visualizer.core.Ticket;

public record TicketEvent(EventType eventType, String eventDesc, int poolSize) {
}
