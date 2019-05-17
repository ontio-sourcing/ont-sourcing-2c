package com.ontology.sourcing2c.model.contract.cyano;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvokeConfig {

    @SerializedName("contractHash")
    @Expose
    private String contractHash;
    @SerializedName("functions")
    @Expose
    private List<Function> functions = null;
    @SerializedName("payer")
    @Expose
    private String payer;
    @SerializedName("gasLimit")
    @Expose
    private Integer gasLimit;
    @SerializedName("gasPrice")
    @Expose
    private Integer gasPrice;

    public String getContractHash() {
        return contractHash;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Integer getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Integer gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Integer getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Integer gasPrice) {
        this.gasPrice = gasPrice;
    }

}
