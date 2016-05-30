package com.grizzly.restServices.Controllers.Models.Responses;

/**
 * Created by fpardo on 4/5/16.
 */
public class Responser {
    public int ExpectedOperations = 0;
    public int DoneOperations = 0;
    public int previousAmount = -1;

    public Responser(){}

    public int getExpectedOperations() {
        return ExpectedOperations;
    }

    public void addExpectedOperations(int operations){
        ExpectedOperations = ExpectedOperations+operations;
    }

    public void setExpectedOperations(int expectedOperations) {
        ExpectedOperations = expectedOperations;
    }

    public int getDoneOperations() {
        return DoneOperations;
    }

    public boolean addDoneOperations(int doneOperations) {
        DoneOperations = DoneOperations+doneOperations;
        return Done();
    }

    public boolean Done(){
        /*if(previousAmount != (ExpectedOperations - DoneOperations)) {
            System.out.println("We have " + (ExpectedOperations - DoneOperations) + " tasks to complete");
        }*/
        previousAmount = (ExpectedOperations - DoneOperations);
        if(DoneOperations>=ExpectedOperations) return true;
        return false;
    }
}