public class Block {

    /**
     * Record that is here a land mine.
     */
    private boolean landMine;

    /**
     * Record that is here a flag.
     */
    private boolean flag;

    /**
     * Record that is here swept.
     */
    private boolean open;

    /**
     * Record that is here some land mines around.
     */
    private int mineAround;

    /**
     * Constructs all value to default.
     */
    public Block() {
        this(false);
    }

    /**
     * Constructs with a specified value.
     * @param landMine set is here a land mine
     */
    public Block(boolean landMine) {
        this.landMine = landMine;
        flag = false;
        open = false;
        mineAround = 9;
    }

    /**
     * Check here is swept or not.
     * @return {@code true} if here is swept otherwise
     * {@code false}
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Check here is a land mine
     * @return {@code true} if here is a land mine otherwise
     * {@code false}
     */
    public boolean isLandMine() {
        return landMine;
    }

    /**
     * Check here is marked.
     * @return {@code true} if here is a flag otherwise
     * {@code false}
     */
    public boolean haveFlag() {
        return flag;
    }

    /**
     * Check here is no land mines around.
     * @return {@code true} if any land mine isn't around here otherwise
     * {@code false}
     */
    public boolean isSpace() {
        return open && (mineAround == 0);
    }

    /**
     * Check is here a land mine and has been swept.
     * @return {@code true} if here is opened and is a land mine otherwise
     * {@code false}
     */
    public boolean isSweepLandMine() {
        return (open && landMine);
    }

    /**
     * Set if here is a land mine or not
     * @param b if here is a land mine
     */
    public void setLandMine(boolean b) {
        landMine = b;
    }

    /**
     * Set if here was marked or not
     * @param b if here was marked
     */
    public void setFlag(boolean b) {
        flag = b;
    }

    /**
     * Set if here was swept or not
     * @param b if here was swept
     */
    public void setOpen(boolean b) {
        open = b;
    }

    /**
     * Set if here are some land mines around
     * @param i the amount of land mines around
     */
    public void setMineAround(int i) {
        mineAround = i;
    }

    /**
     * Get here are some land mines around or not
     * @return the amount of land mines around
     */
    public int getMineAround() {
        return mineAround;
    }
}
