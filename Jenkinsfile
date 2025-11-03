def dockerImageName = "wesleypradodev/moviee_app"

pipeline {
    agent any

    tools {
        maven 'maven-3.9.11'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
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
                echo 'Building Java project with Maven...'
                sh './mvnw clean package -DskipTests=true'

                echo 'Building Docker image: ${dockerImageName}'

                sh "docker build -t ${dockerImageName} ."
            }
        }
        stage('Publish Docker Image') {
            steps {
                echo 'Publishing Docker image to Docker Hub...'

                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"

                    sh "docker tag ${dockerImageName} ${dockerImageName}:${BUILD_NUMBER}"
                    sh "docker push ${dockerImageName}:${BUILD_NUMBER}"

                    sh "docker tag ${dockerImageName}:${BUILD_NUMBER} ${dockerImageName}:latest"
                    sh "docker push ${dockerImageName}:latest"
                }
            }
        }
        stage('Cleanup') {
            steps {
                echo 'Cleaning up local Docker images...'
                sh "docker rmi ${dockerImageName}:${BUILD_NUMBER}"
                sh "docker rmi ${dockerImageName}:latest"
            }
        }
    }

    post {
        always {
            echo 'Build process completed. Logging out from Docker Hub...'
            sh 'docker logout'
        }
    }
}
