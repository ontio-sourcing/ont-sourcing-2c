package com.ontology.sourcing2c.util._crypt;

import lombok.Data;

/**
 * RequestBean，网页加密传过来的数据都是这个样子
 */
@Data
public class RequestBean {

    /**
     * 返回的数据
     */
    private Object data;

    public RequestBean(Object requestBean) {

        this.data = requestBean;
    }

    public RequestBean() {

    }
}
