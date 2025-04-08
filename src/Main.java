import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Main {
    static boolean[][] visited;

    static char[][] map;

    static char[] mapLegend = {'B', 'O', 'X', 'S'};


    /*
     * Path finding algorithm variables
     */

    static boolean canExit = false;
    static ArrayList<Integer> pathX = new ArrayList<>();
    static ArrayList<Integer> pathY = new ArrayList<>();

    static int startX, startY;
    static int rows, cols;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        startMenuGUI();

        visited = new boolean[rows][cols];
        map = new char[rows][cols];

        generateMap();

        // DEBUGGING DISPLAY

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        dfs(startX, startY);

        for (int i = 0; i < pathX.size(); i++) {
            System.out.println(pathX.get(i) + " " + pathY.get(i));
        }
    }

    public static void startMenuGUI() {
        JFrame welcomeFrame = new JFrame();
        welcomeFrame.setTitle("Sam & Edi Maze Assignment 2025");
        welcomeFrame.setSize(1920,1080);
        welcomeFrame.setLayout(new GridBagLayout());
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel preTitle = new JLabel();
        preTitle.setText("Welcome to the Sam & Edi");
        preTitle.setFont(new Font("Roboto", Font.PLAIN, 48));
        preTitle.setHorizontalAlignment(JLabel.CENTER);
        preTitle.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        welcomeFrame.add(preTitle, gbc);

        JLabel title = new JLabel();
        title.setText("Maze Assignment 2025");
        title.setFont(new Font("Roboto", Font.BOLD, 60));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        welcomeFrame.add(title, gbc);

        JButton mazeButton = new JButton();
        mazeButton.setText("Generate Random Maze");
        mazeButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        mazeButton.setMargin(new Insets(10,10,10,10));
        mazeButton.setHorizontalAlignment(JLabel.CENTER);
        mazeButton.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(60,0,0,0);

        mazeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mazeSettingGUI("Generate Random Maze");
            }
        } );

        welcomeFrame.add(mazeButton, gbc);

        JButton randMazeButton = new JButton();
        randMazeButton.setText("Generate Maze (Purely Random)");
        randMazeButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        randMazeButton.setMargin(new Insets(10,10,10,10));
        randMazeButton.setHorizontalAlignment(JLabel.CENTER);
        randMazeButton.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(60,0,0,0);

        randMazeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mazeSettingGUI("Generate Maze (Purely Random)");
            }
        } );

        welcomeFrame.add(randMazeButton, gbc);

        JButton readFileButton = new JButton();
        readFileButton.setText("Generate Maze (File)");
        readFileButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        readFileButton.setMargin(new Insets(10,10,10,10));
        readFileButton.setHorizontalAlignment(JLabel.CENTER);
        readFileButton.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(60,0,0,0);
        welcomeFrame.add(readFileButton, gbc);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.GREEN);

        welcomeFrame.setVisible(true);
    }

    public static void mazeSettingGUI(String currentSetting) {
        JFrame settingsFrame = new JFrame();
        settingsFrame.setTitle("Maze Settings");
        settingsFrame.setSize(800,600);
        settingsFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel();
        title.setText(currentSetting);
        title.setFont(new Font("Roboto", Font.BOLD, 48));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0,0,30,0);
        settingsFrame.add(title, gbc);

        JPanel sizeEditor = new JPanel();
        sizeEditor.setLayout(new FlowLayout());

        JLabel rowsText = new JLabel();
        rowsText.setText("Rows:");
        rowsText.setFont(new Font("Roboto", Font.BOLD, 28));
        rowsText.setHorizontalAlignment(JLabel.CENTER);
        rowsText.setVerticalAlignment(JLabel.CENTER);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0,0,0,0);
        sizeEditor.add(rowsText, gbc);

        SpinnerModel rowModel = new SpinnerNumberModel(5, 3, 20, 1);
        JSpinner rowSpinner = new JSpinner(rowModel);
        rowSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0,0,0,0);
        sizeEditor.add(rowSpinner, gbc);

        JLabel colsText = new JLabel();
        colsText.setText("     Columns:");
        colsText.setFont(new Font("Roboto", Font.BOLD, 28));
        colsText.setHorizontalAlignment(JLabel.CENTER);
        colsText.setVerticalAlignment(JLabel.CENTER);
//        gbc.gridx = 2;
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0,20,0,0);
        sizeEditor.add(colsText, gbc);

        SpinnerModel colModel = new SpinnerNumberModel(5, 3, 20, 1);
        JSpinner colSpinner = new JSpinner(colModel);
        colSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
