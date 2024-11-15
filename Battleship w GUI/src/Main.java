import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

    static JFrame frame = new JFrame();
    static JPanel header, footer, rNavbar, center, lNavbar, hpPanel, hpBar;
    static JLabel rLabel, lLabel, headerLabel, hpLabel;
    static JButton[][] buttons;
    static boolean[][] table, clicked;
    static int shipCounter = 0, totalShips, increment, size = 6, hp = 1000, currentHP = 1000;
    static Timer timer;

    public static void main(String[] args) {

        initializeDigitalBoard();

        //Initialize button-clicked checker
        clicked = new boolean[size][size];
        for(int row = 0;row < size;row++){
            for(int column = 0;column < size;column++) {
                clicked[row][column] = false;
            }
        }

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);   //Goes Fullscreen
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        //Header
        header = new JPanel();
        header.setBackground(Color.blue);
        header.setPreferredSize(new Dimension(50,50));
        header.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLUE));

        headerLabel = new JLabel();

        //Footer
        footer = new JPanel();
        footer.setBackground(Color.DARK_GRAY);
        footer.setPreferredSize(new Dimension(50,50));
        footer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        footer.setLayout(new BorderLayout());

        hpPanel = new JPanel();
        hpPanel.setLayout(new BorderLayout());
        hpPanel.setPreferredSize(new Dimension(300,50));

        hpLabel = new JLabel("HP: "+currentHP+" / "+hp, SwingConstants.CENTER);
        hpLabel.setForeground(Color.BLACK);
        hpLabel.setFont(new Font("Arial", Font.BOLD, 40));

        hpBar = new JPanel();
        hpBar.setBackground(Color.RED);
        hpBar.setOpaque(true);
        hpBar.setPreferredSize(new Dimension(300, 30));

        hpPanel.add(hpBar, BorderLayout.CENTER);
        footer.add(hpPanel, BorderLayout.CENTER);
        footer.add(hpLabel, BorderLayout.SOUTH);

        //Right Navbar
        rNavbar = new JPanel();
        rNavbar.setBackground(Color.RED);
        rNavbar.setPreferredSize(new Dimension(300,300));
        rNavbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.RED));
        rNavbar.setLayout(new BorderLayout());

        rLabel = new JLabel("FIRE!");
        rLabel.setHorizontalAlignment(JLabel.CENTER);
        rLabel.setForeground(Color.BLACK);
        rLabel.setFont(new Font("Arial", Font.BOLD, 40));

        //Appending components to RNavbar
        rNavbar.add(rLabel, BorderLayout.CENTER);

        //Left Navbar
        lNavbar = new JPanel();
        lNavbar.setBackground(Color.RED);
        lNavbar.setPreferredSize(new Dimension(300,300));
        lNavbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.RED));
        lNavbar.setLayout(new BorderLayout());

        lLabel = new JLabel();
        lLabel.setText(String.valueOf(shipCounter));
        lLabel.setHorizontalAlignment(JLabel.CENTER);
        lLabel.setForeground(Color.BLACK);
        lLabel.setFont(new Font("Arial", Font.BOLD, 50));

        lNavbar.add(lLabel, BorderLayout.CENTER);

        //Center
        center = new JPanel();
        center.setBackground(Color.LIGHT_GRAY);
        center.setPreferredSize(new Dimension(300,300));
        center.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
        center.setLayout(new GridLayout(size,size));

        //Make the game field
        instantiateButtons();

        //Appending components to MainFrame
        frame.add(header, BorderLayout.NORTH);
        frame.add(footer, BorderLayout.SOUTH);
        frame.add(lNavbar, BorderLayout.WEST);
        frame.add(rNavbar, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true); //place this at the bottom
    }

    static void instantiateButtons(){
        buttons = new JButton[size][size];
        for(int row = 0;row < size;row++){
            for(int column = 0;column < size;column++){
                int x = row;
                int y = column;
                buttons[row][column] = new JButton();

                buttons[row][column].addActionListener(e -> {
                    for (int i = 0; i < buttons.length; i++) {
                        for (int j = 0; j < buttons[i].length; j++) {
                            if(!clicked[i][j]) {
                                buttons[i][j].setOpaque(false);
                            }
                            buttons[i][j].setEnabled(false);
                        }
                    }
                    playSound("fire.wav");
                    rLabel.setText("FIRING...");
                    timer = new Timer(1500, evt -> {
                        rLabel.setText(checker(x, y));
                        timer = new Timer(1500, evt2 -> {
                            rLabel.setText("FIRE!");
                            for (JButton[] button : buttons) {
                                for (JButton jButton : button) {
                                    jButton.setEnabled(true);
                                    jButton.setOpaque(true);
                                }
                            }
                            buttons[x][y].setEnabled(false);
                            //Winner check
                            if(shipCounter == 0) {
                                headerLabel.setText("You WIN!");
                                headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
                                headerLabel.setForeground(Color.BLACK);
                                header.add(headerLabel);
                                //Disable all tiles
                                for (int i = 0; i < buttons.length; i++) {
                                    for (int j = 0; j < buttons[i].length; j++) {
                                        if (!clicked[i][j]) {
                                            buttons[i][j].setOpaque(false);
                                        }
                                        buttons[i][j].setEnabled(false);
                                    }
                                }
                            }
                            //Loser check
                            if(currentHP == 0){
                                headerLabel.setText("You LOSE!");
                                headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
                                headerLabel.setForeground(Color.BLACK);
                                header.add(headerLabel);
                                //Disable all tiles
                                for (int i = 0; i < buttons.length; i++) {
                                    for (int j = 0; j < buttons[i].length; j++) {
                                        if(!clicked[i][j]) {
                                            buttons[i][j].setOpaque(false);
                                        }
                                        buttons[i][j].setEnabled(false);
                                    }
                                }
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    });
                    timer.setRepeats(false);
                    timer.start();
                });

                buttons[row][column].setFocusable(false);
                buttons[row][column].setOpaque(true);
                buttons[row][column].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                buttons[row][column].setBackground(Color.WHITE);

                center.add(buttons[row][column]);
            }
        }
    }

    static void initializeDigitalBoard(){
        table = new boolean[size][size];
        for(int row = 0;row < size;row++){
            for(int column = 0;column < size;column++) {
                int rand = (int) (Math.random() * 5);
                if(rand == 0){
                    shipCounter++;
                    table[row][column] = true;
                }
            }
        }

        totalShips = shipCounter;

        //Print the table in Terminal
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                System.out.print(table[row][column] + " ");
            }
            System.out.println();
        }
    }

    static int remainingTiles(){
        int counter = 0;
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if(!clicked[i][j]) {
                    counter++;
                }
            }
        }
        return counter;
    }

    static String checker(int x, int y){
        System.out.println(x+" "+y);
        clicked[x][y] = true;

        if(table[x][y]){
            buttons[x][y].setOpaque(true);
            buttons[x][y].setBackground(new Color(0x019A01));
            playSound("hit.wav");
            shipCounter--;
            lLabel.setText(String.valueOf(shipCounter));
            return "HIT!";
        } else{
            buttons[x][y].setOpaque(true);
            buttons[x][y].setBackground(new Color(0xB53737));
            playSound("miss.wav");
            
            if (currentHP != 0) {
                decreaseHP();
            }
            System.out.println("Damage: "+increment);
            return "MISS!";
        }
    }

    static void decreaseHP() {
        increment = (int) ((hp) / ((remainingTiles() - shipCounter) * 1.5));
        if (currentHP > 0) {
            if(increment >= currentHP){
                currentHP = 0;
            } else {
                currentHP -= increment;
            }

            int newWidth = (int) (((currentHP / (double) hp)/100) * 300);

            hpBar.setPreferredSize(new Dimension(newWidth, 30));
            hpBar.revalidate();

            hpLabel.setText("HP: "+currentHP+" / "+hp);
        }
    }

    static public void playSound(String file) {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
}
}