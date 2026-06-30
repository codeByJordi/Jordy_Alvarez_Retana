# Caso Práctico #1 — EventApp

## Información general

| Dato | Valor |
|------|-------|
| Curso | SC-403 Desarrollo de Aplicaciones Web y Patrones |
| Universidad | Fidélitas |
| Modalidad | Individual, sincrónico durante la clase con extensión definida por el profesor |
| Valor | **15%** de la nota final |
| Duración en clase | 3 horas |
| Fecha de entrega | La indica el profesor en la sesión presencial |
| Material entregado | `eventapp.zip` (proyecto base) + este documento |

---

## Contexto del caso

Sos parte de un equipo que desarrolla **EventApp**, un sistema interno para gestionar eventos públicos (conciertos, talleres, conferencias, ferias). La versión actual del proyecto solo permite **listar y ver el detalle** de los eventos cargados en la base de datos. Te toca completar el sistema agregando las funcionalidades que faltan.

El proyecto base ya tiene:

- Entidad `Evento` con todos los campos.
- Repositorio extendido y `Service` con métodos básicos.
- Endpoints `GET /eventos` y `GET /eventos/{id}` funcionando.
- Vistas Thymeleaf con Bootstrap (listado básico + detalle).
- Script `seed-data.sql` con 10 eventos de ejemplo.

Tu trabajo es **terminar las funcionalidades del CRUD** aplicando todo lo aprendido en las Clases 1 a 6.

---

## Lo que tenés que entregar

### 1. Implementación funcional

Debe cumplir todos los **requisitos obligatorios** (ver siguiente sección).

### 2. Código fuente subido al repositorio personal

En tu repo personal del curso, en una carpeta `Caso_Practico_1/eventapp/`.

### 3. Capturas de pantalla (carpeta `Caso_Practico_1/evidencia/`)

Mínimo:

- `01-listado.png` — listado de eventos con los botones de acción.
- `02-form-crear.png` — formulario "Nuevo evento" lleno antes de guardar.
- `03-validaciones.png` — formulario con errores de validación visibles.
- `04-modal-eliminar.png` — modal de confirmación abierto sobre un evento.
- `05-filtro.png` — listado filtrado por categoría (endpoint paramétrico).

### 4. README de lógica (`Caso_Practico_1/SOLUCION.md`)

Documento corto (1 a 2 páginas) explicando con tus palabras:

- Qué endpoints implementaste y qué hace cada uno.
- Por qué elegiste las anotaciones de validación que pusiste en `Evento`.
- Cómo resolviste el modal de confirmación (qué atributos `data-bs-*` usaste).
- Cualquier decisión técnica que hayas tomado (ej: usaste `LocalDate`, agregaste un método util, etc.).

**No copies código aquí.** Explicalo en prosa. Si en la calificación parece autogenerado, vale 0.

---

## Requisitos obligatorios

### R1. Configuración inicial

- [ ] Copiar `eventapp/` dentro de tu repo personal en `Caso_Practico_1/`.
- [ ] Crear la base `eventappdb` y configurar `DB_PASSWORD` con `setx`.
- [ ] Verificar que la app arranca con cualquiera de las dos opciones del README:
  - **Opción A:** clic en el botón **Run (▷)** sobre `EventappApplication.java` desde VS Code.
  - **Opción B:** `./mvnw spring-boot:run` (Linux/Mac) o `.\mvnw.cmd spring-boot:run` (Windows).
- [ ] Ejecutar `seed-data.sql` y comprobar que `/eventos` muestra los 10 eventos.

### R2. Validaciones en la entidad Evento

- [ ] Agregar al `pom.xml` la dependencia `spring-boot-starter-validation`.
- [ ] Anotar los campos de `Evento` con las validaciones que consideres adecuadas para el dominio (mínimo: nombre obligatorio, fecha futura, cupo máximo > 0, precio >= 0, longitudes razonables).
- [ ] En el `EventoController`, los métodos POST deben usar `@Valid` + `BindingResult`.

> **Documentación oficial de anotaciones de Bean Validation:**
> https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html#builtinconstraints
>
> Las más usadas: `@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Max`, `@Positive`, `@PositiveOrZero`, `@Future`, `@FutureOrPresent`, `@Email`, `@Pattern`.

### R3. Endpoint paramétrico (READ con parámetro)

- [ ] Implementar **uno** de los siguientes endpoints (a elegir):
  - `GET /eventos/categoria/{categoria}` — filtra por categoría (`@PathVariable`).
  - `GET /eventos/buscar?q=texto` — búsqueda por nombre (`@RequestParam`).
  - `GET /eventos/proximos` — eventos con fecha desde hoy en adelante.
- [ ] La vista correspondiente debe reutilizar `eventos.html` o crear una versión modificada.

> **Documentación oficial:**
> - `@PathVariable` y `@RequestParam`: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html
> - Query methods de Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

### R4. CREATE — crear nuevo evento

