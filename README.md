# MoodSound - Backend (Spring Boot)

Servidor de la aplicación MoodSound: plataforma de recomendación musical basada en el estado de ánimo del usuario.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.0
- YouTube Data API v3

## 🔧 Instalación

### 1. Clonar repositorio
```bash
git clone https://github.com/JesusDEV91/moodsound-backend.git
cd moodsound-backend
```

### 2. Crear base de datos
```sql
CREATE DATABASE moodsound;
```

### 3. Configurar `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moodsound
spring.datasource.username=root
spring.datasource.password=tu_contraseña

youtube.api-key=TU_API_KEY_AQUI
youtube.api-url=https://www.googleapis.com/youtube/v3
```

### 4. Ejecutar
```bash
./mvnw spring-boot:run
```

Servidor disponible en: `http://localhost:8080`

## 📡 Documentación de la API

### Base URL
```
http://localhost:8080/api
```

---

### **Endpoints MOODS**

#### `GET /mood/all`
Obtiene los 4 moods disponibles (happy, sad, energetic, chill).

**Response:**
```json
[
  {
    "id": 1,
    "name": "happy",
    "displayName": "Feliz",
    "emoji": "😄",
    "color": "#FFD93D"
  }
  // ... 3 más
]
```

---

#### `POST /mood/analyze`
Analiza texto libre o recibe mood seleccionado directamente.

**Request (texto libre):**
```json
{
  "text": "Estoy muy feliz"
}
```

**Request (selección directa):**
```json
{
  "moodOption": "sad"
}
```

**Response - Detectado:**
```json
{
  "detected": true,
  "mood": "happy",
  "moodId": 1,
  "displayName": "Feliz",
  "emoji": "😄"
}
```

**Response - No detectado:**
```json
{
  "detected": false,
  "message": "No se pudo detectar tu estado de ánimo..."
}
```

---

### **Endpoints PLAYLISTS**

#### `GET /playlist/{moodName}`
Obtiene playlist de un mood (15 canciones).

**Ejemplo:**
```
GET /api/playlist/happy
```

**Response:**
```json
{
  "mood": "happy",
  "displayName": "Feliz",
  "emoji": "😄",
  "color": "#FFD93D",
  "tracks": [
    {
      "id": 1,
      "youtubeId": "I35paFqFOPk",
      "title": "Happy Music Mix",
      "artist": "Happy Music Channel",
      "thumbnailUrl": "https://i.ytimg.com/vi/.../hqdefault.jpg",
      "externalUrl": "https://music.youtube.com/watch?v=I35paFqFOPk"
    }
    // ... 14 más
  ]
}
```

---

#### `POST /playlist/{moodName}/refresh`
Actualiza playlist con canciones nuevas de YouTube.

**Ejemplo:**
```
POST /api/playlist/happy/refresh
```

**Response:**
```json
{
  "message": "Playlist actualizada con 15 canciones",
  "count": 15
}
```

**Proceso:** Elimina canciones antiguas → Busca 15 nuevas en YouTube → Guarda en BD

**Tiempo:** 2-4 segundos

---

## 🗄️ Modelo de Datos
```
MOOD (1) ──(1:N)──> MOOD_TRACK <──(N:1)── TRACK
```

**3 tablas:**
- `moods`: 4 estados de ánimo (happy, sad, energetic, chill)
- `tracks`: Canciones de YouTube
- `mood_tracks`: Relación N:M con posiciones 1-15

---

## 🔑 YouTube API Key

1. [Google Cloud Console](https://console.cloud.google.com/)
2. Crear proyecto → Habilitar **YouTube Data API v3**
3. Credenciales → Crear clave de API
4. Copiar a `application.properties`

**Cuota:** 10,000 unidades/día (~100 búsquedas/día gratis)

---

## 📊 Estructura del Proyecto
```
src/main/java/com/moodsound/backend/
├── controller/     (2) MoodController, PlaylistController
├── model/          (3) Mood, Track, MoodTrack
├── repository/     (3) Interfaces JPA
├── service/        (3) MoodService, TrackService, YouTubeService
├── response/       (4) Clases DTO
└── youtube/        (2) DTOs YouTube API
```

**Total:** 18 archivos Java (~1.200 líneas)

---

## 🧪 Pruebas
```bash
./mvnw test
```

- **Cobertura:** 85% con JUnit
- **Pruebas:** 12 tests unitarios (todos pasan ✅)

---

## 📝 Notas

- **CORS:** Habilitado para desarrollo (`@CrossOrigin(origins = "*")`)
- **Seguridad:** API Key en variables de entorno en producción
- **Transacciones:** `@Transactional` en operaciones críticas

---

## 👤 Autor

**Jesús Barroso Bonilla**  
Proyecto Final de Ciclo

**Frontend:** [moodsound-frontend](https://github.com/JesusDEV91/moodsound-frontEnd)
