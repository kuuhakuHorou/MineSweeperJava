import java.io.Serializable;

public class Difficulty implements Serializable {
    public final int rows;
    public final int columns;
    public final int landmines;

    public static Difficulty EASY = new Difficulty(9, 9, 10);
    public static Difficulty NORMAL = new Difficulty(16, 16, 40);
    public static Difficulty HARD = new Difficulty(16, 30, 99);

    public Difficulty(int rows, int columns, int landmines) {
        this.rows = rows;
        this.columns = columns;
        this.landmines = landmines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Difficulty c)) return false;

        return (this.rows == c.rows) && (this.columns == c.columns) && (this.landmines == c.landmines);
    }

}
