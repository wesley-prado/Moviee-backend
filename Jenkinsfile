def dockerImageName = "wesleypradodev/moviee_app"
def appVersion

pipeline {
    agent any

    tools {
        maven 'maven-3.9.11'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }
        stage('Test') {
            steps {
                echo "Running tests with Maven..."
                sh "./mvnw clean test"
            }
        }
        stage('Build & Package') {
            steps {
                echo "Building the application..."
                sh './mvnw clean package -DskipTests=true'

                script {
                    echo "Reading application version from pom.xml..."
                    def pom = readMavenPom file: 'pom.xml'
                    appVersion = pom.version
                    echo "Application version: ${appVersion}"
                }

                echo "Building Docker image: ${dockerImageName}:${appVersion} and ${dockerImageName}:latest..."
                sh "docker build -t ${dockerImageName}:${appVersion} -t ${dockerImageName}:latest ."
            }
        }
        stage('Publish Docker Images') {
            steps {
                echo "Publishing ${dockerImageName}:${appVersion} and ${dockerImageName}:latest images to Docker Hub..."

                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                    sh "docker push ${dockerImageName}:${appVersion}"
                    sh "docker push ${dockerImageName}:latest"
                }
            }
        }
        stage('Cleanup') {
            steps {
                echo "Cleaning up local Docker images..."
                sh "docker rmi ${dockerImageName}:${appVersion}"
                sh "docker rmi ${dockerImageName}:latest"
            }
        }
    }

    post {
        always {
            echo "Build process completed. Logging out from Docker Hub..."
            sh 'docker logout'
        }
    }
}
