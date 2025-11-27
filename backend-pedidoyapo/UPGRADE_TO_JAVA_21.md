# Upgrade a Java 21 (LTS)

Este repositorio se ha actualizado para compilar con Java 21. Cambios realizados:

- `pom.xml`: la propiedad `java.version` fue actualizada de 17 a 21.
- Se añadió `maven-compiler-plugin` (v3.11.0) configurado para usar `<release>21`.
 - `pom.xml`: la propiedad `java.version` fue actualizada de 17 a 21.
 - Se añadió `maven-compiler-plugin` (v3.11.0) configurado para usar `<release>21`.
 - `spring-boot-starter-parent` actualizado de `3.2.1` a `3.5.8` para aprovechar correcciones y soporte extendido.
  
### CI / GitHub Actions
Se añadió un workflow de GitHub Actions (`.github/workflows/ci.yml`) que:

También se añadió soporte para nombre y tags basados en la versión del `pom.xml` (project.version). El workflow extrae `project.version` y etiqueta la imagen con `:latest`, `:${project.version}` y `:${GITHUB_SHA}` para facilitar la trazabilidad.

Si quieres que el workflow publique imágenes automáticamente, puedes usar GHCR (recomendado) o Docker Hub:

  - En `Settings -> Actions` del repositorio, asegúrate que el `GITHUB_TOKEN` tiene permisos `packages: write` (la plantilla de workflow ya solicita `packages: write`).
  - El workflow ya empuja a `ghcr.io/${{ github.repository_owner }}/<repo>` de forma automática (requiere `packages: write` en los permisos del workflow).
  - Crea secrets en GitHub: `DOCKERHUB_USERNAME` y `DOCKERHUB_TOKEN`.
  - Si están presentes, el workflow iniciará sesión y empujará la imagen a `DOCKERHUB_USERNAME/backend:latest`.

### Dockerfile
El repositorio incluye un workflow `deploy.yml` que construye y empuja la imagen a GHCR y luego despliega en un servidor remoto vía SSH.

Requisitos:
- El servidor remoto debe tener Docker y Docker Compose v2 (o `docker compose`) instalados.
- El usuario SSH debe tener permisos para ejecutar Docker y para escribir en el `DEPLOY_PATH` configurado en los secrets.
- En GitHub debes crear los siguientes secrets:
  - `SSH_HOST` - IP o hostname del servidor
  - `SSH_USERNAME` - Nombre de usuario SSH
  - `SSH_PRIVATE_KEY` - Private key (sin passphrase) cuyo public key debe estar autorizado en el servidor (`~/.ssh/authorized_keys`)
  - `SSH_PORT` - Puerto SSH (opcional; 22 por defecto)
  - `DEPLOY_PATH` - Ruta en server donde se guardarán `docker-compose.prod.yml` y `.env.prod` (ej: `/home/ubuntu/app`)
  - `PROD_DB_HOST`, `PROD_DB_USERNAME`, `PROD_DB_PASSWORD` - Variables para la base de datos productiva
  - `PROD_APP_OWNER`, `PROD_APP_REPO` - Usualmente dueño/repositorio para formar el nombre de la imagen

Cómo funciona:
- El workflow compila y empuja la imagen a GHCR con tags `latest`, `:{version}` y `:{sha}`.
- Crea un archivo `.env.prod` con las variables definidas en los secrets y lo copia junto con `docker-compose.prod.yml` al servidor usando SCP.
- Ejecuta `docker compose pull` y `docker compose up -d --remove-orphans` en el servidor vía SSH.

El workflow `deploy.yml` se encuentra configurado para ejecutarse automáticamente solo cuando se crea un tag (por ejemplo `v1.0.0`). También se puede ejecutar manualmente desde Actions (workflow_dispatch) para situaciones de emergencia o despliegues manuales.

