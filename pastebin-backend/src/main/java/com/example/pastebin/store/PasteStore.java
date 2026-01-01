package com.example.pastebin.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PasteStore {

    // id -> paste data
    public static final Map<String, Map<String, Object>> STORE =
            new ConcurrentHashMap<>();
}
