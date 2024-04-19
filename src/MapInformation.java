public enum MapInformation {
    /**
     * The place doesn't be swept.
     */
    NonSweep,

    /**
     * There are some land mines around the place
     */
    MineAround,

    /**
     * The place has been swept and has no land mine around
     */
    Space,

    /**
     * The land mine at the map
     */
    LandMine,

    /**
     * The land mine has been swept
     */
    SweepLandMine,

    /**
     * When player lose the game, land mine will show out
     */
    // LoseLandMine,

    /**
     * When player win the game, land mine will show
     */
    // WinLandMine,

    /**
     * The place doesn't be marked
     */
    // NoFlag,

    /**
     * The place is marked
     */
    // Flag,

    /**
     * The place isn't marked at the place which is land mine
     */
    // WrongFlag,

    /**
     * The border of the map
     */
    // Border;

}