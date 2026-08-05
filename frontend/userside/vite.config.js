import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
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