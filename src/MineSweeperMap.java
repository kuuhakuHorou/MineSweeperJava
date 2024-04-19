import java.util.ArrayList;
import java.util.Objects;

import map.Coordinate;
import map.Map;

public class MineSweeperMap {
    /**
     * The main map of minesweeper game
     */
    private final Map<MapInformation> map;

    /**
     * The flag map of minesweeper game
     */
    private final Map<Boolean> mark;

    /**
     * The map of how many mines are around the block
     */
    private final Map<Integer> around;

    /**
     * The rows of the map
     */
    private final int rows;

    /**
     * The columns of the map
     */
    private final int columns;

    /**
     * The amount of land mines in the whole map
     */
    private final int landmines;

    /**
     * Constructs the map of minesweeper game with specified values.
     * @param rows the rows of the map
     * @param columns the columns of the map
     * @param landmines the amount of land mines in the whole map
     */
    public MineSweeperMap(int rows, int columns, int landmines) {
        this.rows = rows;
        this.columns = columns;
        this.landmines = landmines;
        map = new Map<>(this.rows, this.columns, MapInformation.NonSweep);
        mark = new Map<>(this.rows, this.columns, Boolean.FALSE);
        around = new Map<>(this.rows, this.columns, Integer.valueOf(0));
    }

    /**
     * Initialize all maps with default value
     */
    public void initialization() {
        map.initialization(MapInformation.NonSweep);
        mark.initialization(Boolean.FALSE);
        around.initialization(Integer.valueOf(0));
    }

    /**
     * Generate land mines, and away from specified coordinate.
     * @param c the coordinate can't be land mine
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public void landMineGenerate(Coordinate c) {
        Objects.requireNonNull(c);
        ArrayList<Coordinate> cList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cList.add(new Coordinate(j, i));
            }
        }
        cList.remove(c);

        int pow = 1;
        int land = cList.size();
        while (land != 0) {
            land /= 10;
            pow++;
        }

        while (land < landmines) {
            int rand = (int)(Math.random() * Math.pow(10, pow)) % cList.size();
            map.setValue(cList.get(rand), MapInformation.LandMine);
            cList.remove(rand);
            land++;
        }
    }

    /**
     * Test the coordinate which is space or land mine.
     * @param c the coordinate for test
     * @return {@code true} if the coordinate at the map is land mine otherwise
     * {@code false}
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public boolean landMineTester(Coordinate c) {
        if (getMapValue(c).equals(MapInformation.LandMine)) {
            setMapValue(c, MapInformation.SweepLandMine);
            return true;
        }
        else if (getMarkValue(c).booleanValue()) {
            return false;
        }

        landMineTest(c);

        return false;
    }

    private void landMineTest(Coordinate c) {
        if (overRange(c)) return;

        int landmine = 0;
        Coordinate loop = new Coordinate();
        MapInformation mapValue = getMapValue(c);
        Boolean markValue = getMarkValue(c);

        if (markValue.booleanValue()) {
            return;
        }
        else if (!mapValue.equals(MapInformation.NonSweep)) {
            return;
        }

        for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
            for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                if (overRange(loop) || loop.equals(c)) continue;

                mapValue = getMapValue(loop);
                if (mapValue.equals(MapInformation.LandMine) || mapValue.equals(MapInformation.SweepLandMine)) {
                    landmine++;
                }
            }
        }

        if (landmine == 0) {
            setMapValue(c, MapInformation.Space);
            for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
                for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                    if (overRange(loop) || loop.equals(c)) continue;

                    if (nonSweepAndNoFlag(loop)) {
                        landMineTest(loop);
                    }
                }
            }
        }
        else {
            setMapValue(c, MapInformation.MineAround);
            setAroundValue(c, Integer.valueOf(landmine));
        }
    }

    private boolean nonSweepAndNoFlag(Coordinate c) {
        return getMapValue(c).equals(MapInformation.NonSweep) && !getMarkValue(c).booleanValue();
    }

    /**
     * Check the spot at the map is over range.
     * @param c the coordinate for checking
     * @return {@code true} if the spot is over range otherwise
     * {@code false}
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public boolean overRange(Coordinate c) {
        Objects.requireNonNull(c);
        return (c.x < 0 || c.x > columns - 1 || c.y < 0 || c.y > rows - 1);
    }

    /**
     * set the information at the specified coordinate at the main map.
     * @param c the coordinate at the main map
     * @param m the information which is write into the map
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c}, {@code m} is {@code null}
     */
    public void setMapValue(Coordinate c, MapInformation m) {
        map.setValue(c, m);
    }

