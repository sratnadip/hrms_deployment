#!/bin/bash

echo "=== HRMS Build Script ==="

# Build Backend
echo "Building Backend..."
cd Backend_hrms
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Backend build failed!"
    exit 1
fi
cd ..

# Build Frontend
echo "Building Frontend..."
cd frontend
npm install
ng build --configuration production
if [ $? -ne 0 ]; then
    echo "Frontend build failed!"
    exit 1
fi
cd ..

echo "=== Build Completed Successfully ==="
