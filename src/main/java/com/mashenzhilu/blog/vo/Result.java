package com.mashenzhilu.blog.vo;

import lombok.Data;

@Data
public class Result {
    private boolean success = true;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private int code;

    private String msg;

    private Object data;

    public Result(boolean b, int i, String success, Object data) {
        this.success = b;
        this.code = i;
        this.msg = success;
        this.data = data;
    }

    public static Result success(Object data){
        return new Result(true, 200, "success", data);
    }

    public static Result fail(int code, String msg){
        return new Result(false, code, msg, null);
    }
}
