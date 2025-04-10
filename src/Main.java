import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

public class Main {
    static boolean[][] visited;

    static char[][] map;

    static char[] mapLegend = {'B', 'O', 'S', 'X', '+'};
    static HashMap<Character, Color> colourLegend = new HashMap<>();


    /*
     * Path finding algorithm variables
     */

    static boolean canExit = false;
    static ArrayList<Integer> pathX = new ArrayList<>();
    static ArrayList<Integer> pathY = new ArrayList<>();

    static int startX, startY;
    static int rows, cols;

    public static void main(String[] args) throws IOException {
        colourLegend.put('B',new Color(157, 119, 95));
        colourLegend.put('O',new Color(246, 228, 145));
        colourLegend.put('S',new Color(176, 176, 176));
        colourLegend.put('X',new Color(190, 248, 171));
        colourLegend.put('+',new Color(104, 223, 248));
        buildStartMenuGUI();
    }

    public static void buildStartMenuGUI() {
        JFrame welcomeFrame = new JFrame();
        welcomeFrame.setTitle("Sam & Edi Maze Assignment 2025");
        welcomeFrame.setSize(1920, 1080);
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
                buildMazeSettingGUI("Generate Random Maze", welcomeFrame);
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
                buildMazeSettingGUI("Generate Maze (Purely Random)", welcomeFrame);
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

        // TODO read file, need to change button
        readFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFileGUI();
            }
        } );

        welcomeFrame.add(readFileButton, gbc);

        JButton exitButton = new JButton();
        exitButton.setText("Exit Program");
        exitButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        exitButton.setMargin(new Insets(10,10,10,10));
        exitButton.setHorizontalAlignment(JLabel.CENTER);
        exitButton.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(60,0,0,0);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        } );

        welcomeFrame.add(exitButton, gbc);

        welcomeFrame.setVisible(true);
    }

    public static void buildMazeSettingGUI(String currentSetting, JFrame welcomeFrame) {
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
        sizeEditor.add(rowsText, gbc);

        SpinnerModel rowModel = new SpinnerNumberModel(20, 3, 50, 1);
        JSpinner rowSpinner = new JSpinner(rowModel);
        rowSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
        sizeEditor.add(rowSpinner, gbc);

        JLabel colsText = new JLabel();
        colsText.setText("     Columns:");
        colsText.setFont(new Font("Roboto", Font.BOLD, 28));
        colsText.setHorizontalAlignment(JLabel.CENTER);
        colsText.setVerticalAlignment(JLabel.CENTER);
        sizeEditor.add(colsText, gbc);

        SpinnerModel colModel = new SpinnerNumberModel(20, 3, 50, 1);
        JSpinner colSpinner = new JSpinner(colModel);
        colSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
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
                settingsFrame.setVisible(false);
                welcomeFrame.setVisible(false);
                if (currentSetting.equals("Generate Random Maze")) {
                	generateMap();
                }
                else {
                	generateRandomMap();
                }
                buildMazeGUI();
            }
        } );

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(100,0,0,0);
        settingsFrame.add(generateMazeButton, gbc);

        settingsFrame.setVisible(true);
    }

    public static void buildMazeGUI() {
        JFrame mazeFrame = new JFrame();
        mazeFrame.setVisible(true);
        mazeFrame.getContentPane().removeAll();
        mazeFrame.setTitle("Maze");
        mazeFrame.setSize(1800, 1080);
        mazeFrame.setLayout(new BorderLayout());
        mazeFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                buildStartMenuGUI();
            }
        });

        JPanel mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows,cols));
        mazeFrame.add(mazePanel, BorderLayout.CENTER);


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JPanel positionPanel = new JPanel(new GridBagLayout());
                positionPanel.setBorder(BorderFactory.createLineBorder(Color.black));

                JLabel positionLabel = new JLabel();
                positionLabel.setFont(new Font("Roboto", Font.BOLD, 32));
                positionLabel.setHorizontalAlignment(JLabel.CENTER);
                positionLabel.setVerticalAlignment(JLabel.CENTER);

                for (int k = 0; k < pathX.size(); k++) {
                    if (pathX.get(k) == j && pathY.get(k) == i) {
                        positionLabel.setText("+");
                        positionPanel.setBackground(colourLegend.get('+'));
                    }
                }
                if (positionLabel.getText().isEmpty()) {
                    positionLabel.setText("" + map[i][j]);
                    positionPanel.setBackground(colourLegend.get(map[i][j]));
                }

                positionPanel.add(positionLabel);
                mazePanel.add(positionPanel);
            }
        }

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mazeFrame.add(buttonsPanel, BorderLayout.SOUTH);

        JButton mainMenuButton = new JButton();
        mainMenuButton.setText("Main Menu");
        mainMenuButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        mainMenuButton.setMargin(new Insets(10,10,10,10));
        mainMenuButton.setHorizontalAlignment(JLabel.CENTER);
        mainMenuButton.setVerticalAlignment(JLabel.CENTER);

        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mazeFrame.setVisible(false);
                buildStartMenuGUI();
            }
        } );
        buttonsPanel.add(mainMenuButton, BorderLayout.LINE_START);

        JLabel progressLabel = new JLabel();
        progressLabel.setText("");
        progressLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        progressLabel.setVerticalAlignment(JLabel.CENTER);
        buttonsPanel.add(progressLabel, BorderLayout.CENTER);

        JButton findPathButton = new JButton();
        findPathButton.setText("Find Path");
        findPathButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        findPathButton.setMargin(new Insets(10,10,10,10));
        findPathButton.setHorizontalAlignment(JLabel.CENTER);
        findPathButton.setVerticalAlignment(JLabel.CENTER);

        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dfs(startX,startY);
                for (int i = 0; i < pathX.size(); i++) {
                    System.out.println(pathX.get(i) + " " + pathY.get(i));
                }
                mazeFrame.setVisible(false);
                buildMazeGUI();
            }
        } );
        buttonsPanel.add(findPathButton, BorderLayout.LINE_END);
    }

    public static void readFileGUI() {
        JFrame readFileFrame = new JFrame();
        readFileFrame.setTitle("Maze Settings");
        readFileFrame.setSize(800, 600);

        JButton fileButton = new JButton("Select File:");
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showOpenDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    try {
                        readMazeFile(fileChooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } );

        readFileFrame.add(fileButton);

        readFileFrame.setVisible(true);
    }

    /**
     * This method generates a random map based on the given size by the user. This map is not directly displayed to the
     * user in the GUI, but works as a strong foundation for the later DFS and maze solver.
     *
     * We utilize a web process, beginning at the start and slowly spreading outwards. We do this by randomly picking a
     * point that can still be branched out of and
     */
    public static void generateMap() {
        // Reset and initialize all map variables
        visited = new boolean[rows][cols];
        map = new char[rows][cols];
        canExit = false;
        pathX.clear();
        pathY.clear();

        ArrayList<Integer> openX = new ArrayList<>(); // X Coordinates of all open paths
        ArrayList<Integer> openY = new ArrayList<>(); // Y Coordinates of all open paths

        // Set all map positions to borders
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 'B';
            }
        }

        openX.add((int) (Math.random() * (cols / 3)) + (cols / 3)); // Generate Start X Position
        openY.add((int) (Math.random() * (rows / 3)) + (rows / 3)); // Generate Start Y Position
        map[openY.get(0)][openX.get(0)] = 'S';

        // Store Start Position for Later DFS
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

        // TODO Debug Output (Remove Later)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * This method checks whether the new randomly chosen point is viable to be set as an open path. There are three
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
        if (posY == 0 || posY == rows - 1 || posX == 0 || posX == cols - 1) {
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

    public static void generateRandomMap() {
        visited = new boolean[rows][cols];
        map = new char[rows][cols];
        canExit = false;
        pathX.clear();
        pathY.clear();


    	for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 'B';
            }
        }

    	map[(int) ((Math.random() * (rows / 3)) + (rows / 3))][(int) ((Math.random() * (cols / 3)) + (cols / 3))] = 'S';
    	for (int i = 1; i < rows - 1; i++) {
    		for (int j = 1; j < cols - 1; j++) {
    			int choice = (int) (Math.random() * 2);
    			// If choice == 0, do nothing keep as B
    			// Base case: if the square we are on is the start, don't change it
    			if (map[i][j] == 'S') {
    				continue;
    			}
    			if (choice == 1) {
    				map[i][j] = 'O';
    			}
    		}
    	}

    	// Which side the exit is on
    	int exitSide = (int) (Math.random() * 4);
    	switch (exitSide) {
    		case 0: // Top
    			map[0][(int) (Math.random() * (cols - 2)) + 1] = 'X';
    			break;
    		case 1: // Right
    			map[(int) (Math.random() * (rows - 2)) + 1][cols - 1] = 'X';
    			break;
    		case 2: // Bottom
    			map[rows - 1][(int) (Math.random() * (cols - 2)) + 1] = 'X';
    			break;
    		case 3: // Left
    			map[(int) (Math.random() * (rows - 2)) + 1][0] = 'X';
    			break;
    	}
    }


    public static void readMazeFile(String path) throws Exception {
    	File file = new File(path);
    	Scanner scan = new Scanner(file);

    	rows = scan.nextInt();
    	cols = scan.nextInt();

    	map = new char[rows][cols];
    	visited = new boolean[rows][cols];

    	for (int i = 0; i < 4; i++) {
    		mapLegend[i] = scan.next().charAt(0);
    	}
    	for (int i = 0; i < rows; i++) {
    		String row = scan.next();
    		for (int j = 0; j < cols; j++) {
    			map[i][j] = row.charAt(j);
                if (row.charAt(j) == mapLegend[2]) {
                    startX = j;
                    startY = i;
                }
    		}
    	}

    	scan.close();

    	buildMazeGUI();
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