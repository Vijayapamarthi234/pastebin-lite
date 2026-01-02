import { useState } from "react";

function App() {
  const [content, setContent] = useState("");
  const [resultUrl, setResultUrl] = useState("");
  const [error, setError] = useState("");

  const createPaste = async () => {
    setError("");
    setResultUrl("");

    try {
      const res = await fetch(
        "https://pastebin-lite-bzmb.onrender.com/api/pastes",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ content }),
        }
      );

      if (!res.ok) throw new Error("Request failed");

      const data = await res.json();
      setResultUrl(data.url);
    } catch {
      setError("Backend not reachable or error occurred");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "auto" }}>
      <h2>Pastebin Lite</h2>

      <textarea
        placeholder="Write your paste here..."
        value={content}
        onChange={(e) => setContent(e.target.value)}
        rows={8}
        style={{ width: "100%" }}
      />

      <br /><br />

      <button onClick={createPaste}>Create Paste</button>

      <br /><br />

      {resultUrl && (
        <div>
          <strong>Paste URL:</strong>{" "}
          <a href={resultUrl} target="_blank" rel="noreferrer">
            {resultUrl}
          </a>
        </div>
      )}

      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}

export default App;
