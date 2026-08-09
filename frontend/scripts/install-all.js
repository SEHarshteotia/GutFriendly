import { spawnSync } from "node:child_process";
import { fileURLToPath } from "node:url";
import path from "node:path";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const npm = process.platform === "win32" ? "npm.cmd" : "npm";

for (const app of ["userside", "vendor", "admin-inspector"]) {
  const result = spawnSync(npm, ["install"], {
    cwd: path.join(root, app),
    stdio: "inherit",
  });
  if (result.status !== 0) process.exit(result.status ?? 1);
}
