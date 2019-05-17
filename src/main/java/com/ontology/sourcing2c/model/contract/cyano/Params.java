package com.ontology.sourcing2c.model.contract.cyano;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Params {

    @SerializedName("invokeConfig")
    @Expose
    private InvokeConfig invokeConfig;

    public InvokeConfig getInvokeConfig() {
        return invokeConfig;
    }

    public void setInvokeConfig(InvokeConfig invokeConfig) {
        this.invokeConfig = invokeConfig;
    }

}