# Proyecto de Autenticación con DummyJSON

## Descripción

Este proyecto implementa un sistema de autenticación que utiliza la API de DummyJSON para validar usuarios y registra los inicios de sesión exitosos en una base de datos PostgreSQL.

## Funcionalidades

### 1. Autenticación con DummyJSON
El sistema utiliza el endpoint de autenticación de DummyJSON para validar credenciales de usuario:

```bash
curl --request POST \
--url https://dummyjson.com/auth/login \
--header 'Content-Type: application/json' \
--data '{
  "username": "emilys",
  "password": "emilyspass"
}'
```

### 2. Consulta de Usuario Autenticado
Una vez autenticado, se puede obtener información del usuario mediante:
- **Endpoint:** `GET https://dummyjson.com/auth/me`
- **Requiere:** Cookie `accessToken` obtenida en el login
- **Retorna:** Datos del usuario (id, username, email, etc.)

### 3. Registro de Login en Base de Datos
Cada autenticación exitosa se registra en la tabla `login_log` con la siguiente estructura:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | UUID/Autoincremental | Identificador único |
| username | VARCHAR | Nombre de usuario |
| login_time | TIMESTAMP | Fecha y hora del login |
| access_token | TEXT | Token de acceso |
| refresh_token | TEXT | Token de renovación |

### 4. Usuarios de Prueba
Para obtener usuarios de prueba, utiliza:
```
GET https://dummyjson.com/users
```

## Configuración

### Variables de Entorno
El proyecto incluye un archivo `.env` con las siguientes configuraciones:

```env
PORT=8090
DATABASE=jdbc:postgresql://localhost:5432/postgres
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=Admin
```

> **⚠️ Nota:** Normalmente el archivo `.env` no se incluye en el repositorio por seguridad. En este proyecto se incluye únicamente por practicidad para facilitar las pruebas.

### Base de Datos
- **Motor:** PostgreSQL
- **Host:** localhost:5432
- **Base de datos:** postgres
- **Usuario:** postgres
- **Contraseña:** Admin

La base de datos se crea y elimina automáticamente junto con el proyecto.

## Instalación y Ejecución

1. Clona el repositorio
2. Asegúrate de tener PostgreSQL ejecutándose en tu sistema
3. Las tablas necesarias se crean automáticamente al iniciar la aplicación
4. Ejecuta el proyecto (el puerto por defecto es 8090)

## Tecnologías Utilizadas

- PostgreSQL (Base de datos)
- DummyJSON API (Autenticación externa)
- Spring Boot
