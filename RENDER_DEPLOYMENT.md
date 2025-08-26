# Deployment en Render con Aiven MySQL

Esta guía te ayudará a deployar la aplicación en Render usando el perfil `test` y la base de datos MySQL de Aiven.

## 🎯 Configuración Actual

- ✅ **Perfil**: `test` (configurado en Dockerfile)
- ✅ **Base de datos**: Aiven MySQL (credenciales en application-test.yml)
- ✅ **Puerto**: Dinámico (${PORT:8080})
- ✅ **Esquema**: No se regenera (ddl-auto: none)

## 🚀 Pasos para Deploy en Render

### 1. Preparar el repositorio

```bash
# Hacer commit de los cambios
git add .
git commit -m "Configure test profile for Render deployment with Aiven"
git push origin main
```

### 2. Crear servicio en Render

1. Ve a [Render Dashboard](https://dashboard.render.com)
2. Click en **"New +"** → **"Web Service"**
3. Conecta tu repositorio Git
4. Configura el servicio:

   **Basic Settings:**
   - **Name**: `gm-article-test`
   - **Region**: `Oregon (US West)` o el más cercano
   - **Branch**: `main`
   - **Runtime**: `Docker`

   **Build & Deploy:**
   - **Dockerfile Path**: `./Dockerfile` (por defecto)
   - **Docker Context**: `.` (por defecto)

### 3. Variables de entorno (Requeridas)

Configura las siguientes variables de entorno en Render:

```
DATABASE_URL=jdbc:mysql://tu-host-aiven.aivencloud.com:puerto/defaultdb?useSSL=true&requireSSL=true&serverTimezone=UTC&autoReconnect=true
DB_USERNAME=tu-usuario-aiven
DB_PASSWORD=tu-password-aiven
```

### 4. Deploy

1. Click **"Create Web Service"**
2. Render automáticamente:
   - Clonará tu repositorio
   - Ejecutará `docker build`
   - Iniciará el contenedor
   - La aplicación usará el perfil `test` automáticamente

## 📊 Monitoreo

### Logs de la aplicación
- Ve a tu servicio en Render
- Click en **"Logs"** para ver el output en tiempo real
- Busca líneas como:
  ```
  The following profiles are active: test
  Started Application in X.XXX seconds
  ```

### Health Check
Una vez deployado, prueba:
```bash
curl https://tu-app.onrender.com/actuator/health
```

## 🔧 Configuración del Perfil Test

El perfil `test` está optimizado para Render:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:mysql://...}  # Aiven MySQL
    hikari:
      maximum-pool-size: 5      # Optimizado para Render
      connection-timeout: 30000
  jpa:
    hibernate:
      ddl-auto: none           # No regenera esquema
    show-sql: true             # Debug habilitado
  sql:
    init:
      mode: never              # No ejecuta scripts
server:
  port: ${PORT:8080}           # Puerto dinámico de Render
```

## 🐛 Troubleshooting

### Error de conexión a base de datos
- Verifica que las credenciales de Aiven sean correctas
- Asegúrate de que el SSL esté habilitado (`useSSL=true&requireSSL=true`)

### Timeout en el build
- Render tiene un timeout de 15 minutos para builds
- El build actual toma ~3-5 minutos, así que debería estar bien

### Error de puerto
- Render asigna automáticamente el puerto via variable `PORT`
- La aplicación está configurada para usar `${PORT:8080}`

## 📝 Notas Importantes

- ⚠️ **No regenera esquema**: La aplicación usa el esquema existente en Aiven
- ⚠️ **Datos persistentes**: Todos los cambios se guardan en Aiven
- ✅ **SSL habilitado**: Conexión segura a Aiven
- ✅ **Logging detallado**: Para debug en desarrollo