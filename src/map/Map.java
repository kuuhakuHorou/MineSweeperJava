package map;
import java.util.Arrays;
import java.util.Objects;

/**
 * 
 */
public class Map<T> {
    /**
     * Using one dimension array like a two dimensions array.
     */
    private final Object[] map;

    /**
     * The rows of the map.
     */
    private final int rows;

    /**
     * The columns of the map.
     */
    private final int columns;

    /**
     * The size of the map.
     */
    private final int size;

    /**
     * Constructs a map with specified inital value.
     * 
     * @param rows the inital rows of the map
     * @param cols the inital columns of the map
     * @param val the inital value of the map
     */
    public Map(int rows, int cols, T val) {
        this.rows = rows;
        this.columns = cols;
        this.size = rows * cols;
        this.map = new Object[this.size];
        this.initialization(val);
    }

    /**
     * Constructs the map with empty value.
     * @param rows the inital rows of the map
     * @param cols the inital columns of the map
     */
    public Map(int rows, int cols) {
        this(rows, cols, null);
    }

    /**
     * Initialize the whole map with val.
     * @param val use to rewrite to the whole map.
     */
    public void initialization(T val) {
        Arrays.fill(map, val);
    }

    /**
     * The convertion of coordinate
     * @param c the coordinate at the map
     * @return the index at the map
     * @throws IndexOutOfBoundsException if {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    private int location(Coordinate c) {
        c = Objects.requireNonNull(c);
        Objects.checkIndex(c.x, columns);
        Objects.checkIndex(c.y, rows);
        return columns * c.y + c.x;
    }

    /**
     * Set the value to the specified location.
     * 
     * @param c the coordinate at the map
     * @param val the value write into the map
     * @throws IndexOutOfBoundsException if {@code c} is out of range
     * @throws NullPointerException if {@code c}, {@code m} is {@code null}
     */
    public void setValue(Coordinate c, T val) {
        int index = location(c);
        Objects.requireNonNull(val);
        map[index] = val;
    }

    /**
     * Set the value to the specified location.
     * 
     * @param index the index at the map
     * @param val the value write into the map
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     * @throws NullPointerException if {@code m} is {@code null}
     */
    public void setValue(int index, T val) {
        Objects.checkIndex(index, size);
        Objects.requireNonNull(val);
        map[index] = val;
    }

    /**
     * Get the value at the specified location.
     * 
     * @param c the coordinate at the map
     * @return the value at the map
     * @throws IndexOutOfBoundsException if {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public T getValue(Coordinate c) {
        int index = location(c);
        return (T) map[index];
    }

    /**
     * Get the value at the specified location.
     * @param index the index at the map
     * @return the value at the map
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     */
    @SuppressWarnings("unchecked")
    public T getValue(int index) {
        Objects.checkIndex(index, size);
        return (T) map[index];
    }

    /**
     * Get rows.
     * @return the rows of the map
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get columns.
     * @return the columns of the map
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get size.
     * @return the size of the map({@code rows * columns})
     */
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Map)) return false;

        boolean equal = (equalsSize((Map<?>) o)) && (equalsMap((Map<?>) o));

        return equal;
    }

    private boolean equalsSize(Map<?> other) {
        return this.rows == other.rows && this.columns == other.columns;
    }

    private boolean equalsMap(Map<?> other) {
        final int s = this.size;
        boolean equal;
        if (equal = (s == other.size)) {
            final Object[] otherM = other.map;
            final Object[] m = this.map;
            for (int i = 0; i < s; i++) {
                if (!Objects.equals(m[i], otherM[i])) {
                    equal = false;
                    break;
                }
            }
        }

        return equal;
    }

    @Override
    public int hashCode() {
        final Object[] m = this.map;
        int hashCode = 1;
        for (int i = 0; i < m.length; i++) {
            Object o = m[i];
            hashCode = 29 * hashCode + (o == null? 0: o.hashCode());
        }

        return hashCode;
    }
}
