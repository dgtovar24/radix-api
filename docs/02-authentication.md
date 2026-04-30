# Guía de Autenticación en la API

Esta guía práctica te muestra paso a paso cómo registrar nuevos usuarios clínicos y cómo autenticar a cualquier usuario para obtener sus credenciales de acceso desde la API.

> **Note:** Todos los ejemplos asumen que estás ejecutando las llamadas contra la URL base configurada en tu entorno (por ejemplo, `https://api.raddix.pro/v2`). Si realizas peticiones locales, reemplaza el dominio por `http://localhost:8080`.

## Registrar un nuevo facultativo

El registro actual de backend sigue usando el endpoint de doctor, pero el
frontend normaliza el rol clínico como **FACULTATIVO**. La creación de pacientes
debe ser gestionada por un facultativo a través del endpoint de alta
correspondiente.

Para crear un facultativo en el sistema, configura una petición POST:

1. Modifica la URL a `/api/auth/register/doctor`.
2. Especifica en los encabezados que el contenido es JSON (`Content-Type: application/json`).
3. Envía el nombre, apellido, correo y contraseña del doctor.

**Ejemplo de petición:**

```bash
curl -X POST https://api.raddix.pro/v2/api/auth/register/doctor \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Gregory",
    "lastName": "House",
    "email": "house@hospital.com",
    "password": "mypassword123"
  }'
```

**Respuesta Exitosa:**
```json
{
  "message": "User created"
}
```

> **Warning:** Si intentas usar un correo electrónico que ya existe en el sistema, recibirás una respuesta `400 Bad Request` indicando `{"error": "Email already exists"}`.

## Iniciar sesión con un usuario existente

Una vez que un usuario está registrado en el sistema, puedes enviarle sus credenciales al endpoint de `login` para validar la sesión y determinar su cuenta y nivel de acceso.

1. Construye una petición POST hacia el destino `/api/auth/login`.
2. Proporciona las llaves `email` y `password` en el cuerpo de la petición.

**Ejemplo de petición:**

```bash
curl -X POST https://api.raddix.pro/v2/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
  "email": "house@hospital.com",
  "password": "mypassword123"
}'
```

**Entendiendo la Respuesta:**
Si los datos son validados correctamente, el backend te responderá con un código `200 OK` y el perfil mínimo del usuario.

```json
{
  "id": 14,
  "firstName": "Gregory",
  "role": "FACULTATIVO"
}
```

- **`id`:** Corresponde al identificador primario de la tabla de usuarios. Debe utilizarse luego para vincular o buscar perfiles asociados (p. ej. perfil de reloj de paciente).
- **`role`:** Garantiza al frontend saber qué permisos debe renderizar. Los roles operativos son `DESARROLLADOR`, `ADMIN` y `FACULTATIVO`.

Si envías una contraseña incorrecta o un correo que no existe en el registro, el sistema devolverá un `401 Unauthorized`.

```json
{
  "error": "Invalid credentials"
}
```

## Próximos pasos

- Conoce todos los parámetros técnicos de estos endpoints dentro de la **[Referencia de la API](./03-api-reference.md)**.
- Revisa las definiciones sobre la estructura de la aplicación en la lectura de **[Descripción General](./01-overview.md)**.
