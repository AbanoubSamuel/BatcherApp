package org.abg.batcher.controller;

import lombok.RequiredArgsConstructor;
import org.abg.batcher.utils.JsonResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JobController {

    private final Job job;
    private final JobLauncher jobLauncher;

    @PostMapping("/run")
    public ResponseEntity<JsonResponse> runJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(job, new JobParameters());
        JsonResponse jsonResponse = new JsonResponse<>();
        jsonResponse.setStatus(true);
        jsonResponse.setMessage("Job executed successfully");
        return new ResponseEntity<>(jsonResponse,HttpStatus.OK);
    }
}

