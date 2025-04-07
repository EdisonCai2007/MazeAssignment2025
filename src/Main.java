import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {
    static boolean[][] visited;
    static int pathLength = -1;

    static char[][] map;

    static char[] mapLegend = {'B', 'O', 'X', 'S'};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        initGUI();

        System.out.println("Ohayo gozaimasu");

        System.out.println("Please enter size of grid:");
        System.out.println("Please Enter Rows:");
        int rows = Integer.parseInt(br.readLine());

        System.out.println("Please Enter Columns:");
        int cols = Integer.parseInt(br.readLine());

        visited = new boolean[rows][cols];
        map = new char[rows][cols];

        generateMap(rows, cols);

        // DEBUGGING DISPLAY

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void initGUI() {
        JFrame welcomeFrame = new JFrame();
        welcomeFrame.setTitle("Sam & Edi Maze Assignment 2025");
        welcomeFrame.setSize(800,500);
        welcomeFrame.setLayout(new BoxLayout(welcomeFrame.getContentPane(),BoxLayout.Y_AXIS));

        JLabel preTitle = new JLabel();
        preTitle.setText("Welcome to the Sam & Edi");
        preTitle.setFont(new Font("Roboto", Font.PLAIN, 24));
        preTitle.setHorizontalAlignment(JLabel.CENTER);
        preTitle.setVerticalAlignment(JLabel.CENTER);
        JLabel title = new JLabel();
        title.setText("Maze Assignment 2025");
        title.setFont(new Font("Roboto", Font.BOLD, 28));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.GREEN);
        titlePanel.add(preTitle);

        welcomeFrame.add(preTitle);
        welcomeFrame.add(title);
        welcomeFrame.setVisible(true);

    }

    public static void generateMap(int rows, int cols) {
        ArrayList<Integer> openX = new ArrayList<Integer>();
        ArrayList<Integer> openY = new ArrayList<Integer>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 'B';
            }
        }

        openX.add((int) (Math.random() * (cols - 2)) + 1); // Generate Start X Position
        openY.add((int) (Math.random() * (rows - 2)) + 1); // Generate Start Y Position
        map[openY.get(0)][openX.get(0)] = 'S';

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
                //
                if (openX.getLast() == 0 || openX.getLast() == cols - 1 || openY.getLast() == 0 || openY.getLast() == rows - 1) {
                    map[openY.getLast()][openX.getLast()] = 'X';
                    openY.removeLast();
                    openX.removeLast();
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

    public static void dfs(char map[][], int x, int y, int counter) {
        if (map[y][x] == 'X') {
            pathLength = counter;
        }
        if (map[y][x - 1] == 'O' || map[y][x - 1] == 'X') {
            if (visited[y][x - 1] == false) {
                visited[y][x - 1] = true;
                dfs(map, x - 1, y, counter + 1);
            }
        }
        if (map[y][x + 1] == 'O' || map[y][x + 1] == 'X') {
            if (visited[y][x + 1] == false) {
                visited[y][x + 1] = true;
                dfs(map, x + 1, y, counter + 1);
            }
        }
        if (map[y - 1][x] == 'O' || map[y - 1][x] == 'X') {
            if (visited[y - 1][x] == false) {
                visited[y - 1][x] = true;
                dfs(map, x, y - 1, counter + 1);
            }
        }
        if (map[y + 1][x] == 'O' || map[y + 1][x] == 'X') {
            if (visited[y + 1][x] == false) {
                visited[y + 1][x] = true;
                dfs(map, x, y + 1, counter + 1);
            }
        }
    }
}