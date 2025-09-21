package site.visualizer.controller;

import org.springframework.web.bind.annotation.*;
import site.visualizer.config.Configuration;
import site.visualizer.run.VisualizerRun;

@RestController
@RequestMapping("api")
public class VisualizerController {

    private final VisualizerRun service = new VisualizerRun();

    @PostMapping("runConfig")
    public void runVisualizer(@RequestBody Configuration configuration) {

    }

}
