#!/bin/bash

set -e

echo "=== HRMS Deployment Script ==="
echo "EC2 IP: 3.133.96.165"
echo "Starting deployment process..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running as root for Docker commands
if ! docker ps >/dev/null 2>&1; then
    print_error "Cannot connect to Docker daemon. Make sure Docker is running and user has permissions."
    exit 1
fi

# Stop existing containers
print_status "Stopping existing containers..."
docker compose down || true

# Clean up old images (optional)
print_status "Cleaning up old images..."
docker system prune -f

# Build backend
print_status "Building Spring Boot backend..."
cd Backend_hrms
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    print_error "Backend build failed!"
    exit 1
fi
cd ..

# Build frontend
print_status "Building Angular frontend..."
cd frontend
npm install
ng build --configuration production
if [ $? -ne 0 ]; then
    print_error "Frontend build failed!"
    exit 1
fi
cd ..

# Build Docker images
print_status "Building Docker images..."
docker compose build

# Start services
print_status "Starting HRMS services..."
docker compose up -d

# Wait for services to be ready
print_status "Waiting for services to start..."
sleep 30

# Check service status
print_status "Checking service status..."
docker compose ps

# Display access information
echo ""
echo "=== DEPLOYMENT COMPLETED ==="
echo "Frontend: http://3.133.96.165:4200"
echo "Backend API: http://3.133.96.165:8080"
echo "MySQL: 3.133.96.165:3306"
echo "Jenkins: http://3.133.96.165:8081"
echo ""
echo "To check logs: docker compose logs -f"
echo "To stop services: docker compose down"
echo "=========================="
