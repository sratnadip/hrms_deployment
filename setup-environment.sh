#!/bin/bash

echo "=== HRMS Environment Setup ==="

# Add user to docker group if not already added
if ! groups $USER | grep -q docker; then
    echo "Adding user to docker group..."
    sudo usermod -aG docker $USER
    echo "Please logout and login again for docker group changes to take effect"
fi

# Create necessary directories
mkdir -p mysql-init
mkdir -p logs

# Set permissions
chmod +x deploy.sh
chmod +x build.sh

# Create MySQL initialization script
cat > mysql-init/01-init.sql << 'EOF'
-- Create database if not exists
CREATE DATABASE IF NOT EXISTS hrmsportal;
USE hrmsportal;

-- Grant privileges
GRANT ALL PRIVILEGES ON hrmsportal.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- Basic configuration
SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';
EOF

# Create nginx configuration for frontend
cat > frontend/nginx.conf << 'EOF'
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Handle Angular routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API proxy to backend
    location /api/ {
        proxy_pass http://backend:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header X-Content-Type-Options "nosniff" always;
}
EOF

# Update frontend Dockerfile to use custom nginx config
cat > frontend/Dockerfile << 'EOF'
# Build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist/hrms/browser/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN chmod -R 755 /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
EOF

echo "Environment setup completed!"
echo "Next steps:"
echo "1. Run: ./deploy.sh"
echo "2. Access Jenkins: http://3.133.96.165:8081"
echo "3. Configure Jenkins pipeline with this repository"
