# Gestor de lista de reproducción (lista doblemente enlazada circular)

Proyecto de estructuras de datos: gestor de canciones implementado con una lista
doblemente enlazada circular propia (sin `LinkedList`, `ArrayList` ni arreglos).

## Versión de Java
Java 17 o superior (desarrollado y probado con OpenJDK 21).

## Estructura
```
src/
  Cancion.java          Datos de la canción (id, título, artista, duración > 0)
  Nodo.java             Nodo con referencias anterior y siguiente
  ListaReproduccion.java Lista circular (inicio, fin, actual, tamaño, ids automáticos)
  Menu.java             Interfaz de consola (solo interacción)
  Main.java             Punto de entrada
  PruebasLista.java     Pruebas reproducibles (sin librerías externas)
resultados_pruebas.txt  Salida de las pruebas
```

## Compilar y ejecutar
Desde la carpeta raíz del proyecto:

```bash
mkdir out
javac -encoding UTF-8 -d out src/*.java

# Ejecutar el programa
java -cp out Main

# Ejecutar las pruebas
java -cp out PruebasLista
```

En Windows (CMD) conviene ejecutar antes `chcp 65001` para ver bien los acentos.

## Complejidades
| Operación | Complejidad |
|---|---|
| Agregar al inicio / final | O(1) |
| Consultar canción actual | O(1) |
| Avanzar / retroceder | O(1) |
| Eliminar canción actual | O(1) |
| Cantidad de canciones | O(1) |
| Buscar / seleccionar por id | O(n) |
| Eliminar por id | O(n) (búsqueda) + O(1) (enlaces) |
| Mostrar todas las canciones | O(n) |
| Simular k reproducciones | O(k) |

## Integrantes
 Andalon Molina Victor Armando
 Carbajal Domínguez David
 García Jorge Santiago Dazaev
 Hern+andez Huerta Josué Jassiel
 Trujillo Quezada Pablo Mauricio