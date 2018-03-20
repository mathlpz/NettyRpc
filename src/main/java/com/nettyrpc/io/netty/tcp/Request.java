package com.nettyrpc.io.netty.tcp;

public class Request {
    
    
    public int id;
    public String name;
    public String reqeustMessag;
    public byte[] attachment;
    
    
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
    public String getReqeustMessag() {
        return reqeustMessag;
    }
    public void setReqeustMessag(String reqeustMessag) {
        this.reqeustMessag = reqeustMessag;
    }
    public byte[] getAttachment() {
        return attachment;
    }
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
    
    
}
