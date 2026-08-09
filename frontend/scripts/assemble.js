import { cpSync, mkdirSync, rmSync } from "node:fs";
import { fileURLToPath } from "node:url";
import path from "node:path";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const output = path.join(root, "dist");

rmSync(output, { recursive: true, force: true });
cpSync(path.join(root, "userside", "dist"), output, { recursive: true });
for (const [app, portal] of [["vendor", "vendor-portal"], ["admin-inspector", "staff-portal"]]) {
  const destination = path.join(output, portal);
  mkdirSync(destination, { recursive: true });
  cpSync(path.join(root, app, "dist"), destination, { recursive: true });
}

console.log(`Unified frontend created at ${output}`);
