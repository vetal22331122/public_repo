package com.vetal22331122.webfluxproject.model.functions;

import com.vetal22331122.webfluxproject.model.entities.Answer;
import com.vetal22331122.webfluxproject.model.entities.CombinedAnswer;
import com.vetal22331122.webfluxproject.model.exceptions.InvalidInputException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ResultsFluxer {

    private volatile int[] funcReadyResults = new int[2];

    private int numberOfIterations;
    private String function1;
    private String function2;
    private String orderingType;



    public ResultsFluxer(int numberOfIterations, String function1, String function2, String orderingType) {
        this.numberOfIterations = numberOfIterations;
        this.function1 = function1;
        this.function2 = function2;
        this.orderingType = orderingType;
    }

    public Flux<String> getResult() {
        if (orderingType.equals("ordered")) {
            return getOrderedResults(function1, function2)
                    .onErrorResume(e -> {
                        throw new InvalidInputException(e.getMessage());
                    });
        } else if (orderingType.equals("asap")) {
            return getUnorderedResults(function1, function2)
                    .onErrorResume(e -> {
                        throw new InvalidInputException(e.getMessage());
                    });
        } else return Flux.just("Specify results ordering type correctly in application.properties file");
    }

    private Flux<String> getUnorderedResults(String function1, String function2) {
        return Flux.merge(getFuncResultsFluxForUnordered(numberOfIterations, function1, 1),
                getFuncResultsFluxForUnordered(numberOfIterations, function2, 2)).map(Answer::toString);

    }

    private Flux<String> getOrderedResults(String function1, String function2) {
        return getFuncResultsFluxForOrdered(numberOfIterations, function1, 1)
                .zipWith(getFuncResultsFluxForOrdered(numberOfIterations, function2, 2),
                        ((answer, answer2) -> new CombinedAnswer(answer, answer2, funcReadyResults[0], funcReadyResults[1])
                                .toString()));
    }

    private Flux<Answer> getFuncResultsFluxForUnordered(int numberOfIterations, String function, int functionNumber) {
        return FunctionResolver.makeCalculations(function, numberOfIterations, functionNumber)
                .subscribeOn(Schedulers.newElastic(function));
    }

    private Flux<Answer> getFuncResultsFluxForOrdered(int numberOfIterations, String function, int functionNumber) {

            return FunctionResolver.makeCalculations(function, numberOfIterations, functionNumber)
                    .subscribeOn(Schedulers.newElastic(function))
                    .map(answer -> {
                        funcReadyResults[functionNumber - 1] = answer.getIterationNumber();
                        return answer;
                    })
                    .onBackpressureBuffer();

    }
}
