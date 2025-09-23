package site.visualizer.event;

import site.visualizer.core.Ticket;

public record TicketEvent(EventType eventType, Ticket ticket, int poolSize) {
}
