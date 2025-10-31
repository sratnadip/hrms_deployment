pipeline {
    agent { label 'linux' }

    tools {
        maven 'Maven-3.8.7'
    }

    environment {
        PROJECT_DIR = '/home/ubuntu/hrms_deployment'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'echo "GitHub Account: sratnadip"'
                sh 'echo "Repository: hrms_deployment"'
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
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }

        stage('Health Check') {
            steps {
                sleep(30)
                sh 'docker-compose ps'
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