//        gbc.gridx = 3;
//        gbc.gridy = 1;
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0,0,0,0);
        sizeEditor.add(colSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0,0,0,0);
        settingsFrame.add(sizeEditor, gbc);

        JButton generateMazeButton = new JButton();
        generateMazeButton.setText("Generate Maze");
        generateMazeButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        generateMazeButton.setMargin(new Insets(10,10,10,10));
        generateMazeButton.setHorizontalAlignment(JLabel.CENTER);
        generateMazeButton.setVerticalAlignment(JLabel.CENTER);

        generateMazeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rows = (Integer) rowSpinner.getValue();
                cols = (Integer) colSpinner.getValue();
            }
        } );

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(100,0,0,0);
        settingsFrame.add(generateMazeButton, gbc);

        settingsFrame.setVisible(true);
    }

    public static void generateMap() {
        ArrayList<Integer> openX = new ArrayList<>();
        ArrayList<Integer> openY = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 'B';
            }
        }

        openX.add((int) (Math.random() * (cols - 2)) + 1); // Generate Start X Position
        openY.add((int) (Math.random() * (rows - 2)) + 1); // Generate Start Y Position
        map[openY.get(0)][openX.get(0)] = 'S';

        startX = openX.get(0);
        startY = openY.get(0);

        boolean hasExit = false;

        while (!openX.isEmpty()) {
            int randomIdx = (int) (Math.random() * openX.size()); // Picks random open point
            int posX = openX.get(randomIdx), posY = openY.get(randomIdx);
            ArrayList<String> openDirections = new ArrayList<>();
            openDirections.add("up");
            openDirections.add("right");
            openDirections.add("down");
            openDirections.add("left");

            boolean builtNewPoint = false;
            while (!openDirections.isEmpty() && !builtNewPoint) {
                String randomDirection = openDirections.get((int) (Math.random() * openDirections.size()));
                switch (randomDirection) {
                    case "up": // Up
                        if (checkDirectionViable(posX, posY - 1, hasExit)) {
                            openY.add(posY - 1);
                            openX.add(posX);
                            map[posY - 1][posX] = 'O';
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("up");
                        }
                        break;
                    case "right": // Right
                        if (checkDirectionViable(posX + 1, posY, hasExit)) {
                            openY.add(posY);
                            openX.add(posX + 1);
                            map[posY][posX + 1] = 'O';
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("right");
                        }
                        break;
                    case "down": // Down
                        if (checkDirectionViable(posX, posY + 1, hasExit)) {
                            openY.add(posY + 1);
                            openX.add(posX);
                            map[posY + 1][posX] = 'O';
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("down");
                        }
                        break;
                    case "left": // Left
                        if (checkDirectionViable(posX - 1, posY, hasExit)) {
                            openY.add(posY);
                            openX.add(posX - 1);
                            map[posY][posX - 1] = 'O';
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("left");
                        }
                        break;
                }
            }
            if (builtNewPoint) {
                if (openX.get(openX.size() - 1) == 0 || openX.get(openX.size() - 1) == cols - 1 || openY.get(openY.size() - 1) == 0 || openY.get(openY.size() - 1) == rows - 1) {
                    map[openY.get(openY.size() - 1)][openX.get(openX.size() - 1)] = 'X';
                    openY.remove(openY.size() - 1);
                    openX.remove(openX.size() - 1);
                    hasExit = true;
                }
            } else {
                openY.remove(randomIdx);
                openX.remove(randomIdx);
            }
        }
    }

    /** This method checks whether the new randomly chosen point is viable to be set as an open path. There are three
     * different cases we must check for. First, the new point cannot be on the edge of the map if there is already an
     * exit point (Can't have two exit points). Second, the new point cannot be on a point that is already set to an
     * open path. Third, the new point cannot have any adjacent squares that are open paths. This prevents open areas
     * like this:
     *
     * B B X B
     * O O O B
     * B O O B
     *
     * @param posX - The x position of our new potential point.
     * @param posY - The y position of our new potential point.
     * @param hasExit - Whether we have already created an exit point.
     * @return Whether the new position is viable to set as an open path.
     */
    public static boolean checkDirectionViable(int posX, int posY, boolean hasExit) {
        int countAdjacent = 0;

        // Check if new position in on the edge of the map
        if (posY == 0 || posY == map.length - 1 || posX == 0 || posX == map[0].length - 1) {
            if (hasExit) { // If an exit has already been found, this new point is not viable
                return false;
            } else { // If an exit has not been found, this point will now be our exit point
                return true;
            }
        }

        if (map[posY][posX] == 'O' || map[posY][posX] == 'S') {
            return false;
        }

        if (map[posY - 1][posX] == 'O' || map[posY - 1][posX] == 'S') {
            countAdjacent++;
        }
        if (map[posY][posX + 1] == 'O' || map[posY][posX + 1] == 'S') {
            countAdjacent++;
        }
        if (map[posY + 1][posX] == 'O' || map[posY + 1][posX] == 'S') {
            countAdjacent++;
        }
        if (map[posY][posX - 1] == 'O' || map[posY][posX - 1] == 'S') {
            countAdjacent++;
        }

        return countAdjacent <= 1;
    }

    public static void dfs(int posX, int posY) {
    	if (canExit) {
    		return;
    	}
        if (map[posY][posX] == 'X') {
            canExit = true;
            return;
        }
        visited[posY][posX] = true;
        pathX.add(posX);
    	pathY.add(posY);
        if (map[posY][posX] != 'S') {
        	//Update GUI here
        	// GUI map[posY][posX] = '+'
        }
        if (map[posY][posX - 1] != 'B' && !visited[posY][posX - 1]) {
            dfs(posX - 1, posY);
        }
        if (map[posY][posX + 1] != 'B' && !visited[posY][posX + 1]) {
            dfs(posX + 1, posY);
        }
        if (map[posY - 1][posX] != 'B' && !visited[posY - 1][posX]) {
            dfs(posX, posY - 1);
        }
        if (map[posY + 1][posX] != 'B' && !visited[posY + 1][posX]) {
            dfs(posX, posY + 1);
        }
        if (!canExit) {
            pathX.remove(pathX.size() - 1);
            pathY.remove(pathY.size() - 1);
        }
    }
}