# Proyecto CAPEBE App
Aplicacion movil para la gestion de vuelos recreativos.

## Integrantes
- Francisco Riveros
- Victoria Rivera

## Roles
- Invitado
- Administrador

## Herramientas usadas
- Kotlin
- Jetpack Compose
- Material Design 3
- Room / SQLite
- MVVM
- Android Studio
- GitHub
- Trello

## Funcionalidades

### Invitado
- Registro de nuevos usuarios.
- Inicio de sesion.
- Visualizacion de vuelos disponibles.
- Inscripcion a vuelos con cupos disponibles.
- Validacion para evitar inscripciones duplicadas.
- Visualizacion de vuelos inscritos.
- Aceptar consentimiento antes de confirmar una inscripción.
- Habilidad de tomar una foto de identificacion mediante la camara.
- Persistencia de la foto de identificacion.
- Recordatorio mediante notificacion asociado a la fecha y hora del vuelo.

### Administrador
- Inicio de sesion como administrador.
- Programacion de nuevos vuelos.
- Seleccion de fecha y hora mediante DatePicker y TimePicker.
- Visualizacion de vuelos programados.
- Visualizacion de cupos totales y disponibles.
- Visualizacion de pasajeros inscritos por vuelo.
- Visualizacion de fotografia de identificacion del pasajero.
- Visualizacion del estado del consentimiento.
- Cambio de un pasajero a otro vuelo posterior con cupos disponibles.
- Actualizacion automatica de cupos al cambiar un pasajero de vuelo.

## Persistencia local

La aplicacion utiliza Room como capa de acceso a una base de datos SQLite local.
Actualmente se utilizan las siguientes tablas:

- `users`: almacena los usuarios registrados y la ruta de su fotografia de identificacion.
- `jornadas`: almacena los vuelos programados.
- `inscripciones`: almacena la relacion entre los usuarios y los vuelos, junto con el estado del consentimiento.
La informacion puede ser revisada en cualquier momento desde Android Studio en "View-Tool Windows- App inspection.

## Recursos nativos:
### Notificaciones
Se utiliza el sistema de notificaciones de Android para recordar al Invitado la fecha y hora de su vuelo.
Si el Administrador cambia al pasajero a otro vuelo, el recordatorio se actualiza automáticamente con la nueva información.

### Camara
El Invitado puede utilizar la camara del dispositivo para registrar una foto como metodo de apoyo de identificacion.
La imagen queda almacenada localmente y el administrador la puede ver posteriormente al revisar los pasajeros inscritos.

## Estructura del proyecto

El proyecto utiliza una estructura basada en MVVM, separando las responsabilidades principales en:

- `data`: base de datos, entidades y DAO.
- `model`: modelos utilizados por la aplicacion.
- `ui/screen`: pantallas de la aplicacion.
- `ui/viewmodel`: logica y gestion de estado.
- `ui/navigation`: navegacion entre pantallas.
- `ui/components`: componentes reutilizables.
- `notification`: manejo de recordatorios y notificaciones.

## Como ejecutar el proyecto
1. Abrir el proyecto en Android Studio.
2. Esperar la sincronizacion de Gradle.
3. Ejecutar la aplicacion en un emulador o dispositivo Android.
4. Registrar un nuevo usuario para ingresar como Invitado.

Para ingresar como Administrador utilizar:
- Correo: `admin@capebe.cl`
- Contraseña: `admin123`

## Flujo principal
### Invitado
Registro → Inicio de sesion → Ver vuelos disponibles → Aceptar consentimiento → Inscribirse → Mis vuelos → Recordatorio de vuelo.

El Invitado tambien puede registrar su fotografia desde la opcion **Mi identificacion**.

### Administrador
Inicio de sesion → Programar vuelo → Ver vuelos programados → Ver pasajeros inscritos → Revisar identificacion y consentimiento → Cambiar pasajero de vuelo.

## Estado del proyecto
Version funcional desarrollada para la "evaluacion N2" Con futuras mejoras a implementar.
