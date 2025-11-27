# PedidosYapo - Backend

Este repositorio contiene el backend de la app PedidosYapo.

## Requisitos para desarrollo
- Java 21 (Temurin/OpenJDK 21)
- Maven 3.9+
- Docker (opcional, para contenedores)

## Instrucciones rápidas

Compilar localmente:

```cmd
mvn -DskipTests package
```

Levantar el entorno con Docker Compose (MySQL + App):

```bash
docker compose up --build
```
La aplicación quedará accesible en `http://localhost:8085`.

## CI/CD y Docker images

El repositorio incluye un workflow de GitHub Actions que compila el proyecto y despliega imágenes Docker.

- Por defecto, el workflow empuja imágenes a GHCR (`ghcr.io/<owner>/<repo>:latest`). Con `GITHUB_TOKEN` y permisos de `packages: write` en Actions esto será automático.
- Si quieres empujar a Docker Hub en vez de (o además de) GHCR, añade estos secrets en GitHub:
  - `DOCKERHUB_USERNAME` con tu usuario de Docker Hub.
  - `DOCKERHUB_TOKEN` con tu token/app password generado en Docker Hub.

- El workflow ahora etiqueta automáticamente las imágenes usando la versión de `pom.xml` (por ejemplo, `:1.0.0`) además de `:latest` y el commit SHA. Esto facilita releases y rollbacks.
 
## Deploy automático con GitHub Actions + SSH

Se incluyó un workflow `deploy.yml` que hace lo siguiente:

- Compila la app, construye la imagen y la sube a GHCR (tags: `latest`, `:{version}`, `:{sha}`) — o puedes integrarlo para empujar a Docker Hub si configuras los secrets.
El workflow de despliegue está configurado para activarse automáticamente solamente cuando se crea un tag en el repositorio (ejemplo: `v1.0.0`). También se puede disparar manualmente desde la pestaña "Actions" (workflow_dispatch).
- Copia `docker-compose.prod.yml` y un archivo `.env.prod` generado en el runner al host remoto vía SCP.
- Ejecuta `docker compose pull` y `docker compose up -d --remove-orphans` en el host remoto vía SSH.

Requisitos y secrets que debes agregar en GitHub:
- `SSH_HOST` — IP o hostname del servidor.
- `SSH_USERNAME` — Usuario SSH.
- `SSH_PRIVATE_KEY` — Private key (sin passphrase) cuyo public key debe estar en `~/.ssh/authorized_keys` del server.
- `SSH_PORT` — (opcional) puerto SSH (por defecto 22).
- `DEPLOY_PATH` — ruta absoluta en el servidor donde se copiará `docker-compose.prod.yml` y `.env.prod`.
- `PROD_DB_HOST`, `PROD_DB_USERNAME`, `PROD_DB_PASSWORD` — credenciales/host para la base de datos productiva.
- `PROD_APP_OWNER`, `PROD_APP_REPO` — para indicar dónde está la imagen publicada en el registry.
- `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION` — credenciales IAM para subir backups a S3.
 - `S3_BUCKET_NAME` — bucket destino para los backups.

Nota: Asegúrate que el usuario SSH pueda ejecutar docker y que DEPLOY_PATH exista y tenga permisos. No añadas credenciales en texto plano, usa GitHub Secrets.


**Nota**: Los tags y repositorios pueden ajustarse en `.github/workflows/ci.yml`.

## Notas
- El proyecto ahora compila con Java 21 y Spring Boot 3.5.8.
- `springdoc-openapi` actualizado a la versión `2.8.14`.

Si quieres que configure push a un registry privado (ACR, ECR, DockerHub, etc.), dímelo y lo adapto.

## Subir archivos (uploads)

Puedes subir archivos desde Postman o curl. A continuación un resumen para probar los endpoints de `FileController` y `ProductoController`:

- `POST /api/uploads/imagen` — sube una única imagen.
- `POST /api/productos/{id}/imagenes` — sube múltiples imágenes asociadas a un producto.

### Configuración importante

