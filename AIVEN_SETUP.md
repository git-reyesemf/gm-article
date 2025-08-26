# Configuración para desarrollo local con Aiven

Este documento explica cómo configurar el entorno de desarrollo local para conectarse directamente a la base de datos MySQL de Aiven.

## Ventajas

- ✅ Desarrollo más rápido (sin necesidad de deploy para probar)
- ✅ Datos reales de producción para testing
- ✅ Mismo esquema que producción
- ✅ Ahorro de tiempo en ciclos de desarrollo

## Configuración

### 1. Variables de entorno

Configura las siguientes variables de entorno con los datos de tu instancia Aiven:

```bash
export DATABASE_URL='jdbc:mysql://tu-host-aiven.aivencloud.com:puerto/defaultdb?sslMode=REQUIRED'
export DB_USERNAME='tu-usuario-aiven'
export DB_PASSWORD='tu-password-aiven'
```

### 2. Obtener credenciales de Aiven

1. Ve a tu consola de Aiven
2. Selecciona tu servicio MySQL
3. En la pestaña "Overview", encontrarás:
   - **Host**: tu-host-aiven.aivencloud.com
   - **Port**: puerto (usualmente 3306)
   - **User**: usuario por defecto
   - **Password**: contraseña generada

### 3. Ejecutar la aplicación

Usa el script proporcionado:

```bash
./run-local-aiven.sh
```

O ejecuta manualmente:

```bash
./gradlew bootRun --args='--spring.profiles.active=local-aiven'
```

## Configuración del perfil

El perfil `local-aiven` está configurado con:

- ✅ `ddl-auto: none` - No regenera el esquema
- ✅ `sql.init.mode: never` - No ejecuta scripts de inicialización
- ✅ Logging habilitado para debug
- ✅ Pool de conexiones optimizado para desarrollo local

## Precauciones

⚠️ **IMPORTANTE**: Este perfil se conecta a la base de datos real de Aiven. Ten cuidado con:

- Operaciones de escritura que puedan afectar datos de producción
- Testing de operaciones destructivas
- Compartir credenciales de acceso

## Troubleshooting

### Error de conexión SSL

Si tienes problemas de SSL, verifica que la URL incluya `?sslMode=REQUIRED`:

```
jdbc:mysql://host:puerto/database?sslMode=REQUIRED
```

### Timeout de conexión

Si la conexión es lenta, ajusta los timeouts en `application-local-aiven.yml`:

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 60000
      idle-timeout: 300000
```