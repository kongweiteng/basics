package com.enn.web.vo;

import org.hibernate.validator.constraints.NotBlank;

public class WebReqId {
    @NotBlank(message = "id 不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
