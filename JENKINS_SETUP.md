# Jenkins Setup Guide for HRMS Deployment

## Jenkins Access
- **URL**: http://3.133.96.165:8081
- **Initial Admin Password**: Run `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`

## Initial Setup Steps

### 1. First Time Setup
1. Access Jenkins at http://3.133.96.165:8081
2. Enter the initial admin password
3. Install suggested plugins
4. Create admin user

### 2. Install Required Plugins
Go to **Manage Jenkins > Manage Plugins > Available** and install:
- Docker Pipeline
- NodeJS Plugin
- Maven Integration Plugin
- Git Plugin (usually pre-installed)

### 3. Configure Tools
Go to **Manage Jenkins > Global Tool Configuration**:

#### Maven Configuration
- Name: `Maven-3.8.7`
- Install automatically: ✓
- Version: 3.8.7

#### NodeJS Configuration
- Name: `NodeJS-18`
- Install automatically: ✓
- Version: 18.20.8

### 4. Configure Docker Hub Credentials (Optional)
Go to **Manage Jenkins > Manage Credentials > System > Global credentials**:
- Kind: Username with password
- ID: `docker-hub-credentials`
- Username: Your Docker Hub username
- Password: Your Docker Hub password

### 5. Create Pipeline Job

#### Create New Job
1. Click **New Item**
2. Enter name: `HRMS-Deployment`
3. Select **Pipeline**
4. Click **OK**

#### Configure Pipeline
In the job configuration:

**General Section:**
- Description: `HRMS Application Deployment Pipeline`

**Pipeline Section:**
- Definition: `Pipeline script from SCM`
- SCM: `Git`
- Repository URL: Your Git repository URL
- Branch: `*/main`
- Script Path: `Jenkinsfile-prod`

### 6. Pipeline Script (Alternative)
If you prefer to use inline script instead of SCM:

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.7'
        nodejs 'NodeJS-18'
    }
    
    environment {
        EC2_IP = '3.133.96.165'
        PROJECT_DIR = '/home/ubuntu/hrms_deployment'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Using local files...'
                sh 'cd /home/ubuntu/hrms_deployment && pwd'
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('/home/ubuntu/hrms_deployment/Backend_hrms') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                dir('/home/ubuntu/hrms_deployment/frontend') {
                    echo 'Building Angular application...'
                    sh 'npm ci'
                    sh 'ng build --configuration production --ssr=false'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                dir('/home/ubuntu/hrms_deployment') {
                    echo 'Building Docker images...'
                    sh 'docker compose -f docker-compose-fixed.yml build'
                }
            }
        }
        
        stage('Deploy Application') {
            steps {
                dir('/home/ubuntu/hrms_deployment') {
                    echo 'Deploying HRMS application...'
                    sh 'docker compose -f docker-compose-fixed.yml down || true'
                    sh 'docker compose -f docker-compose-fixed.yml up -d'
                }
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Performing health checks...'
                script {
                    sleep(30)
                    sh 'cd /home/ubuntu/hrms_deployment && docker compose -f docker-compose-fixed.yml ps'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
        }
        success {
            echo '✅ HRMS deployment successful!'
            echo "🌐 Frontend: http://${EC2_IP}:4200"
            echo "🔧 Backend API: http://${EC2_IP}:8080"
            echo "🗄️ MySQL: ${EC2_IP}:3307"
        }
        failure {
            echo '❌ HRMS deployment failed!'
            sh 'cd /home/ubuntu/hrms_deployment && docker compose -f docker-compose-fixed.yml logs'
        }
    }
}
```

## Webhook Setup (Optional)
To trigger builds automatically on Git push:

1. Go to your Git repository settings
2. Add webhook: `http://3.133.96.165:8081/github-webhook/`
3. Select "Just the push event"
4. In Jenkins job, enable "GitHub hook trigger for GITScm polling"

## Troubleshooting

### Common Issues
1. **Permission Denied**: Add jenkins user to docker group:
   ```bash
   sudo usermod -aG docker jenkins
   sudo systemctl restart jenkins
   ```

2. **Port Conflicts**: Ensure ports 8080, 4200, 3307, 8081 are available

3. **Build Failures**: Check logs in Jenkins console output

### Useful Commands
```bash
# Check Jenkins status
sudo systemctl status jenkins

# Restart Jenkins
sudo systemctl restart jenkins

# View Jenkins logs
sudo journalctl -u jenkins -f

# Check Docker permissions
groups jenkins
```

## Security Recommendations
1. Change default admin password
2. Enable CSRF protection
3. Configure proper user roles
4. Use HTTPS in production
5. Regular backup of Jenkins configuration
