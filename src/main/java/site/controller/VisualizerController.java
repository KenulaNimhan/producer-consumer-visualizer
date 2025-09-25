package site.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import site.visualizer.config.Configuration;
import site.visualizer.event.EventPublisher;
import site.visualizer.run.VisualizerRun;

@Controller
public class VisualizerController {

    private final VisualizerRun service;
    private final EventPublisher publisher;

    public VisualizerController(VisualizerRun service, EventPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    /**
     * starts the visualizer if the config is correct or else an error message is sent.
     * @param configuration of client.
     */
    @MessageMapping("/start")
    public void establishConnectionAndStart(Configuration configuration) {
        String response = service.accept(configuration);
        System.out.println(response);
        if (response.equals("ok")) {
            service.run(configuration);
        } else {
            publisher.sendError(response);
        }
    }

    /**
     * stops the visualizer by interrupting all threads.
     */
    @MessageMapping("/stop")
    public void stop() {
        service.stop();
    }
}
