/**
 * Nodo de la lista doblemente enlazada circular.
 * Guarda una canción y las referencias al nodo anterior y al siguiente.
 */
public class Nodo {
    private final Cancion cancion;
    private Nodo anterior;
    private Nodo siguiente;

    public Nodo(Cancion cancion) {
        this.cancion = cancion;
    }

    public Cancion getCancion() { return cancion; }
    public Nodo getAnterior() { return anterior; }
    public Nodo getSiguiente() { return siguiente; }
    public void setAnterior(Nodo anterior) { this.anterior = anterior; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
}
