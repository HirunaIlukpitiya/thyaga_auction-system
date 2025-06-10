# Thyaga Auction System

A real-time auction system built with Spring Boot that allows users to create auction items, place bids, and automatically closes auctions when their end time is reached.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## Database Setup

1. Install MySQL if not already installed
2. Log in to MySQL as root:
   ```
   mysql -u root -p
   ```
3. Create the database:
   ```sql
   CREATE DATABASE thyaga_auction;
   ```
4. Create a user and grant privileges:
   ```sql
   CREATE USER 'auction_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON thyaga_auction.* TO 'auction_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Database Schema

The application automatically creates the following tables via Hibernate:

- **user**: Stores user information
  ```sql
  CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
  );
  ```

- **item**: Stores auction item information
  ```sql
  CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    starting_price DECIMAL(19,2) NOT NULL,
    current_bid DECIMAL(19,2),
    auction_status VARCHAR(20),
    auction_end_time DATETIME NOT NULL,
    seller_id BIGINT NOT NULL,
    version BIGINT,
    FOREIGN KEY (seller_id) REFERENCES user(id)
  );
  ```

- **bid**: Stores bid information
  ```sql
  CREATE TABLE bid (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    bid_time DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    winning_bid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
  );
  ```

## Application Setup

1. Clone the repository:
   ```
   git clone https://github.com/HirunaIlukpitiya/thyaga_auction-system.git
   cd thyaga_auction-system
   ```

2. Configure database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/thyaga_auction
   spring.datasource.username=auction_user
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

## Building and Running

1. Build the project:
   ```
   mvn clean install
   ```

2. Run the application:
   ```
   mvn spring-boot:run
   ```

3. Access the application at `http://localhost:8080`
4. Access API documentation at `http://localhost:8080/swagger-ui/index.html`

## Key Components

### Controllers
- **UserAPI**: Handles user registration and authentication
- **ItemAPI**: Manages auction items (creation, retrieval)
- **BidAPI**: Manages bidding operations

### Services
- **UserService**: User management logic
- **ItemService**: Item and auction management
- **BidService**: Bid processing logic
- **AuctionSchedulerService**: Background service for closing expired auctions

### Repositories
- **UserRepository**: Data access for users
- **ItemRepository**: Data access for auction items
- **BidRepository**: Data access for bids

## Design Decisions

### Auction Closing Mechanism
- Implemented as a scheduled background task using Spring's `@Scheduled` annotation
- Runs periodically to check for expired auctions
- Updates auction status to CLOSED when end time is reached
- Determines winning bid (highest amount) and marks it accordingly

### Concurrency Considerations
- Transactions used for critical operations to maintain data consistency
- Optimistic locking for bid operations using `@Version` annotation
- Spring's scheduling with fixed delay to prevent overlapping tasks

## API Documentation

### Authentication API

#### Register a new user
```
POST /auth/register
```
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "securepassword"
}
```
**Response:**
- `201 CREATED` - Registration successful with JWT token in response

#### Login
```
POST /auth/login
```
**Request Body:**
```json
{
  "username": "johndoe",
  "password": "securepassword"
}
```
**Response:**
- `200 OK` - Login successful with JWT token in response

### Item API

#### Create a new auction item
```
POST /user/{userId}/items
```
**Path Variables:**
- `userId` - ID of the user creating the item

**Request Body:**
```json
{
  "name": "Vintage Watch",
  "description": "Rare collectible watch from 1950s",
  "startingPrice": 100.00,
  "auctionEndTime": "2023-12-31T23:59:59"
}
```
**Response:**
- `201 CREATED` - Item created successfully with item details

#### Get all active auction items
```
GET /user/{userId}/items
```
**Path Variables:**
- `userId` - ID of the user making the request

**Response:**
- `200 OK` - List of active auction items

#### Get a specific auction item
```
GET /user/{userId}/items/{itemId}
```
**Path Variables:**
- `userId` - ID of the user making the request
- `itemId` - ID of the requested item

**Response:**
- `200 OK` - Detailed information about the requested item
- For closed auctions, only includes winning bid information

### Bid API

#### Place a bid on an item
```
POST /user/{userId}/items/{itemId}/bids
```
**Path Variables:**
- `userId` - ID of the user placing the bid
- `itemId` - ID of the item being bid on

**Request Body:**
```json
{
  "amount": 150.00
}
```
**Response:**
- `201 CREATED` - Bid placed successfully with bid details
- `400 BAD REQUEST` - If bid amount is below minimum or auction is closed

For complete API documentation with all possible response codes and schemas, access the Swagger UI at `http://localhost:8080/swagger-ui/index.html` when the application is running.