- `application.properties` contiene:
  - `uploads.path` — ruta absoluta donde se guardan los archivos (por ejemplo: `C:/Users/favia/Downloads/backend-pedidoyapo/uploads/`).
  - `spring.servlet.multipart.max-file-size` y `spring.servlet.multipart.max-request-size` — límites en el tamaño de archivos; ajusta según tu necesidad.

### Probar desde Postman

1. URL: `http://localhost:8085/api/uploads/imagen` o `http://localhost:8085/api/productos/{id}/imagenes`.
2. Método: POST.
3. En "Body" selecciona `form-data`.
4. Para el endpoint `/api/uploads/imagen` agrega una key:
   - Key: `file`, tipo: `File`, sube la imagen.
5. Para el endpoint `/api/productos/{id}/imagenes` agrega una, dos o más keys:
   - Key: `files` (o `files[]` según tu cliente), tipo: `File`, repítelo por cada archivo que quieras subir.
6. Envia y revisa la respuesta.

### Probar desde curl

Subir una sola imagen:

```bash
curl -v -F "file=@/path/to/photo.png" "http://localhost:8085/api/uploads/imagen"
```

Subir varios archivos (product images):

```bash
curl -v \
  -F "files=@/path/to/a.png" \
  -F "files=@/path/to/b.jpg" \
  "http://localhost:8085/api/productos/1/imagenes"
```

Si recibes 400 (`No se enviaron imágenes`), revisa que el nombre de las keys sea `file` o `files` según el endpoint.

### Mensajes comunes y soluciones

- `Archivo demasiado grande` (413): aumenta `spring.servlet.multipart.max-file-size` y `spring.servlet.multipart.max-request-size` en `application.properties` o reduce la calidad del archivo.
- `Tipo de archivo no soportado` (415): asegúrate que estés enviando un `image/*` válido (jpeg, png, gif...) y que el contenido-type sea correcto en el cliente.
- `No se enviaron imágenes` (400): la key en el form-data no es la esperada (`file` / `files`).
- `Error subiendo archivo` (500): revisa la consola del servidor; incluye logs del backend (hemos agregado prints) con el path de destino y nombre de archivo.


## Producción con Docker Compose

El repo incluye un `docker-compose.prod.yml` que asume que la imagen ya ha sido publicada en GHCR o Docker Hub. Puedes desplegar usando un archivo `.env.prod` con variables de entorno que reemplacen el `APP_IMAGE_TAG` y la configuración de la DB.

Ejemplo de `.env.prod`:

```
APP_OWNER=your-gh-username-or-org
APP_REPO=backend-pedidoyapo
APP_IMAGE_TAG=1.0.0
DB_HOST=your-db-host
DB_USERNAME=root
DB_PASSWORD=1234
```

Lanzar producción:

```bash
docker compose -f docker-compose.prod.yml --env-file .env.prod up -d
```

## Backup y rollback automático (durante deploy)

El workflow `deploy.yml` hace un backup de la base de datos antes de actualizar los servicios en el host remoto. Esto crea un archivo en `backups/` dentro del `DEPLOY_PATH` con un timestamp (por ejemplo `backups/pedidoyapo-20251125010101.sql`).

Si la comprobación de salud (`/actuator/health`) falla después del despliegue, el workflow intentará revertir al `APP_IMAGE_TAG` anterior (guardado en `.previous_image`) y relanzar la stack. Requisitos:

- El servidor remoto debe tener `mysqldump` (o el workflow intentará instalar `default-mysql-client` si el servidor permite `apt-get` e `sudo`).
- Las variables necesarias deben estar disponibles como secrets: `PROD_DB_HOST`, `PROD_DB_USERNAME`, `PROD_DB_PASSWORD`, `SSH_HOST`, `SSH_USERNAME`, `SSH_PRIVATE_KEY`, `DEPLOY_PATH`, `PROD_APP_OWNER`, `PROD_APP_REPO`.

Advertencia: Aunque se intenta automatizar rollback, revisa siempre los backups y pruebas antes de restaurar una base de datos en producción.

