pipeline {
    // 1. Указываем, на каком сервере запускать. 'any' — на любом доступном.
    agent any

    // 2. Инструменты (те, что мы настраивали в Manage Jenkins -> Tools)
    tools {
        allure 'allure'
    }

    stages {
        // Стадия 1: Скачивание кода
        stage('Checkout') {
            steps {
                // Магическая команда, которая скачивает код из твоего GitHub репозитория
                checkout scm
            }
        }

        // Стадия 2: Запуск тестов
        stage('Run Docker Tests') {
            steps {
                // Заходим в папку и запускаем наш знакомый docker compose
                dir('DockerTests') {
                    sh 'docker compose up --build --abort-on-container-exit --exit-code-from tests'
                }
            }
        }
    }

    // 3. Блок, который выполняется ПОСЛЕ всех стадий
    post {
        always {
            // Генерация и публикация отчета Allure прямо в интерфейс Jenkins
            script {
                allure includeProperties: false, jdk: '', results: [[path: 'DockerTests/target/allure-results']]
            }

            // Очистка: тушим контейнеры, если они вдруг не потухли сами
            dir('DockerTests') {
                sh 'docker compose down'
            }
        }
    }
}