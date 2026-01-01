**# Pastebin Lite**



**Pastebin Lite is a simple web application that allows users to create**

**text pastes and share a URL to view them. Pastes can optionally expire**

**based on time (TTL) or view count.**



**This project was built as a take-home assignment.**



**---**



**##  Features**



**- Create a text paste**

**- Receive a shareable URL**

**- View paste content via browser**

**- Optional time-based expiry (TTL)**

**- Optional view-count limit**

**- Paste becomes unavailable when constraints are exceeded**

**- Safe HTML rendering (no script execution)**



**---**



**## Technology Stack**



**### Backend**

**- Java 17**

**- Spring Boot**

**- In-memory storage (ConcurrentHashMap)**



**### Frontend**

**- React**

**- Vite**



**---**



**## ▶️ How to Run the Project Locally**



**### 1️⃣ Backend**



**Open Command Prompt:**



**```bash**

**cd pastebin-backend**

**mvn spring-boot:run**



**Backend will start at:**



**http://localhost:8082**



**Health check:**



**GET /api/healthz**



**Expected response:**



**{ "ok": true }**





**2️⃣ Frontend**



**Open a new Command Prompt:**



**cd pastebin-frontend**



**npm install**

**npm run dev**



**Frontend will start at:**



**http://localhost:5173**



 **API Endpoints**



**POST /api/pastes**



**Request body:**



**{**

  **"content": "Hello world",**

  **"ttl\_seconds": 60,**

  **"max\_views": 5**

**}**



**Response:**



**{**

  **"id": "abcd1234",**

  **"url": "http://localhost:8082/p/abcd1234"**

**}**





**Fetch Paste (API)**



**GET /api/pastes/{id}**



**Response:**



**{**

  **"content": "Hello world",**

  **"remaining\_views": 4,**

  **"expires\_at": "2026-01-01T00:00:00.000Z"**

**}**







**View Paste (HTML)**



**GET /p/{id}**



**Returns an HTML page showing the paste content.**







**⏱ Deterministic Time Support**



**If the environment variable TEST\_MODE=1 is set, the backend uses**

**the request header x-test-now-ms as the current time for TTL logic.**





 **Persistence**



**This implementation uses in-memory storage via ConcurrentHashMap.**

**This keeps the application simple and avoids external dependencies.**



**For production or serverless environments, a persistent store such as**

**Redis or a database should be used.**







 **Notes**



**No secrets or credentials are committed**



**No hard-coded localhost URLs are used in backend logic**



**Application starts with standard commands only**

















