La app crea bibliotecaria1 y lector con el commandliner, para tener el cuenta los usuarios que se crean en la base de datos, ya que los que traia en un inicio, no funcionaban y las contrasenas no eran las correspondientes. Entonces, ya app crea estos usuarios al iniciarse: bibliotecaria1 | password123 y lector | password123

- Cómo modelaste la relación `Prestamo → Libro` y por qué.

    @NotNull(message = "No puede ser null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libroid", nullable = false)
    private Libro libro;

    @NotNull(message = "Usuario obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioid", nullable = false)
    private Usuario usuario;

Bueno, empezado por la relación "many to one", esto es debido a las reglas de normalización y el modelo relacional. En este caso, un libro puede tener varios prestamos, no necesariamente activos, pero un unico prestamo, el cual solo se crea por libro, crea la relación muchos (prestamos) a uno (libro). En java, manytoOne va en la entidad muchos, sobre el objeto o entidad uno. Asimismo, la misma relación ocurre con Usuario, el prestamo es por un usuario, pero el usuario puede tener varios de estos. Ahora bien, usé el FetchType.LAZY, ya que con esta, solo se solucita la entidad uno cuando se llame. Ya de ultimo, el @JoinColumn es para crear la columna que lleva la llave foranea de usuario a la tabla muchos (prestamos).

    @Query("SELECT u FROM Prestamo u JOIN FETCH u.usuario")
    List<Prestamo> findAllConUsuario();

    @Query("SELECT u FROM Prestamo u JOIN FETCH u.libro WHERE u.id = :id") 
    Optional<Prestamo> findConLibro(Long id);

En la parte del repositorio, creé los siguientes metodos con @Query, este es para crear statements más especificas que las que nos da el JPA e Hibernate, el primero nos trae a los usuarios junto a los prestamos. El segundo hace lo mismo, pero con un unico y especifico libro, para obtener ese especifico y unico y no nos de error, de usa el 'WHERE' y un ':id'.

- Qué hace cada `@PreAuthorize` que agregaste y por qué esa regla y no otra.

Primero que nada, en esta parte necesité modificar la clase 'SecurityConfig', anadiendo @EnableMethodSecurity y @EnableWebSecurity, si no recuerdo mal, esta clase solo contaba con @Configuration en un inicio. Luego, en el filter chain, usando 'requestMatchers', la cual es para especificar cuales URLs estan permitidas dependiendo el usuario, está logueado o no, en esta establecí que zonas del sitio web son accesibles para los usuarios sin logueo, eso con el 'permitAll'.

Ahora bien, en la parte de los controllers.

    @GetMapping("/prestamos")
        public String listar(Model model,  Authentication authentication)

Este no lleva ninguna especificación @PreAuthorize, pero sí un Authentication, esto para crear un if dependiendo del usuario, para que puedan ver sus prestamos, esta interface (o objecto que se crea) obtiene los datos del usuario autenticado en el sitio, y nos permite obtener su rol, sea LECTOR o BIBLIOTERCARIO. Una cosa es que existe la nota @AuthenticationPrincipal, el cual nos permite algo similar de obtener los datos del usuario, esa nota junto con el UserDetails, probe ambas, y creo ambas están bien, pero no sé en sí cual es mejor y sus diferencias.

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @GetMapping("/nuevo/{id}")
    public String mostrarCrearPrestamo(Model modelo, @PathVariable Long id)

Según el caso, unicamente el bibliotecario puede crear los prestamos, por ente, el @PreAuthorize hace que ningun rol LECTOR sea capaz de acceder a esta URL, ya que este verifica el rol de cada usuario logueado o no en el sitio. Y para que el sitio no se caiga al codigo detectar que no es del rol adecuado, se crea un 'exceptionHandling' en el metodo filterChain de SecurityConfig, el cual redirige al usuario con el rol Lector a un URL diferente, como lo seria el 403.html, que muestra un mensaje que advertencia que acceso denegado.

Otra cosa, para ocultar que salgan los botones, link y forms, para un rol especifico en el html, usé el sec:authorize="hasRole('')", con este especifico que pueden ver o no los roles en la pag, pero eso no significa que no puedan acceder mediante el URL, por ende, usuar el @PreAuthorize provoca que un rol no especificado, no acceda.

