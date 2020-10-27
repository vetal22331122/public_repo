package com.vetal22331122.webfluxproject.controllers;

import com.vetal22331122.webfluxproject.model.exceptions.InvalidInputException;
import com.vetal22331122.webfluxproject.model.functions.ResultsFluxer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/resolve")
public class MainController {

    @Value("${resultsOrdering}")
    private String orderingType;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getResolving(@RequestParam(defaultValue = "0") int numberOfIterations,
                                       @RequestParam(defaultValue = "0") String function1,
                                       @RequestParam(defaultValue = "0") String function2) {

            if (orderingType.equals("ordered")) {
                return new ResultsFluxer(numberOfIterations)
                        .getOrderedResults(function1, function2)
                        .onErrorResume(e -> {
                            throw new InvalidInputException(e.getMessage());
                        });
            } else if (orderingType.equals("asap")) {
                return new ResultsFluxer(numberOfIterations)
                        .getUnorderedResults(function1, function2)
                        .onErrorResume(e -> {
                            throw new InvalidInputException(e.getMessage());
                        });
            } else return Flux.just("Specify results ordering type in application.properties file");


    }

}
