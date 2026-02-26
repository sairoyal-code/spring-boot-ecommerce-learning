## Simple Spring Boot E‑Commerce (Learning Project)

This is a **beginner‑friendly e‑commerce web app** built with:

- **Spring Boot 3** (no Spring Security)
- **Thymeleaf** templates
- **Spring Data JPA** with **H2 in‑memory database**

It is designed to look like a small project you build while learning, and you can put it on GitHub and your resume.

### Features

- **Product catalog** (list + detail page)
- **Search by product name**
- **Simple cart** stored in the HTTP session
- **Fake checkout** page and success message
- **Sample products** loaded automatically on startup

### Project structure (main folders)

- `src/main/java/com/example/ecommerce`
  - `EcommerceApplication` – main Spring Boot class
  - `model` – `Product`, `CartItem`
  - `repository` – `ProductRepository` (extends `JpaRepository`)
  - `service` – `ProductService`
  - `controller` – `ProductController`
  - `DataLoader` – inserts demo products on startup
- `src/main/resources`
  - `application.properties` – H2 + Thymeleaf config
  - `templates` – Thymeleaf pages (`product-list`, `product-detail`, `cart`, `checkout`, `order-success`)
  - `static/css/styles.css` – simple styling
  - `static/images` – put your own product images here

### How to open in IntelliJ IDEA

1. **Open IntelliJ IDEA** (Community is enough).
2. Click **“Open”** and select the folder `e-website` (where this `pom.xml` lives).
3. IntelliJ should detect it as a **Maven** project and import dependencies automatically.

### How to run

You can run it from IntelliJ or from the command line.

- **From IntelliJ:**
  1. Open `EcommerceApplication` class.
  2. Click the green **Run** icon next to the `main` method.

- **From command line (inside project folder):**

   ```bash
   mvn spring-boot:run
   ```

Then open your browser at:

- `http://localhost:8080` – product list
- `http://localhost:8080/h2-console` – H2 database console  
  - JDBC URL: `jdbc:h2:mem:ecommercedb`
  - User: `sa`
  - Password: (empty)

### Where to add your own images

Place your images in:

- `src/main/resources/static/images`

Sample data refers to:

- `phone.jpg`
- `laptop.jpg`
- `headphones.jpg`
- `camera.jpg`

You can:

- Add real `.jpg` files with those names, or
- Change the `imageUrl` values in `DataLoader` to match your own file names.

### Ideas to extend (for learning)

- Add **categories filter** on the product list page.
- Add **simple admin page** to create/edit products.
- Add **basic validation** for checkout form.
- Replace H2 with **MySQL/PostgreSQL**.

No Spring Security is configured, so all pages are public to keep it simple while you are learning.

