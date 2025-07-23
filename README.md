# 🏆 Prode - Sistema de Pronósticos Deportivos (Backend)

API REST para sistema de pronósticos de partidos de fútbol con autenticación JWT, administración de usuarios y sistema de puntuación automática.

## 🔗 Repositorios del Proyecto

- **Backend (este repo)**: [prode2.0](https://github.com/JuanFarre/prode2.0)
- **Frontend Angular**: [FrontProde](https://github.com/JuanFarre/FrontProde)

## 🚀 Características

- ✅ **API REST** completa con Spring Boot
- ✅ **Autenticación JWT** con roles (USER/ADMIN)
- ⚽ **Gestión de equipos, partidos y fechas**
- 🎯 **Sistema de pronósticos** con puntuación automática
- 🏆 **Ranking de usuarios** (general y por fecha)
- 🎫 **Sistema de tickets** para agrupar pronósticos
- 📧 **Registro por email** con Spring Mail
- 🔒 **Seguridad robusta** con validaciones

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **Spring Mail**
- **MySQL 8** con Hibernate
- **Maven** para gestión de dependencias

## 📋 Requisitos Previos

- **Java 17** o superior
- **MySQL 8.0** o superior
- **Maven 3.6** o superior
- Cuenta de **Gmail** con verificación en 2 pasos (para notificaciones)

## ⚙️ Configuración Local

### 1. Clonar el Repositorio
```bash
git clone https://github.com/JuanFarre/prode2.0.git
cd prode2.0
```

### 2. Configurar Base de Datos
```sql
-- Crear base de datos en MySQL
CREATE DATABASE pronosticos_db;
```

### 3. Configurar Variables de Entorno
```bash
# Copiar archivo de configuración
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 4. Editar application.properties

Configurar con tus valores reales:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pronosticos_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_password_mysql

# Email Configuration (Gmail App Password)
spring.mail.username=tu_email@gmail.com
spring.mail.password=tu_app_password_gmail

# JWT Secret (mínimo 64 caracteres)
jwt.secret=tu_clave_jwt_super_segura_y_larga_minimo_64_caracteres
```

### 5. Obtener Gmail App Password

1. Ve a [Cuenta de Google](https://myaccount.google.com/)
2. **Seguridad** → **Verificación en 2 pasos** (habilitarla si no está)
3. **Contraseñas de aplicación** → **Generar nueva**
4. Selecciona "Otra aplicación" → Escribe "Prode API"
5. Usa la contraseña generada en `spring.mail.password`

## 🚀 Ejecutar la Aplicación

```bash
# Instalar dependencias y ejecutar
mvn clean install
mvn spring-boot:run

# La API estará disponible en: http://localhost:8080
```

## 🌐 Frontend

Para la interfaz de usuario completa:

```bash
# Clonar el frontend
git clone https://github.com/JuanFarre/FrontProde.git
cd FrontProde

# Seguir las instrucciones del README del frontend
```

## 📡 API Endpoints Principales

### Autenticación
```
POST /api/auth/login      - Iniciar sesión
POST /api/auth/register   - Registrar usuario
```

### Pronósticos
```
GET    /api/pronosticos           - Obtener pronósticos
POST   /api/pronosticos           - Crear pronóstico
PUT    /api/pronosticos/{id}      - Actualizar pronóstico
DELETE /api/pronosticos/{id}      - Eliminar pronóstico
```

### Partidos y Equipos
```
GET    /api/partidos              - Obtener partidos
GET    /api/equipos               - Obtener equipos
POST   /api/partidos              - Crear partido (Admin)
```

### Ranking y Usuarios
```
GET    /api/usuarios/ranking      - Ranking de usuarios
GET    /api/usuarios/perfil       - Perfil del usuario
```

## 🏗️ Estructura del Proyecto

```
src/main/java/PelusaDev/Prode/
├── controller/           # Controladores REST
├── dto/                 # DTOs para transferencia de datos
├── entity/              # Entidades JPA (Usuario, Partido, Pronostico, etc.)
├── repository/          # Repositorios JPA
├── service/             # Lógica de negocio
├── security/            # Configuración JWT y Security
└── config/              # Configuraciones generales
```

## 🔐 Seguridad

- **JWT Authentication** con tokens seguros
- **Role-based access** (USER, ADMIN)
- **Password encoding** con BCrypt
- **CORS** configurado para frontend
- **Validación** de datos con Bean Validation
- **Variables de entorno** para datos sensibles

## 📊 Funcionalidades del Sistema

### API de Usuarios
- Registro y autenticación
- Gestión de perfiles
- Sistema de roles y permisos

### API de Pronósticos
- CRUD completo de pronósticos
- Validación de fechas límite
- Cálculo automático de puntuación

### API de Administración
- Gestión de partidos y equipos
- Control de fechas y torneos
- Estadísticas y reportes

## 📁 Configuración de Archivos

- **Tamaño máximo de archivo**: 10MB
- **Tamaño máximo de request**: 10MB
- **Tipos permitidos**: Configurables

## 🔧 Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL de base de datos | `jdbc:mysql://localhost:3306/pronosticos_db` |
| `DB_USERNAME` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | `mi_password` |
| `MAIL_USERNAME` | Email para notificaciones | `miapp@gmail.com` |
| `MAIL_PASSWORD` | App Password de Gmail | `abcd efgh ijkl mnop` |
| `JWT_SECRET` | Clave secreta para JWT | `clave_super_segura_64_caracteres_minimo` |

## 🤝 Contribuir

1. Fork el proyecto
2. Crea tu feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Add: nueva funcionalidad'`)
4. Push a la branch (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 👨‍💻 Autor

**Juan Farré**
- GitHub: [@JuanFarre](https://github.com/JuanFarre)
- Email: juanfarre99@gmail.com

## 🚨 Importante

- ⚠️ **NUNCA** subas `application.properties` con datos reales
- ✅ Usa siempre `application.properties.example` como plantilla
- 🔐 En producción, configura variables de entorno del servidor
- 🔑 Genera una nueva clave JWT segura para producción
- 📧 Usa App Passwords de Gmail, no tu contraseña principal

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

⭐ **¡Dale una estrella si te gustó el proyecto!** ⭐

### 🔗 Enlaces Relacionados
- [Frontend Angular](https://github.com/JuanFarre/FrontProde) - Interfaz de usuario
