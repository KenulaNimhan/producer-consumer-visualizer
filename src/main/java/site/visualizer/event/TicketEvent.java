package site.visualizer.event;

public record TicketEvent(EventType eventType, String eventDesc, int poolSize) {
}
