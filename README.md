# Proyecto de Autenticación con DummyJSON

## Descripción

Aplicación de autenticación que utiliza la API de DummyJSON para validar usuarios y registra los inicios de sesión en una base de datos PostgreSQL. Implementa una arquitectura de capas con principios SOLID y patrones de diseño para buenas prácticas de desarrollo.

## Arquitectura

- **Arquitectura de Capas:** Separación clara entre controladores, servicios y repositorios
- **JPA/Hibernate:** Para el manejo de persistencia de datos
- **Principios SOLID:** Aplicados en toda la estructura del código
- **Patrones de Diseño:** Implementados para mantener código limpio y mantenible

## Instrucciones de Ejecución

### Prerequisitos
- Java 21 o superior
- PostgreSQL ejecutándose en localhost:5432
- Maven o Gradle (según tu configuración)

### Configuración de Base de Datos
```env
PORT=8090
DATABASE=jdbc:postgresql://localhost:5432/postgres
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=Admin
```

### Pasos para Ejecutar
1. Clona el repositorio
2. Configura PostgreSQL con las credenciales mencionadas
3. Ejecuta la aplicación con tu IDE o mediante línea de comandos
4. La aplicación estará disponible en `http://127.0.0.1:8090`

> **Nota:** La tabla `login_log` y sus registros se crean/eliminan automáticamente al iniciar/detener la aplicación.

## Endpoints Disponibles

### Autenticación
- **POST** `http://127.0.0.1:8090/auth/login` - Iniciar sesión
- **GET** `http://127.0.0.1:8090/auth/me` - Obtener información del usuario autenticado

### Usuarios
- **GET** `http://127.0.0.1:8090/users` - Listar usuarios disponibles

## Usuario y Contraseña de Prueba

Puedes usar cualquiera de estos usuarios de prueba:

| Username | Password |
|----------|----------|
| emilys | emilyspass |
| michaelw | michaelwpass |
| sophiab | sophiabpass |
| jamesd | jamesdpass |
| emilyj | emilyjpass |

## Ejemplo cURL de Login

```bash
curl --request POST \
--url http://127.0.0.1:8090/auth/login \
--header 'Content-Type: application/json' \
--data '{
  "username": "emilys",
  "password": "emilyspass"
}'
```

**Respuesta esperada:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "username": "emilys",
  "email": "emily.johnson@x.dummyjson.com",
  "firstName": "Emily",
  "lastName": "Johnson"
}
```

## Explicación del Registro de Login

### Tabla login_log
Cada vez que un usuario se autentica exitosamente, se crea automáticamente un registro en la tabla `login_log`.

### Proceso de Registro
1. **Usuario se autentica** → Se valida contra DummyJSON API
2. **Login exitoso** → Se obtienen tokens de acceso y refresh
3. **Registro automático** → Se guarda en `login_log` usando JPA:
   - `id`: UUID generado automáticamente
   - `username`: Nombre del usuario autenticado
   - `login_time`: Timestamp del momento del login
   - `access_token`: Token JWT de acceso
   - `refresh_token`: Token JWT de renovación

### Gestión de Datos
- **Al iniciar la aplicación:** Se crea la tabla automáticamente
- **Al detener la aplicación:** Se eliminan la tabla y todos los registros
- **Persistencia:** Manejada completamente por JPA/Hibernate
- **Repository Pattern:** Implementado para el acceso a datos

## Tecnologías Utilizadas

- **Spring Boot** - Framework principal
- **JPA/Hibernate** - ORM para persistencia
- **PostgreSQL** - Base de datos
- **DummyJSON API** - Servicio de autenticación externo
- **Maven/Gradle** - Gestión de dependencias

## Variables de Entorno

> **⚠️ Importante:** El archivo `.env` se incluye en este repositorio únicamente por practicidad para pruebas. En un entorno de producción, estas credenciales deben manejarse de forma segura y no incluirse en el control de versiones.