Nota: dejé el <a 'sec:authorize="hasRole('')"' th:href="@{/p/nuevo/{id} (id=${libro.id})}" class="btn btn-primary">Realizar prestamo</a> sin el 'sec' en el html, para que se pueda probar que un usario con rol LECTOR, no puede acceder al form de crear, esto para hacerlo más rapido sin tener que escribir en el URL, hehe.

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping("/nuevo/post")
    public String guardar(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
                          BindingResult result, Model modelo)
Usar un @PreAuthorize("hasRole('BIBLIOTECARIO')") en este POST en completamente necesario así no puedan forzar el crear un nuevo prestamo sin ser del rol BIBLIOTECARIO. Usuando el postman y con un json para crear un usuario se muestra mejor como un usuario puede acceder a crear un prestamo, ya que acceder a una URL post es más de transición al crear o modificar el sitio.

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
        @PostMapping("/prestamos/{id}/devolucion")
        public String devolucion(@PathVariable Long id)

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
        @PostMapping("/prestamos/{id}/eliminar")
        public String eliminar(@PathVariable Long id)
Al igual que las otras, este @PreAuthorize es necesario para impedir que un rol no especificado como BIBLIOTECARIO tenga acceso a estos endpoint y pueda provocar el eliminar un prestamo o crear la devolución.

Por otra parte, en libroController, se uso la misma logica de que solo el rol BIBLIOTECARIO pueda crear, editar, actualizar o eliminar un libro.
En sí @PreAuthorize crea una capa sobre el URL, en el cual verifica el rol de cada usuario, y dependiendo de este, la URL será accesible para él o no, ya luego cosas como el SEC son más que nada esteticas para ocultar herramientas en el sitio, pero sin una protección más especifica como @PreAuthorize y el filterChain con los requestMatchers, es muy sencillo acceder a estas por medio de las URLs.

- Cómo escribiste tu propia consulta JPQL del Requisito 5.3

    @Query("Select p From Prestamo p Where p.fechaDevolucion is NULL AND p.fechaLimite < CURRENT_DATE ") 
    List<Prestamo> prestamosAtrasados();
Como ya mencioné el @Query nos permite crear consultas más espeficicas desde Java a la base de datos. Entonces, usuando el SELECT obtenemos los datos que conforman 'p' (Prestamo), es como el '*' para obtener los datos de todas columnas en una tabla de SQL. Luego, usando 'WHERE' para especificar la caracteriticas que deben de tener esos datos, usamos un 'p.fechaDevolucion', esto especificar el atributo de devolución el cual debe ser NULL (nulo), ya que si se retrasó, no debe de existir una fecha de devolución registrada. Por ultimo, obteniendo el atributo 'fechaLimite', la cual es la fecha donde se realizó el prestamos más 14 días, debe de ser menor a la fecha actual obtenida por CURRENT_DATE, por ejemplo, fecha limite es '8 de noviembre', pero CURRENT_DATE marca que ya es '9 de noviembre', entonces, quiere decir que esta fechalimite ya se pasó porque es menor. Es obligatiorio que estas dos caracteristicas se deban cumplir juntas para poder obtener los prestamos atrasados, los cuales se almacenar en un array 'List' especifico de la clase Prestamo en java.

- Qué endpoints de tu API implementaste y qué código de estado devuelve cada uno.

    @GetMapping
    public List<Libro> listarLibros() {
        return libroService.listar();
    }
Usando el libroService listar, nos devuelve todos los libros almacendos en la base de datos. En sí al realizarse, responde con un 200 OK request successful

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarLibroPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
Dentro de la URL /api/libros/{id}, usando el ResponseEntity para devolver el tipo de respuesta, o sea, el codigo o caso de esta, en este caso, al obtener la 'Id' mediante el '@PathVariable', y usar el service para buscar el libro con cuyo 'Id', si se encuentra en la base, este será mostrado y el ResponseEntity nos devuelve un 'OK', en cual seria el codigo 200. Si este no existe en la base de datos, el Response envia un .NotFound 404. Estos se marcan con el ::ok y .notFound

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody Libro libro) {
        Libro guardarLibro = libroService.guardar(libro);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(guardarLibro.getId()).toUri();
        return ResponseEntity.created(location).body(guardarLibro);
    }
