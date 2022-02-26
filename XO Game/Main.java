import java.util.*;

public class Main {
    static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String statement = new String();
        String stringInResumeOrScoreboard = new String();
        Game g = new Game(3, 3);
        int row = 3, column = 3;
        boolean in_menu = true;
        int gameChoosed = 0;
        boolean flagForTrueStatementInMenuGiven = false;
        boolean flagForTrueStatementInGameGiven = false;
        boolean flagForTrueStatementInNewGame = false;

        while (!statement.equals("quit")) {
            while (in_menu) {
                if (in_menu && statement.equals("quit")) {
                    return;
                }
                //in menu
                flagForTrueStatementInMenuGiven = false;
                flagForTrueStatementInNewGame = false;
                statement = scanner.nextLine();
                statement.trim();
                String[] state = statement.split(" ");

                if (in_menu && state[0].equals("new")) {
                    if (state.length < 4 && state.length >= 2 && state[1].equals("game")) {
                        System.out.println("Invalid players");
                        flagForTrueStatementInNewGame = true;
                    }
                    if (state.length > 4) {
                        System.out.println("Invalid command");
                        flagForTrueStatementInNewGame = true;
                    }
                    if (state.length < 2) {
                        System.out.println("Invalid command");
                        flagForTrueStatementInNewGame = true;
                    }
                    if (state.length >= 2 && !state[1].equals("game")) {
                        System.out.println("Invalid command");
                        flagForTrueStatementInNewGame = true;
                    } else if (state.length == 4 && state[1].equals("game")) {
                        String player1Name = state[2];
                        String player2Name = state[3];
                        g = new Game(row, column);
                        Game.addNewGame(g);
                        Player p1 = new Player(player1Name);
                        Player p2 = new Player(player2Name);

                        for (Player player : Player.getPlayers()) {
                            if (player.getAPlayerName().equals(player1Name)) {
                                p1 = player;
                            }
                            if (player.getAPlayerName().equals(player2Name)) {
                                p2 = player;
                            }
                        }
                        g.setPlayer(p1);
                        g.setPlayer(p2);
                        p1.addGame(g);
                        p2.addGame(g);
                        if (!Player.getAllPlayersNames().contains(player1Name)) {
                            Player.addNewPlayer(p1);
                        }
                        if (!Player.getAllPlayersNames().contains(player2Name)) {
                            Player.addNewPlayer(p2);
                        }
                        g.drawTable();

                        in_menu = false;
                        flagForTrueStatementInMenuGiven = true;
                    }
                }
                if (in_menu && state[0].equals("set") && state[1].equals("table")) {
                    flagForTrueStatementInMenuGiven = true;
                    if (state.length == 3 && state[2].contains("*")) {
                        int index = state[2].indexOf("*");
                        row = Integer.parseInt(state[2].substring(0, index));
                        column = Integer.parseInt(state[2].substring(index + 1, state[2].length()));
                    }
                    if (state.length == 2) {
                        row = 3;
                        column = 3;
                    }
                }
                if (in_menu && state[0].equals("scoreboard")) {
                    flagForTrueStatementInMenuGiven = true;
                    Collections.sort(Player.getPlayers(), new sortBynumOfWins());
                    for (int i = 0; i < Player.getPlayers().size(); i++) {
                        for (int j = i + 1; j < Player.getPlayers().size(); j++) {
                            if (Player.getPlayers().get(i).getNumberOfWins() == Player.getPlayers().get(j).getNumberOfWins()) {
                                if (Player.getPlayers().get(i).getNumberOfLoses() > Player.getPlayers().get(j).getNumberOfLoses()) {
                                    Collections.swap(Player.getPlayers(), i, j);
                                }
                            }
                        }
                    }

                    for (int i = 0; i < Player.getPlayers().size(); i++) {
                        for (int j = i + 1; j < Player.getPlayers().size(); j++) {
                            if (Player.getPlayers().get(i).getNumberOfWins() == Player.getPlayers().get(j).getNumberOfWins()
                                    && Player.getPlayers().get(i).getNumberOfLoses() == Player.getPlayers().get(j).getNumberOfLoses()) {
                                if (Player.getPlayers().get(i).getNumberOfDraws() > Player.getPlayers().get(j).getNumberOfDraws()) {
                                    Collections.swap(Player.getPlayers(), i, j);
                                }
                            }
                        }
                    }

                    for (int i = 0; i < Player.getPlayers().size(); i++) {
                        for (int j = i + 1; j < Player.getPlayers().size(); j++) {
                            if (Player.getPlayers().get(i).getNumberOfWins() == Player.getPlayers().get(j).getNumberOfWins()
                                    && Player.getPlayers().get(i).getNumberOfLoses() == Player.getPlayers().get(j).getNumberOfLoses()
                                    && Player.getPlayers().get(i).getNumberOfDraws() == Player.getPlayers().get(j).getNumberOfDraws()) {
                                if (Player.getPlayers().get(i).getAPlayerName().compareTo(Player.getPlayers().get(j).getAPlayerName()) > 0) {
                                    Collections.swap(Player.getPlayers(), i, j);
                                }
                            }
                        }
                    }

                    for (int i = 0; i < Player.getPlayers().size(); i++) {
                        System.out.println(Player.getPlayers().get(i).getAPlayerName() + " "
                                + Player.getPlayers().get(i).getNumberOfWins() + " "
                                + Player.getPlayers().get(i).getNumberOfLoses() + " "
                                + Player.getPlayers().get(i).getNumberOfDraws());
                    }

                    while (!stringInResumeOrScoreboard.equals("back") || !stringInResumeOrScoreboard.equals("quit")) {
                        stringInResumeOrScoreboard = scanner.nextLine();
                        stringInResumeOrScoreboard.trim();
                        if (stringInResumeOrScoreboard.equals("back")) {
                            in_menu = true;
                            break;
                        }
                        if (stringInResumeOrScoreboard.equals("quit")) {
                            return;
                        }
                        System.out.println("Invalid command");
                    }
                }
                if (in_menu && state[0].equals("resume")) {
                    flagForTrueStatementInMenuGiven = true;
                    int rank = 1;
                    for (int i = 0; i < Game.getGames().size(); i++) {
                        if (Game.getGames().get(i).getStop()) {
                            Game.getGames().remove(i);
                        }
                    }
                    for (int i = Game.getGames().size() - 1; i >= 0; i--) {
                        System.out.println(rank + ". " + Game.getGames().get(i).getGamePlayers().get(0).getAPlayerName()
                                + " " + Game.getGames().get(i).getGamePlayers().get(1).getAPlayerName());
                        rank++;
                    }

                    while (!stringInResumeOrScoreboard.equals("back") || gameChoosed == 0) {
                        stringInResumeOrScoreboard = scanner.nextLine();
                        stringInResumeOrScoreboard.trim();
                        if (isNumber(stringInResumeOrScoreboard)
                                && Integer.parseInt(stringInResumeOrScoreboard) < rank
                                && Integer.parseInt(stringInResumeOrScoreboard) > 0) {
                            g = Game.getGames().get(Game.getGames().size() - Integer.parseInt(stringInResumeOrScoreboard));//unadadi ke entekhab mikone
                            g.drawTable();
                            gameChoosed = 1;
                            in_menu = false;
                            Game.getGames().remove(g);
                            Game.getGames().add(g);
                            break;
                        }

                        if (isNumber(stringInResumeOrScoreboard) && Integer.parseInt(stringInResumeOrScoreboard) >= rank)
                            System.out.println("Invalid number");

                        if (isNumber(stringInResumeOrScoreboard) && Integer.parseInt(stringInResumeOrScoreboard) <= 0)
                            System.out.println("Invalid number");

                        if (!isNumber(stringInResumeOrScoreboard) && !stringInResumeOrScoreboard.equals("back"))
                            System.out.println("Invalid command");

                        if (stringInResumeOrScoreboard.equals("back") || gameChoosed == 1) {
                            in_menu = true;
                            gameChoosed = 0;
                            break;
                        }
                    }
                }
                if (!flagForTrueStatementInMenuGiven && !statement.equals("quit") && !flagForTrueStatementInNewGame) {
                    System.out.println("Invalid command");
                }
            }
            while (!in_menu) {
                //in the game
                flagForTrueStatementInGameGiven = false;
                statement = scanner.nextLine();
                statement.trim();
                String[] state1 = statement.split(" ");

                if (!in_menu && state1[0].contains("put")) {
                    if (!state1[0].contains(",")) {
                    }
                    if (state1[0].contains(",")) {
                        flagForTrueStatementInGameGiven = true;
                        int index = state1[0].indexOf(',');
                        int x = Integer.parseInt(state1[0].substring(4, index));
                        int y = Integer.parseInt(state1[0].substring(index + 1, state1[0].length() - 1));
                        x--;
                        y--;
                        g.getGamePlayers().get(g.getTurn()).putBead(g, g.getGamePlayers().get(g.getTurn()), x, y);

                        if (g.getFinished()) {
                            in_menu = true;
                        }
                    }
                }

                if (!in_menu && state1[0].equals("undo")) {
                    flagForTrueStatementInGameGiven = true;
                    int flagp = 0;
                    if (g.getTurn() == 0 && flagp == 0) {
                        if (g.getGamePlayers().get(1).getNumOfUndoes() >= 1) {
                            System.out.println("Invalid undo");
                            g.drawTable();
                        }
                        if (g.getGamePlayers().get(1).getNumOfUndoes() < 1) {
                            g.getGamePlayers().get(1).undo(g, g.getGamePlayers().get(1));
                            g.drawTable();
                            flagp = 1;
                        }
                    }

                    if (g.getTurn() == 1 && flagp == 0) {
                        if (g.getGamePlayers().get(0).getNumOfUndoes() >= 1) {
                            System.out.println("Invalid undo");
                            g.drawTable();
                        }
                        if (g.getGamePlayers().get(0).getNumOfUndoes() < 1) {
                            g.getGamePlayers().get(0).undo(g, g.getGamePlayers().get(0));
                            g.drawTable();
                        }
                    }

                }
                if (!in_menu && state1[0].equals("pause")) {
                    flagForTrueStatementInGameGiven = true;
                    in_menu = true;
                }

                if (!in_menu && state1[0].equals("stop")) {
                    flagForTrueStatementInGameGiven = true;
                    Player.deleteGame(g);
                    Game.deleteGame(g);
                    in_menu = true;
                }
                if (!flagForTrueStatementInGameGiven) {
                    System.out.println("Invalid command");
                    g.drawTable();
                }
            }
        }
    }
}


