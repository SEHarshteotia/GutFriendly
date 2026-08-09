import http from "node:http";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { createServer as createViteServer } from "vite";

import {
  createBackend,
  isApiPath,
  proxyToBackend,
} from "./backend-proxy.js";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const port = Number(process.env.PORT ?? 5173);
const backend = createBackend();

function redirectTrailingSlash(req, res, pathname) {
  if (pathname === "/vendor-portal" || pathname === "/staff-portal") {
    const url = new URL(req.url, "http://localhost");
    url.pathname = `${pathname}/`;
    res.writeHead(302, { Location: `${url.pathname}${url.search}` });
    res.end();
    return true;
  }
  return false;
}

function runMiddleware(middlewares, req, res) {
  return new Promise((resolve, reject) => {
    middlewares(req, res, (error) => {
      if (error) reject(error);
      else resolve();
    });
  });
}

async function createAppVite({ configFile, appRoot, base, hmrPort }) {
  return createViteServer({
    configFile,
    root: appRoot,
    ...(base ? { base } : {}),
    server: {
      middlewareMode: true,
      hmr: { port: hmrPort },
      // Isolated apps already declare their own proxies; unified server
      // handles API routing before Vite middlewares run.
      proxy: {},
    },
    appType: "spa",
  });
}

async function start() {
  const userside = await createAppVite({
    configFile: path.join(root, "userside", "vite.config.js"),
    appRoot: path.join(root, "userside"),
    hmrPort: 24678,
  });

  const vendor = await createAppVite({
    configFile: path.join(root, "vendor", "vite.config.js"),
    appRoot: path.join(root, "vendor"),
    base: "/vendor-portal/",
    hmrPort: 24679,
  });

  const staff = await createAppVite({
    configFile: path.join(root, "admin-inspector", "vite.config.js"),
    appRoot: path.join(root, "admin-inspector"),
    base: "/staff-portal/",
    hmrPort: 24680,
  });

  const httpServer = http.createServer(async (req, res) => {
    try {
      const pathname = decodeURIComponent(
        new URL(req.url ?? "/", "http://localhost").pathname
      );

      if (isApiPath(pathname)) {
        return proxyToBackend(req, res, backend);
      }

      if (redirectTrailingSlash(req, res, pathname)) return;

      if (
        pathname === "/vendor-portal" ||
        pathname.startsWith("/vendor-portal/")
      ) {
        return await runMiddleware(vendor.middlewares, req, res);
      }

      if (
        pathname === "/staff-portal" ||
        pathname.startsWith("/staff-portal/")
      ) {
        return await runMiddleware(staff.middlewares, req, res);
      }

      return await runMiddleware(userside.middlewares, req, res);
    } catch (error) {
      console.error(error);
      if (!res.headersSent) {
        res.writeHead(500, { "content-type": "text/plain; charset=utf-8" });
      }
      res.end(error instanceof Error ? error.message : "Dev server error");
    }
  });

  httpServer.listen(port, () => {
    console.log(`GutFriendly unified dev: http://localhost:${port}`);
    console.log(`  Customer / landing: http://localhost:${port}/`);
    console.log(
      `  Vendor portal:       http://localhost:${port}/vendor-portal/login`
    );
    console.log(
      `  Staff portal:        http://localhost:${port}/staff-portal/login`
    );
    console.log(`Backend proxy: ${backend.origin}`);
  });
}

start().catch((error) => {
  console.error(error);
  process.exit(1);
});
