package com.example.pastebin.controller;

import com.example.pastebin.store.PasteStore;
import com.example.pastebin.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasteViewController {

    @GetMapping("/p/{id}")
    public ResponseEntity<String> viewPaste(
            @PathVariable String id,
            HttpServletRequest request) {

        var paste = PasteStore.STORE.get(id);
        if (paste == null) {
            return ResponseEntity.status(404).body("Not found");
        }

        long now = TimeUtil.now(request);

        int views = (int) paste.get("views");
        Integer maxViews = (Integer) paste.get("maxViews");
        Long expiresAt = (Long) paste.get("expiresAt");

        if (expiresAt != null && now >= expiresAt) {
            PasteStore.STORE.remove(id);
            return ResponseEntity.status(404).body("Expired");
        }

        if (maxViews != null && views >= maxViews) {
            return ResponseEntity.status(404).body("View limit exceeded");
        }

        paste.put("views", views + 1);

        String html = """
            <html>
              <body>
                <pre>%s</pre>
              </body>
            </html>
            """.formatted(
                org.springframework.web.util.HtmlUtils.htmlEscape(
                        (String) paste.get("content"))
            );

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);
    }
}
