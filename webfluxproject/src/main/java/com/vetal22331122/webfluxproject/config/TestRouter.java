package com.vetal22331122.webfluxproject.config;

import com.vetal22331122.webfluxproject.handlers.ResolveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TestRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ResolveHandler resolveHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/resolveFlux")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                                , resolveHandler::resolve);
    }
}
