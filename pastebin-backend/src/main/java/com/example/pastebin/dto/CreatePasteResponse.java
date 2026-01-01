package com.example.pastebin.dto;

public class CreatePasteResponse {
    public String id;
    public String url;

    public CreatePasteResponse(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
