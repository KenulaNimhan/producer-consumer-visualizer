package site.visualizer.core.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public EventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * sends events related to ticket production and consumption.
     * @param event TicketEvent.
     */
    public void sendEvent(TicketEvent event) {
        messagingTemplate.convertAndSend("/topic/event", event);
    }

    public void sendEventToUser(String username, TicketEvent event) {
        messagingTemplate.convertAndSendToUser(username, "/queue/event", event);
    }

    public void sendErrorToUser(String username, String errorMsg) {
        messagingTemplate.convertAndSendToUser(username, "/queue/error", errorMsg);
    }

    /**
     * sends messages related to configuration validation errors.
     * @param errorMsg - config invalid error message.
     */
    public void sendError(String errorMsg) {
        messagingTemplate.convertAndSend("/topic/error", errorMsg);
    }
}
