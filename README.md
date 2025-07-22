# Test Task for Liketik

**Created by:** Yashkumar Lukhi
**Email:** [yashlukhi96@gmail.com](mailto:yashlukhi96@gmail.com)

# Shopify Integration Backend (Java + Spring Boot + Axon)

This project implements a **Shopify integration backend** with the following features:

* **Fetch Customers** from Shopify (`/api/customers`)
* **Fetch Orders & Shipping Data** from Shopify (`/api/orders`)
* **Push Products** to Shopify (using **CQRS & Axon Framework**)
* **Real-Time Webhooks** for order creation (`/api/webhooks/orders`)
* **Product Update Support** (update if the product already exists)
* **Dockerized Application** for easy deployment

---

## **1. Project Structure**

```
src/
├── main/
│   ├── java/com/shopify/integration/marketconnect/
│   │    ├── controller/        # REST controllers
│   │    ├── service/           # Services for API calls
│   │    ├── command/           # Axon Commands
│   │    ├── event/             # Axon Events & Event Handlers
│   │    ├── aggregate/         # Axon Aggregate
│   │    ├── config/            # App configurations (RestTemplate, Axon)
│   └── resources/
│        ├── application.properties
│        ├── products.json      # Product data for pushing
```

---

## **2. Setup**

### **2.1 Clone the Repository**

```bash
git clone https://github.com/yLukhi/liketik-task-shopify-java.git
cd liketik-task-shopify-java
```

---

### **2.2 Prerequisites**

* Java 17+
* Gradle (or use the included `gradlew`)
* Docker (optional, for containerized runs)
* A Shopify **dev store** and **Admin API access token**

---

### **2.3 Configure Shopify Credentials**

Edit `src/main/resources/application.properties`:

```properties
shopify.api.url=https://your-dev-store.myshopify.com/admin/api/2023-07/
shopify.api.token=shpat_your_token_here
```

---

## **3. Running the Application**

### **3.1 Local Run**

```bash
./gradlew bootRun
```

The app will start at `http://localhost:8080`.

---

### **3.2 Docker Run**

1. Build the JAR:

   ```bash
   ./gradlew clean bootJar
   ```
2. Build Docker image:

   ```bash
   docker build -t shopify-integration .
   ```
3. Run the container:

   ```bash
   docker run -p 8080:8080 shopify-integration
   ```

---

## **4. Endpoints**

### **4.1 Customers**

**GET** `/api/customers`
Fetches customer data (id, email, name, total\_spent).

### **4.2 Orders & Shipping**

**GET** `/api/orders`
Fetches last 50 orders with shipping info.

### **4.3 Product Push**

**POST** `/api/products/push`
Pushes products from `products.json` to Shopify.

* Updates existing products if they already exist.

### **4.4 Webhooks**

**POST** `/api/webhooks/orders`
Receives real-time order creation events from Shopify.

**GET** `/api/webhooks/received`
Returns last 10 received webhook payloads.

---

## **5. CQRS + Axon Overview**

* **Commands**: `CreateProductCommand`
* **Events**: `ProductCreatedEvent`
* **Aggregate**: `ProductAggregate`
* **Event Handler**: Pushes product data to Shopify API upon `ProductCreatedEvent`.

---

## **6. How to Test Webhooks**

1. Run your app and expose port 8080:

   ```bash
   ngrok http 8080
   ```
2. In Shopify Admin → **Settings → Notifications → Webhooks**,
   set `https://<ngrok_url>/api/webhooks/orders`.
3. Place a test order or send a test notification.

---

## **7. Example cURL Calls**

**Fetch customers:**

```bash
curl http://localhost:8080/api/customers
```

**Push products:**

```bash
curl -X POST http://localhost:8080/api/products/push
```

---

## **8. License**

This project is for educational purposes and adheres to Shopify’s API usage policies.
