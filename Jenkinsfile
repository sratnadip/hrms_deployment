pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.7'
        nodejs 'NodeJS-18'
    }
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        BACKEND_IMAGE = 'your-dockerhub-username/hrms-backend'
        FRONTEND_IMAGE = 'your-dockerhub-username/hrms-frontend'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-username/hrms-deployment.git'
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('Backend_hrms') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'ng build --configuration production'
                }
            }
        }
        
        stage('Build Docker Images') {
            parallel {
                stage('Backend Docker Image') {
                    steps {
                        dir('Backend_hrms') {
                            script {
                                def backendImage = docker.build("${BACKEND_IMAGE}:${BUILD_NUMBER}")
                                docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                    backendImage.push()
                                    backendImage.push('latest')
                                }
                            }
                        }
                    }
                }
                stage('Frontend Docker Image') {
                    steps {
                        dir('frontend') {
                            script {
                                def frontendImage = docker.build("${FRONTEND_IMAGE}:${BUILD_NUMBER}")
                                docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                                    frontendImage.push()
                                    frontendImage.push('latest')
                                }
                            }
                        }
                    }
                }
            }
        }
        
        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'HRMS deployment successful!'
        }
        failure {
            echo 'HRMS deployment failed!'
        }
    }
}
