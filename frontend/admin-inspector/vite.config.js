import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'path'
import { fileURLToPath } from 'url'

const __dirname = path.dirname(fileURLToPath(import.meta.url))

/** Let SPA routes refresh in dev; only proxy API calls to Spring Boot. */
function springBootProxy() {
  return {
    target: 'http://localhost:8080',
    changeOrigin: true,
    bypass(req) {
      const accept = req.headers.accept ?? ''
      if (accept.includes('text/html')) {
        return '/index.html'
      }
    },
  }
}

// https://vite.dev/config/
export default defineConfig({
  base: '/staff-portal/',
  
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@shared': path.resolve(__dirname, '../shared'),
      react: path.resolve(__dirname, 'node_modules/react'),
      'react-dom': path.resolve(__dirname, 'node_modules/react-dom'),
    },
  },
  server: {
    port: 5175,
    proxy: {
      '/admin': springBootProxy(),
      '/inspector': springBootProxy(),
    },
  },
})
