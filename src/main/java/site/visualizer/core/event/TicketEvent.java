package site.visualizer.core.event;

public record TicketEvent(EventType eventType, String eventDesc, int poolSize) {
}
