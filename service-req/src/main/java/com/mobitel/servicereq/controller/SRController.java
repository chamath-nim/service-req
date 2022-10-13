package com.mobitel.servicereq.controller;

import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.service.SRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SRController {

    private final SRService srService;

    public SRController(SRService srService) {
        this.srService = srService;
    }

    @PostMapping("/stop")
    public ResponseEntity<String> endMainJob(@RequestParam String jobGroup){
        srService.endMainJob(jobGroup);
        return ResponseEntity.ok("Reccuring job ended");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getApiTest(){
        return ResponseEntity.ok("test pass ok");
    }

    @PostMapping("/add/sr")
    public ResponseEntity<String> addRequst(@RequestBody SR sr){
        srService.addRequst(sr);

        return ResponseEntity.ok("Successfully added");
    }
}
