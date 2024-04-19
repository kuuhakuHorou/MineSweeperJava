import javax.swing.*;
import javax.swing.event.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;


public class FrameGame extends JFrame implements Game {
    private Difficulty dif = Difficulty.HARD;
    private int width = 24;
    private PlayBoard pb;
    private JCheckBoxMenuItem easy;
    private JCheckBoxMenuItem normal;
    private JCheckBoxMenuItem hard;
    private JCheckBoxMenuItem customize;

    private CustomizeDialog customizeDialog;

    public FrameGame() {
        super("Minesweeper");
        setLayout(new BorderLayout());
        pb = new PlayBoard(dif, width);
        // add(text, BorderLayout.NORTH);
        add(pb, BorderLayout.CENTER);
        setJMenuBar(menuConstruct());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // customizeDialog = new JDialog(this, "Customize", true);
        customizeDialog = new CustomizeDialog(this);
        // customizeDialog.add(new CustomizePanel());
        customizeDialog.pack();
        customizeDialog.setLocationRelativeTo(this);
        customizeDialog.setResizable(false);
    }

    private JMenuBar menuConstruct() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Setting");
        menu.setMnemonic(KeyEvent.VK_S);

        ButtonGroup group = new ButtonGroup();
        easy = new JCheckBoxMenuItem("Easy");
        if (dif.equals(Difficulty.EASY)) {
            easy.setSelected(true);
        }
        easy.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changeDifficuly(e);
            }
        });
        menu.add(easy);
        group.add(easy);

        normal = new JCheckBoxMenuItem("Normal");
        if (dif.equals(Difficulty.NORMAL)) {
            normal.setSelected(true);
        }
        normal.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changeDifficuly(e);
            }
        });
        menu.add(normal);
        group.add(normal);

        hard = new JCheckBoxMenuItem("Hard");
        if (dif.equals(Difficulty.HARD)) {
            hard.setSelected(true);
        }
        hard.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changeDifficuly(e);
            }
        });
        menu.add(hard);
        group.add(hard);

        customize = new JCheckBoxMenuItem("Customize");
        customize.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changeDifficuly(e);
            }
        });
        menu.add(customize);
        group.add(customize);

        menuBar.add(menu);
        return menuBar;
    }

    public void changeWidth(int w) {
        pb.setWidth(w);
        pack();
    }

    public void changeDifficuly(Difficulty d) {
        pb.setDifficulty(d);
        pack();
    }

    private void changeDifficuly(ItemEvent e) {
        JCheckBoxMenuItem c = (JCheckBoxMenuItem)e.getSource();
        if (!c.isSelected()) return;
        Difficulty d = dif;
        
        if (c == easy) {
            dif = Difficulty.EASY;
        }
        else if (c == normal) {
            dif = Difficulty.NORMAL;
        }
        else if (c == hard) {
            dif = Difficulty.HARD;
        }
        else {
            customizeDialog.reEnter();
            customizeDialog.setVisible(true);
            while (!customizeDialog.isDone());
            if (!customizeDialog.isCanceled()) {
                dif = new Difficulty(customizeDialog.getRowAmount(), customizeDialog.getColumnAmount(), customizeDialog.getLandMineAmount());
            }
        }

        if (!d.equals(dif)) {
            changeDifficuly(dif);
            setLocationRelativeTo(null);
        }
    }

    @Override
    public void newGame() {
        setVisible(true);
    }

    class CustomizeDialog extends JDialog {

        private boolean isFinish;
        private boolean isCancel;
        private final Font buttonFont = new Font(null, Font.BOLD, 14);
        private final Font labelFont = new Font(null, Font.PLAIN, 14);

        JPanel upPanel;
        JPanel downPanel;
        JPanel yesNoPanel;

        JLabel rowLabel;
        JLabel columnLabel;
        JLabel landMineLabel;
        JLabel rowAmountLabel;
        JLabel columnAmountLabel;
        JLabel landMineAmountLabel;

        JSlider rowSlider;
        JSlider columnSlider;
        JSlider landMineSlider;

        JButton rowAdd;
        JButton rowMinus;
        JButton columnAdd;
        JButton columnMinus;
        JButton landMineAdd;
        JButton landMineMinus;

        JButton sureButton;
        JButton cancelButton;

        public CustomizeDialog(JFrame frame) {
            super(frame, "Customize", true);
            reEnter();
            initializeComponents();
            initializeLayout();

            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    isFinish = true;
                    isCancel = true;
                }
            });
        }

        public void reEnter() {
            isFinish = false;
            isCancel = false;
        }

        public boolean isDone() {
            return isFinish;
        }

        public boolean isCanceled() {
            return isCancel;
        }

        public int getRowAmount() {
            return rowSlider.getValue();
        }

        public int getColumnAmount() {
            return columnSlider.getValue();
        }

        public int getLandMineAmount() {
            return landMineSlider.getValue();
        }

        private void initializeComponents() {
            upPanel = new JPanel();
            downPanel = new JPanel();
            yesNoPanel = new JPanel();

            rowLabel = new JLabel("Rows: ");
            columnLabel = new JLabel("Columns: ");
            landMineLabel = new JLabel("Land mines: ");
            rowAmountLabel = new JLabel("9", SwingConstants.RIGHT);
            columnAmountLabel = new JLabel("9", SwingConstants.RIGHT);
            landMineAmountLabel = new JLabel("10", SwingConstants.RIGHT);
            rowLabel.setFont(labelFont);
            columnLabel.setFont(labelFont);
            landMineLabel.setFont(labelFont);
            rowAmountLabel.setFont(labelFont);
            columnAmountLabel.setFont(labelFont);
            landMineAmountLabel.setFont(labelFont);

            rowSlider = new JSlider(9, 24, 9);

            rowSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    rowAmountLabel.setText(String.format("%3d", rowSlider.getValue()));
                    landMineSlider.setMaximum((rowSlider.getValue() - 1) * (columnSlider.getValue() - 1));
                }
            });

            rowAdd = new JButton("+");
            rowMinus = new JButton("-");
            rowAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rowSlider.setValue(rowSlider.getValue() + 1);
                }
            });
            rowAdd.setFocusable(false);
            rowAdd.setFont(buttonFont);

            rowMinus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rowSlider.setValue(rowSlider.getValue() - 1);
                }
            });
            rowMinus.setFocusable(false);
            rowMinus.setFont(buttonFont);

            columnSlider = new JSlider(9, 30, 9);
            columnSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    columnAmountLabel.setText(String.format("%3d", columnSlider.getValue()));
                    landMineSlider.setMaximum((rowSlider.getValue() - 1) * (columnSlider.getValue() - 1));
                }
            });

            columnAdd = new JButton("+");
            columnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    columnSlider.setValue(columnSlider.getValue() + 1);
                }
            });
            columnAdd.setFocusable(false);
            columnAdd.setFont(buttonFont);

            columnMinus = new JButton("-");
            columnMinus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    columnSlider.setValue(columnSlider.getValue() - 1);
                }
            });
            columnMinus.setFocusable(false);
            columnMinus.setFont(buttonFont);

            landMineSlider = new JSlider(10, (rowSlider.getValue() - 1) * (columnSlider.getValue() - 1), 10);
            landMineSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    landMineAmountLabel.setText(String.format("%3d", landMineSlider.getValue()));
                }
            });

            landMineAdd = new JButton("+");
            landMineAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    landMineSlider.setValue(landMineSlider.getValue() + 1);
                }
            });
            landMineAdd.setFocusable(false);
            landMineAdd.setFont(buttonFont);

            landMineMinus = new JButton("-");
            landMineMinus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    landMineSlider.setValue(landMineSlider.getValue() - 1);
                }
            });
            landMineMinus.setFocusable(false);
            landMineMinus.setFont(buttonFont);

            sureButton = new JButton("Sure");
            sureButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    customizeDialog.setVisible(false);
                    isFinish = true;
                }
            });
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    customizeDialog.setVisible(false);
                    isFinish = true;
                    isCancel = true;
                }
            });
        }

        private void initializeLayout() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.add(upPanel);
            panel.add(downPanel);
            panel.add(yesNoPanel);
            this.setContentPane(panel);

            upPanel.setLayout(new BorderLayout());
            panel = new JPanel();
            upPanel.add(panel, BorderLayout.WEST);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            Box box = Box.createVerticalBox();
            box.add(rowLabel);
            box.add(columnLabel);
            box.add(landMineLabel);
            panel.add(box);

            // upPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            panel = new JPanel();
            upPanel.add(panel, BorderLayout.EAST);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            box = Box.createVerticalBox();
            rowAmountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            columnAmountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            landMineAmountLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            box.add(rowAmountLabel);
            box.add(columnAmountLabel);
            box.add(landMineAmountLabel);
            panel.add(box);
            upPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            GroupLayout layout = new GroupLayout(downPanel);
            downPanel.setLayout(layout);

            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
            GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

            hGroup.addGroup(layout.createParallelGroup().addComponent(rowSlider).addComponent(columnSlider).addComponent(landMineSlider));
            hGroup.addGroup(layout.createParallelGroup().addComponent(rowAdd).addComponent(columnAdd).addComponent(landMineAdd));
            hGroup.addGroup(layout.createParallelGroup().addComponent(rowMinus).addComponent(columnMinus).addComponent(landMineMinus));
            vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(rowSlider).addComponent(rowAdd).addComponent(rowMinus));
            vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(columnSlider).addComponent(columnAdd).addComponent(columnMinus));
            vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER).addComponent(landMineSlider).addComponent(landMineAdd).addComponent(landMineMinus));
            layout.setHorizontalGroup(hGroup);
            layout.setVerticalGroup(vGroup);

            yesNoPanel.setLayout(new BoxLayout(yesNoPanel, BoxLayout.LINE_AXIS));
            yesNoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            yesNoPanel.add(sureButton);
            yesNoPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            yesNoPanel.add(cancelButton);
            yesNoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
    }
}