- [ ] `GET /eventos/nuevo` — muestra un formulario vacío (`eventos/form.html`).
- [ ] `POST /eventos` — recibe el evento, valida con `@Valid`, guarda en BD y redirige a `/eventos`.
- [ ] Si hay errores de validación, debe mostrarlos en el formulario.

> **Documentación oficial:**
> - Formularios en Thymeleaf con `th:object` y `th:field`: https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html#handling-the-command-object
> - `@ModelAttribute` y `BindingResult`: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/modelattrib-method-args.html

### R5. UPDATE — editar evento

- [ ] `GET /eventos/{id}/editar` — formulario precargado con los datos del evento.
- [ ] `POST /eventos/{id}` — actualiza, valida, redirige a `/eventos`.
- [ ] Reutilizar `eventos/form.html` (la misma vista debe servir para CREATE y UPDATE).
- [ ] El form debe tener `<input type="hidden" th:field="*{id}">` para preservar el ID en UPDATE.

### R6. DELETE con modal de confirmación

- [ ] `POST /eventos/{id}/eliminar` — borra el evento y redirige a `/eventos`.
- [ ] En cada card del listado, agregar botón "Eliminar" con `data-bs-toggle="modal"`.
- [ ] Modal de Bootstrap con el nombre del evento a eliminar.
- [ ] **No** usar `confirm()` nativo del navegador — debe ser modal de Bootstrap.

> **Documentación oficial:**
> - Modal de Bootstrap (atributos `data-bs-*` y JavaScript): https://getbootstrap.com/docs/5.3/components/modal/

### R7. Botones y feedback visual

- [ ] Botón "Nuevo evento" arriba del listado.
- [ ] Botones "Ver", "Editar" y "Eliminar" en cada card.

### R8. Workflow Git

- [ ] Mínimo 3 commits con mensajes convencionales (`feat:`, `docs:`, etc.).
- [ ] Push final al repo personal con la estructura `Caso_Practico_1/`.

---

## Rúbrica de evaluación (15%)

La nota se asigna sobre 100 y se multiplica por 0.15 para el porcentaje final.

| # | Criterio | Peso |
|---|----------|:--:|
| 1 | App arranca sin errores, BD configurada con env variable | 5 |
| 2 | Validaciones aplicadas correctamente en `Evento` | 10 |
| 3 | CREATE funcional (form + POST + guardado en BD) | 15 |
| 4 | Errores de validación visibles en el formulario | 10 |
| 5 | UPDATE funcional (form precargado + POST) | 15 |
| 6 | DELETE con modal de Bootstrap | 15 |
| 7 | Endpoint paramétrico funcionando (R3) | 5 |
| 8 | Botones de acción en el listado | 5 |
| 9 | Código organizado en capas MVC | 10 |
| 10 | Workflow Git con commits convencionales | 5 |
| 11 | Push al repo personal con estructura `Caso_Practico_1/` | 5 |

---

## Puntos extra (bonus)

> **Regla de habilitación:** los bonus **solo se evalúan si completaste al menos el 65% de la nota obligatoria** (es decir, al menos 65 puntos sumando los criterios 1 a 11). No tiene sentido sumar bonus sobre un proyecto incompleto: no vale subir una imagen a Firebase y dejar el CRUD sin terminar.
>
> Si cumplís el umbral del 65%, los bonus **sí pueden superar los 100 puntos** y se reflejan en la nota final.

### Bonus +10 — Firebase Storage (imagen de portada)

Permitir subir una **imagen de portada** del evento al crear/editar, almacenada en Firebase Storage. Mostrarla en el listado y en el detalle. Requiere agregar:

- Campo `imagenUrl` en `Evento`.
- `FirebaseService` con `init()` y `subir(MultipartFile)`.
- `firebase-key.json` en resources (NO subir al repo, ya está en el `.gitignore`).
- `enctype="multipart/form-data"` en el form.
- Mostrar imagen con `<img>` cuando el evento tenga `imagenUrl`.

> **Hint:** el procedimiento completo está en la presentación **SEM6** de la Clase 6 (slides 17–19 con setup y subida) y en `guion_clase6.md` (parte E del lab).

### Bonus +5 — Flash messages con `RedirectAttributes`

Mostrar un mensaje de éxito ("Evento guardado", "Evento actualizado", "Evento eliminado") después de las operaciones POST, usando el patrón **PRG (Post-Redirect-Get)**.

Implementación:

- En los métodos POST del controller, agregar `RedirectAttributes ra` como parámetro y usar `ra.addFlashAttribute("ok", "Evento guardado")`.
- En `eventos.html`, agregar un `<div class="alert alert-success" th:if="${ok}" th:text="${ok}"></div>` arriba del listado.
- Opcional: mostrar `${error}` con `alert alert-danger` para errores.

> **Hint:** Flash messages se vio en **Clase 6** — slide 12 de SEM6 ("Flash Messages con RedirectAttributes") y en el proyecto de práctica de esa semana. El patrón es: `POST → addFlashAttribute → redirect → GET → muestra el flash en la vista`.
>
> **Documentación:** https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/support/RedirectAttributes.html

