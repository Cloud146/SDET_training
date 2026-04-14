pipeline {
    agent any

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
                // 1) Готовим папку в workspace (НЕ валим билд если папки нет)
                sh 'rm -rf allure-results || true'
                sh 'mkdir -p allure-results'

                // 2) Забираем results из контейнера (контейнер может быть STOPPED — docker cp работает)
                // Важно: имя контейнера должно совпадать с container_name в docker-compose.yml
                sh 'docker cp tests-runner:/app/target/allure-results/. allure-results || true'

                // 3) Дебаг — покажем, что реально приехало
                sh 'ls -la allure-results || true'

                // 4) Генерим/публикуем Allure отчет
                allure commandline: 'allure', results: [[path: 'allure-results']]
            }

            // 5) Чистим окружение
            dir('DockerTests') {
                sh 'docker compose down || true'
            }
        }
    }
}