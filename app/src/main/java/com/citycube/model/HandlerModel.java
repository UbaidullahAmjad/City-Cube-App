package com.citycube.model;

public class HandlerModel {
    String handlerId,handlerName,handlerType;
    boolean chk;
    int handlerImage;

    public HandlerModel(String handlerId, String handlerName, String handlerType, int handlerImage,boolean chk) {
        this.handlerId = handlerId;
        this.handlerName = handlerName;
        this.handlerType = handlerType;
        this.handlerImage = handlerImage;
        this.chk = chk;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public int getHandlerImage() {
        return handlerImage;
    }

    public void setHandlerImage(int handlerImage) {
        this.handlerImage = handlerImage;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }
}
