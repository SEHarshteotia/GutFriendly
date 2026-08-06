import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  resolve: {
    alias: {
      "@shared": path.resolve(__dirname, "../shared"),
      react: path.resolve(__dirname, "node_modules/react"),
      "react-dom": path.resolve(__dirname, "node_modules/react-dom"),
    },
  },
  server: {
    port: 5174,
    proxy: {
      "/users": "http://localhost:8080",
      "/home": "http://localhost:8080",
      "/shops": "http://localhost:8080",
      "/foods": "http://localhost:8080",
      "/cart": "http://localhost:8080",
      "/wishlist": "http://localhost:8080",
      "/orders": "http://localhost:8080",
      "/reviews": "http://localhost:8080",
    },
  },
});