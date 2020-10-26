package com.vetal22331122.webfluxproject.model.functions;

import com.vetal22331122.webfluxproject.model.entities.Answer;
import reactor.core.publisher.Flux;

import javax.script.ScriptException;

public class FunctionResolver {


    public static Flux<Answer> makeCalculations(String input, int numberOfIterations, int funcNumber) throws Exception {
        return Flux.range(1, numberOfIterations).flatMap(integer -> {

            try {
                long start = System.currentTimeMillis();
                Object result = null;
                result = new JavaScriptResolver().resolveIntArgFunction(input, integer);
                return Flux.just(new Answer(funcNumber, integer, result, System.currentTimeMillis() - start));
            } catch (ScriptException | NoSuchMethodException e) {
                try {
                    throw new Exception(e.getMessage());
                } catch (Exception ex) {
                    return Flux.empty();
                }
            }

        });
    }
}
