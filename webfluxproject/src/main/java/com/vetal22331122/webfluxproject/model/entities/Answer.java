package com.vetal22331122.webfluxproject.model.entities;

public class Answer {

    private int funcNumber;
    private int iterationNumber;
    private Object result;
    private long time;

    public Answer(int funcNumber, int iterationNumber, Object result, long time) {
        this.funcNumber = funcNumber;
        this.iterationNumber = iterationNumber;
        this.result = result;
        this.time = time;
    }

    public int getFuncNumber() {
        return funcNumber;
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public Object getResult() {
        return result;
    }

    public long getTime() {
        return time;
    }


    @Override
    public String toString() {
        return iterationNumber + ", "
                + funcNumber + ", "
                + result + ", "
                + time;
    }
}
