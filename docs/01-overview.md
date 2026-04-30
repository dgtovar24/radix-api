# Descripción de la API de Radix

Este documento proporciona una visión general y contextual de la **Radix API**, el componente backend diseñado para administrar pacientes, usuarios y registros médicos integrados bajo un estándar.

La API de Radix está estructurada como un servicio RESTful estándar desarrollado sobre Spring Boot (Java 21), ofreciendo alta disponibilidad, seguridad y tipado a través de respuestas en formato JSON.

## Punto de acceso (Base URL)

Todos los endpoints descritos en la documentación técnica deben prefijarse con la URL base activa provista por tu entorno.

Para el entorno de Producción actual (desplegado en Dokploy), la URL base es:

```text
https://api.raddix.pro/v2
```

> **Note:** El sufijo `/v2` forma parte de la configuración global (`context-path`) de la aplicación. Esto significa que si deseas acceder al endpoint de login (`/api/auth/login`), la ruta completa debe construirse como `https://api.raddix.pro/v2/api/auth/login`.

## Arquitectura de Entidades

La API agrupa principalmente dos entidades de negocio:

- **Usuarios (`Usuario`):** Personas que interactúan con el sistema. Los roles operativos del frontend son `DESARROLLADOR`, `ADMIN` y `FACULTATIVO`.
- **Pacientes (`Pacientes`):** Corresponde a los registros clínicos y datos adicionales atados a un perfil de Usuario. Un paciente típicamente está asociado o gestionado por un Médico en el sistema.

> **Note:** La página separada de facultativos fue eliminada del frontend. Los
> facultativos se gestionan desde la página de usuarios y pueden pertenecer
> opcionalmente a un departamento cuando exista el endpoint backend.

## Convenciones de Formato

Todas las respuestas de la API utilizan el estándar JSON. 

### Peticiones

Cuando los endpoints requieren el envío de información, esperan por defecto un cuerpo de petición en formato JSON. Debes asegurarte de incluir las cabeceras HTTP correctas:

```http
Content-Type: application/json
Accept: application/json
```

### Respuestas y Códigos de Estado

La API hace un uso estándar de los códigos HTTP para describir el resultado de cada solicitud:

- **200 OK:** La petición se ejecutó exitosamente.
- **400 Bad Request:** La petición contiene errores. Por ejemplo, al intentar registrar un email que ya existe.
- **401 Unauthorized:** Las credenciales suministradas son incorrectas o no están presentes.
- **404 Not Found:** El recurso solicitado no existe en la base de datos (p. ej. paciente inactivo).
- **500 Internal Server Error:** Error genérico en el procesamiento del servidor.

### Errores Comunes

Cuando una petición falla (errores 400 o 404), la API devolverá una respuesta JSON estandarizada con un campo `error` describiendo el motivo:

```json
{
  "error": "Email already exists"
}
```

## Próximos pasos

- Si estás construyendo un cliente frontend o móvil y necesitas implementar el flujo de acceso, revisa la **[Guía de Autenticación](./02-authentication.md)**.
- Para conocer la URL exacta y los requisitos de cuerpo (Payload) de cada método, lee la **[Referencia de la API](./03-api-reference.md)**.
