package site.visualizer.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class TicketEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public TicketEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendEvent(TicketEvent event) {
        messagingTemplate.convertAndSend("topic/tickets", event);
    }
}
