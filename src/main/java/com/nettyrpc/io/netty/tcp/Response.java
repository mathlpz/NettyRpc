package com.nettyrpc.io.netty.tcp;

public class Response {
    
    public int id;
    public String name;
    public String  ResponseMessage;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getResponseMessage() {
        return ResponseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
    
    
}
