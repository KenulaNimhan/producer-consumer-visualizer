package site.visualizer.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public EventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendEvent(TicketEvent event) {
        messagingTemplate.convertAndSend("/topic/event", event);
    }

    public void sendError(String errorMsg) {
        messagingTemplate.convertAndSend("/topic/error", errorMsg);
    }
}