### Bonus chicos

- **+3** Badges visuales por estado (ej: rojo si está lleno, verde si tiene cupos).
- **+3** Búsqueda por nombre como endpoint adicional al de R3.
- **+2** `LocalDate` formateada en español en las vistas.

---

## Penalizaciones automáticas

| Falta | Penalidad |
|-------|-----------|
| Subir contraseña de MySQL al repo | Nota máxima 60 |
| Subir `firebase-key.json` al repo | Nota máxima 50 |
| App no arranca | Los criterios 3–8 se evalúan en 0 |
| Plagio detectable entre estudiantes | **Nota 0 a ambas partes** y reporte a la dirección |
| Código generado con IA sin entender la lógica (detectable por inconsistencias en SOLUCION.md) | Nota máxima 50 |
| Entrega tardía sin justificación | Según reglamento del curso |

---

## Política anti-plagio y uso de IA

Este caso práctico es un **examen individual**. La nota refleja **lo que vos sabés hacer**, no lo que otra persona o herramienta hace por vos.

**No está permitido:**

- Copiar código de otro estudiante.
- Compartir tu código con otro estudiante durante el examen.
- **Usar modelos generativos de IA (ChatGPT, Claude, Copilot, etc.) para resolver el examen completo.** Si los usás como apoyo puntual de consulta (por ejemplo "qué hace `@Future`"), tenés que poder explicar la lógica final en `SOLUCION.md`.
- Pegar el enunciado completo en un modelo y entregar la salida.

**Sí está permitido:**

- Consultar la documentación oficial (Spring, Thymeleaf, Bootstrap).
- Consultar tus notas de clase, tus prácticas anteriores y los proyectos vistos.
- Consultar el material del curso (presentaciones, guiones, lecturas).
- Pedir aclaración al profesor sobre el enunciado.

Durante la corrección se revisa el código con detectores de plagio y se compara con código generado por IA. Si la diferencia entre tu código y el SOLUCION.md sugiere autogeneración, la nota baja al máximo 50.

---

## Cómo entregar

Subí al campus virtual (Moodle), en el espacio designado para esta práctica:

1. **URL del repositorio** completa.
2. **Hash del último commit** (`git log --oneline -1`).

No subir el código ni las capturas al campus. La revisión se hace en GitHub.

Estructura esperada en tu repo:

```
tu-repositorio-del-curso/
├── Clase_1/
├── ...
├── Practica_1/
├── Practica_2/
└── Caso_Practico_1/
    ├── eventapp/                <- proyecto Spring Boot
    │   ├── pom.xml
    │   ├── src/
    │   └── seed-data.sql
    ├── evidencia/
    │   ├── 01-listado.png
    │   ├── 02-form-crear.png
    │   ├── 03-validaciones.png
    │   ├── 04-modal-eliminar.png
    │   └── 05-filtro.png
    └── SOLUCION.md              <- explicación de la lógica
```

---

## Problemas comunes

| Síntoma | Solución |
|---------|----------|
| `Access denied for user 'root'` | Verificar `DB_PASSWORD` con `echo $env:DB_PASSWORD` en terminal nueva. |
| `Unknown database 'eventappdb'` | Crear la BD con el SQL del paso 1 del README. |
| `Table 'eventappdb.eventos' doesn't exist` al cargar seed | Arrancaste el seed antes que la app. Arrancar primero la app desde VS Code o con `mvnw` para que Hibernate cree la tabla. |
| El botón Run de VS Code no aparece | Esperá a que la extensión "Extension Pack for Java" termine de indexar el proyecto. Tarda 1-2 min la primera vez. |
| Validaciones no se ejecutan | Falta `spring-boot-starter-validation` en `pom.xml` o `@Valid` en el controller. |
| `BindingResult` no funciona | Va INMEDIATAMENTE después de `@Valid @ModelAttribute`. |
| UPDATE crea duplicado en vez de actualizar | Falta `<input type="hidden" th:field="*{id}">` en el form. |
| Modal abre vacío | Falta el `<script>` que lee `data-id` del botón y lo inyecta en el form de borrado. |
| Captura de pantalla muestra `application-local.properties` en el repo | Quitarlo del repo, verificar `.gitignore`, hacer nuevo commit. |
| `./mvnw: Permission denied` en Linux/Mac | `chmod +x mvnw` y volver a intentar. |

---

## Recursos permitidos

- Tu repo personal con Practica 1, Practica 2, y los ejemplos de clase.
- Material del aula virtual (presentaciones, guiones, lecturas).
- Documentación oficial:
  - Spring Boot: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
  - Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
  - Bean Validation: https://jakarta.ee/specifications/bean-validation/3.0/
  - Thymeleaf: https://www.thymeleaf.org/documentation.html
  - Bootstrap 5: https://getbootstrap.com/docs/5.3
  - Firebase Admin Java: https://firebase.google.com/docs/admin/setup