Advertencias:
- No añadas credenciales en texto plano al repo. Usa GitHub Secrets para las claves y credenciales.
- Actualiza `docker-compose.prod.yml` si necesitas configuraciones específicas para producción (logs, volúmenes, redes internas, secretos).
También se incluyó un `Dockerfile` multi-stage que utiliza `maven:3.9.4-eclipse-temurin-21` como etapa de construcción y `eclipse-temurin:21-jdk-jammy` como runtime. Esto facilita crear imágenes reproducibles y consistentes con JDK 21.

### docker-compose (desarrollo local)
Se añadió un archivo `docker-compose.yml` que levanta:
- Un container `db` con MySQL 8.0 configurado usando variables de entorno.
- Un container `app` que construye la imagen usando el `Dockerfile` del proyecto y se conecta a la base `db`.

Usar el compose en local:

```bash
docker compose up --build
```
Esto iniciará la base de datos y la aplicación; la app estará accesible en `http://localhost:8085`.

Recomendaciones para usar Java 21 en tu entorno local y en CI/CD

1) **Instalar JDK 21 localmente (Windows)**

- Descarga Temurin 21 desde https://adoptium.net o adoptium OpenJDK (o usa el proveedor que prefieras).
- Instala JDK 21, por ejemplo en `C:\Program Files\Java\jdk-21`.
- Ajusta `JAVA_HOME` y `Path` en variables de entorno: 
  - `setx JAVA_HOME "C:\\Program Files\\Java\\jdk-21"` 
  - Agrega `%JAVA_HOME%\\bin` a la variable `Path`.
- Verifica con:

```cmd
java -version
mvn -version
```

2) **Si usas Docker**

- En la imagen base, usa `eclipse-temurin:21` o `openjdk:21-jdk` para correr tu app en runtime.

Ejemplo simple (Dockerfile):

```dockerfile
FROM eclipse-temurin:21-jdk-jammy as build
WORKDIR /app
COPY pom.xml mvnw /app/
COPY src /app/src
RUN mvn -DskipTests package

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/backend-1.0.0.jar ./backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]
```

3) **Actualizar CI/CD**

- Cambia el runner matrix para usar `adoptopenjdk:21` o `temurin-21`.
- Si tu pipeline instala JDK 11 u 17, actualiza a 21.

4) **Notas de compatibilidad**

- La app está basada en Spring Boot `3.2.1` que soporta Java 21, pero se recomienda mantener las librerías y parent a la versión más reciente del `3.2.x` o actualizar a `3.3.x`/`3.5.x` si necesitas correcciones.
- Si usas dependencias con bindings nativos o versiones específicas, prueba en un entorno con JDK 21.

Si quieres que actualice config adicional (Dockerfile, Github Actions) o actualice Spring Boot a una versión más reciente compatible, dime y lo hago.

### Backup y Rollback en despliegues
El workflow de despliegue (`.github/workflows/deploy.yml`) implementa un paso de backup de base de datos y una estrategia básica de rollback:

- Antes de desplegar, crea un volcado con `mysqldump` a `backups/pedidoyapo-<timestamp>.sql` dentro del `DEPLOY_PATH` en el servidor remoto.
- La imagen anterior del servicio se guarda en `.previous_image` para permitir revertir a la versión previa si la comprobación de salud falla tras el despliegue.
- Si `mysqldump` no está instalado en el servidor, el script intentará instalar `default-mysql-client` usando apt (requiere permisos y red).
- La comprobación de salud hace una petición a `http://localhost:8085/actuator/health` tras el despliegue; si falla, el workflow actualiza `APP_IMAGE_TAG` a la versión previa y relanza `docker compose up`.

- El backup se descarga al runner y también se sube a un bucket S3 si defines los secrets `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION` y `S3_BUCKET_NAME`.

Recomendaciones:
- Revisa la política de backups en el servidor (rotación y retención), y automatiza la transferencia de backups a un almacenamiento seguro (S3 o similar).
- Valida manualmente los backups antes de una restauración en producción.