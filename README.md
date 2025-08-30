Perfect 👍 — I’ve fully updated your `README.md` so it mirrors the Employee Management System style, **with proper placeholders for screenshots under each endpoint and key sections**.

Here’s the complete version you can use:

---

# 🛒 E-Commerce Backend - Spring Boot, Java, MySQL, Hibernate, JPA, JWT, Stripe

## 📚 Contents

1. [Brief Summary](#summary)
2. [Aims and Motivation](#aims)
3. [Technologies, Requirements and Software Tools](#tech)
4. [Configuration Instructions](#config)
5. [Design](#design)
6. [Application Screenshots](#demo)
7. [Installation & Setup](#setup)
8. [API Endpoints](#api)

---

<a name="summary"></a>

## 📄 Brief Summary

* Developed a **real-world e-commerce backend** using **Spring Boot 3.4.1**, **Hibernate JPA**, and **MySQL**.
* Implements **JWT authentication** with **role-based access control** (Admin & User).
* Integrated **Stripe API** for secure payment processing.
* Features include:

  * Product catalog
  * Shopping cart (add/remove items)
  * Checkout with Stripe payments
  * Order management
* **Flyway** is used for DB migrations & seeding initial data.
* **Swagger/OpenAPI** provides interactive API documentation.

---

<a name="aims"></a>

## 🎯 Aims and Motivation

* Build a **production-ready backend** with modern Spring Boot practices.
* Implement **JWT-based authentication** and **Stripe checkout**.
* Showcase **enterprise-ready practices**: DTO mapping, layered architecture, DB migrations, environment-based config.
* Provide a **portfolio project** that mirrors real-world e-commerce backends.

---

<a name="tech"></a>

## ⚙️ Technologies, Requirements and Software Tools

### 🧠 Programming & Scripting Languages

* **Java 21** — Backend logic
* **JSON** — API data exchange
* **SQL (MySQL)** — Database queries

### 📚 Frameworks & Libraries

* **Spring Boot 3.4.1** — REST API framework
* **Spring Security 6** — JWT authentication & RBAC
* **Hibernate JPA** — ORM for MySQL
* **Flyway** — Database migrations
* **MapStruct** — DTO mapping
* **Springdoc OpenAPI** — API documentation (Swagger UI)
* **Thymeleaf** — Optional view rendering
* **Lombok** — Boilerplate reduction
* **Stripe SDK** — Payment integration

### 🛠️ Database

* **MySQL 8+** — Persistent storage
* Example Tables: `users`, `products`, `carts`, `orders`, `order_items`

---

<a name="config"></a>

## ⚙️ Configuration Instructions

### 🔧 Backend Setup

1. Clone the repository:

   ```bash
    git clone https://github.com/ParadoxAnubhav20/spring-boot-store-api
   ```

2. Copy `.env.example` → `.env` and set environment variables:

   ```bash
   JWT_SECRET=<your_secure_jwt_secret>
   STRIPE_SECRET_KEY=<your_stripe_secret_key>
   STRIPE_WEBHOOK_SECRET_KEY=<your_stripe_webhook_secret>
   ```

3. Update `src/main/resources/application.yaml` with your MySQL config:

   ```yaml
   spring:
     application:
       name: store
     datasource:
       url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
       username: your-username
       password: your-password
     jpa:
       show-sql: true
       hibernate:
         ddl-auto: validate
     jwt:
       secret: ${JWT_SECRET}
       accessTokenExpiration: 900      # 15 minutes
       refreshTokenExpiration: 604800  # 7 days

   stripe:
     secretKey: ${STRIPE_SECRET_KEY}
     webhookSecretKey: ${STRIPE_WEBHOOK_SECRET_KEY}
   ```

4. Ensure **MySQL is running**. Database `store_api` will be auto-created.

---

<a name="design"></a>

## ✏️ Design – Back-end

* **Spring Boot 3.4.1** used to create a modular, layered backend architecture.
* **Entities** like `User`, `Product`, `Cart`, and `Order` were defined with JPA annotations.
* **Flyway** handles DB migrations and sample seeding.
* **JWT Authentication** ensures secure endpoints with role-based access.
* **Stripe Integration** powers checkout flows.
* **Swagger UI** provides API documentation and interactive testing.


<a name="demo"></a>

## 📸 Application Screenshots

### Swagger UI – API Documentation

<img width="1780" height="1030" alt="Screenshot 2025-08-30 175252" src="https://github.com/user-attachments/assets/ba4ad2bd-515f-4c9e-92a0-cec462d92c4d" />

### Database Schema (via MySQL Workbench)
![store_api_schema_page-0001](https://github.com/user-attachments/assets/90bd9404-a890-4b6e-8b3a-a85104ef4e50)

---

<a name="setup"></a>

## 📥 Installation & Setup

### ✅ Prerequisites

* Java 21
* MySQL 8+
* Maven
* Stripe Account

### ▶️ Running the Project

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

The app will start at:
👉 `http://localhost:8080`

### 🌐 API Docs (Swagger)

👉 `http://localhost:8080/swagger-ui.html`

### 💳 Stripe Webhook

Install Stripe CLI & forward webhooks:

```bash
stripe login
stripe listen --forward-to http://localhost:8080/checkout/webhook
```

---

<a name="api"></a>

## 🔌 API Endpoints

The following RESTful API endpoints are available:

| Method | Endpoint                | Description                     |
| ------ | ----------------------- | ------------------------------- |
| GET    | `/products`             | Fetch all products              |
| POST   | `/carts`                | Create a new cart               |
| POST   | `/carts/{cartId}/items` | Add an item to cart             |
| POST   | `/users`                | Register a new user             |
| POST   | `/auth/login`           | Login user & generate JWT       |
| POST   | `/checkout`             | Checkout with Stripe            |
| POST   | `/checkout/webhook`     | Stripe webhook for order status |

---

### Example Request/Response & Screenshots

#### 1. Get All Products (GET `/products`)

📌 Auto-populated with sample data via Flyway.

<img width="1441" height="971" alt="Screenshot 2025-08-30 180059" src="https://github.com/user-attachments/assets/e904e5ba-7eb6-47bd-86fe-20c0e08c2349" />


---

#### 2. Create Cart (POST `/carts`)

Request:

```json
{ }
```

Response:

```json
{ "cartId": "abc123" }
```

<img width="1447" height="970" alt="Screenshot 2025-08-30 180248" src="https://github.com/user-attachments/assets/ec9fdf41-5ff1-4098-b651-9f3058cf89be" />


---

#### 3. Add Item to Cart (POST `/carts/{cartId}/items`)

Request:

```json
{
  "productId": 1
}
```

Response:

```json
{
  "cartId": "abc123",
  "items": [
    { "productId": 1, "quantity": 1 }
  ]
}
```

<img width="1445" height="967" alt="Screenshot 2025-08-30 180403" src="https://github.com/user-attachments/assets/ce9676d3-e20b-471f-8f25-ac5583083817" />


---

#### 4. Register User (POST `/users`)

Request:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "123456"
}
```

Response:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

<img width="1445" height="967" alt="Screenshot 2025-08-30 175815" src="https://github.com/user-attachments/assets/d6bf4639-25e4-4446-a768-bab0f4024586" />


---

#### 5. Login (POST `/auth/login`)

Request:

```json
{
  "email": "john@example.com",
  "password": "123456"
}
```

Response:

```json
{
  "token": "your-jwt-token"
}
```

<img width="1442" height="970" alt="Screenshot 2025-08-30 175926" src="https://github.com/user-attachments/assets/612abea0-2838-44d0-ba4a-54968adcc44e" />


---

#### 6. Checkout (POST `/checkout`)

Headers:

```
Authorization: Bearer <your-jwt-token>
```

Body:

```json
{
  "cartId": "abc123"
}
```

Response:

```json
{
  "checkoutUrl": "https://checkout.stripe.com/pay/cs_test_..."
}
```

<img width="1440" height="962" alt="Screenshot 2025-08-30 180526" src="https://github.com/user-attachments/assets/069810c7-8c25-4f3d-b514-148bdea10522" />
