# Build stage with resource limits
FROM node:20-alpine AS build

# Add build optimizations
ENV NODE_OPTIONS="--max-old-space-size=512"
ENV CI=true

WORKDIR /app

# Copy package files first for better layer caching
COPY package*.json ./

# Install dependencies with optimizations
RUN npm ci --no-audit --no-fund --silent

# Copy source code (only what's needed thanks to .dockerignore)
COPY . .

# Build with reduced memory usage
RUN npm run build

# Production stage - minimal runtime image
FROM nginx:alpine

# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

# Copy built files to nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copy nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

# Use non-root user for security and lower memory usage
USER nginx

CMD ["nginx", "-g", "daemon off;"]