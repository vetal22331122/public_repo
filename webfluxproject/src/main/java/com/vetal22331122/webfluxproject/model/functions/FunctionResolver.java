package com.vetal22331122.webfluxproject.model.functions;

import com.vetal22331122.webfluxproject.model.entities.Answer;
import reactor.core.publisher.Flux;

import javax.script.ScriptException;

public class FunctionResolver {


    public static Flux<Answer> makeCalculations(String input, int numberOfIterations, int funcNumber) {
        return Flux.range(1, numberOfIterations).map(integer -> {
                try {
                    long start = System.currentTimeMillis();
                    Object result = new JavaScriptResolver().resolveIntArgFunction(input, integer);
                    return new Answer(funcNumber, integer, result, System.currentTimeMillis() - start);
                } catch (ScriptException | NoSuchMethodException | IllegalArgumentException e) {
                    throw new IllegalArgumentException("Couldn't resolve function " + funcNumber + " . Validate your input");
                }
        });
    }
}
