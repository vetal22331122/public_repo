package com.vetal22331122.webfluxproject.handlers;

import com.vetal22331122.webfluxproject.model.functions.ResultsFluxer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ResolveHandler {

    @Value("${resultsOrdering}")
    private String orderingType;

    public Mono<ServerResponse> resolve(ServerRequest request) {

        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        new ResultsFluxer(
                            Integer.valueOf(request.queryParam("numberOfIterations").orElse("0")),
                            request.queryParam("function1").orElse(""),
                            request.queryParam("function2").orElse(""),
                            orderingType
                        )
                        .getResult()
                        .onErrorResume(e -> Flux.just(e.getMessage()))
                        , Flux.class);

    }
}
