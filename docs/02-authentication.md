# Authentication Architecture

## Roles and Logic
The Radix API has three main roles:
1. **ADMIN**: The system administrator.
2. **DOCTOR**: A medical professional who can manage patients.
3. **PATIENT**: A user receiving treatment.

### Initial Seed
On startup, the system automatically checks if the Administrator exists. If not, it creates it:
- **Email / Username**: `Radix`
- **Password**: `prgtest`
- **Role**: `ADMIN`

### Workflows
- **Login**: Anyone can login at `/api/auth/login` using their email (or `Radix`) and password. A pseudo-token is generated (`Bearer <ID>`).
- **Doctor Registration**: Only the **ADMIN** can create doctors at `/api/auth/register/doctor`. The ADMIN must send their generated token in the `Authorization` header.
- **Patient Registration**: Only a **DOCTOR** can create patients at `/api/auth/register/patient`. It automatically creates both the user entity for login capabilities, and the patient record pointing to the doctor who created it.

### Required Fields
For registration endpoints, the required fields are:
- `firstName`
- `lastName`
- `email`
- `password`
