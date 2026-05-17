# SDET Training Project — End-to-End Test Automation Stand

[![GitHub Actions](https://img.shields.io/badge/CI-GitHub_Actions-blue?logo=github)]()
[![GitLab CI](https://img.shields.io/badge/CI-GitLab_CI-orange?logo=gitlab)]()
[![Jenkins](https://img.shields.io/badge/CI-Jenkins-red?logo=jenkins)]()
[![TeamCity](https://img.shields.io/badge/CI-TeamCity-black?logo=teamcity)]()
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)]()
[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?logo=springboot)]()
[![Selenium](https://img.shields.io/badge/Selenium-Grid-43B02A?logo=selenium)]()
[![REST Assured](https://img.shields.io/badge/API-RestAssured-25A162)]()
[![TestNG](https://img.shields.io/badge/Tests-TestNG-FF6F00)]()
[![Allure](https://img.shields.io/badge/Report-Allure-FF7043)]()

> Pet-проект по различным CiCd инструментам:  
> поднимается тестовый стенд (Spring Boot + H2),  
> на нём — UI/API автотесты (Selenium + RestAssured + TestNG),  
> вся инфраструктура запускается в Docker Compose,  
> прогон + отчётность интегрированы с **GitHub Actions / GitLab CI / Jenkins**,  
> отчёты — **Allure** (включая публикацию на GitHub Pages / GitLab Pages).

---

## Цель проекта

Показать **полный цикл работы SDET**: от написания UI/API тестов, до контейнеризации стенда и тестов, до интеграции в несколько CI/CD платформ и публикации отчётности.

Это проект-практикум, в котором сознательно повторены реальные ситуации из работы:
- разные окружения (локально / Docker / CI),
- разные системы CI/CD,
- разные способы хранения и публикации результатов тестов.

---

## Архитектура проетка

```text
SDET_training/
├── TestStand/          # Тестируемое приложение (SUT): Spring Boot + H2 + REST + HTML форма
│   ├── src/main/java/...
│   ├── src/main/resources/static/index.html
│   └── Dockerfile
│
├── DockerTests/        # Автотесты (Java + TestNG + Selenium + RestAssured + Allure)
│   ├── src/test/java/...
│   ├── src/test/resources/test.properties
│   ├── src/test/resources/sql/clean_db.sql
│   ├── pom.xml
│   ├── testng.xml
│   ├── Dockerfile
│   └── docker-compose.yml   # Поднимает app + selenium + tests
│
├── .github/workflows/main.yml      # GitHub Actions pipeline
├── .gitlab-ci.yml                  # GitLab CI pipeline
├── Jenkinsfile                     # Jenkins pipeline
└── README.md
```

---

## Тестовый стенд
Представляет собой E2E экосистему: Spring Boot Приложение (SUT) + H2 Database.

### UI часть стенда
Приложение запускает форму регистрации:

Поля "Логин" и "Пароль" для ввода и кнопка "Отправить" для отправки запроса регистрации.
Если поля не заполнять будет ошибка:

После заполнения полей валидными полями появляется сообщение об успехе:

А в базе данных появится соответствующая запись:

Если ввести данные существующего пользователя - появится сообщение об ошибке, данные добавлены не будут:

### API часть стенда

POST запрос по URL http://localhost:8080/api/register также зарегестрирует нового пользователя и занесёт его в базу данных.
Тело запроса:

```json
{
    "username": "Olechka3",
    "password": "cute"
}
```

Тело ответа:

```json
{
    "id": 4,
    "username": "Olechka3",
    "password": "cute"
}
```

### Контуры взаимодействия
- **Локально (IDE)**: запускаю приложение `TestStandApplication`, тесты из IDEA.
- **Локально в Docker**: один `docker compose up` поднимает app + Selenium + tests.
- **CI/CD**: тот же `docker compose` запускается на GitHub/GitLab/Jenkins.

---

## 🛠 Технологический стек

### Тестовый стенд (SUT)
- **Java 17/21**
- **Spring Boot 3** (Web, Data JPA)
- **H2 Database** (file mode, `AUTO_SERVER=TRUE`)
- HTML страница с формой регистрации (`/`) + REST API (`/api/register`, `/api/users`)

### Автотесты (DockerTests)
- **Java 21**
- **TestNG** (suite через `testng.xml`)
- **Selenium WebDriver** (через `RemoteWebDriver` → Selenium Grid в Docker)
- **REST Assured** (API тесты)
- **Hamcrest** (матчеры)
- **Allure** (репортинг + `allure-rest-assured` для авто-логирования API)
- **Logback / SLF4J**
- **Lombok**
- Утилиты:
  - `ConfigProvider` — единый источник конфигов (properties + ENV)
  - `DbUtils` — выполнение SQL из файла (например `clean_db.sql`)

### Инфраструктура
- **Docker** (App, Selenium Standalone Chrome, Tests)
- **Docker Compose** (оркестрация трёх сервисов)
- **Selenium standalone-chrome** контейнер (вместо локального Chrome+driver)
- **GitHub Actions / GitLab CI / Jenkins** — CI/CD
- **Allure CLI** (генерация HTML отчёта)
- **GitHub Pages / GitLab Pages** — публикация отчёта как сайта

---

## 🚀 Запуск проекта

### 1. Локально (IDE)

1) Запусти приложение:
   - Открой `TestStand` в IntelliJ IDEA
   - Запусти `TestStandApplication`
   - Приложение доступно на `http://localhost:8080`

2) Запусти тесты:
   - Открой `DockerTests`
   - Запусти `testng.xml` или `mvn test`

3) Отчёт:
   ```bash
   mvn allure:serve
   ```

### 2. Локально через Docker Compose

В папке `DockerTests`:
```bash
docker compose up --build --abort-on-container-exit --exit-code-from tests
```

Что произойдёт:
- поднимется Spring Boot стенд (контейнер `test-app`)
- поднимется Selenium Chrome (контейнер `selenium-chrome`)
- стартанёт тестовый контейнер (`tests-runner`)
- по завершении тестов всё аккуратно завершится
- результаты Allure окажутся в `DockerTests/target/allure-results`

Открыть HTML отчёт:
```bash
mvn allure:serve
```

### 3. CI/CD

#### GitHub Actions

Workflow: `.github/workflows/main.yml`

Что делает:
- Checkout
- `docker compose up ...`
- Сохраняет `allure-results` как artifact
- Генерирует HTML Allure отчёт
- Публикует отчёт через **GitHub Pages** с поддержкой **trends/history**

> Pages нужно один раз включить:  
> `Settings → Pages → Source: GitHub Actions`

#### GitLab CI

Файл: `.gitlab-ci.yml`

Стадии:
- `test` — `docker compose up` через Docker-in-Docker
- `deploy` (`pages`) — генерация и публикация Allure отчёта в **GitLab Pages**

#### Jenkins

Файл: `Jenkinsfile`

Особенности:
- Jenkins поднимается в отдельном Docker-окружении (отдельная папка с `Dockerfile` + `docker-compose.yml`)
- Jenkins использует **Docker-outside-of-Docker** (`/var/run/docker.sock`)
- Allure CLI установлен **внутрь Jenkins образа** для стабильности
- Allure results копируются из контейнера тестов через `docker cp` и публикуются через Allure Jenkins Plugin

---

## 🧪 Что покрывается тестами

### UI (Selenium + Page Object)
- Открытие страницы регистрации
- Заполнение формы (логин + пароль)
- Проверка появления сообщения об успешной регистрации
- Скриншоты как attachments в Allure

### API (RestAssured)
- `POST /api/register` — создание пользователя
- Валидация:
  - `statusCode 201`
  - `Content-Type: application/json`
  - поля ответа (`id`, `username`)
- Логи запроса/ответа автоматически прикладываются в Allure через `AllureRestAssured`

### База данных
- Перед тестами выполняется `clean_db.sql` через `DbUtils`
- Подключение к H2 (file mode, `AUTO_SERVER=TRUE`)
- Возможность подключиться к этой же БД из DBeaver параллельно с приложением

---

## 📂 Конфигурация и среда (Config-as-Code)

`DockerTests/src/test/resources/test.properties`:

```properties
host.url=http://localhost:8080
api.url=http://localhost:8080/api
db.url=jdbc:h2:file:./data/testdb;AUTO_SERVER=TRUE
db.user=sa
db.password=password
selenium.url=http://localhost:4444/wd/hub
```

В Docker/CI значения **перекрываются** через переменные окружения  
(`HOST_URL`, `API_URL`, `DB_URL`, `SELENIUM_URL`, …) — это делает `ConfigProvider`.

В Docker-сети адреса другие:
- `http://test-app:8080`
- `http://selenium-chrome:4444/wd/hub`

Так один и тот же код работает и локально, и в Docker, и в CI.

---

## 📊 Отчёты Allure

- Локально:
  ```bash
  mvn allure:serve
  ```
- В Docker: 
  ```text
  DockerTests/target/allure-results
  ```
- В GitHub Actions: публикуется на GitHub Pages
- В GitLab CI: публикуется на GitLab Pages
- В Jenkins: вкладка Allure Report у билда
- В TeamCity: артефакт `allure-report/index.html`

Скриншоты UI и логи API запросов автоматически попадают в отчёт.
