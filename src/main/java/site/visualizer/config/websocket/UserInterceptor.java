package site.visualizer.config.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

/**
 * each stomp connection has an accessor. each accessor includes a user which is a Principal.
 * the username of the Principle has to be set to the unique id to which we are publishing the events to.
 * in this application for the unique id we are using the session id of the StompMessageHeaderAccessor.
 * This interceptor sets configures the session id with the Principal username so events are sent to the correct queue.
 */
public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            Principal principal = () -> sessionId;
            accessor.setUser(principal);
        }

        return message;
    }
}
