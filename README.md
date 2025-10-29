}# Pedidos Yapo

**Evaluación Parcial 2 – Encargo**  
**Asignatura:** DSY1105 – Desarrollo de Aplicaciones Móviles  
**Institución:** Duoc UC

---

## Autores
- **Fabián Córdova**
- **Vicente Sánchez**

---

## Descripción del Proyecto
**Pedidos Yapo** es una **aplicación móvil Android** desarrollada en **Kotlin + Jetpack Compose + Room** que permite **gestionar pedidos de comida** desde distintos restaurantes.  
Incluye **validación de formularios**, **persistencia local** y **diseño Material 3** con arquitectura **MVVM**.

---

## Características principales
- 📦 **Catálogo** de restaurantes/productos (mock/local).
- 🛒 **Carrito de compras** con totales y validaciones.
- ✅ **Checkout** con formularios validados y feedback visual.
- 💾 **Persistencia local** con Room (SQLite).
- 🎨 **UI** en Jetpack Compose (Material 3, theming).
- 🧩 **Arquitectura MVVM** (ViewModel + LiveData/State).
- ⚙️ **Coroutines** para operaciones asíncronas.

---

## Requisitos de entorno
- **Android Studio**: *Giraffe/Narwhal o superior*  
- **JDK**: 17  
- **SDK mínimo**: 24 (Android 7.0)  
- **Compile SDK**: 34  
- **Gradle/AGP**: (según `gradle.properties` / `build.gradle` del proyecto)

---

## Tecnologías utilizadas
- **Kotlin**, **Coroutines**
- **Jetpack Compose** (Material 3)
- **Room Database** (persistencia local)
- **ViewModel** + **LiveData/State**
- **Arquitectura MVVM**

---

## Estructura (resumen)

app/
├─ data/
│ ├─ local/ # Entities, DAO, RoomDatabase
│ └─ repository/ # Repositorios
├─ domain/ # Modelos de dominio (si aplica)
├─ ui/
│ ├─ screens/ # Pantallas Compose
│ ├─ components/ # Componentes reutilizables
│ └─ theme/ # Theming M3
├─ viewmodel/ # ViewModels
└─ ...


---

## Instrucciones para ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/faviancordovaurra/PedidosYapo.git
   cd PedidosYapo
Abrir en Android Studio

File → Open… y selecciona la carpeta del proyecto.

Espera la sincronización de Gradle.

Configurar SDK/Emulador

Asegura SDK 34 instalado.

Crea un AVD (Android 13/14) o usa un dispositivo físico con Android ≥ 7.0.

Ejecutar

Run ▶ sobre el módulo app.

Verifica que la app levante el catálogo, carrito y checkout.

Pruebas rápidas (sugeridas)

Smoke test del flujo: Catálogo → Detalle → Agregar al Carrito → Checkout → Confirmación.

Validar reintentos y mensajes de error en formularios (campos vacíos, formatos inválidos).

Roadmap breve

v1.0 (actual): MVP móvil con catálogo, carrito y checkout local.

v1.1: Estados de pedido (Preparando/En camino/Entregado), mejoras de accesibilidad.

v2.0 (Web): Migración/Adaptación a app web (catálogo, carrito, checkout, confirmación) y panel simple para restaurante.

Notas

Este proyecto está enfocado en experiencia de usuario y validaciones locales; la integración con API remota y pasarela de pagos queda como trabajo futuro.

Para la versión Web, se recomienda SPA (React/Vite) o HTML/CSS/JS con enrutamiento y estado centralizado.
