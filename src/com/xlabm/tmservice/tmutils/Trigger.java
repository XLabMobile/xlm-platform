package com.xlabm.tmservice.tmutils;

public abstract class Trigger {

    protected int status;
    public String qid;


    public Trigger(String qid) {
        status = 0;
        this.qid = qid;
    }


    //Abstract Methods
    public abstract boolean execute();

    public boolean halt() {
        return false;
    }

    public int getStatus() {
        return status;
    }

    public abstract void debugMessage();
    //public abstract boolean execute(ArrayList<String> extParams);

}
