/**
 * Lista doblemente enlazada circular de canciones (TAD lista de reproducción).
 *
 * Invariantes:
 *  - Lista vacía: inicio == fin == actual == null y tamano == 0.
 *  - Un solo nodo: su anterior y su siguiente son él mismo.
 *  - Lista no vacía: fin.siguiente == inicio e inicio.anterior == fin.
 *  - Para todo nodo n: n.siguiente.anterior == n y n.anterior.siguiente == n.
 *  - actual pertenece siempre a la lista.
 *
 * Los identificadores se generan con un contador incremental, por lo que
 * insertar no requiere buscar duplicados.
 */
public class ListaReproduccion {
    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int tamano;
    private int siguienteId = 1;

    public ListaReproduccion() {
        inicio = null;
        fin = null;
        actual = null;
        tamano = 0;
    }

    // ------------------------------------------------------------------
    // Consultas básicas
    // ------------------------------------------------------------------

    /** O(1) */
    public int tamano() { return tamano; }

    /** O(1) */
    public boolean estaVacia() { return tamano == 0; }

    /** O(1). Devuelve null si la lista está vacía. */
    public Cancion cancionActual() {
        return actual == null ? null : actual.getCancion();
    }

    /** O(1) */
    public Cancion getInicio() { return inicio == null ? null : inicio.getCancion(); }

    /** O(1) */
    public Cancion getFin() { return fin == null ? null : fin.getCancion(); }

    // ------------------------------------------------------------------
    // Inserción (O(1))
    // ------------------------------------------------------------------

