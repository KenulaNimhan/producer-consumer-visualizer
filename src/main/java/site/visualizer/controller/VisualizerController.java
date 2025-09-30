package site.visualizer.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import site.visualizer.config.data.Configuration;
import site.visualizer.service.VisualizerService;

@Controller
public class VisualizerController {

    private final VisualizerService service;

    public VisualizerController(VisualizerService service) {
        this.service = service;
    }

    /**
     * starts the visualizer if the config is correct or else an error message is sent.
     * @param configuration of client.
     */
    @MessageMapping("/start")
    public void establishConnectionAndStart(Configuration configuration, SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getSessionId();

        if (service.accept(configuration)) {
            service.instantiateAndStart(configuration, username);
        }
    }
}
