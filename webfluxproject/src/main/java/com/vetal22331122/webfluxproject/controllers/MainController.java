package com.vetal22331122.webfluxproject.controllers;

import com.vetal22331122.webfluxproject.model.exceptions.InvalidInputException;
import com.vetal22331122.webfluxproject.model.functions.ResultsFluxer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/resolve")
public class MainController {

    @Value("${resultsOrdering}")
    private String orderingType;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity getResolving(@RequestParam(defaultValue = "0") int numberOfIterations,
                                       @RequestParam(defaultValue = "0") String function1,
                                       @RequestParam(defaultValue = "0") String function2) {

        return new ResponseEntity(new ResultsFluxer(numberOfIterations, function1, function2, orderingType)
                .getResult()
                .onErrorResume(e -> Flux.just(e.getMessage())), HttpStatus.OK);

    }

}
