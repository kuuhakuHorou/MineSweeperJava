import map.Coordinate;

public abstract class Game {

    protected boolean over;
    protected boolean restart;
    protected boolean sweepLandMine;
    protected boolean exit;
    protected int flag;
    protected MineSweeperMap map;
    protected final Config easy = new Config(9, 9, 10);
    protected final Config normal = new Config(16, 16, 40);
    protected final Config hard = new Config(16, 30, 99);
    protected Config customize;

    protected class Config {
        public int rows;
        public int columns;
        public int landmines;

        public Config(int rows, int columns, int landmines) {
            this.rows = rows;
            this.columns = columns;
            this.landmines = landmines;
        }
    }

    public Game() {
        initialization();
    }

    protected void initialization() {
        over = false;
        restart = false;
        sweepLandMine = false;
        exit = false;
        flag = 0;
    }

    protected void mapConstructs(Config c) {
        map = new MineSweeperMap(c.rows, c.columns, c.landmines);
    }

    protected void mapInitialization() {
        map.initialization();
    }

    public void isWin() {
        int untreatedPlaces = 0, i;

        for (i = 0; i < map.getSize(); i++) {
            if (map.getMapValue(i).equals(MapInformation.NonSweep)) {
                untreatedPlaces++;
            }
        }

        if (untreatedPlaces == 0) {
            for (i = 0; i < map.getSize(); i++) {
                if (map.getMapValue(i).equals(MapInformation.LandMine) && !map.getMarkValue(i).booleanValue()) {
                    map.setMarkValue(i, Boolean.TRUE);
                    flag++;
                }
            }
            over = true;
        }
    }

    public void sweep(Coordinate c) {
        MapInformation mapValue = map.getMapValue(c);

        if (mapValue.equals(MapInformation.MineAround)) {
            sweepOnNumber(c);
            sweepLandMine = over;
        }
        else {
            sweepLandMine = map.landMineTester(c);
            if (sweepLandMine) {
                over = sweepLandMine;
            }
        }
        if (!sweepLandMine) {
            isWin();
        }
    }

    private void sweepOnNumber(Coordinate c) {
        Coordinate loop = new Coordinate();
        int flags = 0;
        for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
            for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                if (map.overRange(loop) || loop.equals(c)) {
                    continue;
                }
                if (map.getMarkValue(loop).booleanValue()) {
                    flags++;
                }
            }
        }

        if (map.getAroundValue(c).equals(flags)) {
            for (loop.y = c.y - 1; loop.y <= c.y + 1; loop.y++) {
                for (loop.x = c.x - 1; loop.x <= c.x + 1; loop.x++) {
                    if (map.overRange(loop) || loop.equals(c)) {
                        continue;
                    }
                    if (map.getMarkValue(loop).booleanValue()) {
                        continue;
                    }
                    sweepLandMine = map.landMineTester(loop);
                    if (sweepLandMine) {
                        over = sweepLandMine;
                    }
                }
            }
        }
    }

    public void mark(Coordinate c) {
        MapInformation mapValue = map.getMapValue(c);

        if (mapValue.equals(MapInformation.LandMine) || mapValue.equals(MapInformation.NonSweep)) {
            if (!map.getMarkValue(c).booleanValue()) {
                map.setMarkValue(c, Boolean.TRUE);
                flag++;
            }
            else {
                map.setMarkValue(c, Boolean.FALSE);
                flag--;
            }
        }
    }

    abstract public Coordinate getCoordinate();
    abstract public void degreeOfDifficulty();
    abstract public void gameDifficultyChoose();
    abstract public void gameStart();
    abstract public void gamePlay();
}
