/**
 * Proyecto: Gestor de Tareas Avanzado en Java
 * Autor: Daniel Cobos Freire
 * Descripción: Aplicación de consola con funcionalidades extendidas para gestionar tareas,
 * incluyendo registro, listado, filtrado, eliminación y actualización de estado de las tareas.
 * Se sigue el principio SRP de SOLID.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Clase principal que implementa el menú interactivo.
 */
public class Dashboard {

    /**
     * Clase interna que representa una tarea en el sistema.
     */
    static class Tarea {
        private String titulo;
        private Estado estado;

        /**
         * Constructor de la clase Tarea.
         * @param titulo Título o descripción breve de la tarea.
         */
        public Tarea(String titulo) {
            this.titulo = titulo;
            this.estado = Estado.PENDIENTE;
        }

        public String getTitulo() {
            return titulo;
        }

        public Estado getEstado() {
            return estado;
        }

        public void setEstado(Estado estado) {
            this.estado = estado;
        }

        @Override
        public String toString() {
            return "Tarea: " + titulo + " [" + estado + "]";
        }
    }

    /**
     * Enum que define los posibles estados de una tarea.
     */
    enum Estado {
        PENDIENTE,
        EN_PROGRESO,
        COMPLETADA
    }

    /**
     * Servicio interno para manejar la lógica de negocio sobre las tareas.
     */
    static class TareaServicio {
        private List<Tarea> tareas;

        public TareaServicio() {
            this.tareas = new ArrayList<>();
        }

        /**
         * Agrega una tarea al listado.
         * @param tarea La tarea a agregar.
         */
        public void agregarTarea(Tarea tarea) {
            tareas.add(tarea);
        }

        /**
         * Lista todas las tareas registradas.
         */
        public void listarTareas() {
            if (tareas.isEmpty()) {
                System.out.println("No hay tareas registradas por el momento.");
            } else {
                System.out.println("Listado de tareas:");
                for (int i = 0; i < tareas.size(); i++) {
                    System.out.println((i+1) + ". " + tareas.get(i));
                }
            }
        }

        /**
         * Elimina una tarea por índice.
         * @param indice El índice de la tarea.
         * @return true si la eliminación fue exitosa.
         */
        public boolean eliminarTarea(int indice) {
            if (indice >= 0 && indice < tareas.size()) {
                tareas.remove(indice);
                return true;
            }
            return false;
        }

        /**
         * Cambia el estado de una tarea.
         * @param indice El índice de la tarea.
         * @param nuevoEstado Nuevo estado a asignar.
         * @return true si la operación fue exitosa.
         */
        public boolean actualizarEstado(int indice, Estado nuevoEstado) {
            if (indice >= 0 && indice < tareas.size()) {
                tareas.get(indice).setEstado(nuevoEstado);
                return true;
            }
            return false;
        }

        /**
         * Lista las tareas filtradas por estado.
         * @param estado El estado por el que filtrar.
         */
        public void listarPorEstado(Estado estado) {
            List<Tarea> filtradas = tareas.stream()
                                           .filter(t -> t.getEstado() == estado)
                                           .collect(Collectors.toList());
            if (filtradas.isEmpty()) {
                System.out.println("No hay tareas con estado: " + estado);
            } else {
                System.out.println("Tareas con estado " + estado + ":");
                for (Tarea t : filtradas) {
                    System.out.println(t);
                }
            }
        }
    }

    private TareaServicio tareaServicio;
    private Scanner scanner;

    public Dashboard() {
        this.tareaServicio = new TareaServicio();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra el menú principal e invoca las funcionalidades correspondientes.
     */
    public void mostrarMenu() {
        System.out.println("=================================");
        System.out.println("Bienvenido al Gestor de Tareas Avanzado");
        System.out.println("Autor: Daniel Cobos Freire");
        System.out.println("=================================");

        int opcion = -1;

        while (opcion != 0) {
            mostrarOpciones();
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    registrarTarea();
                    break;
                case 2:
                    tareaServicio.listarTareas();
                    break;
                case 3:
                    eliminarTarea();
                    break;
                case 4:
                    actualizarEstadoTarea();
                    break;
                case 5:
                    filtrarPorEstado();
                    break;
                case 0:
                    System.out.println("Gracias por usar el Gestor. Hasta la próxima.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    /**
     * Muestra el listado de opciones del menú.
     */
    private void mostrarOpciones() {
        System.out.println("\nSeleccione una opción:");
        System.out.println("1. Registrar nueva tarea");
        System.out.println("2. Mostrar todas las tareas");
        System.out.println("3. Eliminar tarea");
        System.out.println("4. Cambiar estado de una tarea");
        System.out.println("5. Ver tareas por estado");
        System.out.println("0. Salir");
    }

    /**
     * Permite al usuario registrar una nueva tarea.
     */
    private void registrarTarea() {
        System.out.println("Ingrese el título de la tarea:");
        String titulo = scanner.nextLine();
        Tarea tarea = new Tarea(titulo);
        tareaServicio.agregarTarea(tarea);
        System.out.println("Tarea registrada exitosamente.");
    }

    /**
     * Permite al usuario eliminar una tarea por su índice.
     */
    private void eliminarTarea() {
        tareaServicio.listarTareas();
        System.out.println("Ingrese el número de la tarea a eliminar:");
        try {
            int numero = Integer.parseInt(scanner.nextLine()) - 1;
            if (tareaServicio.eliminarTarea(numero)) {
                System.out.println("Tarea eliminada.");
            } else {
                System.out.println("Número inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    /**
     * Permite cambiar el estado de una tarea.
     */
    private void actualizarEstadoTarea() {
        tareaServicio.listarTareas();
        System.out.println("Ingrese el número de la tarea a actualizar:");
        int numero;
        try {
            numero = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
            return;
        }

        System.out.println("Seleccione nuevo estado:");
        System.out.println("1. Pendiente");
        System.out.println("2. En progreso");
        System.out.println("3. Completada");

        int estadoInt;
        try {
            estadoInt = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Estado inválido.");
            return;
        }

        Estado nuevoEstado;
        switch (estadoInt) {
            case 1:
                nuevoEstado = Estado.PENDIENTE;
                break;
            case 2:
                nuevoEstado = Estado.EN_PROGRESO;
                break;
            case 3:
                nuevoEstado = Estado.COMPLETADA;
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        if (tareaServicio.actualizarEstado(numero, nuevoEstado)) {
            System.out.println("Estado actualizado.");
        } else {
            System.out.println("No se pudo actualizar.");
        }
    }

    /**
     * Permite ver tareas filtradas por estado.
     */
    private void filtrarPorEstado() {
        System.out.println("Seleccione estado para filtrar:");
        System.out.println("1. Pendiente");
        System.out.println("2. En progreso");
        System.out.println("3. Completada");

        int estadoInt;
        try {
            estadoInt = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        Estado estado;
        switch (estadoInt) {
            case 1:
                estado = Estado.PENDIENTE;
                break;
            case 2:
                estado = Estado.EN_PROGRESO;
                break;
            case 3:
                estado = Estado.COMPLETADA;
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        tareaServicio.listarPorEstado(estado);
    }

    /**
     * Método principal para ejecutar la aplicación.
     */
    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard();
        dashboard.mostrarMenu();
    }
}