    /**
     * Agrega una canción al inicio. Lanza IllegalArgumentException si los datos
     * son inválidos (en ese caso la lista y el contador de ids no cambian).
     */
    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (estaVacia()) {
            insertarPrimero(nuevo);
        } else {
            nuevo.setSiguiente(inicio);
            nuevo.setAnterior(fin);
            inicio.setAnterior(nuevo);
            fin.setSiguiente(nuevo);
            inicio = nuevo;
        }
        tamano++;
        return nuevo.getCancion();
    }

    /** Agrega una canción al final. */
    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (estaVacia()) {
            insertarPrimero(nuevo);
        } else {
            nuevo.setAnterior(fin);
            nuevo.setSiguiente(inicio);
            fin.setSiguiente(nuevo);
            inicio.setAnterior(nuevo);
            fin = nuevo;
        }
        tamano++;
        return nuevo.getCancion();
    }

    private Nodo crearNodo(String titulo, String artista, int duracion) {
        // Si Cancion lanza excepción, siguienteId no se incrementa.
        Cancion c = new Cancion(siguienteId, titulo, artista, duracion);
        siguienteId++;
        return new Nodo(c);
    }

    private void insertarPrimero(Nodo nuevo) {
        nuevo.setAnterior(nuevo);
        nuevo.setSiguiente(nuevo);
        inicio = nuevo;
        fin = nuevo;
        actual = nuevo; // la primera canción insertada es la actual
    }

    // ------------------------------------------------------------------
    // Búsqueda y selección (O(n))
    // ------------------------------------------------------------------

    /** Localiza el nodo con ese id. Recorre exactamente 'tamano' nodos como máximo. */
    private Nodo buscarNodo(int id) {
        if (estaVacia()) return null;
        Nodo n = inicio;
        for (int i = 0; i < tamano; i++) {
            if (n.getCancion().getId() == id) return n;
            n = n.getSiguiente();
        }
        return null;
    }

    /** O(n). Devuelve null si no existe. */
    public Cancion buscar(int id) {
        Nodo n = buscarNodo(id);
        return n == null ? null : n.getCancion();
    }

    /** O(n). Convierte la canción en la actual. Devuelve false si no existe. */
    public boolean seleccionar(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) return false;
        actual = n;
        return true;
    }

    // ------------------------------------------------------------------
    // Navegación (O(1))
    // ------------------------------------------------------------------

    /** Avanza a la siguiente (circular). Devuelve la nueva actual o null si vacía. */
    public Cancion avanzar() {
        if (estaVacia()) return null;
        actual = actual.getSiguiente();
        return actual.getCancion();
    }

    /** Retrocede a la anterior (circular). Devuelve la nueva actual o null si vacía. */
    public Cancion retroceder() {
        if (estaVacia()) return null;
        actual = actual.getAnterior();
        return actual.getCancion();
    }

    // ------------------------------------------------------------------
    // Eliminación
    // ------------------------------------------------------------------

    /** O(n) por la búsqueda; el desenlace en sí es O(1). */
    public boolean eliminarPorId(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) return false;
        desenlazar(n);
        return true;
    }

    /** O(1): parte de una referencia ya disponible. Devuelve la canción eliminada o null si vacía. */
    public Cancion eliminarActual() {
        if (estaVacia()) return null;
        Nodo eliminado = actual;
        Cancion c = eliminado.getCancion();
        desenlazar(eliminado);
        return c;
    }

    /** Quita un nodo de la lista actualizando enlaces y referencias. O(1). */
    private void desenlazar(Nodo n) {
        if (tamano == 1) {
            inicio = null;
            fin = null;
            actual = null;
        } else {
            // Se guardan las referencias antes de modificar enlaces.
            Nodo ant = n.getAnterior();
            Nodo sig = n.getSiguiente();
            ant.setSiguiente(sig);
            sig.setAnterior(ant);
            if (n == inicio) inicio = sig;
            if (n == fin) fin = ant;
            if (n == actual) actual = sig; // la que le seguía
        }
        // El nodo eliminado ya no apunta a la lista (y ya nadie lo referencia).
        n.setAnterior(null);
        n.setSiguiente(null);
        tamano--;
    }

    // ------------------------------------------------------------------
    // Recorridos (O(n)) y reproducción simulada (O(k))
    // ------------------------------------------------------------------

    /** Una canción por línea, de inicio a fin, visitando cada nodo una sola vez. */
    public String recorridoAdelante() {
        StringBuilder sb = new StringBuilder();
        Nodo n = inicio;
        for (int i = 0; i < tamano; i++) {
            sb.append(n.getCancion());
            if (n == actual) sb.append("  <-- actual");
            if (i < tamano - 1) sb.append("\n");
            n = n.getSiguiente();
        }
        return sb.toString();
    }

    /** Una canción por línea, de fin a inicio, visitando cada nodo una sola vez. */
    public String recorridoAtras() {
        StringBuilder sb = new StringBuilder();
        Nodo n = fin;
        for (int i = 0; i < tamano; i++) {
            sb.append(n.getCancion());
            if (n == actual) sb.append("  <-- actual");
            if (i < tamano - 1) sb.append("\n");
            n = n.getAnterior();
        }
        return sb.toString();
    }

    /** Solo ids, útil para pruebas: "1 -> 2 -> 3". Vacío si no hay canciones. */
    public String idsAdelante() {
        StringBuilder sb = new StringBuilder();
        Nodo n = inicio;
        for (int i = 0; i < tamano; i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(n.getCancion().getId());
            n = n.getSiguiente();
        }
        return sb.toString();
    }

    /** Solo ids de fin a inicio. */
    public String idsAtras() {
        StringBuilder sb = new StringBuilder();
        Nodo n = fin;
        for (int i = 0; i < tamano; i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(n.getCancion().getId());
            n = n.getAnterior();
        }
        return sb.toString();
    }

    /**
     * Muestra la actual y avanza, exactamente k veces. O(k).
     * Al final, actual queda en la que correspondería reproducir después.
     * Devuelve null si la lista está vacía; lanza excepción si k <= 0.
     */
    public String reproducir(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k debe ser un entero positivo.");
        }
        if (estaVacia()) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append("Reproduciendo: ").append(actual.getCancion());
            if (i < k - 1) sb.append("\n");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }


    public boolean esConsistente() {    
        if (tamano == 0) {
            return inicio == null && fin == null && actual == null;
        }

        if (inicio == null || fin == null || actual == null) return false;
        if (fin.getSiguiente() != inicio || inicio.getAnterior() != fin) return false;

        // Recorrido hacia adelante.
        int contados = 0;
        boolean actualEncontrado = false;
        Nodo n = inicio;
        do {
            if (n.getSiguiente() == null || n.getAnterior() == null) return false;
            if (n.getSiguiente().getAnterior() != n) return false;
            if (n.getAnterior().getSiguiente() != n) return false;
            if (n == actual) actualEncontrado = true;
            contados++;
            if (contados > tamano) return false; // ciclo más largo que el contador
            n = n.getSiguiente();
        } while (n != inicio);
        if (contados != tamano || !actualEncontrado) return false;

        // Recorrido hacia atrás.
        contados = 0;
        n = fin;
        do {
            contados++;
            if (contados > tamano) return false;
            n = n.getAnterior();
        } while (n != fin);
        return contados == tamano;
    }
}
