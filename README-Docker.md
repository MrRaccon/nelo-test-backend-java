# 🐳 Docker Compose Setup

## 📋 Requisitos Previos
- Docker y Docker Compose instalados
- Puerto 4343 disponible en tu máquina

## 🚀 Iniciar el Entorno

### 1. **Levanta los servicios**
```bash
docker-compose up -d
```

### 2. **Verifica los contenedores**
```bash
docker-compose ps
```

### 3. **Verifica los logs**
```bash
# Logs de la API
docker-compose logs nelo-api

# Logs de la base de datos
docker-compose logs mysql-db
```

## 🌐 Acceso a los Servicios

### **API REST**
- **URL**: http://localhost:8080/api/v1/
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **Health Check**: http://localhost:8080/api/v1/actuator/health

### **Base de Datos MySQL**
- **Host**: localhost
- **Puerto**: 4343
- **Database**: restaurantdb
- **Usuario**: nelo_user
- **Contraseña**: nelo_password

### **Conexión con MySQL Workbench**
```
Connection Name: Nelo Restaurant DB
Hostname: 127.0.0.1
Port: 4343
Username: nelo_user
Password: nelo_password
Default Schema: restaurantdb
```

### **Conexión con DBeaver**
```
Host: localhost
Port: 4343
Database: restaurantdb
User: nelo_user
Password: nelo_password
```

## 📊 Datos Iniciales

El sistema incluye datos de ejemplo automáticamente:
- ✅ 5 restaurantes con diferentes horarios
- ✅ Endosos de restricciones dietéticas
- ✅ Mesas con capacidades variadas (2, 4, 6, 8 personas)
- ✅ Listo para pruebas inmediatas

## 🔧 Configuración

### **Variables de Entorno**
- `SPRING_PROFILES_ACTIVE=docker` - Usa configuración de Docker
- `SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/restaurantdb`
- `SPRING_DATASOURCE_USERNAME=nelo_user`
- `SPRING_DATASOURCE_PASSWORD=nelo_password`

### **Volúmenes Persistentes**
- `mysql_data` - Datos de la base de datos persisten entre reinicios
- `./docker/init-db.sql` - Script de inicialización automática

## 🛠️ Comandos Útiles

### **Reiniciar servicios**
```bash
docker-compose restart
```

### **Detener servicios**
```bash
docker-compose down
```

### **Limpiar y reconstruir**
```bash
docker-compose down -v
docker-compose up --build
```

### **Acceder al contenedor de la API**
```bash
docker exec -it nelo-api bash
```

### **Acceder a MySQL directamente**
```bash
docker exec -it mysql-db mysql -u nelo_user -p restaurantdb
# Contraseña: nelo_password
```

## 🔍 Verificación

### **Verificar que la API responde**
```bash
curl -X GET http://localhost:8080/api/v1/
```

### **Verificar conexión a la base de datos**
```bash
# Desde el contenedor API
docker exec nelo-api curl -X GET http://localhost:8080/api/v1/actuator/health
```

## 🚨 Solución de Problemas

### **Error: "Connection refused"**
- Verifica que los contenedores estén corriendo: `docker-compose ps`
- Espera 30 segundos a que MySQL inicie completamente
- Verifica que el puerto 8080 no esté ocupado

### **Error: "Database connection failed"**
- Revisa los logs de MySQL: `docker-compose logs mysql-db`
- Verifica credenciales en `application-docker.properties`
- Confirma que el puerto 4343 esté disponible

### **Error: "Table doesn't exist"**
- Espera a que `init-db.sql` termine de ejecutarse
- Verifica logs de inicialización: `docker-compose logs mysql-db`

## 📝 Notas Importantes

- 🔄 **Los datos persisten** entre reinicios gracias al volumen `mysql_data`
- 🐳 **Red interna**: Los contenedores se comunican por red `nelo-network`
- 🚀 **Auto-reinicio**: Los servicios se reinician automáticamente si fallan
- 📊 **Datos listos**: Sistema viene con datos de ejemplo para pruebas inmediatas

¡Listo para desarrollo y pruebas! 🎉
