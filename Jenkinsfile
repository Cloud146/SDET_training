pipeline {
    agent any

    tools {
        allure 'allure'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Docker Tests') {
            steps {
                dir('DockerTests') {
                    sh 'docker compose up --build --abort-on-container-exit --exit-code-from tests'
                }
            }
        }
    }

    post {
        always {
            script {
                allure commandline: 'allure', results: [[path: 'DockerTests/target/allure-results']]
            }

            dir('DockerTests') {
                sh 'docker compose down'
            }
        }
    }
}