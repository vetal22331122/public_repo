package com.vetal22331122.webfluxproject.model.entities;

public class CombinedAnswer {
    private int iterationNumber;
    private Object result1;
    private long time1;
    private int readyResults1;
    private Object result2;
    private long time2;
    private int readyResults2;

    public CombinedAnswer(Answer answer1, Answer answer2, int publishedResults1, int publishedResults2) {
        this.iterationNumber = answer1.getIterationNumber();
        this.result1 = answer1.getResult();
        this.time1 = answer1.getTime();
        this.result2 = answer2.getResult();
        this.time2 = answer2.getTime();
        this.readyResults1 = publishedResults1 - answer1.getIterationNumber();
        this.readyResults2 = publishedResults2 - answer2.getIterationNumber();
    }


     @Override
    public String toString() {
        return iterationNumber + ", "
                + result1 + ", "
                + time1 + ", "
                + readyResults1 + ", "
                + result2 + ", "
                + time2 + ", "
                + readyResults2;
    }
}
