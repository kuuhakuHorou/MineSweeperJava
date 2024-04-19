import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import map.Coordinate;

public class PlayBoard extends JPanel {

    private final int gap = 5;
    private final Color background = Color.LIGHT_GRAY;
    private MineSweeperMap map;
    private Difficulty difficulty;
    private int width;
    private Board board;
    private ScoreBoard scoreBoard;
    private Timer timer;
    private int timerSecond;

    private boolean first_sweep;
    
    public PlayBoard(Difficulty dif, int w) {
        difficulty = dif;
        mapConstructs(dif);
        width = w;
        first_sweep = true;
        timerSecond = 0;
        board = new Board();
        scoreBoard = new ScoreBoard();
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timerSecond++;
                scoreBoard.repaint();
            }
        });

        setLayout(new BorderLayout());
        add(scoreBoard, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
    }

    private void mapConstructs(Difficulty d) {
        map = new MineSweeperMap(d.rows, d.columns, d.landmines);
    }

    private java.net.URL getResourceURL(String path) {
        return this.getClass().getResource(path);
    }

    public void restart() {
        this.first_sweep = true;
        map.initialization();
        scoreBoard.setFaceIndex(0);
        repaint();
        timer.stop();
        timerSecond = 0;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            this.width = width;
            scoreBoard.setWidth();
            board.changeSize();
            repaint();
        }
    }

    public void setDifficulty(Difficulty d) {
        if (!this.difficulty.equals(d)) {
            this.difficulty = d;
            mapConstructs(d);
            scoreBoard.setWidth();
            board.changeSize();
            restart();
        }
    }

    private int getLandMinesAmount() {
        return map.getLandMines() - map.getFlag();
    }

    class ScoreBoard extends JPanel implements MouseListener {

        private int width;
        private int faceIndex;
        private Image[] counter;
        private Image[] face;
        private Timer ms500;

        public ScoreBoard() {
            loadImage();
            setWidth();
            faceIndex = 0;
            addMouseListener(this);
            ms500 = new Timer(500, null);
            ms500.setRepeats(false);
        }

        public void setWidth() {
            this.width = (int)(PlayBoard.this.width * 1.6);
            setPreferredSize(new Dimension(map.getColumns() * PlayBoard.this.width + gap * 2, width + gap * 4));
        }

        public void setFaceIndex(int index) {
            faceIndex = index;
        }

        private void loadImage() {
            String[] path = new String[]{"/img/face_unpress.png","/img/face_win.png","/img/face_lose.png","/img/face_press.png"};
            java.net.URL url;
            face = new Image[path.length];
            for (int i = 0; i < path.length; i++) {
                url = getResourceURL(path[i]);
                face[i] = (new ImageIcon(url)).getImage();
            }
            counter = new Image[10];
            for (int i = 0; i < 10; i++) {
                url = getResourceURL("/img/counter_" + i + ".png");
                counter[i] = (new ImageIcon(url)).getImage();
            }
        }

        public void paintCounter(int x, int y, int value, Graphics g) {
            int height = width;
            int width = this.width / 2;
            int i;
            int[] index = new int[3];
            for (i = 0; i < 3; i++) {
                index[i] = value % 10;
                value /= 10;
            }
            for (i = 0; i < 3; i++) {
                g.drawImage(counter[index[2 - i]], x + i * width, y, width, height, this);
            }
        }

        public void paintFace(int index, Graphics g) {
            int x = this.getWidth() / 2 - this.width / 2;
            int y = gap * 2;
            g.drawImage(face[index], x, y, width, width, this);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(background);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            this.paintCounter(gap * 2, gap * 2, getLandMinesAmount(), g);
            this.paintFace(faceIndex, g);
            this.paintCounter(this.getWidth() - gap * 2 - 3 * this.width / 2, gap * 2, timerSecond, g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            paintFace(3, getGraphics());
            ms500.start();
            while (ms500.isRunning());
            restart();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
    }

    class Board extends JPanel implements MouseListener {

        private Image cover;
        private Image[] type;
        private Image flag;
        private Image mine;
        private Image mine_red;
        private Image wrong_flag;

        public Board() {
            changeSize();
            loadImage();
            addMouseListener(this);
        }

        public void changeSize() {
            setPreferredSize(new Dimension(map.getColumns() * width + gap * 2, map.getRows() * width + gap));
        }

        private void loadImage() {
            java.net.URL image_location = getResourceURL("/img/cover.png");
            cover = (new ImageIcon(image_location)).getImage();
            image_location = getResourceURL("/img/flag.png");
            flag = (new ImageIcon(image_location)).getImage();
            image_location = getResourceURL("/img/wrong_flag.png");
            wrong_flag = (new ImageIcon(image_location)).getImage();
            image_location = getResourceURL("/img/mine.png");
            mine = (new ImageIcon(image_location)).getImage();
            image_location = getResourceURL("/img/mine_red.png");
            mine_red = (new ImageIcon(image_location)).getImage();
            type = new Image[9];
            for (int i = 0; i <= 8; i++) {
                image_location = getResourceURL("/img/type_" + i + ".png");
                type[i] = (new ImageIcon(image_location)).getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Coordinate loop = new Coordinate();
            Block mapValue;
            Image cell = cover;
            g.setColor(background);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            for (loop.y = 0; loop.y < map.getRows(); loop.y++) {
                for (loop.x = 0; loop.x < map.getColumns(); loop.x++) {
                    mapValue = map.getMapValue(loop);

                    if (mapValue.haveFlag()) {
                        if (!map.isWin() && !mapValue.isLandMine()) {
                            cell = wrong_flag;
                            // System.out.print("XX");
                        } 
                        else {
                            cell = flag;
                            // System.out.print("◢");
                        }
                    }
                    else if (!mapValue.isOpen()) {
                        if (!map.isWin() && mapValue.isLandMine()) {
                            cell = mine;
                            // System.out.print("※");
                        }
                        else {
                            cell = cover;
                            // System.out.print("[]");
                        }
                    }
                    else if (mapValue.isSweepLandMine()) {
                        cell = mine_red;
                        // System.out.print("◎");
                    }
                    else if (mapValue.isSpace()) {
                        cell = type[0];
                        // System.out.print("  ");
                    }
                    else if (!mapValue.isSpace()) {
                        cell = type[mapValue.getMineAround()];
                        // System.out.printf("%2d", mapValue.getMineAround());
                    }

                    g.drawImage(cell, gap + loop.x * width, loop.y * width, width, width, this);
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {
            if (map.isEnd()) return;
            boolean isRepaint = false;
            int x = e.getX() - gap;
            int y = e.getY();
            if (x < 0) return;
            Coordinate player = new Coordinate(x / width, y / width);
            if (map.overRange(player)) return;
            Block mapValue = map.getMapValue(player);

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (!timer.isRunning()) {
                    timer.restart();
                }
                if (first_sweep) {
                    isRepaint = true;
                    first_sweep = false;
                    map.landMineGenerate(player);
                    map.sweep(player);
                }
                else if (!(mapValue.haveFlag() || mapValue.isSpace())) {
                    isRepaint = true;
                    map.sweep(player);
                }
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
                if (!timer.isRunning()) {
                    timer.restart();
                }
                if (!mapValue.isOpen() || mapValue.haveFlag()) {
                    isRepaint = true;
                    map.mark(player);
                }
            }

            if (map.isEnd()) {
                timer.stop();
                if (map.isWin()) {
                    scoreBoard.setFaceIndex(1);
                }
                else {
                    scoreBoard.setFaceIndex(2);
                }
            }
            if (isRepaint) {
                scoreBoard.repaint();
                repaint();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

    }
}
