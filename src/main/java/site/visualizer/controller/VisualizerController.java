package site.visualizer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.visualizer.config.Configuration;
import site.visualizer.run.VisualizerRun;

@RestController
@RequestMapping("api")
public class VisualizerController {

    private final VisualizerRun service = new VisualizerRun();

    @PostMapping("runConfig")
    public ResponseEntity<String> runVisualizer(@RequestBody Configuration configuration) {
        String response = service.accept(configuration);
        if (response.equals("ok")) {
            return ResponseEntity.ok("configuration is successful and running");
        } else {
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
