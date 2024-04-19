import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import map.Coordinate;
import map.Map;

public class MineSweeperMap {

    /**
     * The main map of minesweeper game.
     */
    private final Map<Block> map;

    /**
     * The amount of land mines in the whole map.
     */
    private final int landmines;

    /**
     * Record the mine sweeper game is end.
     */
    private boolean end;

    /**
     * Record if player swept land mine.
     */
    private boolean sweepLandMine;

    /**
     * The amount of flags that player marked.
     */
    private int flag;

    /**
     * Constructs the map of minesweeper game with specified values.
     * @param rows the rows of the map
     * @param columns the columns of the map
     * @param landmines the amount of land mines in the whole map
     */
    public MineSweeperMap(int rows, int columns, int landmines) {
        this.landmines = landmines;
        map = new Map<>(rows, columns);
        initialization();
    }

    /**
     * Initialize all maps with default value
     */
    public void initialization() {
        end = false;
        sweepLandMine = false;
        flag = 0;
        for (int i = 0; i < getSize(); i++) {
            map.setValue(i, new Block());
        }
    }

    /**
     * Generate land mines, and away from specified coordinate.
     * @param c the coordinate can't be land mine
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public void landMineGenerate(Coordinate c) {
        Objects.requireNonNull(c);
        ArrayList<Coordinate> cList = new ArrayList<>();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                cList.add(new Coordinate(j, i));
            }
        }
        cList.remove(c);

        Random random = new Random();
        int mines = 0;
        // int pow = 1;
        // int mines = cList.size();
        // while (land != 0) {
        //     land /= 10;
        //     pow++;
        // }

        while (mines < landmines) {
            // int rand = (int)(Math.random() * Math.pow(10, pow)) % cList.size();
            int rand = random.nextInt(cList.size());
            map.setValue(cList.get(rand), new Block(true));
            cList.remove(rand);
            mines++;
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
    private boolean landMineTester(Coordinate c) {
        if (getMapValue(c).isLandMine()) {
            getMapValue(c).setOpen(true);
            return true;
        }
        else if (getMapValue(c).haveFlag()) {
            return false;
        }

        landMineTest(c);

        return false;
    }

    private void landMineTest(Coordinate c) {
        if (overRange(c)) return;

        int landmine = 0;
        Coordinate loop = new Coordinate();
        Block mapValue = getMapValue(c);

        if (mapValue.haveFlag()) {
            return;
        }
        else if (mapValue.isOpen()) {
            return;
        }

        mapValue.setOpen(true);

        for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
            for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                if (overRange(loop) || loop.equals(c)) continue;

                mapValue = getMapValue(loop);
                if (mapValue.isLandMine()) {
                    landmine++;
                }
            }
        }

        getMapValue(c).setMineAround(landmine);

        if (landmine == 0) {
            for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
                for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                    if (overRange(loop) || loop.equals(c)) continue;

                    if (nonSweepAndNoFlag(loop)) {
                        landMineTest(loop);
                    }
                }
            }
        }
    }

    private boolean nonSweepAndNoFlag(Coordinate c) {
        return !getMapValue(c).isOpen() && !getMapValue(c).haveFlag();
    }

    /**
     * Check all space has been swept.
     */
    public void checkWin() {
        int untreatedPlaces = 0, i;

        for (i = 0; i < map.getSize(); i++) {
            if (!getMapValue(i).isOpen() && !getMapValue(i).isLandMine()) {
                untreatedPlaces++;
            }
        }

        if (untreatedPlaces == 0) {
            for (i = 0; i < map.getSize(); i++) {
                if (getMapValue(i).isLandMine() && !getMapValue(i).haveFlag()) {
                    getMapValue(i).setFlag(true);
                    flag++;
                }
            }
            end = true;
        }
    }

