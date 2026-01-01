package com.example.pastebin.controller;

import com.example.pastebin.dto.CreatePasteRequest;
import com.example.pastebin.dto.CreatePasteResponse;
import com.example.pastebin.store.PasteStore;
import com.example.pastebin.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PasteController {

    // ==========================
    // 1️⃣ CREATE PASTE
    // ==========================
    @PostMapping("/pastes")
    public ResponseEntity<?> createPaste(
            @RequestBody CreatePasteRequest req,
            HttpServletRequest request) {

        if (req.content == null || req.content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid content"));
        }
        if (req.ttl_seconds != null && req.ttl_seconds < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid ttl_seconds"));
        }
        if (req.max_views != null && req.max_views < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid max_views"));
        }

        String id = UUID.randomUUID().toString().substring(0, 8);
        long now = TimeUtil.now(request);

        Map<String, Object> paste = new HashMap<>();
        paste.put("content", req.content);
        paste.put("views", 0);
        paste.put("maxViews", req.max_views);
        paste.put("expiresAt",
                req.ttl_seconds == null ? null : now + req.ttl_seconds * 1000L
        );

        PasteStore.STORE.put(id, paste);

        return ResponseEntity.ok(
                new CreatePasteResponse(
                        id,
                        "http://localhost:8082/p/" + id
                )
        );
    }

    // ==========================
    // 2️⃣ FETCH PASTE (API)
    // ==========================
    @GetMapping("/pastes/{id}")
    public ResponseEntity<?> getPaste(
            @PathVariable String id,
            HttpServletRequest request) {

        Map<String, Object> paste = PasteStore.STORE.get(id);
        if (paste == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Not found"));
        }

        long now = TimeUtil.now(request);

        int views = (int) paste.get("views");
        Integer maxViews = (Integer) paste.get("maxViews");
        Long expiresAt = (Long) paste.get("expiresAt");

        if (expiresAt != null && now >= expiresAt) {
            PasteStore.STORE.remove(id);
            return ResponseEntity.status(404).body(Map.of("error", "Expired"));
        }

        if (maxViews != null && views >= maxViews) {
            return ResponseEntity.status(404).body(Map.of("error", "View limit exceeded"));
        }

        paste.put("views", views + 1);

        Map<String, Object> response = new HashMap<>();
        response.put("content", paste.get("content"));
        response.put(
                "remaining_views",
                maxViews == null ? null : Math.max(0, maxViews - (views + 1))
        );
        response.put(
                "expires_at",
                expiresAt == null ? null : Instant.ofEpochMilli(expiresAt)
        );

        return ResponseEntity.ok(response);
    }
}
