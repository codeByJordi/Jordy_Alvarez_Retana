En total implemente 6 metodos con sus debidos endpoints


//Para buscar 1
    @GetMapping("/buscar")
    public String buscar(Model model, @RequestParam(required = false) String buscar) {
        if(buscar != null && !buscar.isBlank()) {
            model.addAttribute("eventos", service.buscarPorNombreContainingIgnoreCase(buscar));
        }
        else {
            model.addAttribute("eventos", service.listar());
        }
        return "eventos";
    }
Su funcion es la de buscar por medio de un form con un input tipo search. Este toma el string que un usuario pone en la barra de buscar, luego con un metodo implementado en el repository
que se llama List<Evento> findByNombreContainingIgnoreCase(String texto), lo buscara, el containing ignore case hace que no importa si es un minucula, mayuscula, o simplemente una palabra medianamente relacionable, este metodo la buscara en la base de datos y devolvera todos esos eventos en forma de lista. Una vez los obtiene, con el modelo, y el nombre de atributo 'eventos', el
cual debe ser igual al que se usa en el public String listar(Model model) ya que el objecto usado en el html tiene ese nombre. Es como la etiqueta que liga la lista de java con los html.
Importante mencionar que para que el 'String buscar' obtenga ese nombre o busqueda, se requiere un RequestParam para recogerlo y asi almacenarlo en la variable String

Problemas que tuve fueron que el nombre de la variable relacionada con el RequestParam debe ser el mismo que el 'name' puesto en input de search. Le tenia un nombre diferente y por ende, no obtenia el dato. El segundo problema fue que utilizaba la url '/eventos/buscar?q=texto' la cual se dublicaba, y terminaba siendo '/eventos/buscar?q=texto/eventos/buscar?q=texto', cambie el nombre por '/buscar' y ya funciono. 
 

    //Para crea un nuevo evento 2
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("evento", new Evento());
        return "eventos/form";
    }
Este endpoint es simplemente para lanzar la vista del formulario, se usa un model para crear el vinculo y asi obtener el objeto 'evento' y usarlo en el form del html el cual debe tener todos los atributos de ese objeto y asi poder settearlo y enviarlo al postmapping para se guardado.


    @PostMapping 3
    public String guardar(@Valid @ModelAttribute("evento") Evento evento, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "eventos/form";
        }
        service.guardar(evento);
        ra.addFlashAttribute("ok", "Evento guardado correctamente");
        return "redirect:/eventos";
    }
Usamos el @Valid para las validaciones de los atributos que creamos en la clase evento, el @ModelAttribute es el objeto que se creo en el form y aqui lo estamos obteniendo mediante esa anotacion y almacenando en Evento evento. El BindingResult verifica si las validaciones son correctas y el RedirectAttributes es para crear un pequeno mensaje al terminar de guardar.
Con el if verificamos si hay errores o no, si los encuentra, nos devuelve al form anterior. Si todo sale bien, con el service guardmos el nuevo evento, el cual lo almacena en la base de datos. Luego nos redirige a la url de eventos

Errores que tuve fueron que no coloque todos los atributos entonces quedaban en null y el objeto incompleto, entonces al intentar guardarla en la base de datos, no lo hacia. Por ende, al crear un objeto en con un form, y uno de los atributos de este no se pone, pero es necesario para la base de datos, se debe hacer un set de ese atributo antes de guardarlo, si no, no funcionara.




   //Para editar eventos 4
    @GetMapping("/{id}/editar")
    public String eventoEditar(@PathVariable Long id, Model model) {
        Evento evento = service.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "eventos/form";
    }
Se reutiliza el mismo html de form, pero desde el eventos.html y con un (id = ${e.id}) para obtener el id exacto de ese evento, y con el @PathVariable lo obtenemos en el .java, una vez obtenido, usamos la clase evento para almacenar el evento extraido el service, este service busca el evento con base al ID que obtuvimos previamente. Luego con el model creamos un vinculo entre el .java y el form.html, entonces los campos del form se rellenan con base en objeto



    @PostMapping("/{id}") 5
    public String actualizar(@Valid @ModelAttribute("evento") Evento evento, BindingResult result, @PathVariable Long id, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "eventos/form";
        }
        evento.setId(id);
        service.guardar(evento);
        ra.addFlashAttribute("ok", "Evento guardado correctamente");
        return "redirect:/eventos";
    }
Luego de editar el objeto, se obtiene como si fuera un nuevo objeto al guardar, y sigue el mismo procedimiento que al crearlo, pero esta vez y gracias al con un if en el th:method enviamos el id y lo obtenemos con el @PathVariable, entonces antes de almacenar, simplemente setteamos el objeto con la vieja id y asi se conserva con esa y no crea un objeto dublicado en la base de datos.


   //Para eliminar 6
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("ok", "Evento eliminado correctamente");
        return "redirect:/eventos";
    }

Por ultimo para eliminar objectos, desde el eventos.html obtenemos la id del evento a eliminar, luego con el service y un metodo eliminar de la base de datos que elimina por medio del id, este sera eliminado y luego al redirigirnos a eventos, este ya no saldra porque la base ya no lo tiene almacenado.


- Por qué elegiste las anotaciones de validación que pusiste en `Evento`.

Nombre: use @NotBlank para que el usuarios no deje el campo sin llenar, y @Size para que tuviera minimo un caracter y maximo 50 ya que el nombre seria muy extenso

Descripcion: use un @Size con un minimo de 0 ya que no lo encuentro como un campo obligatorio de llenar, y un maximo de 500 caracteres para que vaya ligado al mismo que la base de datos le permite

Fecha: @NotNull para que se deba llenar y no quede vacio, @DateTimeFormart para que el patron sea dia, mes y annio. Y el @Future para que no se permitan fecha anteriores al dia actual.

Lugar: @NotBlack para que se deba agregar un lugar, me falto el un @Size o @Minimo para que no sea unico caracter

Categoria: @NotBlack y lo mismo con Lugar

Organizador: @NotBlack y lo mismo que con lugar y Categoria

CupoMaximo: @Min para sea obligatoria que tenga al menos un cupo

CuposVendios: @Min y @Max, el minimo es 0, y con un maximo deacuerdo al maximo de cupoMaximo

Precio: @Min y @Max con el minimo de 0 ya que puede ser gratis y el maximo de 10000

- Cómo resolviste el modal de confirmación (qué atributos `data-bs-*` usaste).

data-bs-toggle="modal" para indicar que modal

data-bs-dismiss="modal" para los botones de cerrar o cancelar la operacion de eliminar

data-bs-target="#modalEliminar" para senalar al modal con el que se comparte el id entre el boton en flash card

th:data-id="${e.id}" para obtener el id del evento y luego pasarlo al script

th:data-nombre="${e.nombre}" para obtener el nombre del evento y pasarlo al script en la part de getAttribute

getElementById('modalEliminar') dentro del script para senalar al div con ese id, es decir al modal

document.getElementById('formEliminar').action para realizar el th:action dentro del form, el cual obtiene el id por medio de  const id = boton.getAttribute('data-id') que proviene del th:data-id