class Game {
    private static ArrayList<Game> games = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();//just with 2 members
    private ArrayList<Integer> X = new ArrayList<>();
    private ArrayList<Integer> Y = new ArrayList<>();
    private int column;
    private int row;
    private char[][] table;
    private char[][] copyOfTable;
    private char[][] tableOccupied;
    private int[] rowCountX;
    private int[] columnCountX;
    private int[] diameterCountX;
    private int[] rowCountO;
    private int[] columnCountO;
    private int[] diameterCountO;
    private int[] diameter2CountX;
    private int[] diameter2CountO;
    private int diameter;
    private int turn;
    private boolean finished;
    private boolean draw;
    private boolean XWon;
    private boolean OWon;
    private boolean stop;
    private char[] beadArray;
    private int lastX;
    private int lastY;


    public Game(int row, int column) {
        this.row = row;
        this.column = column;
        this.table = new char[row][column];
        this.tableOccupied = new char[row][column];
        this.copyOfTable = new char[row][column];
        this.beadArray = new char[2];
        turn = 0;
        finished = false;
        draw = false;
        XWon = false;
        OWon = false;
        stop = false;

        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < column; j++) {
                table[i][j] = '_';
                tableOccupied[i][j] = 0;
            }
        }
        diameter = column + row - 1;
        rowCountX = new int[row];
        columnCountX = new int[column];
        diameterCountX = new int[diameter];
        rowCountO = new int[row];
        columnCountO = new int[column];
        diameterCountO = new int[diameter];
        diameter2CountX = new int[diameter];
        diameter2CountO = new int[diameter];
        beadArray[0] = 'X';
        beadArray[1] = 'O';
        Arrays.fill(rowCountX, 0);
        Arrays.fill(columnCountX, 0);
        Arrays.fill(diameterCountX, 0);
        Arrays.fill(rowCountO, 0);
        Arrays.fill(columnCountO, 0);
        Arrays.fill(diameterCountO, 0);
        Arrays.fill(diameter2CountX, 0);
        Arrays.fill(diameter2CountO, 0);
    }

    public static void addNewGame(Game game) {
        games.add(game);
    }

    public static void deleteGame(Game g) {
        g.stop = true;
        games.remove(g);
    }


    public void putBead(Game game, Player player, int x, int y) {
        if (y < column && x < row && x >= 0 && y >= 0) {

            if (tableOccupied[x][y] == 1) {
                System.out.println("Invalid coordination");
                drawTable();
            }

            if (tableOccupied[x][y] == 0) {
                X.add(x);
                Y.add(y);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        copyOfTable[i][j] = table[i][j];
                    }
                }

                lastX = x;
                lastY = y;
                table[x][y] = game.beadArray[turn];
                checkTheGameLogic();
                tableOccupied[x][y] = 1;
                checkContinuingOfTheGame(game);
                if (!finished) {
                    game.turn++;
                    game.turn %= 2;
                    drawTable();
                }
            }

        }
        if (y >= column || x >= row || x < 0 || y < 0) {
            System.out.println("Invalid coordination");
            drawTable();
        }
    }

    public void drawTable() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print(table[i][j]);
                if (j < column - 1) {
                    System.out.print('|');
                }
            }
            System.out.println();
        }
        if (!finished) {
            turn %= 2;
            System.out.println(players.get(turn).getAPlayerName());
        }
    }

    public void checkContinuingOfTheGame(Game game) {
        //draw
        boolean allOccupied = true;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (tableOccupied[i][j] == 0) {
                    allOccupied = false;
                    break;
                }
            }
        }
        if (allOccupied && !XWon && !OWon) {
            draw = true;
            finished = true;
            players.get(0).setNumOfUndoes(-1);
            players.get(1).setNumOfUndoes(-1);
            System.out.println("Draw");
            players.get(0).setNumberOfDraws(1);
            players.get(1).setNumberOfDraws(1);
        }

        //winner
        if (column == 3 || row == 3) {
            for (int i = 0; i < row; i++) {
                if (rowCountX[i] == 3) {
                    XWon = true;
                    finished = true;
                    break;
                }
                if (rowCountO[i] == 3) {
                    OWon = true;
                    break;
                }
            }
            for (int i = 0; i < column; i++) {
                if (columnCountX[i] == 3) {
                    XWon = true;
                    finished = true;
                    break;
                }
                if (columnCountO[i] == 3) {
                    OWon = true;
                    finished = true;
                    break;
                }
            }
            for (int i = 0; i < diameter; i++) {
                if (diameterCountX[i] == 3 || diameter2CountX[i] == 3) {
                    XWon = true;
                    finished = true;
                    break;
                }
                if (diameterCountO[i] == 3 || diameter2CountO[i] == 3) {
                    OWon = true;
                    finished = true;
                    break;
                }
            }
        }

        if (column > 3 && row > 3) {
            for (int i = 0; i < row; i++) {
                if (rowCountX[i] == 4) {
                    XWon = true;
                    finished = true;
                    break;
                }
                if (rowCountO[i] == 4) {
                    OWon = true;
                    finished = true;
                    break;
                }
            }
            for (int i = 0; i < column; i++) {
                if (columnCountX[i] == 4) {
                    XWon = true;
                    finished = true;
                    break;
                } else {
                    if (columnCountO[i] == 4) {
                        OWon = true;
                        finished = true;
                        break;
                    }
                }
            }
            for (int i = 0; i < diameter; i++) {
                if (diameterCountX[i] == 4 || diameter2CountX[i] == 4) {
                    XWon = true;
                    finished = true;
                    break;
                }
                if (diameterCountO[i] == 4 || diameter2CountO[i] == 4) {
                    OWon = true;
                    finished = true;
                    break;
                }
            }
        }


        if (XWon && !OWon) {
            System.out.println("Player " + players.get(0).getAPlayerName() + " won");
            finished = true;
            players.get(0).setNumOfUndoes(-1);
            players.get(1).setNumOfUndoes(-1);
            players.get(0).setNumberOfWins(1);
            players.get(1).setNumberOfLoses(1);
        }

        if (!XWon && OWon) {
            System.out.println("Player " + players.get(1).getAPlayerName() + " won");
            finished = true;
            players.get(0).setNumOfUndoes(-1);
            players.get(1).setNumOfUndoes(-1);
            players.get(1).setNumberOfWins(1);
            players.get(0).setNumberOfLoses(1);
        }

        if (finished)
            getGames().remove(game);

    }

    public void checkTheGameLogic() {
        for (int i = 0; i < row; i++) {
            rowCountX[i] = 0;
            rowCountO[i] = 0;
        }

        for (int i = 0; i < column; i++) {
            columnCountX[i] = 0;
            columnCountO[i] = 0;
        }

        for (int i = 0; i < diameter; i++) {
            diameter2CountO[i] = 0;
            diameter2CountX[i] = 0;
            diameterCountX[i] = 0;
            diameterCountO[i] = 0;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (table[i][j] == 'X') {
                    rowCountX[i]++;
                } else if (table[i][j] == 'O') {
                    rowCountO[i]++;
                }
            }
        }

        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                if (table[j][i] == 'X') {
                    columnCountX[i]++;
                }
                if (table[j][i] == 'O') {
                    columnCountO[i]++;
                }
            }
        }

        for (int line = 1; line <= (row + column - 1); line++) {
            int start_col = max(0, line - row);
            int count = min(line, (column - start_col), row);
            for (int j = 0; j < count; j++) {
                if (
                        table[min(row, line) - j - 1][start_col + j] == 'X') {
                    diameterCountX[line - 1]++;
                }
                if (table[min(row, line) - j - 1][start_col + j] == 'O') {
                    diameterCountO[line - 1]++;
                }
            }
        }

        for (int k = column - 1; k >= -row + 1; k--) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if ((i - j) == -k && table[i][j] == 'X') {
                        diameter2CountX[column - 1 - k]++;
                    }
                    if ((i - j) == -k && table[i][j] == 'O') {
                        diameter2CountO[column - 1 - k]++;
                    }
                }
            }
        }

    }

    public void undo(Game game, Player player) {
        int flagX = 0;
        int flagY = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (table[i][j] == 'X') {
                    flagX = 1;
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (table[i][j] == 'O') {
                    flagY = 1;
                }
            }
        }
        if (flagX == 0 || flagY == 0) System.out.println("Invalid undo");
        if (flagX == 1 && flagY == 1) {

            int i = X.get(X.size() - 1);
            int j = Y.get(Y.size() - 1);
            tableOccupied[i][j] = 0;
            table[i][j] = '_';
            game.turn++;
            game.turn %= 2;
            player.setNumOfUndoes(1);
            X.remove(X.size() - 1);
            Y.remove(Y.size() - 1);
            checkTheGameLogic();
            checkContinuingOfTheGame(game);
        }
    }

    public void setPlayer(Player player) {
        players.add(player);

    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Player> getGamePlayers() {
        return players;
    }

    public int getTurn() {
        turn %= 2;
        return turn;
    }


    public boolean getFinished() {
        return finished;
    }

    public boolean getStop() {
        return stop;
    }

    static int min(int a, int b) {
        return (a < b) ? a : b;
    }

    static int min(int a, int b, int c) {
        return min(min(a, b), c);
    }

    static int max(int a, int b) {
        return (a > b) ? a : b;
    }
}