En este endpoint el usuario debe tener el rol BIBLIOTECARIO para poder crear libros, ya que no implemente JWT, para poder acceder con este rol, se ocupan las cookies generadas al ingresar con un usuario del rol correspondiente, al crear un libro mediante este, si se es del rol autorizado, nos dará como resultado un codigo 201 de CREATED. EL URI genera el url, tomando la URL actual donde se ubica la petición ("/api/libros") con el ID asociado al nuevo libro creado, y el ResponseEntity, si todo salió bien, nos senala esto, tambien nos devuelve el body el cual seria el libro que creamos ubicado en su URL.

Ojo, cuando creé un nuevo libro por medio de este endpoint, anadi el id y esto me lanzó error debido a que el Entity Libro genera estás con @GeneratedValue(strategy = GenerationType.IDENTITY) de manera automatica.

    @GetMapping("/atrasados")
    public List<Prestamo> prestamosAtrasados() {
        return prestamoService.atrasados();
    }
El PrestamoRestController usuando el autowire para iyectar el PrestamoService y asi obtener el metodo atrasados(), el cual nos devulve una lista de los prestamos pasados de la fecha de limite y que no han sido devueltos, este devuelve un codigo 200 OK.

- Cualquier decisión técnica adicional

  prestamo.setFechaLimite(prestamo.getFechaPrestamo().plusDays(14)); ---> en PrestamoController, en el endpoint Post de guardarPrestamo

Para la creación de la fecha limite, en un principio planeé que hubiera un form donde el bibliotecario eligiera esta, al final, todo eso lo deje para que se hicera de manera automatica, en este caso, al ser un objeto LocalDate al igual que FechaPrestamo (está si es manual en la vista, para que sea el bibliotecario quien decida cuando se genera esa fecha, pero esta no permite que sea de una fecha anterior a la actual, solo presente y futuro), usando el metodo plusDays() en la que se realizo el prestamo, se crea la fecha cuando se debe de devolver, esta se almacena en el atributo FechaLimite.

    public String eliminar(@PathVariable Long id) {
            Libro libro = libroService.buscarPorId(id).orElse(null);
            prestamoService.deleteLibro(libro);
            libroService.eliminar(id);
            return "redirect:/libros";
        }
    
    @Transactional --> Esto es en el PrestamoService
    public void  deleteLibro(Libro libro){
        prestamoRepository.deleteByLibro(libro);
    }

A la hora de eliminar, si bien, se puede eliminar un libro, si este ya fue prestado, se hace un constraint (no recuerdo realmente como se llama) de llave foranea en la tabla de Prestamos, por ende, nos impide borranlo sin primero tener que eliminar el prestamo asociado a su id, entonces anadi el prestamoService al metodo, esto antes de eliminar al libro, ya que primero los prestamos asociados deben de ser eliminados, esto genera un error lo impide, ya que un id de libro puede estar varios prestamos entonces se deben eliminar todos ellos, para poder realizar esto se ocupa la anotación @Transactional que nos permite que todos esos datos se eliminen de una. Ya luego solo que el metodo eliminar del libroService lo haga en la base datos.

Todos estan en endpoint guardarLibro:
    modelo.addAttribute("libro", prestamo.getLibro());

Lo usé para mostrar el titulo del libro, el libro se obtuvo por medio del id, esto ya que para crear el prestamo, se debe estar en la URL del libro, y este, obtenemos el id por medio de (id=${libro.id}) antes de movernos al endpoint de crear, y ya luego se almacenó en el prestamo.

    modelo.addAttribute("usuarios", usuarioService.buscarTodos());
Esto es para mostrar una lista de los usuarios son un select option del html.

        if (result.hasErrors()) {
            modelo.addAttribute("libro", prestamo.getLibro());
            return "prestamos/form";
        }
Si ocurre un error en el post, nos devuelve al form, de ahí me salia un error por falta del libro, entonces, usando model el error no sucede ya los datos del libro de mantiene.

Otro punto a anotar es que al intentar iniciar la app, la password no coincide con los usuarios ya creados, por ende, implementé la interface de CommandLineRunner en el main, esto hace que se ejecute codigo, por medio de un metodo heredado, en la terminal. Y con if (usuarioRepository.count() == 0 && !usuarioRepository.findByUsername("biblotecario1").isPresent() && !usuarioRepository.findByUsername("lector").isPresent()) se crean los usuarios la primera vez que se inicia la app.