    /**
     * 
     * @param c the coordinate that want to sweep
     * @throws IndexOutOfBoundsException if {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public void sweep(Coordinate c) {
        Block mapValue = getMapValue(c);

        if (mapValue.isOpen() && !mapValue.isSpace()) {
            sweepOnNumber(c);
            sweepLandMine = end;
        }
        else {
            sweepLandMine = landMineTester(c);
            if (sweepLandMine) {
                end = sweepLandMine;
            }
        }
        if (!sweepLandMine) {
            checkWin();
        }
    }

    private void sweepOnNumber(Coordinate c) {
        Coordinate loop = new Coordinate();
        int flags = 0;
        for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
            for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                if (overRange(loop) || loop.equals(c)) {
                    continue;
                }
                if (getMapValue(loop).haveFlag()) {
                    flags++;
                }
            }
        }

        if (getMapValue(c).getMineAround() == flags) {
            for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
                for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                    if (overRange(loop) || loop.equals(c)) {
                        continue;
                    }
                    if (getMapValue(loop).haveFlag()) {
                        continue;
                    }
                    sweepLandMine = landMineTester(loop);
                    if (sweepLandMine) {
                        end = sweepLandMine;
                    }
                }
            }
        }
    }

    /**
     * 
     * @param c the coordinate that want to mark
     * @throws IndexOutOfBoundsException if {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public void mark(Coordinate c) {
        Block mapValue = getMapValue(c);

        if (!mapValue.isOpen()) {
            if (!getMapValue(c).haveFlag()) {
                getMapValue(c).setFlag(true);;
                flag++;
            }
            else {
                getMapValue(c).setFlag(false);
                flag--;
            }
        }
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
        return (c.x < 0 || c.x > getColumns() - 1 || c.y < 0 || c.y > getRows() - 1);
    }

    /**
     * Set minesweeper game is end
     * @param b {@code true} if game is end otherwise
     * {@code false}
     */
    public void setEnd(boolean b) {
        end = b;
    }

    /**
     * Set player swept at land mine
     * @param b {@code true} if player swept at land mine otherwise
     * {@code false}
     */
    public void setSweepLandMine(boolean b) {
        sweepLandMine = b;
    }

    /**
     * Set the amount of flags that player marked
     * @param i the amount of flags
     */
    public void setFlag(int i) {
        flag = i;
    }

    // /**
    //  * set the information at the specified coordinate at the main map.
    //  * @param c the coordinate at the main map
    //  * @param m the information which is write into the map
    //  * @throws IndexOutOfBoundsException if the {@code c} is out of range
    //  * @throws NullPointerException if {@code c}, {@code m} is {@code null}
    //  */
    // public void setMapValue(Coordinate c, Block m) {
    //     map.setValue(c, m);
    // }

    // /**
    //  * Set the information at the specified index at the main map.
    //  * @param i the index at the main map
    //  * @param m the information which is write into the map
    //  * @throws IndexOutOfBoundsException if the {@code i} is out of range
    //  * @throws NullPointerException if {@code m} is {@code null}
    //  */
    // public void setMapValue(int i, Block m) {
    //     map.setValue(i, m);
    // }

    /**
     * Get the information at the specified coordinate at the main map.
     * @param c the coordinate at the main map
     * @return the information at the coordinate at the main map
     * @throws IndexOutOfBoundsException if the {@code c} is out of range
     * @throws NullPointerException if {@code c} is {@code null}
     */
    public Block getMapValue(Coordinate c) {
        return map.getValue(c);
    }

    /**
     * Get the information at the specified index at the main map
     * @param i the index at the main map
     * @return the information at the index at the main map
     * @throws IndexOutOfBoundsException if the {@code i} is out of range
     */
    public Block getMapValue(int i) {
        return map.getValue(i);
    }

    /**
     * Check is game end.
     * @return {@code true} is minesweeper game is end otherwise
     * {@code false}
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * Check is player win.
     * @return {@code true} if player clear space without swept at land mine otherwise
     * {@code false}
     */
    public boolean isWin() {
        return !sweepLandMine;
    }

    /**
     * Get the amount of flags.
     * @return the amount of flags that player marked
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Get the count of rows
     * @return the amount of rows
     */
    public int getRows() {
        return map.getRows();
    }

    /**
     * Get the count of columns
     * @return the amount of columns
     */
    public int getColumns() {
        return map.getColumns();
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
