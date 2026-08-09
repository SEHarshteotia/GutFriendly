import http from "node:http";

export const API_PREFIXES = [
  "/users",
  "/home",
  "/shops",
  "/foods",
  "/cart",
  "/wishlist",
  "/orders",
  "/reviews",
  "/vendor",
  "/admin",
  "/inspector",
];

export function createBackend(url = process.env.BACKEND_URL ?? "http://localhost:8080") {
  return new URL(url);
}

export function isApiPath(pathname) {
  return API_PREFIXES.some((prefix) => {
    if (pathname === prefix) return true;
    if (!pathname.startsWith(`${prefix}/`)) return false;
    // Keep `/vendor` from matching `/vendor-portal`.
    if (prefix === "/vendor" && pathname.startsWith("/vendor-portal")) {
      return false;
    }
    return true;
  });
}

export function proxyToBackend(req, res, backend) {
  const target = new URL(req.url, backend);
  const upstream = http.request(
    target,
    {
      method: req.method,
      headers: { ...req.headers, host: backend.host },
    },
    (response) => {
      res.writeHead(response.statusCode ?? 502, response.headers);
      response.pipe(res);
    }
  );
  upstream.on("error", (error) => {
    res.writeHead(502, { "content-type": "application/json" });
    res.end(
      JSON.stringify({
        message: "Backend unavailable",
        detail: error.message,
      })
    );
  });
  req.pipe(upstream);
}
