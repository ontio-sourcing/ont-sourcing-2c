package com.ontology.sourcing2c.model.contract.cyano;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Function {

    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("args")
    @Expose
    private List<Arg> args = null;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public void setArgs(List<Arg> args) {
        this.args = args;
    }

}
