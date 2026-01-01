import { useState } from "react";

function App() {
  const [content, setContent] = useState("");
  const [resultUrl, setResultUrl] = useState("");
  const [error, setError] = useState("");

  // ✅ PASTE YOUR FUNCTION HERE
  const createPaste = async () => {
    setError("");
    setResultUrl("");

    try {
      const res = await fetch("http://localhost:8082/api/pastes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ content }),
      });

      if (!res.ok) throw new Error("Request failed");

      const data = await res.json();
      setResultUrl(data.url);
    } catch {
      setError("Backend not reachable or error occurred");
    }
  };

  return (
    <div>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />

      <button onClick={createPaste}>Create Paste</button>

      {resultUrl && <a href={resultUrl}>{resultUrl}</a>}
      {error && <p>{error}</p>}
    </div>
  );
}

export default App;
