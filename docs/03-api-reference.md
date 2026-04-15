# Referencia de la API de Radix

Este documento define la estructura estricta y detallada de cada endpoint disponible en la API de Radix, los parámetros aceptados y las respuestas generadas.

Asegúrate de agregar la URL base requerida por el entorno antes de cada ruta (P. ej. `https://api.raddix.pro/v2`).

---

## 1. Salud y Metadatos

### Consultar Detalles de la API
Retorna la información y versión actual de la API en funcionamiento.

**Endpoint:** `GET /`

**Respuestas:**
- **`200 OK`**: Retorna el estatus y un mapa de las rutas clave del backend.
  ```json
  {
    "name": "Radix API",
    "version": "v2.0",
    "status": "UP",
    "endpoints": {
      "auth": "/api/auth",
      "patients": "/api/pacientes"
    }
  }
  ```

---

## 2. Autenticación (`/api/auth`)

### Iniciar Sesión (Login)
Valida credenciales para un usuario existente y retorna su nivel de acceso.

**Endpoint:** `POST /api/auth/login`

**Body (application/json):**
| Etiqueta | Tipo | Descripción |
| :--- | :--- | :--- |
| `email` | `string` | Correo electrónico de inicio de sesión |
| `password` | `string` | Contraseña en texto plano |

**Respuestas:**
- **`200 OK`**:
  ```json
  {
    "id": 1,
    "firstName": "Gregory",
    "role": "Doctor"
  }
  ```
- **`401 Unauthorized`**: Contraseña inválida o correo inexistente (`{"error": "Invalid credentials"}`).

### Registrar Médico (Register)
Registra exclusivamente usuarios bajo el rol `Doctor`.

**Endpoint:** `POST /api/auth/register`

**Body (application/json):**
| Etiqueta | Tipo | Opcional | Descripción |
| :--- | :--- | :--- | :--- |
| `firstName` | `string` | No | Nombre del médico |
| `lastName` | `string` | No | Apellido del médico |
| `email` | `string` | No | Correo único |
| `password` | `string` | No | Contraseña |

**Respuestas:**
- **`200 OK`**: `{"message": "User created"}`
- **`400 Bad Request`**: `{"error": "Email already exists"}`

---

## 3. Administración de Pacientes (`/api/pacientes`)

### Alta de Paciente
Crea un perfil de `Usuario` con rol `Paciente` y lo enlaza como entrada activa en la tabla de `Pacientes`. Opcionalmente lo vincula a un médico tratante.

**Endpoint:** `POST /api/pacientes/alta`

**Body (application/json):**
| Etiqueta | Tipo | Opcional | Descripción |
| :--- | :--- | :--- | :--- |
| `firstName` | `string` | No | Nombre del paciente |
| `lastName` | `string` | No | Apellido del paciente |
| `email` | `string` | No | Correo del paciente |
| `password` | `string` | No | Contraseña |
| `medicId` | `string/int` | Sí | `id` del Doctor que da de alta al paciente |

**Respuestas:**
- **`200 OK`**: `{"message": "Patient created successfully"}`
- **`400 Bad Request`**: `{"error": "Email already exists"}`

### Listar Todos los Pacientes Activos
Devuelve únicamente a los pacientes cuya bandera de `activo` sea `true`.

**Endpoint:** `GET /api/pacientes`

**Respuestas:**
- **`200 OK`**:
  ```json
  [
    {
      "id": 105,
      "nombreCompleto": "Juan Perez"
    },
    {
      "id": 106,
      "nombreCompleto": "Maria Gomez"
    }
  ]
  ```

### Buscar Paciente por ID
Busca un paciente específico a partir de su llave primaria de la tabla de Pacientes (`idPaciente`).

**Endpoint:** `GET /api/pacientes/{id}`

- **Parámetros de Ruta:** `id` (Entero) - El ID del Paciente.

**Respuestas:**
- **`200 OK`**:
  ```json
  {
    "id": 105,
    "nombreCompleto": "Juan Perez"
  }
  ```
- **`404 Not Found`**: El paciente no existe o se encuentra inactivo (`{"error": "No encontrado"}`).

### Perfil de Paciente (Por ID de Usuario)
Obtiene la información de perfil asociado a una cuenta de aplicación (Útil cuando un paciente se loguea y solo expone su `id` de `Usuario`).

**Endpoint:** `GET /api/pacientes/perfil/{idUsuario}`

- **Parámetros de Ruta:** `idUsuario` (Entero) - El ID que retornó el endpoint `/login`.

**Respuestas:**
- **`200 OK`**:
  ```json
  {
    "idPaciente": 105,
    "nombreCompleto": "Juan Perez"
  }
  ```
- **`404 Not Found`**: No existe una vinculación de paciente para la base de cuenta solicitada (`{"error": "Paciente no encontrado"}`).
