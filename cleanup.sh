#!/bin/bash

echo "Starting HRMS deployment cleanup..."

# Remove duplicate Jenkinsfiles (keep only the main one)
echo "Removing duplicate Jenkinsfiles..."
rm -f Jenkinsfile-prod Jenkinsfile-simple Jenkinsfile-robust Jenkinsfile-workspace

# Remove build artifacts
echo "Removing build artifacts..."
rm -rf Backend_hrms/target/
rm -rf frontend/dist/
rm -rf frontend/node_modules/
rm -rf frontend/.angular/

# Remove duplicate docker-compose files (keep main one)
echo "Removing duplicate docker-compose files..."
rm -f docker-compose-prod.yml docker-compose-fixed.yml docker-compose-dev.yml

# Remove log files
echo "Removing log files..."
rm -rf logs/

# Remove duplicate documentation files
echo "Removing duplicate documentation..."
rm -f COMPLETE_DEPLOYMENT_GUIDE.txt TECHNICAL_CHANGES_LOG.txt

# Remove test files and analysis files
echo "Removing test and analysis files..."
rm -f test_hrms.sh HRMS_COMPLETE_ANALYSIS_AND_TEST_GUIDE.md

# Remove deployment script duplicates (keep main deploy.sh)
echo "Removing duplicate deployment scripts..."
rm -f deploy-fixed.sh

# Clean up any temporary files
echo "Cleaning temporary files..."
find . -name "*.tmp" -delete
find . -name "*.log" -delete
find . -name ".DS_Store" -delete

echo "Cleanup completed!"
echo "Remaining files:"
ls -la
