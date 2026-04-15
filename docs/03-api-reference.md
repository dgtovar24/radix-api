# API Reference

This document defines the structure and endpoints available in the Radix API.

## 1. Health & Metadata

### Get API Details
Returns the API status and operational information.
**Endpoint:** `GET /`

## 2. Authentication (`/api/auth`)

### Login
Validates credentials for an existing user. Allows login using email.
**Endpoint:** `POST /api/auth/login`

**Body:**
| Field | Type | Description |
| :--- | :--- | :--- |
| `email` | `string` | User's email or username |
| `password` | `string` | Plain text password |

### Register Doctor (ADMIN Only)
Only the Admin user can create doctors.
**Endpoint:** `POST /api/auth/register/doctor`
**Headers:** `Authorization: Bearer <admin_token>`

**Body:**
| Field | Type | Optional | Description |
| :--- | :--- | :--- | :--- |
| `firstName` | `string` | No | Doctor's first name |
| `lastName` | `string` | No | Doctor's last name |
| `email` | `string` | No | Unique email |
| `password` | `string` | No | Password |

**Responses:**
- **`200 OK`**: `{"message": "Doctor user created successfully"}`
- **`403 Forbidden`**: `{"error": "Only Admin can create Doctors"}`

### Register Patient (DOCTOR Only)
Only a Doctor can register patients.
**Endpoint:** `POST /api/auth/register/patient`
**Headers:** `Authorization: Bearer <doctor_token>`

**Body:**
| Field | Type | Optional | Description |
| :--- | :--- | :--- | :--- |
| `firstName` | `string` | No | Patient's first name |
| `lastName` | `string` | No | Patient's last name |
| `email` | `string` | No | Patient's email |
| `password` | `string` | No | Password |
| `phone` | `string` | Yes | Contact phone |
| `address` | `string` | Yes | Address |

**Responses:**
- **`200 OK`**: `{"message": "Patient registered successfully"}`
- **`403 Forbidden`**: `{"error": "Only a Doctor can register Patients"}`
