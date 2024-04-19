package map;

/**
 * Two dimensions coordinate
 */
public class Coordinate {
    /**
     * The x coordinate(columns)
     */
    public int x;

    /**
     * The y coordinate(rows)
     */
    public int y;

    /**
     * Constructs at x = 0, y = 0
     */
    public Coordinate() {
        this.setCoordinate(0, 0);
    }

    /**
     * Constructs a coordinate with the specified value.
     * @param x the x value(columns)
     * @param y the y value(rows)
     */
    public Coordinate(int x, int y) {
        this.setCoordinate(x, y);
    }

    /**
     * Constructs a coordinate with an exist coordinate.
     * @param c an exist coordinate
     */
    public Coordinate(Coordinate c) {
        this(c.x, c.y);
    }

    /**
     * Set x value
     * @param x the x value(columns)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set y value
     * @param y the y value(rows)
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set coordinate to specified location
     * @param x the x value(columns)
     * @param y the y value(rows)
     */
    public void setCoordinate(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Get x value
     * @return the x value(columns)
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get y value
     * @return the y value(rows)
     */
    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Coordinate c)) return false;

        return (this.x == c.x) && (this.y == c.y);
    }

    @Override
    public String toString() {
        return "x: " + Integer.toString(this.x) + ", y: " + Integer.toString(this.y);
    }
}
