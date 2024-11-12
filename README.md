# E-Commerce Microservices System

A hands-on project to create a scalable e-commerce system using microservices. This project demonstrates the use of Node.js, RabbitMQ, Docker, and MongoDB to build independent microservices for products, orders, payments, and notifications. The services communicate asynchronously through RabbitMQ, showcasing an event-driven architecture to improve scalability and maintainability.

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
  - [Product Service](#product-service)
  - [Order Service](#order-service)
  - [Payment Service](#payment-service)
  - [Notification Service](#notification-service)
- [Technologies Used](#technologies-used)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)


## Project Overview

This project simulates an e-commerce system by breaking down application functionality into microservices. Each microservice is responsible for a specific domain and is built to operate independently, improving the system’s scalability and allowing for isolated deployments.

## Architecture

This project adopts a microservices architecture with four main services:
- **Product Service**: Manages the product catalog.
- **Order Service**: Handles customer orders.
- **Payment Service**: Processes payments for orders.
- **Notification Service**: Sends notifications for events like order confirmations.

The services use **RabbitMQ** as a message broker for asynchronous communication, where events are exchanged between services without direct dependencies.

## Microservices

### Product Service
The **Product Service** is responsible for managing products, including creating, updating, and listing products in the catalog.

**Key Features**:
- Exposes RESTful API for managing products.
- Publishes events to RabbitMQ when products are created or updated.

### Order Service
The **Order Service** handles customer orders, including placing orders, updating order statuses, and processing order details.

**Key Features**:
- Exposes RESTful API for creating and managing orders.
- Subscribes to product events from RabbitMQ and communicates with Payment Service for order processing.

### Payment Service
The **Payment Service** processes payments for orders and updates the order status upon successful payment.

**Key Features**:
- Exposes RESTful API to initiate payments.
- Subscribes to order events to process payments and publishes a payment success or failure event.

### Notification Service
The **Notification Service** is responsible for sending out notifications (e.g., order confirmation emails).

**Key Features**:
- Listens for events from other services (Order and Payment) and sends appropriate notifications.
- Can be extended to support additional notification channels.

## Technologies Used

- **Node.js**: JavaScript runtime for building microservices.
- **Express**: Web framework for creating RESTful APIs.
- **MongoDB**: NoSQL database for storing service data.
- **RabbitMQ**: Message broker for event-driven communication.
- **Docker**: Containerization of services for isolated and reproducible environments.
- **Docker Compose**: Manages multi-container Docker applications.

## Running the Application

Once the setup is complete, the application will be available on:

- **Product Service**: `http://localhost:3001`
- **Order Service**: `http://localhost:3002`
- **Payment Service**: `http://localhost:3003`
- **Notification Service**: `http://localhost:3004`

Each service has its own set of endpoints for CRUD operations and event handling.

## API Endpoints

Here are some key endpoints for each service:

### Product Service
- `POST /products` - Create a new product.
- `GET /products` - Get a list of products.
- `GET /products/:id` - Get product details by ID.

### Order Service
- `POST /orders` - Create a new order.
- `GET /orders/:id` - Get order details by ID.

### Payment Service
- `POST /payments` - Process payment for an order.

### Notification Service
- The Notification Service listens to events and does not expose REST endpoints in this example.

