import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

public class Main {
    // Global variable declarations

    static boolean[][] visited; // This is used for path finding (DFS), where true in the 2D array represents that that square has been visited.

    static char[][] map; // This is used to store our maze

    // Legend for our map, used for GUI display
    static char[] mapLegend = {'B', 'O', 'S', 'X'};
    static HashMap<Character, Color> colourLegend = new HashMap<>();

    //Variables used for our path finding algorithm, pathX and pathY to store paths, canExit shows if we can exit our maze
    static boolean canExit = false;
    static ArrayList<Integer> pathX = new ArrayList<>();
    static ArrayList<Integer> pathY = new ArrayList<>();

    // These variables are used throughout the program, where startXY marks our start, while row and cols represent our map size
    static int startX, startY;
    static int rows, cols;

    public static void main(String[] args) throws IOException {
        buildStartMenuGUI();
    }

    /**
     * Builds the GUI fpr the main menu. Prompts the user for four options:
     * 1. Generate Random Maze: Generate a maze with a valid path
     * 2. Generate Purely Random Maze: Generate a maze that may have a valid path
     * 3. Generate Maze From File: Read from a file and generate the maze
     * 4. Exit: Exit and ends the program
     */
    public static void buildStartMenuGUI() {

        // Frame which holds all components
        JFrame welcomeFrame = new JFrame();
        welcomeFrame.setTitle("Sam & Edi Maze Assignment 2025");
        welcomeFrame.setSize(1920, 1080);
        welcomeFrame.setLayout(new GridBagLayout());
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        // PreTitle Label
        JLabel preTitle = new JLabel();
        preTitle.setText("Welcome to the Sam & Edi");
        preTitle.setFont(new Font("Roboto", Font.PLAIN, 48));
        preTitle.setHorizontalAlignment(JLabel.CENTER);
        preTitle.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        welcomeFrame.add(preTitle, gbc);

        // Title Label
        JLabel title = new JLabel();
        title.setText("Maze Assignment 2025");
        title.setFont(new Font("Roboto", Font.BOLD, 60));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        welcomeFrame.add(title, gbc);

        // Generate Random Maze Button
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
                // Opens maze settings with random maze configuration
                buildMazeSettingGUI("Generate Random Maze", welcomeFrame);
            }
        } );

        welcomeFrame.add(mazeButton, gbc);

        // Generate Purely Random Maze Button
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
                // Opens maze settings with purely random maze configuration
                buildMazeSettingGUI("Generate Maze (Purely Random)", welcomeFrame);
            }
        } );

        welcomeFrame.add(randMazeButton, gbc);

        // Read Maze From File Button
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
                // Opens file reader GUI
                readFileGUI("");
            }
        } );

        welcomeFrame.add(readFileButton, gbc);

        // Exit Button
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
                System.exit(0); // Terminates the program
            }
        } );

        welcomeFrame.add(exitButton, gbc);

        welcomeFrame.setVisible(true);
    }

    /**
     * Builds GUI that allows the user to configure maze settings such as the number of rows and columns.
     * @param currentSetting Current configuration of maze (Random vs. Purely Random)
     * @param welcomeFrame Previous Welcome Frame (Needed to close after user confirms maze generation)
     */
    public static void buildMazeSettingGUI(String currentSetting, JFrame welcomeFrame) {

        // Frame holding all components
        JFrame settingsFrame = new JFrame();
        settingsFrame.setTitle("Maze Settings");
        settingsFrame.setSize(800,600);
        settingsFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
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

        // Panel holding size spinners
        JPanel sizeEditor = new JPanel();
        sizeEditor.setLayout(new FlowLayout());

        // Rows Text
        JLabel rowsText = new JLabel();
        rowsText.setText("Rows:");
        rowsText.setFont(new Font("Roboto", Font.BOLD, 28));
        rowsText.setHorizontalAlignment(JLabel.CENTER);
        rowsText.setVerticalAlignment(JLabel.CENTER);
        sizeEditor.add(rowsText, gbc);

        // Row Spinner (Allows user to dynamically change number of rows)
        SpinnerModel rowModel = new SpinnerNumberModel(20, 3, 50, 1);
        JSpinner rowSpinner = new JSpinner(rowModel);
        rowSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
        sizeEditor.add(rowSpinner, gbc);

        // Column Text
        JLabel colsText = new JLabel();
        colsText.setText("     Columns:");
        colsText.setFont(new Font("Roboto", Font.BOLD, 28));
        colsText.setHorizontalAlignment(JLabel.CENTER);
        colsText.setVerticalAlignment(JLabel.CENTER);
        sizeEditor.add(colsText, gbc);

        // Column Spinner (Allows user to dynamically change number of columns)
        SpinnerModel colModel = new SpinnerNumberModel(20, 3, 50, 1);
        JSpinner colSpinner = new JSpinner(colModel);
        colSpinner.setFont(new Font("Roboto", Font.PLAIN, 28));
        sizeEditor.add(colSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0,0,0,0);
        settingsFrame.add(sizeEditor, gbc);

        // Generate Maze Button (Confirmation Button)
        JButton generateMazeButton = new JButton();
        generateMazeButton.setText("Generate Maze");
        generateMazeButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        generateMazeButton.setMargin(new Insets(10,10,10,10));
        generateMazeButton.setHorizontalAlignment(JLabel.CENTER);
        generateMazeButton.setVerticalAlignment(JLabel.CENTER);

        generateMazeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reads for number of rows and columns set by user
                rows = (Integer) rowSpinner.getValue();
                cols = (Integer) colSpinner.getValue();

                // Closes settings and main menu windows
                settingsFrame.setVisible(false);
                welcomeFrame.setVisible(false);
                if (currentSetting.equals("Generate Random Maze")) {
                    generateMap(); // Generates a random maze
                }
                else {
                    generateRandomMap(); // Generates a purely random maze
                }
                buildMazeGUI("Maze Loaded"); // Builds maze GUI
            }
        } );

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(100,0,0,0);
        settingsFrame.add(generateMazeButton, gbc);

        settingsFrame.setVisible(true);
    }

    /**
     * Builds the main maze GUI, which displays the user's maze, as well as the path to the exit if the user wants to
     * view it. An options bar is located at the button to reveal the path or return to the main menu.
     * @param pathCond Text dialogue indicating whether a valid exit path has been found
     */
    public static void buildMazeGUI(String pathCond) {

        // Sets colour values
        colourLegend.put(mapLegend[0],new Color(157, 119, 95));
        colourLegend.put(mapLegend[1],new Color(246, 228, 145));
        colourLegend.put(mapLegend[2],new Color(176, 176, 176));
        colourLegend.put(mapLegend[3],new Color(190, 248, 171));
        colourLegend.put('+',new Color(104, 223, 248));

        // Main frame holding all components
        JFrame mazeFrame = new JFrame();
        mazeFrame.setVisible(true);
        mazeFrame.getContentPane().removeAll();
        mazeFrame.setTitle("Maze");
        mazeFrame.setSize(1800, 1080);
        mazeFrame.setLayout(new BorderLayout());

        // Detect window closing
        mazeFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                buildStartMenuGUI(); // Automatically return back to main menu
            }
        });

        // Panel holding the maze display
        JPanel mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows,cols));
        mazePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        mazeFrame.add(mazePanel, BorderLayout.CENTER);

        // Iterate through all positions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Panel at each position, holding the background colour value
                JPanel positionPanel = new JPanel(new GridBagLayout());
                positionPanel.setBorder(BorderFactory.createLineBorder(Color.black));

                // Label at each position, holding the character associated
                JLabel positionLabel = new JLabel();
                positionLabel.setFont(new Font("Roboto", Font.BOLD, 32));
                positionLabel.setHorizontalAlignment(JLabel.CENTER);
                positionLabel.setVerticalAlignment(JLabel.CENTER);

                // Check if position lies on the path to the exit
                // If so, turn it into a '+' position
                for (int k = 0; k < pathX.size(); k++) {
                    if (pathX.get(k) == j && pathY.get(k) == i && map[i][j] != mapLegend[2]) {
                        positionLabel.setText("+");
                        positionPanel.setBackground(colourLegend.get('+'));
                    }
                }
                // Position hasn't been modified, therefore it does not lie on the path
                // Set position to its associated label
                if (positionLabel.getText().isEmpty()) {
                    positionLabel.setText("" + map[i][j]);
                    positionPanel.setBackground(colourLegend.get(map[i][j]));
                }

                // Add the label and panel to the maze display
                positionPanel.add(positionLabel);
                mazePanel.add(positionPanel);
            }
        }

        // Options panel at the bottom of the screen
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mazeFrame.add(buttonsPanel, BorderLayout.SOUTH);

        // Return to Main Menu Button
        JButton mainMenuButton = new JButton();
        mainMenuButton.setText("Main Menu");
        mainMenuButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        mainMenuButton.setMargin(new Insets(10,10,10,10));
        mainMenuButton.setHorizontalAlignment(JLabel.CENTER);
        mainMenuButton.setVerticalAlignment(JLabel.CENTER);

        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close maze display and return to main menu
                mazeFrame.setVisible(false);
                buildStartMenuGUI();
            }
        } );
        buttonsPanel.add(mainMenuButton, BorderLayout.LINE_START);

        // Label indicating whether a path has been found or not
        JLabel progressLabel = new JLabel();

        progressLabel.setText(pathCond);
        progressLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        progressLabel.setVerticalAlignment(JLabel.CENTER);
        buttonsPanel.add(progressLabel, BorderLayout.CENTER);

        // Find Path Button
        JButton findPathButton = new JButton();
        findPathButton.setText("Find Path");
        findPathButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        findPathButton.setMargin(new Insets(10,10,10,10));
        findPathButton.setHorizontalAlignment(JLabel.CENTER);
        findPathButton.setVerticalAlignment(JLabel.CENTER);

        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dfs(startX,startY); // Begin running our maze solver (DFS)

                // Close current maze display for a refresh
                mazeFrame.setVisible(false);
                if (!canExit) {
                    // Exit not found, rebuild maze with no exit path
                    buildMazeGUI("No Path Found!");
                }
                else {
                    // Exit found, rebuild maze with an exit path
                    buildMazeGUI("Path Found!");
                }
            }
        } );
        buttonsPanel.add(findPathButton, BorderLayout.LINE_END);
    }

    /**
     * Builds GUI which allows user to select their file. Allows for dynamic file input and better user experience
     */
    public static void readFileGUI(String fileCond) {

        // Frame holding all components
        JFrame readFileFrame = new JFrame();
        readFileFrame.setTitle("Maze Settings");
        readFileFrame.setSize(800, 600);
        readFileFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JTextArea mainLabel = new JTextArea("Please write your text file this specific format\n(Not including brackets):");
        mainLabel.setFont(new Font("Roboto", Font.PLAIN, 32));
        mainLabel.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        mainLabel.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        mainLabel.setOpaque(false);
        mainLabel.setForeground(Color.black);
        mainLabel.setEditable(false);
        mainLabel.setHighlighter(null);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,10,0);
        readFileFrame.add(mainLabel, gbc);

        JTextArea instructionsPanel = new JTextArea();
        instructionsPanel.setText("5 (Number of Rows)\n6 (Number of Columns)\nB (Border)\nO (Open Path)\nS (Starting Point)\nX (Exit)\nBBBBXB\nBSBOOB\nBOBOBB\nBOOOOB\nBBBBBB\n(Map)");
        instructionsPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        instructionsPanel.setOpaque(false);
        instructionsPanel.setForeground(Color.black);
        instructionsPanel.setEditable(false);
        instructionsPanel.setHighlighter(null);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,10,0);
        readFileFrame.add(instructionsPanel, gbc);

        // Select file button
        JButton fileButton = new JButton();
        fileButton.setText("Select File");
        fileButton.setFont(new Font("Roboto", Font.BOLD, 32));
        fileButton.setMargin(new Insets(10,10,10,10));
        fileButton.setHorizontalAlignment(JLabel.CENTER);
        fileButton.setVerticalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,0,10,0);

        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Opens file chooser
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showOpenDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    try {
                        // Get file path and begin reading
                        readFileFrame.dispose();
                        readMazeFile(fileChooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } );

        readFileFrame.add(fileButton, gbc);


        JTextArea errorLabel = new JTextArea(fileCond);
        errorLabel.setFont(new Font("Roboto", Font.PLAIN, 32));
        errorLabel.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        errorLabel.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        errorLabel.setOpaque(false);
        errorLabel.setForeground(Color.black);
        errorLabel.setEditable(false);
        errorLabel.setHighlighter(null);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0,0,10,0);
        readFileFrame.add(errorLabel, gbc);

        readFileFrame.setVisible(true);
    }

    /**
     * Reads the text file given by user and converts it into a proper maze format
     * If there are errors within the text, it returns back to the file GUI method, and displays what the error was
     *
     * @param path Path of file
     */
    public static void readMazeFile(String path) throws Exception {
        // Get file objects from file path
        File file = new File(path);
        Scanner scan = new Scanner(file);

        // Read for number of rows and columns
        try {
          rows = scan.nextInt();
          if (rows > 50 || rows < 3) { // If there are errors within, the file GUI method gets called again, returning back and showing what the error is
            readFileGUI("Invalid input, row length has to be\nin between the range of 3 to 50.");
            scan.close();
            return;
          }
        }
        catch(Exception e) { // If they input something that isn't an integer
        	readFileGUI("Invalid input, please input\nan integer for row length");
        	scan.close();
        	return;
        }

        try {
        	cols = scan.nextInt();
        	if (cols > 50 || cols < 3) {
        		readFileGUI("Invalid input, column length has to\nbe in between the range of 3 to 50.");
        		scan.close();
        		return;
        	}
        }
        catch(Exception e) {
        	readFileGUI("Invalid input, please input an\ninteger for column length");
        	scan.close();
        	return;
        }

        scan.nextLine();

        // Initialize and reset needed variables
        map = new char[rows][cols];
        visited = new boolean[rows][cols];

        canExit = false;
        pathX.clear();
        pathY.clear();

        // Read map legend labels; Default: {B, O, X, S, +}
        for (int i = 0; i < 4; i++) {
        	String label = scan.nextLine();
        	if (label.length() == 1) { // If there are more than 1 characters, return
        		mapLegend[i] = label.charAt(0);
        	}
        	else {
        		readFileGUI("Invalid input, please ensure that your\nmap labels are only one character.");
        		scan.close();
        		return;
        	}
        }

        // Iterates through all positions
        boolean startPresent = false, exitPresent = false;
        for (int i = 0; i < rows; i++) {
            String row = scan.nextLine();
            if (row.length() != cols) { // If there is not enough squares for a col
            	readFileGUI("Invalid input, ensure that you have exactly\nthe number of characters you need per column");
            	scan.close();
            	return;
            }
            for (int j = 0; j < cols; j++) {
                // Set map position to the character found at that position
            	char mapValue = row.charAt(j);
            	boolean valuePresent = false;
            	for (int k = 0; k < 4; k++) {
            		if (mapValue == mapLegend[k]) {
            			valuePresent = true;
            		}
            	}
            	if (!valuePresent) { // If the value is not present in the legend
            		readFileGUI("Invalid input, please input a\ncharacter from your map legend.");
            		scan.close();
            		return;
            	}

                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) { // Only have borders or exit on the side
                	if (mapValue != mapLegend[0] && mapValue != mapLegend[3]) {
                		readFileGUI("Invalid input, you can only have borders\nor an exit on the side of your map.");
                		scan.close();
                		return;
                	}
                }
                // Set startX and startY if position is the start
                if (mapValue == mapLegend[2]) {
                	if (!startPresent) {
                		startPresent = true;
                		startX = j;
                		startY = i;
                	}
                	else { // Only one start
                		readFileGUI("Invalid input, you can only have one start.");
                		scan.close();
                		return;
                	}
                }

                if (mapValue == mapLegend[3]) {
                	if (i > 0 && i < rows - 1 && j > 0 && j < cols - 1) { // Only exit on the border
                		readFileGUI("Invalid input, you can only have\nan exit on the borders, not in the middle.");
                		scan.close();
                		return;
                	}
                	if (!exitPresent) { // Do not have more than one exit
                		exitPresent = true;
                	}
                	else {
                		readFileGUI("Invalid input, you can only have one exit.");
                		scan.close();
                		return;
                	}
                }

                // Change value in map
                map[i][j] = mapValue;
            }
        }

        scan.close();

        buildMazeGUI("Maze Loaded"); // Build maze display
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
        mapLegend = new char[]{'B', 'O', 'S', 'X'};

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
                map[i][j] = mapLegend[0];
            }
        }

        openX.add((int) (Math.random() * (cols / 5)) + (cols / 3)); // Generate Start X Position
        openY.add((int) (Math.random() * (rows / 5)) + (rows / 3)); // Generate Start Y Position
        map[openY.get(0)][openX.get(0)] = mapLegend[2];

        // Store Start Position for Later DFS
        startX = openX.get(0);
        startY = openY.get(0);

        boolean hasExit = false;

        while (!openX.isEmpty()) {
            int randomIdx = (int) (Math.random() * openX.size()); // Picks random open point
            int posX = openX.get(randomIdx), posY = openY.get(randomIdx); // Get point position
            ArrayList<String> openDirections = new ArrayList<>(); // Creates ArrayList of valid directions to move
            openDirections.add("up");
            openDirections.add("right");
            openDirections.add("down");
            openDirections.add("left");

            boolean builtNewPoint = false;
            // Continue running as a new point still hasn't been placed and there are still unchecked valid directions
            while (!openDirections.isEmpty() && !builtNewPoint) {
                // Select a random unchecked direction
                String randomDirection = openDirections.get((int) (Math.random() * openDirections.size()));
                switch (randomDirection) {
                    case "up": // Up
                        if (checkDirectionViable(posX, posY - 1, hasExit)) { // Check if direction is viable
                            // Set position to an open path
                            openY.add(posY - 1);
                            openX.add(posX);
                            map[posY - 1][posX] = mapLegend[1];

                            builtNewPoint = true;
                        } else {
                            // Up is not a viable direction
                            // It is not valid so remove it from ArrayList of valid directions
                            openDirections.remove("up");
                        }
                        break;
                    case "right": // Right
                        if (checkDirectionViable(posX + 1, posY, hasExit)) {
                            openY.add(posY);
                            openX.add(posX + 1);
                            map[posY][posX + 1] = mapLegend[1];
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("right");
                        }
                        break;
                    case "down": // Down
                        if (checkDirectionViable(posX, posY + 1, hasExit)) {
                            openY.add(posY + 1);
                            openX.add(posX);
                            map[posY + 1][posX] = mapLegend[1];
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("down");
                        }
                        break;
                    case "left": // Left
                        if (checkDirectionViable(posX - 1, posY, hasExit)) {
                            openY.add(posY);
                            openX.add(posX - 1);
                            map[posY][posX - 1] = mapLegend[1];
                            builtNewPoint = true;
                        } else {
                            openDirections.remove("left");
                        }
                        break;
                }
            }
            if (builtNewPoint) { // Check if a new open path has been placed
                // Check if the new open path lies on the edge
                // If so, it will now become our exit point
                if (openX.get(openX.size() - 1) == 0 || openX.get(openX.size() - 1) == cols - 1 || openY.get(openY.size() - 1) == 0 || openY.get(openY.size() - 1) == rows - 1) {
                    map[openY.get(openY.size() - 1)][openX.get(openX.size() - 1)] = mapLegend[3];
                    openY.remove(openY.size() - 1);
                    openX.remove(openX.size() - 1);
                    hasExit = true; // Exit has been placed, don't build any more exits
                }
            } else {
                // Could not branch out from his position
                // The position is no longer valid to branch out of
                // Remove it from our ArrayList of potential paths
                openY.remove(randomIdx);
                openX.remove(randomIdx);
            }
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

        // Check if the given direction is on an already open path (or start position)
        // We cannot branch out to a path that has already been opened
        if (map[posY][posX] == mapLegend[1] || map[posY][posX] == mapLegend[2]) {
            // Path is on a previous opened path, its not viable
            return false;
        }

        // These four conditions check if the given direction has any adjacent open paths
        // We don't want large open spaces, so we restrict only one width paths
        // B B B B B
        // B O O O B <- This is bad
        // B O O O B
        // B B B B B
        if (map[posY - 1][posX] == mapLegend[1] || map[posY - 1][posX] == mapLegend[2]) {
            countAdjacent++;
        }
        if (map[posY][posX + 1] == mapLegend[1] || map[posY][posX + 1] == mapLegend[2]) {
            countAdjacent++;
        }
        if (map[posY + 1][posX] == mapLegend[1] || map[posY + 1][posX] == mapLegend[2]) {
            countAdjacent++;
        }
        if (map[posY][posX - 1] == mapLegend[1] || map[posY][posX - 1] == mapLegend[2]) {
            countAdjacent++;
        }

        // Return whether there are more than one adjacent open path
        // (There will always be an adjacent path, which is the path we branched out of)
        // If there are no other adjacent open paths, return true
        // Else, return false
        return countAdjacent <= 1;
    }

    /**
     * Generates a purely random map by iterating through all non-edge position and randomly setting it as an open path
     * or a border. Then randomly selects an exit point.
     */
    public static void generateRandomMap() {
        // Initalizes and resets all needed variables
        visited = new boolean[rows][cols];
        map = new char[rows][cols];
        canExit = false;
        pathX.clear();
        pathY.clear();

        // Set all positions initially to a border
    	for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = mapLegend[0];
            }
        }

        // Randomly selects a starting point
        startX = (int) ((Math.random() * (cols / 3)) + (cols / 3));
        startY = (int) ((Math.random() * (rows / 3)) + (rows / 3));
    	map[startY][startX] = mapLegend[2];

        // Iterate through all non-edge positions
    	for (int i = 1; i < rows - 1; i++) {
    		for (int j = 1; j < cols - 1; j++) {
    			int choice = (int) (Math.random() * 2);
    			// If choice == 0, do nothing keep as B
    			// Base case: if the square we are on is the start, don't change it
    			if (map[i][j] == mapLegend[2]) {
    				continue;
    			}
    			if (choice == 1) {
    				map[i][j] = mapLegend[1];
    			}
    		}
    	}

    	// Randomly selects which side the exit is on
    	int exitSide = (int) (Math.random() * 4);
    	switch (exitSide) {
    		case 0: // Top
    			map[0][(int) (Math.random() * (cols - 2)) + 1] = mapLegend[3];
    			break;
    		case 1: // Right
    			map[(int) (Math.random() * (rows - 2)) + 1][cols - 1] = mapLegend[3];
    			break;
    		case 2: // Bottom
    			map[rows - 1][(int) (Math.random() * (cols - 2)) + 1] = mapLegend[3];
    			break;
    		case 3: // Left
    			map[(int) (Math.random() * (rows - 2)) + 1][0] = mapLegend[3];
    			break;
    	}
    }

    /**
     * This is our maze solving algorithm. We used a common algorithm used in graph theory, called depth-first search (DFS). For more information, read https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/
     * How it works: Start from your start position, and move up, down, left, right around your current point to visit all surrounding squares.
     * Keep track of a visited array, keeping track of all the squares you have visited. If we have visited that square before, don't visit it again.
     * Use recursion to visit the squares, then backtrack after reaching the end of a segment
     * We store our path with two array lists called pathX and pathY.
     * Use a boolean variable to store if we can exit our maze or not. If canExit is true, there is a path. Otherwise, there is no path.
     *
     * @param posX the X position of the square we are currently on
     * @param posY the Y position of the square we are currently on
     */

    public static void dfs(int posX, int posY) {
    	if (canExit) { // If we have found a path to the exit, we don't need to do anything else, so just return back
    		return;
    	}
        if (map[posY][posX] == mapLegend[3]) { // This is our base case. If we reach the end, (Ex. 'X'), we should return and now we know that there is an exit.
            canExit = true;
            return;
        }
        visited[posY][posX] = true; // Set the current's square visited state to true, so we don't have to visit this square again
        pathX.add(posX); // Add the current square into our path
    	pathY.add(posY);

        /*
        Recursive Case: Check the surrounding squares and recursively call into them
        We first have to check if the square we are trying to visit is not a border, because we can't travel into borders
        Second, we have to check if the square we are visiting is not visited yet, because it's pointless to visit an already visited square
         */

        if (map[posY][posX - 1] != mapLegend[0] && !visited[posY][posX - 1]) { // Travel Left
            dfs(posX - 1, posY);
        }
        if (map[posY][posX + 1] != mapLegend[0] && !visited[posY][posX + 1]) { // Travel Right
            dfs(posX + 1, posY);
        }
        if (map[posY - 1][posX] != mapLegend[0] && !visited[posY - 1][posX]) { // Travel Up
            dfs(posX, posY - 1);
        }
        if (map[posY + 1][posX] != mapLegend[0] && !visited[posY + 1][posX]) { // Travel Down
            dfs(posX, posY + 1);
        }

        if (!canExit) { // If we have found an exit, do not remove the current squares from the path. These are part of the overall path, we will be using them later.
            pathX.remove(pathX.size() - 1);
            pathY.remove(pathY.size() - 1);
        }
    }
}