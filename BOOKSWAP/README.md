# BOOKSWAP

Read. Exchange. Reuse.

BOOKSWAP is a Java standard-library book marketplace for students. It uses the built-in `HttpServer`, plain Java OOP, and text-file persistence in `data/`.

## Run the backend

Install and approve a JDK 21 installation, then from the `BOOKSWAP` directory run:

```powershell
New-Item -ItemType Directory -Force .\backend\out
javac -d .\backend\out (Get-ChildItem .\backend\src -Recurse -Filter *.java).FullName
java -cp .\backend\out Main
```

The API starts at `http://localhost:8080` and creates or updates:

```text
data/users.txt
data/books.txt
data/exchanges.txt
```

## Run the frontend

From the `DigitalBookExchange` directory, run:

```powershell
python -m http.server 5500 --directory .\BOOKSWAP
```

Open `http://localhost:5500/frontend/index.html`.

The frontend first tries the Java API. If the backend is not running, it uses the browser's local storage fallback so the UI remains explorable.

## API routes

```text
POST /api/auth/register
POST /api/auth/login
GET  /api/books
GET  /api/books/recommended
POST /api/books
GET  /api/exchanges
POST /api/exchanges
```