    /**
     * Set the information at the specified index at the main map.
     * @param i the index at the main map
     * @param m the information which is write into the map
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     * @throws NullPointerException if {@code m} is {@code null}
     */
    public void setMapValue(int i, MapInformation m) {
        map.setValue(i, m);
    }

    /**
     * Set the information at the specified coordinate at the flag map.
     * @param c the coordinate at the flag map
     * @param b {@code Boolean.TURE} if there is a flag otherwise {@code Boolean.FALSE}
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c}, {@code b} is {@code null}
     */
    public void setMarkValue(Coordinate c, Boolean b) {
        mark.setValue(c, b);
    }

    /**
     * Set the information at the specified index at the flag map.
     * @param i the index at the flag map
     * @param b {@code Boolean.TRUE} if there is a flag otherwise {@code Boolean.FALSE}
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     * @throws NullPointerException if {@code b} is {@code null}
     */
    public void setMarkValue(int i, Boolean b) {
        mark.setValue(i, b);
    }

    /**
     * Set the amount of how many land mines around the specified coordinate.
     * @param c the coordinate at the map
     * @param i the amount of how many land mines
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c}, {@code i} is {@code null}
     */
    public void setAroundValue(Coordinate c, Integer i) {
        around.setValue(c, i);
    }

    /**
     * Set the amount of how many land mines aroud the specified index.
     * @param i the index at the map
     * @param I the amount of how many land mines
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     * @throws NullPointerException if {@code I} is {@code null}
     */
    public void setAroundValue(int i, Integer I) {
        around.setValue(i, I);
    }

    /**
     * Get the information at the specified coordinate at the main map.
     * @param c the coordinate at the main map
     * @return the information at the coordinate at the main map
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public MapInformation getMapValue(Coordinate c) {
        return map.getValue(c);
    }

    /**
     * Get the information at the specified index at the main map
     * @param i the index at the main map
     * @return the information at the index at the main map
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     */
    public MapInformation getMapValue(int i) {
        return map.getValue(i);
    }

    /**
     * Get the information at the specified coordinate at the flag map.
     * @param c the coordinate at the flag map
     * @return {@code Boolean.TRUE} if there is a flag otherwise
     * {@code Boolean.FALSE}
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Boolean getMarkValue(Coordinate c) {
        return mark.getValue(c);
    }

    /**
     * Get the information at the specified index at the flag map
     * @param i the index at the flag map
     * @return {@code Boolean.TURE} if there is a flag otherwise
     * {@code Boolean.FALSE}
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     */
    public Boolean getMarkValue(int i) {
        return mark.getValue(i);
    }

    /**
     * Get the amount of how many land mines around the specified block.
     * @param c the coordinate at the map
     * @return the amount of land mines around the block
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Integer getAroundValue(Coordinate c) {
        return around.getValue(c);
    }

    /**
     * Get the count of rows
     * @return the amount of rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Get the count of columns
     * @return the amount of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Get the size of the map
     * @return the size of the map
     */
    public int getSize() {
        return map.getSize();
    }

    /**
     * Get the count of land mines.
     * @return the amount of land mines.
     */
    public int getLandMines() {
        return this.landmines;
    }
}
