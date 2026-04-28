# EasyIntern - Full Stack Application

A complete full-stack web application for managing internship listings and applications, built with **React** (frontend) and **Spring Boot** (backend).

## 📋 Project Structure

```
easyintern-project/
├── frontend/                      # React Vite frontend application
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
├── backend/                       # Spring Boot API server
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/easyintern/api/
│   │   │   │   ├── controller/    # REST Controllers
│   │   │   │   ├── model/         # Entity classes
│   │   │   │   ├── repository/    # Data repositories
│   │   │   │   ├── service/       # Business logic
│   │   │   │   └── EasyInternApiApplication.java
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── pom.xml
├── package.json                   # Root package.json
├── docker-compose.yml             # Docker configuration
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- **Node.js** (v16+) and npm
- **Java** (JDK 17+)
- **Maven** (3.8+)
- **MySQL** (or use H2 database for development)

### Installation

1. **Clone the repository**
   ```bash
   cd e:/downloads\ chrome/easyintern-project
   ```

2. **Install all dependencies**
   ```bash
   npm run install:all
   ```

3. **Configure the database** (optional)
   - Update `backend/src/main/resources/application.yml` with your MySQL credentials
   - Default uses H2 in-memory database for development

### Running the Application

#### Development Mode - Run both frontend and backend concurrently
```bash
npm run dev
```

This will start:
- **Frontend**: http://localhost:5173 (Vite dev server)
- **Backend**: http://localhost:8080/api (Spring Boot server)

#### Run Frontend Only
```bash
npm run frontend:dev
```

#### Run Backend Only
```bash
npm run backend:dev
```

### Building for Production

```bash
npm run build
```

This creates:
- Production-ready frontend build in `frontend/dist/`
- Packaged backend JAR in `backend/target/`

## 🏗️ API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Available Endpoints

#### Status Endpoints
- `GET /api/v1/status/health` - API health check
- `GET /api/v1/status/info` - API information

#### Internship Endpoints
- `GET /api/v1/internships` - Get all internships
- `GET /api/v1/internships/active` - Get active internships only
- `GET /api/v1/internships/{id}` - Get internship by ID
- `GET /api/v1/internships/search?keyword={keyword}` - Search internships
- `GET /api/v1/internships/company/{company}` - Get internships by company
- `POST /api/v1/internships` - Create new internship
- `PUT /api/v1/internships/{id}` - Update internship
- `DELETE /api/v1/internships/{id}` - Delete internship

## 🐳 Docker Deployment

### Build and Run with Docker

```bash
npm run docker:build
npm run docker:up
```

### Stop Docker containers

```bash
npm run docker:down
```

## 🌐 Render Deployment

This repository includes a `render.yaml` blueprint for deploying the backend and frontend to Render:

- `easyintern-api` as a Java web service
- `easyintern-frontend` as a static site

Deploy from the repository root on Render, then verify these service URLs:

- Frontend: `https://easyintern-frontend.onrender.com`
- Backend: `https://easyintern-api.onrender.com/api/v1`

If you rename either service in Render, update the matching environment variables in `render.yaml`:

- `APP_CORS_ALLOWED_ORIGINS` for the backend
- `VITE_API_BASE_URL` for the frontend

## 📁 Frontend Configuration

The frontend is configured to proxy API requests to the backend during development. Update `frontend/vite.config.js` to modify this behavior.

### Environment Variables (Frontend)

Create `frontend/.env.local`:
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

## 🔄 Backend Configuration

### Application Configuration

Edit `backend/src/main/resources/application.yml` to configure:

- **Database**: MySQL or H2
- **Server Port**: Default is 8080
- **Context Path**: Default is `/api`
- **CORS**: Configured from `APP_CORS_ALLOWED_ORIGINS`

### Database Setup (MySQL)

```sql
CREATE DATABASE easyintern;
USE easyintern;
```

Then update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/easyintern
    username: root
    password: your_password
```

## 🔐 Security

CORS is configured to allow requests from:
- `http://localhost:3000`
- `http://localhost:5173`

Update `backend/src/main/java/com/easyintern/api/EasyInternApiApplication.java` to modify allowed origins.

## 📝 Available NPM Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Run frontend and backend concurrently |
| `npm run frontend:dev` | Run frontend dev server |
| `npm run frontend:build` | Build frontend for production |
| `npm run backend:dev` | Run backend Spring Boot server |
| `npm run backend:build` | Build backend JAR |
| `npm run backend:test` | Run backend tests |
| `npm run build` | Build both frontend and backend |
| `npm run install:all` | Install all dependencies |
| `npm run clean` | Clean all artifacts and dependencies |
| `npm run docker:build` | Build Docker images |
| `npm run docker:up` | Start Docker containers |
| `npm run docker:down` | Stop Docker containers |

## 🧪 Testing

### Backend Tests
```bash
npm run backend:test
```

### Frontend Tests
```bash
cd frontend && npm run test
```

## 📦 Technology Stack

### Frontend
- **React 18** - UI library
- **Vite** - Build tool
- **Axios** - HTTP client
- **ESLint** - Code linter

### Backend
- **Spring Boot 3.2** - Framework
- **Spring Data JPA** - ORM
- **Spring Security** - Authentication
- **MySQL/H2** - Database
- **Lombok** - Code generation
- **Maven** - Build tool

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 💬 Support

For issues or questions, please create an issue in the repository.

---

**Happy coding! 🎉**
