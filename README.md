# 🎬 CineBook – Online Movie Ticket Booking System

A full-stack movie ticket booking application built with **Spring Boot (REST API)**, **React.js** and **MySQL**.
Users can browse movies, pick a show, choose seats on a live seat map, book tickets and cancel bookings.
An admin dashboard lets you add movies and shows and track bookings and revenue.

## ✨ Features

**Customer**
- Browse movies, search by title, filter by genre
- View movie details and upcoming shows grouped by theater
- Interactive seat map (available / selected / booked), up to 10 seats per booking
- Book tickets with name, email and phone – total price calculated automatically
- Look up bookings by email and cancel upcoming bookings

**Admin**
- Add / delete movies
- Schedule shows (theater, screen, time, price, seat layout)
- View all bookings with total revenue

**Backend**
- Layered architecture: Controller → Service → Repository (Spring Data JPA)
- Double-booking prevention (seat conflict check inside a transaction)
- Bean Validation on request bodies with clear error messages
- Global exception handling with consistent JSON error responses
- Sample data loaded automatically on first start
- CORS configured for the React dev server

## 🛠 Tech Stack

| Layer     | Technology                                        |
|-----------|---------------------------------------------------|
| Backend   | Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Hibernate, Bean Validation |
| Database  | MySQL 8 (H2 in-memory profile for quick testing)  |
| Frontend  | React 18, React Router 6, Axios, Vite             |
| Tools     | Maven, Git, GitHub Actions                        |

## 📁 Project Structure

```
movie-ticket-booking-system/
├── backend/                         Spring Boot REST API
│   └── src/main/java/com/prachi/movieticket/
│       ├── controller/              REST controllers
│       ├── service/                 Business logic
│       ├── repository/              Spring Data JPA repositories
│       ├── entity/                  JPA entities (Movie, Show, Booking)
│       ├── dto/                     Request / response objects
│       ├── exception/               Custom exceptions + global handler
│       └── config/                  CORS config, sample data loader
└── frontend/                        React application (Vite)
    └── src/
        ├── pages/                   Home, MovieDetails, SeatSelection, MyBookings, Admin
        ├── components/              Navbar, MovieCard
        └── api.js                   Axios API client
```

## 🗄 Database Design

```
movies (id, title, description, genre, language, duration_minutes, certificate, poster_url, release_date)
   1
   │
   * 
shows (id, movie_id FK, theater_name, screen, show_time, ticket_price, total_rows, seats_per_row)
   1
   │
   *
bookings (id, show_id FK, customer_name, customer_email, phone, seat_numbers, number_of_seats,
          total_amount, status [CONFIRMED|CANCELLED], booked_at)
```

## 🔌 REST API

| Method | Endpoint                     | Description                                    |
|--------|------------------------------|------------------------------------------------|
| GET    | `/api/movies`                | List movies (`?search=`, `?genre=`, `?language=`) |
| GET    | `/api/movies/{id}`           | Movie details                                  |
| GET    | `/api/movies/{id}/shows`     | Upcoming shows of a movie                      |
| POST   | `/api/movies`                | Add movie                                      |
| PUT    | `/api/movies/{id}`           | Update movie                                   |
| DELETE | `/api/movies/{id}`           | Delete movie (only if no shows)                |
| GET    | `/api/shows/{id}`            | Show details                                   |
| GET    | `/api/shows/{id}/seats`      | Seat layout + booked seats                     |
| POST   | `/api/shows`                 | Add show                                       |
| DELETE | `/api/shows/{id}`            | Delete show (only if no bookings)              |
| POST   | `/api/bookings`              | Book seats                                     |
| GET    | `/api/bookings?email=`       | Bookings of a customer (all if no email)       |
| GET    | `/api/bookings/{id}`         | Booking details                                |
| PUT    | `/api/bookings/{id}/cancel`  | Cancel booking                                 |

Example booking request:

```json
POST /api/bookings
{
  "showId": 1,
  "customerName": "Prachi Karad",
  "customerEmail": "prachi@example.com",
  "phone": "9876543210",
  "seats": ["C5", "C6"]
}
```

## 🚀 Getting Started

### Prerequisites
- Java 17+, Maven 3.8+
- Node.js 18+
- MySQL 8 (optional – see H2 option below)

### 1. Backend

```bash
cd backend
# update spring.datasource.username / password in src/main/resources/application.properties
mvn spring-boot:run
```

The database `movie_booking_db` is created automatically. API runs on **http://localhost:8080**.

No MySQL? Run with the in-memory H2 database:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**.

## 👨‍💻 Author

**Prachi Karad** – Java Full Stack Developer
