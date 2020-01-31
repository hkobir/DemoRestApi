package com.hk.demoapiuser.ModelClass;

import com.google.gson.annotations.SerializedName;
import com.hk.demoapiuser.ModelClass.User;

public class LoginResponse {
    @SerializedName("error")
    private Boolean err;

    @SerializedName("message")
    private String  msg;

    private User user;

    public LoginResponse() {
    }

    public LoginResponse(Boolean err, String msg, User user) {
        this.err = err;
        this.msg = msg;
        this.user = user;
    }

    public Boolean getErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }

    public User getUser() {
        return user;
    }
}
