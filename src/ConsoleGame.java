import java.util.Scanner;

import map.Coordinate;

public class ConsoleGame implements Game {

    private Scanner scan;
    private MineSweeperMap map;

    public ConsoleGame() {
        super();
        scan = new Scanner(System.in);
    }

    private void mapConstructs(Difficulty d) {
        map = new MineSweeperMap(d.rows, d.columns, d.landmines);
    }

    @Override
    public void newGame() {
        gameDifficultyChoose();
        gamePlay();
    }

    public Difficulty degreeOfDifficulty() {
        int rows, columns, landmines;
        System.out.print("Please enter the amount of rows(9 - 24): ");
        rows = scan.nextInt();
        System.out.print("Please enter the amount of columns(9 - 30): ");
        columns = scan.nextInt();
        System.out.printf("Please enter the amount of land mines(10 - %3d): ", (rows - 1) * (columns - 1));
        landmines = scan.nextInt();
        return new Difficulty(rows, columns, landmines);
    }

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
                mapConstructs(Difficulty.EASY);
                break;
            case 2:
                mapConstructs(Difficulty.NORMAL);
                break;
            case 3:
                mapConstructs(Difficulty.HARD);
                break;
            case 4:
                Difficulty customize = degreeOfDifficulty();
                mapConstructs(customize);
                break;
        }
    }

    public void gameStart() {
        Coordinate player;
        printMap();
        System.out.print("Please enter a coordinate(x y): ");
        player = getCoordinate();
        map.landMineGenerate(player);
        map.sweep(player);
    }

    public void gamePlay() {
        Coordinate player;
        int modChoose = 0;
        gameStart();
        printMap();
        map.checkWin();

        while (!map.isEnd()) {
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
                    map.sweep(player);
                    break;
                case 2:
                    map.mark(player);
                    break;
            }
            printMap();
        }

        if (!map.isWin()) {
            System.out.println("You lose");
        }
        else {
            System.out.println("You win");
        }

        System.out.print("Enter any charactor to continue...");
        scan.next();
    }

    public Coordinate getCoordinate() {
        int x, y;
        x = scan.nextInt();
        y = scan.nextInt();
        return new Coordinate(x - 1, y - 1);
    }

    public void printMap() {
        int i;
        Coordinate loop = new Coordinate();
        Block mapValue;

        System.out.print("  x:");
        for (i = 1; i <= map.getColumns(); i++) {
            System.out.printf("%2d", i);
        }
        System.out.printf("        ");
        System.out.printf("land mines: %2d%n", (map.getLandMines() - map.getFlag()));

        System.out.print(" y");
        for (i = 0; i <= map.getColumns() + 1; i++) {
            System.out.print("--");
        }
        System.out.println();

        for (loop.y = 0; loop.y < map.getRows(); loop.y++) {
            System.out.printf("%2d--", loop.y + 1);

            for (loop.x = 0; loop.x < map.getColumns(); loop.x++) {
                mapValue = map.getMapValue(loop);

                if (mapValue.haveFlag()) {
                    if (!map.isWin() && !mapValue.isLandMine()) {
                        System.out.print("XX");
                    } 
                    else {
                        System.out.print("◢");
                    }
                }
                else if (!mapValue.isOpen()) {
                    if (!map.isWin() && mapValue.isLandMine()) {
                        System.out.print("※");
                    }
                    else {
                        System.out.print("[]");
                    }
                }
                else if (mapValue.isSweepLandMine()) {
                    System.out.print("◎");
                }
                else if (mapValue.isSpace()) {
                    System.out.print("  ");
                }
                else if (!mapValue.isSpace()) {
                    System.out.printf("%2d", mapValue.getMineAround());
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
