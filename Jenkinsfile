def dockerImageName = "wesleypradodev/moviee_app"
def appVersion
def shouldRunHeavyStages

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
        stage('Check changes') {
            steps {
                script {
                    echo 'Checking for relevant changes in the push...'
                    def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true).trim()

                    echo "Changed files in this push:\n${changedFiles}"

                    shouldRunHeavyStages = false

                    changedFiles.split('\n').each { file ->
                        if (file.startsWith('src/') || file == 'pom.xml' || file.startsWith('Dockerfile') || file == "Jenkinsfile") {
                            echo "Detected changes in relevant files: ${file}"
                            shouldRunHeavyStages = true
                        }
                    }

                    if (!shouldRunHeavyStages) {
                        echo "No relevant changes detected. Skipping stages..."
                    }
                }
            }
        }
        stage('Test') {
            when {
                expression { return shouldRunHeavyStages }
            }
            steps {
                echo "Running tests with Maven..."
                sh "./mvnw clean test"
            }
        }
        stage('Build & Package') {
            when {
                expression { return shouldRunHeavyStages }
            }
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
            when {
                expression { return shouldRunHeavyStages }
            }
            steps {
                echo "Publishing ${dockerImageName}:${appVersion} and ${dockerImageName}:latest images to Docker Hub..."

                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASS"
                    sh "docker push ${dockerImageName}:${appVersion}"
                    sh "docker push ${dockerImageName}:latest"
                }
            }
        }
        stage('Deploy to production') {
            when {
                expression { return shouldRunHeavyStages }
            }
            steps {
                echo "Deploying application to production environment..."

                sshagent(['vps-app-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no root@134.199.242.115 << 'EOF'
                        cd /srv/moviee &&
                        
                        # 1. Update the Docker image
                        sed -i "s|image: ${dockerImageName}:.*|image: ${dockerImageName}:${appVersion}|g" docker-compose.yml &&

                        # 2. Pull the new image
                        docker compose pull moviee_app &&
                        
                        # 3. Restart the service
                        docker compose up -d --force-recreate moviee_app
EOF
                    """
                }
            }
        }
        stage('Cleanup') {
            when {
                expression { return shouldRunHeavyStages }
            }
            steps {
                echo "Cleaning up local Docker images..."
                sh "docker rmi ${dockerImageName}:${appVersion} || true"
                sh "docker rmi ${dockerImageName}:latest || true"
            }
        }
    }

    post {
        always {
            echo "Build process completed."

            script {
                if (shouldRunHeavyStages) {
                    echo "Logging out from Docker Hub..."
                    sh 'docker logout'
                }
            }
        }
    }
}
