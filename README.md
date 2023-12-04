# MacJavaServer
Bienvenido a la API MacJava, desarrollada por Jaime Lozano, Diego Torres, Oscar Encabo y Kelvin Sánchez. Esta API proporciona una gestión segura y completa de bases de datos, diseñada para tiendas online, y está basada en el framework SpringBoot.

## Autores

- [Kelvin Sánchez](https://github.com/KevinSanchez5)
- [Jaime Lozano](https://github.com/jaime9lozano)
- [Oscar Encabo](https://github.com/Diokar017)
- [Diego Torres Mijarra](https://github.com/DiegoTorresMijarra)

## Pdf
[Documento](https://github.com/DiegoTorresMijarra/MacJavaServer/tree/master/pdf/MacJava.pdf)

## Video
[Video](https://www.youtube.com/watch?v=nvKjzIZSk80)

## Contenido
1. [Introducción](#introduction)
2. [GitFlow](#gitflow)
3. [Dependencias](#dependencies)
4. [Bases de Datos](#databases)
5. [Características Comunes de los Endpoints](#common-features)
6. [Categorías](#categories)
7. [Trabajadores y Clientes](#workers-and-clients)
8. [Restaurantes y Productos](#restaurants-and-products)
9. [Autenticación](#authentication)
10. [Usuarios](#users)
11. [Pedidos](#orders)
12. [Despliegue](#deployment)
13. [Tests](#tests)
##
### 1. Introducción <a name="introduction"></a> 
La API MacJava ofrece una administración segura y completa de bases de datos, diseñada para tiendas online. Basada en SpringBoot, destaca por su facilidad de configuración y rápido desarrollo. Se sigue la metodología GitFlow para la gestión de ramas, con ramas de características integradas en una rama de desarrollo y fusionadas en una rama principal después de pruebas.

### 2. GitFlow <a name="gitflow"></a>
Se ha seguido la metodología GitFlow para la organización del proyecto, con ramas de características fusionadas en una rama de desarrollo, integrando cambios de manera segura.

### 3. Dependencias <a name="dependencies"></a>
Las dependencias incluyen SpringBoot (con varias características), bases de datos como PostgreSQL y H2, Lombok, Swagger, Jackson y JWT. Consulta la sección de dependencias para obtener detalles completos.

### 4. Bases de Datos <a name="databases"></a>
Para el desarrollo, se utiliza H2, que funciona en memoria y facilita la configuración de datos. En producción, se emplea PostgreSQL por su robustez y escalabilidad.

### 5. Características Comunes de los Endpoints <a name="common-features"></a>
Se detallan características comunes en los modelos, UUIDs y autonuméricos, DTOs, Mappers, Excepciones, Repositorios, Servicios y Controladores. Los métodos comunes incluyen findAll, findById, save, update y deleteById. Se utiliza caché para mejorar el rendimiento.

### 6. Categorías <a name="categories"></a>
El endpoint de categorías gestiona roles (puestos) con detalles como id, nombre limitado, y salario base.

### 7. Trabajadores y Clientes <a name="workers-and-clients"></a>
Estos endpoints comparten comportamiento. Solo los trabajadores y administradores pueden realizar cambios en las bases de datos. El programa está diseñado para cobertura local y presencial, con planes de expansión para compras online.

### 8. Restaurantes y Productos <a name="restaurants-and-products"></a>
Estos endpoints ofrecen control mixto: acciones permitidas para todos y acciones solo para administradores. Se manejan respuestas detalladas y códigos de estado.

### 9. Autenticación <a name="authentication"></a>
El endpoint de autenticación permite registrarse e iniciar sesión. Se utiliza JWT para gestionar tokens y autenticar usuarios.

### 10. Usuarios <a name="users"></a>
Este endpoint administra información de usuarios, con detalles tanto para administradores como para usuarios. Las contraseñas se codifican para mayor seguridad.

### 11. Pedidos <a name="orders"></a>
Este endpoint es el más complejo, gestionando pedidos de restaurantes, clientes y trabajadores. Utiliza MongoDB para almacenar pedidos, con clases específicas para productos pedidos y pedidos.

### 12. Despliegue <a name="deployment"></a>
El despliegue se realiza mediante Docker, facilitando la distribución y permitiendo ajustes de configuración con archivos Dockerfile y docker-compose.

### 13. Tests <a name="tests"></a>
Se han creado pruebas de caja negra y caja blanca con JUnit y Mockito para verificar el correcto funcionamiento de la aplicación.
