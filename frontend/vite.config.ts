import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    // Reduce chunk size for better caching
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom'],
          router: ['react-router-dom']
        }
      }
    },
    // Use default esbuild for faster builds (not terser)
    // Remove debug info in production
    sourcemap: false
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://backend:8080',
        changeOrigin: true
      }
    }
  }
})
