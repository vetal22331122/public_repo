package com.vetal22331122.webfluxproject.model.functions;

import com.vetal22331122.webfluxproject.model.entities.Answer;
import com.vetal22331122.webfluxproject.model.entities.CombinedAnswer;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ResultsFluxer {

    private volatile int[] funcReadyResults = new int[2];

    private int numberOfIterations;

    public ResultsFluxer(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public Flux<String> getUnorderedResults(String function1, String function2) throws Exception {
        return Flux.merge(getFuncResultsFluxForUnordered(numberOfIterations, function1, 1),
                getFuncResultsFluxForUnordered(numberOfIterations, function2, 2)).map(Answer::toString);

    }

    public Flux<String> getOrderedResults(String function1, String function2) throws Exception {
        return getFuncResultsFluxForOrdered(numberOfIterations, function1, 1)
                .zipWith(getFuncResultsFluxForOrdered(numberOfIterations, function2, 2),
                        ((answer, answer2) -> {
                            return new CombinedAnswer(answer, answer2, funcReadyResults[0], funcReadyResults[1])
                                    .toString();
                        }));
    }

    private Flux<Answer> getFuncResultsFluxForUnordered(int numberOfIterations,
                                                        String function,
                                                        int functionNumber) throws Exception {
        return FunctionResolver.makeCalculations(function, numberOfIterations, functionNumber)
                .subscribeOn(Schedulers.newElastic(function));
    }

    private Flux<Answer> getFuncResultsFluxForOrdered(int numberOfIterations,
                                                      String function,
                                                      int functionNumber) throws Exception {
        return FunctionResolver.makeCalculations(function, numberOfIterations, functionNumber)
                .subscribeOn(Schedulers.newElastic(function))
                .map(answer -> {
                    funcReadyResults[functionNumber - 1] = answer.getIterationNumber();
                    return answer;
                })
                .onBackpressureBuffer();
    }
}