class Player {
    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();
    public char bead;
    private String name;
    private int numOfwins;
    private int numOflosts;
    private int numOfDraws;
    private int numOfUndoes;

    public Player(String name) {
        this.name = name;
        numOfDraws = 0;
        numOflosts = 0;
        numOfwins = 0;
        numOfUndoes = 0;
    }


    public void addGame(Game game) {
        games.add(game);
    }

    public static void deleteGame(Game game) {
        for (int i = 0; i < Player.getPlayers().size(); i++) {
            if (Player.getPlayers().get(i).games.contains(game)) {
                Player.getPlayers().get(i).games.remove(game);
            }
        }
    }

    public static void addNewPlayer(Player player) {
        players.add(player);
        names.add(player.name);
    }

    public void putBead(Game game, Player player, int x, int y) {
        if (Game.getGames().contains(game)) {
            game.putBead(game, player, x, y);
        }
    }

    public void undo(Game game, Player player) {
        if (Game.getGames().contains(game)) {
            game.undo(game, player);
        }
    }

    public void setNumberOfWins(int number) {
        numOfwins += number;
    }

    public void setNumberOfLoses(int number) {
        numOflosts += number;
    }

    public void setNumberOfDraws(int number) {
        numOfDraws += number;
    }

    public void setNumOfUndoes(int numOfUndoes) {
        this.numOfUndoes += numOfUndoes;
    }

    public int getNumOfUndoes() {
        return numOfUndoes;
    }

    public String getAPlayerName() {
        return name;
    }

    public static ArrayList<String> getAllPlayersNames() {
        return names;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberOfWins() {
        return numOfwins;
    }

    public int getNumberOfLoses() {
        return numOflosts;
    }

    public int getNumberOfDraws() {
        return numOfDraws;
    }

}

class sortBynumOfWins implements Comparator<Player> {
    // Used for sorting in descending order of numOfWins
    public int compare(Player a, Player b) {
        return b.getNumberOfWins() - a.getNumberOfWins();
    }
}

