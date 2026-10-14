import java.util.Scanner;

/**
 * Interfaz de consola. Solo interactúa con el usuario; la lógica de la
 * estructura vive en ListaReproduccion.
 */
public class Menu {
    private final Scanner in;
    private final ListaReproduccion lista = new ListaReproduccion();
    private boolean finEntrada = false;

    public Menu(Scanner in) {
        this.in = in;
    }

    public ListaReproduccion getLista() { return lista; }

    public void ejecutar() {
        boolean salir = false;
        while (!salir && !finEntrada) {
            mostrarMenu();
            Integer opcion = leerEntero("Opción: ");
            if (opcion == null) continue;
            switch (opcion) {
                case 1: agregar(true); break;
                case 2: agregar(false); break;
                case 3: buscar(); break;
                case 4: seleccionar(); break;
                case 5: eliminarPorId(); break;
                case 6: eliminarActual(); break;
                case 7: consultarActual(); break;
                case 8: avanzar(); break;
                case 9: retroceder(); break;
                case 10: mostrarLista(); break;
                case 11: System.out.println("Cantidad de canciones: " + lista.tamano()); break;
                case 12: reproducir(); break;
                case 13: salir = true; System.out.println("¡Hasta luego!"); break;
                default: System.out.println("Opción inválida. Elige un número del 1 al 13.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== GESTOR DE LISTA DE REPRODUCCIÓN =====");
        System.out.println(" 1. Agregar canción al inicio");
        System.out.println(" 2. Agregar canción al final");
        System.out.println(" 3. Buscar canción por ID");
        System.out.println(" 4. Seleccionar canción actual por ID");
        System.out.println(" 5. Eliminar canción por ID");
        System.out.println(" 6. Eliminar la canción actual");
        System.out.println(" 7. Consultar la canción actual");
        System.out.println(" 8. Avanzar a la siguiente");
        System.out.println(" 9. Retroceder a la anterior");
        System.out.println("10. Mostrar lista completa (ambos sentidos)");
        System.out.println("11. Cantidad de canciones");
        System.out.println("12. Simular reproducción de k canciones");
        System.out.println("13. Salir");
    }

    // ---------------------- Acciones ----------------------

    private void agregar(boolean alInicio) {
        String titulo = leerTexto("Título: ");
        if (titulo == null) return;
        String artista = leerTexto("Artista: ");
        if (artista == null) return;
        Integer duracion = leerEntero("Duración en segundos: ");
        if (duracion == null) return;
        try {
            Cancion c = alInicio
                    ? lista.agregarAlInicio(titulo, artista, duracion)
                    : lista.agregarAlFinal(titulo, artista, duracion);
            System.out.println("Canción agregada: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("No se agregó la canción: " + e.getMessage());
        }
    }

    private void buscar() {
        if (avisoSiVacia()) return;
        Integer id = leerEntero("ID a buscar: ");
        if (id == null) return;
        Cancion c = lista.buscar(id);
        System.out.println(c == null ? "No existe una canción con ID " + id + "." : "Encontrada: " + c);
    }

    private void seleccionar() {
        if (avisoSiVacia()) return;
        Integer id = leerEntero("ID a seleccionar: ");
        if (id == null) return;
        if (lista.seleccionar(id)) {
            System.out.println("Canción actual: " + lista.cancionActual());
        } else {
            System.out.println("No existe una canción con ID " + id + ". La lista no cambió.");
        }
    }

    private void eliminarPorId() {
        if (avisoSiVacia()) return;
        Integer id = leerEntero("ID a eliminar: ");
        if (id == null) return;
        if (lista.eliminarPorId(id)) {
            System.out.println("Canción con ID " + id + " eliminada.");
            infoActualTrasEliminar();
        } else {
            System.out.println("No existe una canción con ID " + id + ". La lista no cambió.");
        }
    }

    private void eliminarActual() {
        if (avisoSiVacia()) return;
        Cancion eliminada = lista.eliminarActual();
        System.out.println("Eliminada: " + eliminada);
        infoActualTrasEliminar();
    }

    private void infoActualTrasEliminar() {
        if (lista.estaVacia()) {
            System.out.println("La lista quedó vacía.");
        } else {
            System.out.println("Canción actual: " + lista.cancionActual());
        }
    }

    private void consultarActual() {
        if (avisoSiVacia()) return;
        System.out.println("Canción actual: " + lista.cancionActual());
    }

    private void avanzar() {
        if (avisoSiVacia()) return;
        System.out.println("Ahora: " + lista.avanzar());
    }

    private void retroceder() {
        if (avisoSiVacia()) return;
        System.out.println("Ahora: " + lista.retroceder());
    }

    private void mostrarLista() {
        if (avisoSiVacia()) return;
        System.out.println("--- De inicio a fin ---");
        System.out.println(lista.recorridoAdelante());
        System.out.println("--- De fin a inicio ---");
        System.out.println(lista.recorridoAtras());
    }

    private void reproducir() {
        if (avisoSiVacia()) return;
        Integer k = leerEntero("¿Cuántas canciones reproducir (k > 0)? ");
        if (k == null) return;
        if (k <= 0) {
            System.out.println("k debe ser un entero positivo.");
            return;
        }
        System.out.println(lista.reproducir(k));
        System.out.println("Siguiente a reproducir: " + lista.cancionActual());
    }

    // ---------------------- Entrada ----------------------

    private boolean avisoSiVacia() {
        if (lista.estaVacia()) {
            System.out.println("La lista está vacía. Agrega canciones primero.");
            return true;
        }
        return false;
    }

    /** Devuelve null al fin de la entrada. */
    private String leerLinea(String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            finEntrada = true;
            System.out.println();
            return null;
        }
        return in.nextLine().trim();
    }

    private String leerTexto(String prompt) {
        String s = leerLinea(prompt);
        if (s == null) return null;
        if (s.isEmpty()) {
            System.out.println("El texto no puede estar vacío. Operación cancelada.");
            return null;
        }
        return s;
    }

    private Integer leerEntero(String prompt) {
        String s = leerLinea(prompt);
        if (s == null) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida: se esperaba un número entero.");
            return null;
        }
    }
}
