package com.mobitel.servicereq.controller;

import com.mobitel.servicereq.model.JobDetails;
import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.service.SRService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SRController {

    private final SRService srService;

    public SRController(SRService srService) {
        this.srService = srService;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteJob(@RequestParam String jobGroup){
        srService.deleteJob(jobGroup);
        return ResponseEntity.ok("Reccuring job deleted");
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<String> deleteAllJobs(){
        srService.deleteAllJobs();
        return ResponseEntity.ok("All reccuring jobs deleted");
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseTrigger(@RequestParam String triggerGroup){
        srService.pauseTrigger(triggerGroup);
        return ResponseEntity.ok("Reccuring job paused");
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resumeTrigger(@RequestParam String triggerGroup){
        srService.resumeTrigger(triggerGroup);
        return ResponseEntity.ok("Reccuring job resumed");
    }

    @PostMapping("/add/sr")
    public ResponseEntity<String> addRequst(@RequestBody SR sr){
        srService.addRequst(sr);

        return ResponseEntity.ok("Successfully added");
    }

    @PostMapping("/get-fired-jobs")
    public ResponseEntity<List<JobDetails>> getFiredJobs(@RequestParam int count){
        return ResponseEntity.ok().body(srService.getFiredJobs(count));
    }

    @GetMapping("/get")
    public ResponseEntity<String> getApiTest(){
        return ResponseEntity.ok("test pass fine");
    }

}
