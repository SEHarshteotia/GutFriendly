import { cpSync, mkdirSync, rmSync } from "node:fs";
import { spawnSync } from "node:child_process";
import { fileURLToPath } from "node:url";
import path from "node:path";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const output = path.join(root, "dist");
const npm = process.platform === "win32" ? "npm.cmd" : "npm";
const apps = [
  ["userside", output],
  ["vendor", path.join(output, "vendor-portal")],
  ["admin-inspector", path.join(output, "staff-portal")],
];

rmSync(output, { recursive: true, force: true });
for (const [app, destination] of apps) {
  const appRoot = path.join(root, app);
  const result = spawnSync(npm, ["run", "build"], {
    cwd: appRoot,
    stdio: "inherit",
    env: { ...process.env, VITE_API_BASE_URL: process.env.VITE_API_BASE_URL ?? "" },
  });
  if (result.status !== 0) process.exit(result.status ?? 1);
  mkdirSync(destination, { recursive: true });
  cpSync(path.join(appRoot, "dist"), destination, { recursive: true });
}

console.log(`Unified frontend created at ${output}`);
