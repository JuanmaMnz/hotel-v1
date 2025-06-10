# 🏨 Hotel API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

## 📋 Tabla de Contenido

- [📝 Descripción del proyecto](#_2-descripción-del-proyecto)
- [🚀 Funcionalidades principales](#_2-funcionalidades-principales)
- [📖 Documentación](#_2-documentación)
- [🧪 Usuarios de prueba](#_2-usuarios-de-prueba)
- [🛠️ Guía de instalación](#_2-guía-de-instalación)
  - [Prerrequisitos (⚠️ Importante)](#prerrequisitos-️-importante)
  - [Instalación](#instalación)
    - [Backend Local](#backend-local)
    - [Backend Docker](#backend-docker)
- [⚠️ Errores y soluciones](#_2-errores-y-soluciones)
  - [Errores Docker](#errores-docker)

## 📝 Descripción del proyecto

**API RESTful para la gestión de un hotel**, desarrollada como parte del curso *Construcción de Software II*. La API permite gestionar los diferentes procesos administrativos de un hotel

El sistema contempla **dos tipos de usuarios**:

- **Huéspedes**: pueden autenticarse en el sistema, consultar habitaciones disponibles y **realizar reservas**.

- **Empleados**: tienen acceso a funcionalidades administrativas como **gestionar habitaciones, artículos, reservas, check-in, check-out, facturación y servicios** del hotel.

La API soporta **autenticación mediante Json Web Tokens** y ofrece endpoints organizados para facilitar su uso y documentación. Algunos **endpoints son públicos** y accesibles sin autenticación, mientras que otros **requieren un rol específico** para ser utilizados. Los endpoints públicos cuentan con un **rate limiter** para controlar la cantidad de peticiones y asegurar la estabilidad del servicio.

## 🚀 Funcionalidades principales 

#### 🔐 Autenticación
- `POST /api/v1/hotel/authenticate` – Autenticar usuarios existentes.
- `POST /api/v1/hotel/logout` – Cerrar sesión de usuario.

#### 🛏️ Gestión de habitaciones
- `POST /api/v1/hotel/room` – Registrar nueva habitación.
- `GET /api/v1/hotel/room/{id}` – Obtener habitación por ID.
- `PUT /api/v1/hotel/room/{id}` – Actualizar habitación existente.
- `DELETE /api/v1/hotel/room/{id}` – Eliminar habitación.
- `POST /api/v1/hotel/room/{roomId}/images` – Subir imágenes a una habitación.
- `DELETE /api/v1/hotel/room/{roomId}/images` – Eliminar imágenes de habitación.
- `GET /api/v1/hotel/rooms/search` – Buscar habitaciones con filtros y paginación.

#### 📦 Gestión de artículos
- `POST /api/v1/hotel/item` – Registrar nuevo artículo.
- `GET /api/v1/hotel/items` – Obtener todos los artículos.
- `PUT /api/v1/hotel/item/{id}` – Actualizar artículo existente.
- `DELETE /api/v1/hotel/item/{id}` – Eliminar artículo.

#### 🛏️ Artículos en habitaciones
- `POST /api/v1/hotel/room/{roomId}/item/{itemId}` – Asignar artículo a una habitación.
- `PUT /api/v1/hotel/room/{roomId}/item/{itemId}` – Actualizar artículo en habitación.
- `DELETE /api/v1/hotel/room/{roomId}/item/{itemId}` – Eliminar artículo de una habitación.

#### 👥 Gestión de empleados
- `POST /api/v1/hotel/employee` – Registrar nuevo empleado.
- `GET /api/v1/hotel/employee` – Obtener empleado por ID.
- `GET /api/v1/hotel/employees` – Listar todos los empleados.
- `PUT /api/v1/hotel/employee/retire` – Marcar a un empleado como retirado.

#### 🧍‍♂️ Gestión de huéspedes
- `POST /api/v1/hotel/guest` – Registrar nuevo huésped.
- `GET /api/v1/hotel/guest` – Buscar huésped por tipo y número de documento.
- `GET /api/v1/hotel/guests` – Listar todos los huéspedes.
- `PUT /api/v1/hotel/update-guest` – Actualizar información de huésped.

#### 📅 Reservas
- `POST /api/v1/hotel/reservation` – Crear una reserva.
- `PATCH /api/v1/hotel/reservation/{reservationId}/update-state-to-cancel` – Cancelar reserva.
- `GET /api/v1/hotel/reservation/{id}` – Consultar reserva por ID.
- `GET /api/v1/hotel/reservation/by-room-month` – Reservas por habitación y mes.

#### 🧾 Facturación
- `GET /api/v1/hotel/invoice/{invoiceId}` – Obtener factura por ID.

#### 🛎️ Check-in
- `POST /api/v1/hotel/check-in` – Registrar check-in.
- `GET /api/v1/hotel/check-in/{checkInId}` – Obtener check-in por ID.
- `PUT /api/v1/hotel/check-in/{checkInId}` – Actualizar check-in.
- `GET /api/v1/hotel/check-ins` – Listar check-ins por día.

#### 🧾 Check-out
- `POST /api/v1/hotel/check-out` – Registrar check-out.
- `GET /api/v1/hotel/check-out/{checkOutId}` – Obtener check-out por ID.
- `PUT /api/v1/hotel/check-out/{checkOutId}` – Actualizar check-out.
- `GET /api/v1/hotel/check-outs` – Listar check-outs por día.

#### 🧳 Servicios del hotel
- `POST /api/v1/hotel/service` – Crear nuevo servicio.
- `GET /api/v1/hotel/services` – Listar todos los servicios.
- `GET /api/v1/hotel/service/{hotelServiceId}` – Obtener servicio por ID.
- `PUT /api/v1/hotel/service/{hotelServiceId}` – Actualizar servicio.
- `DELETE /api/v1/hotel/service/{hotelServiceId}` – Eliminar servicio.

#### 🧾 Uso de servicios por huésped
- `POST /api/v1/hotel/guest/service` – Registrar uso de un servicio.
- `GET /api/v1/hotel/guest/services` – Obtener servicios usados por un huésped.
- `DELETE /api/v1/hotel/guest/services/{guestServiceId}` – Eliminar un servicio usado.

## 📖 Documentación

La  [Documentación](http://localhost:8080/swagger-ui.html "Documentación en Swagger") completa de la API se puede encontrar y probar en la interfaz de Swagger. Una vez que la aplicación esté en ejecución

puedes acceder a la documentación en: `http://localhost:8080/swagger-ui.html`

## 🧪 Usuarios de prueba

Para facilitar las pruebas y el uso de la API, puedes usar las siguientes credenciales preconfiguradas en entorno de desarrollo.

- **Email:** `employeeone@email.com`
- **Password:** `employee123!`
- **Roles:** `EMPLOYEE, GUEST`

## 🛠️ Guía de instalación

A continuación, se encuentran las instrucciones para poner en marcha el proyecto.

### Prerrequisitos (⚠️ Importante)

**Es fundamental que tengas instalados y configurados los siguientes componentes antes de proceder**, ya que son necesarios para el correcto funcionamiento del proyecto:

- *Java 17*
- *Spring Boot 3.4.5*
- *Maven v4.0.0*
- *MySQL*: Base de datos relacional utilizada
    - database: Asegurese de tener una DB con el nombre "hotel_dev_db"
    - username: root
    - password: password
    - port: 3306
- *Variables de entorno*:
    - Encontradas en el archivo `.env.dev` del proyecto y en `application-dev.yml`

- *Credenciales de Cloudinary*: **(⚠️Importante)**
  - La API utiliza Cloudinary para la gestión de imágenes. Es necesario que agregues las siguientes credenciales para su correcto funcionamiento. Deben configurarse tanto en el archivo `.env.dev` como en `application-dev.yml`:

      * `CLOUDINARY_CLOUD_NAME`
      * `CLOUDINARY_API_KEY`
      * `CLOUDINARY_API_SECRET`

### Instalación

#### Backend Local

- Clona el repositorio: 

```bash
  git clone https://github.com/JuanmaMnz/hotel-v1
```
- Abra el proyecto en su IDE.
- Abra un terminal de comandos dentro del IDE.
- En la terminal de comandos, ejecute el siguiente comando: 
```bash
  ./mvnw spring-boot:run
```
- El servidor se ejecutará en: http://localhost:8080

#### Backend Docker

- Asegúrate de terminar el proceso del backend en la consola (si se está ejecutando localmente) para evitar conflictos de puertos
- Inicia Docker o abre Docker Desktop
- En la terminal, ejecuta el siguiente comando

```bash
  ./start-docker-dev.sh
```

- Este comando construirá la imagen de Docker para la aplicación Spring Boot y levantará un contenedor para MySQL y otro para la aplicación.
- Una vez que el contenedor esté en funcionamiento ...
- El servidor se ejecutará en: http://localhost:8080
- Puedes detener el contenedor de Docker con el comando 

```bash
  ./stop-docker-dev.sh
```

- Después de esto, ve a Docker Desktop y elimina containers, images, volumes, and builds para evitar errores y conflictos de caché antes de levantarlo nuevamente.

## ⚠️ Errores y soluciones

### Errores Docker

ERROR Wsl 2:

- Esto indica que la máquina que intenta ejecutar Docker no tiene un distribuidor de Linux instalado y/o la virtualización no está habilitada en su BIOS.
- Este problema debe resolverse para poder ejecutar Docker Desktop
  - src: [Docker Desktop WSL 2](https://docs.docker.com/desktop/wsl/)

ERROR GENÉRICO:

- Para cualquier otro error relacionado con la integración de Docker y el backend, se recomienda limpiar el caché de Docker ejecutando los siguientes comandos:

    - `docker container prune -f`
    - `docker image prune -a -f`
    - `docker network prune -f`
    - `docker volume prune -f`
