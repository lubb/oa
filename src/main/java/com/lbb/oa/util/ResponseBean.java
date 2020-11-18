package com.lbb.oa.util;

import java.io.Serializable;

public class ResponseBean implements Serializable {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public ResponseBean(){}

    public ResponseBean(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseBean success(Object data) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(data);
        responseBean.setStatus(GlobalConfig.ResponseCode.SUCCESS.getCode());
        responseBean.setMsg(GlobalConfig.ResponseCode.SUCCESS.getDesc());
        return responseBean;
    }

    public static ResponseBean success(Object object, String message) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(object);
        responseBean.setStatus(GlobalConfig.ResponseCode.SUCCESS.getCode());
        responseBean.setMsg(message);
        return responseBean;
    }

    public static ResponseBean error(String message) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMsg(message);
        responseBean.setStatus(GlobalConfig.ResponseCode.ERROR.getCode());
        return responseBean;
    }

    public static ResponseBean errorLogin(String message) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMsg(message);
        responseBean.setStatus(GlobalConfig.ResponseCode.UNLOGIN.getCode());
        return responseBean;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public static ResponseBean formatData(Integer status, String msg, Object data) {
        return new ResponseBean(status, msg, data);
    }
}
