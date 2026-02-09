# 📚 Books Management Microservices System

Система керування каталогом книг, побудована на мікросервісній архітектурі та розгорнута в кластері **Kubernetes (Google Cloud Platform)**.

## 🌍 Live Demo
**Посилання на працюючий сервіс:** [http://35.194.43.217.nip.io/app](http://35.194.43.217.nip.io/app)  
*(Доступна авторизація через будь-який Google-акаунт)*

---

## 🛠 Архітектура та компоненти

### 1. **API Gateway (Spring Boot & Spring Cloud Gateway)**
* **Функціонал:** Єдина точка входу для всього проекту.
* **Auth:** Реалізовано авторизацію через **OAuth 2.0 (Google Login)**.
* **Routing:** Об'єднує фронтенд та бекенд-сервіси в одну мережу, приховуючи складність внутрішньої структури.

### 2. **Books Service (Spring Boot)**
* **Керування каталогом:** Дозволяє переглядати список книг, додавати нових авторів та створювати записи про книги.
* **Data:** Працює з базою даних **PostgreSQL**.
* **Messaging:** Після створення книги генерує подію та відправляє дані в **Apache Kafka**.

### 3. **Email Service (Spring Boot)**
* **Kafka Consumer:** В реальному часі вичитує повідомлення з топіків Kafka.
* **Processing:** Зберігає отримані дані в базу даних для історії.
* **Notification:** Виконує відправку Email-повідомлень (налаштовано через Mailtrap).

### 4. **React Frontend**
* **UI:** Користувацький інтерфейс для управління бібліотекою.
* **Integration:** Взаємодіє з API через Gateway, підтримує стан авторизації користувача.

### 5. **Infrastructure**
* **Apache Kafka & Zookeeper:** Шина повідомлень для асинхронного зв'язку.
* **PostgreSQL:** Реляційне сховище для книг та авторів.
* **Elasticsearch:** Використовується для логування та моніторингу подій.

---

## 🔄 Як це працює (Workflow)



1.  **Вхід:** Користувач логіниться через Google. Gateway перехоплює запит, проводить аутентифікацію та перенаправляє на React-інтерфейс.
2.  **Створення:** Користувач додає нову книгу. Запит іде через Gateway до **Books Service**, де зберігається в базу.
3.  **Подія:** Одразу після збереження **Books Service** публікує повідомлення в Kafka.
4.  **Сповіщення:** **Email Service** миттєво отримує цю подію з Kafka і надсилає лист-підтвердження.

---

## 🔗 Репозиторії проекту
* **Gateway:** [[Встав посилання](https://github.com/Sawaraa/Task-5--Gateway)]
* **Books Service:** [https://github.com/Sawaraa/Task-2--Spring](https://github.com/Sawaraa/Task-2--Spring)
* **Email Service:** [https://github.com/Sawaraa/Task-5--Kafka](https://github.com/Sawaraa/Task-5--Kafka)
* **Frontend:** [https://github.com/Sawaraa/Task-3--React/pulls](https://github.com/Sawaraa/Task-3--React/pulls)

---

## 🚀 Технологічний стек
* **Backend:** Java 21, Spring Boot 3.5, Spring Cloud Gateway, Spring Security OAuth2.
* **Frontend:** React, JavaScript.
* **Infrastructure:** Kubernetes (GKE), Docker, Apache Kafka, PostgreSQL, Elasticsearch.
