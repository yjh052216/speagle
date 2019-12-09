package com.hwdp.seagle.chat;

public class ChatMessage {

    private String sender;          // DB에 저장할 ID
    private String message;        // 메시지

    public ChatMessage() {
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}