package com.ontology.sourcing2c.model.contract.cyano;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CyanoRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("params")
    @Expose
    private Params params;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

}






