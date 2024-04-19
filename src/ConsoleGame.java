import java.util.Scanner;

import map.Coordinate;

public class ConsoleGame extends Game {

    Scanner scan;

    public ConsoleGame() {
        super();
        scan = new Scanner(System.in);
    }

    @Override
    public void degreeOfDifficulty() {
        int rows, columns, landmines;
        System.out.print("Please enter the amount of rows(9 - 24): ");
        rows = scan.nextInt();
        System.out.print("Please enter the amount of columns(9 - 30): ");
        columns = scan.nextInt();
        System.out.printf("Please enter the amount of land mines(10 - %3d): ", (rows - 1) * (columns - 1));
        landmines = scan.nextInt();
        customize = new Config(rows, columns, landmines);
    }

    @Override
    public void gameDifficultyChoose() {
        int choose;
        System.out.println("1: easy");
        System.out.println("2: normal");
        System.out.println("3: hard");
        System.out.println("4: customize");
        System.out.print("Which difficulty do you want to choose? ");
        choose = scan.nextInt();

        switch(choose) {
            case 1:
                mapConstructs(easy);
                break;
            case 2:
                mapConstructs(normal);
                break;
            case 3:
                mapConstructs(hard);
                break;
            case 4:
                degreeOfDifficulty();
                mapConstructs(customize);
                break;
        }
        gamePlay();
    }

    @Override
    public void gameStart() {
        Coordinate player;
        printMap();
        System.out.print("Please enter a coordinate(x y): ");
        player = getCoordinate();
        map.landMineGenerate(player);
        map.landMineTester(player);
    }

    @Override
    public void gamePlay() {
        Coordinate player;
        int modChoose = 0;
        gameStart();
        printMap();
        isWin();

        while (!over) {
            System.out.print("Please enter a coordinate(x y): ");
            player = getCoordinate();
            while (map.overRange(player)) {
                System.out.print("It's over range, please enter again: ");
                player = getCoordinate();
            }
            System.out.print("Please choose action(1:sweep, 2:mark): ");
            modChoose = scan.nextInt();
            switch (modChoose) {
                case 1:
                    sweep(player);
                    break;
                case 2:
                    mark(player);
                    break;
            }
            printMap();
        }

        if (sweepLandMine) {
            System.out.println("You lose");
        }
        else {
            System.out.println("You win");
        }

        System.out.print("Enter any charactor to continue...");
        scan.next();
    }

    @Override
    public Coordinate getCoordinate() {
        int x, y;
        x = scan.nextInt();
        y = scan.nextInt();
        return new Coordinate(x - 1, y - 1);
    }

    public void printMap() {
        int i;
        Coordinate loop = new Coordinate();
        MapInformation mapValue;
        Boolean markValue;
        Integer aroundValue;

        System.out.print("  x:");
        for (i = 1; i <= map.getColumns(); i++) {
            System.out.printf("%2d", i);
        }
        System.out.printf("        ");
        System.out.printf("land mines: %2d%n", (map.getLandMines() - flag));

        System.out.print(" y");
        for (i = 0; i <= map.getColumns() + 1; i++) {
            System.out.print("--");
        }
        System.out.println();

        for (loop.y = 0; loop.y < map.getRows(); loop.y++) {
            System.out.printf("%2d--", loop.y + 1);

            for (loop.x = 0; loop.x < map.getColumns(); loop.x++) {
                mapValue = map.getMapValue(loop);
                markValue = map.getMarkValue(loop);
                aroundValue = map.getAroundValue(loop);

                if (markValue.booleanValue()) {
                    if (this.sweepLandMine && !mapValue.equals(MapInformation.LandMine)) {
                        System.out.print("XX");
                    } 
                    else {
                        System.out.print("◢");
                    }
                }
                else if (mapValue.equals(MapInformation.NonSweep)) {
                    System.out.print("[]");
                }
                else if (mapValue.equals(MapInformation.Space)) {
                    System.out.print("  ");
                }
                else if (mapValue.equals(MapInformation.MineAround)) {
                    System.out.printf("%2d", aroundValue.intValue());
                }
                else if (mapValue.equals(MapInformation.LandMine)) {
                    if (this.sweepLandMine) {
                        System.out.printf("%s", "※");
                    }
                    else {
                        System.out.print("[]");
                    }
                }
                else if (mapValue.equals(MapInformation.SweepLandMine)) {
                    System.out.printf("%s", "◎");
                }
            }

            System.out.println("--");
        }

        System.out.print("  ");
        for (i = 0; i <= map.getColumns() + 1; i++) {
            System.out.print("--");
        }

        System.out.println();
    }
}
