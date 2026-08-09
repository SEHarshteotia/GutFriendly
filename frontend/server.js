import { createReadStream, existsSync, statSync } from "node:fs";
import http from "node:http";
import path from "node:path";
import { fileURLToPath } from "node:url";

import {
  createBackend,
  isApiPath,
  proxyToBackend,
} from "./scripts/backend-proxy.js";

const root = path.join(path.dirname(fileURLToPath(import.meta.url)), "dist");
const port = Number(process.env.PORT ?? 5173);
const backend = createBackend();
const mime = {
  ".css": "text/css; charset=utf-8",
  ".html": "text/html; charset=utf-8",
  ".ico": "image/x-icon",
  ".jpeg": "image/jpeg",
  ".jpg": "image/jpeg",
  ".js": "text/javascript; charset=utf-8",
  ".json": "application/json",
  ".png": "image/png",
  ".svg": "image/svg+xml",
  ".webp": "image/webp",
};

function spaIndex(pathname) {
  if (pathname === "/vendor-portal" || pathname.startsWith("/vendor-portal/")) {
    return path.join(root, "vendor-portal", "index.html");
  }
  if (pathname === "/staff-portal" || pathname.startsWith("/staff-portal/")) {
    return path.join(root, "staff-portal", "index.html");
  }
  return path.join(root, "index.html");
}

http.createServer((req, res) => {
  const pathname = decodeURIComponent(
    new URL(req.url, "http://localhost").pathname
  );
  if (isApiPath(pathname)) return proxyToBackend(req, res, backend);

  const requested = path.resolve(root, `.${pathname}`);
  let file =
    requested.startsWith(path.resolve(root)) &&
    existsSync(requested) &&
    statSync(requested).isFile()
      ? requested
      : spaIndex(pathname);
  if (!existsSync(file)) {
    res.writeHead(503, { "content-type": "text/plain; charset=utf-8" });
    return res.end("Frontend is not built. Run npm run build first.");
  }
  res.writeHead(200, {
    "content-type":
      mime[path.extname(file).toLowerCase()] ?? "application/octet-stream",
  });
  createReadStream(file).pipe(res);
}).listen(port, () => {
  console.log(`GutFriendly frontend: http://localhost:${port}`);
  console.log(`Backend proxy: ${backend.origin}`);
});
