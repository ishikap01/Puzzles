import java.util.*;

class Cube {
    int[] state = new int[8];

    Cube(int... values) {
        System.arraycopy(values, 0, state, 0, 8);
    }
}

class BoardHistory {
    Cube[][] board;
    String history;

    BoardHistory(Cube[][] board, String history) {
        this.board = board;
        this.history = history;
    }
}

public class CubePuzzle {
    static final int BoardSize = 3;

    static boolean isSolved(Cube[][] board) {
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (board[i][j].state[0] != 0 && board[i][j].state[0] != 6) {
                    return false;
                }
            }
        }
        return true;
    }

    static void printBoard(Cube[][] board) {
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                System.out.print(board[i][j].state[0] + " ");
            }
            System.out.println();
        }
    }

    static void down(Cube cube, BoardHistory bh) {
        int x = cube.state[6];
        int y = cube.state[7];
        if (x == 0 || x == 1) {
            int a = cube.state[0];
            cube.state[0] = cube.state[3];
            cube.state[3] = cube.state[5];
            cube.state[5] = cube.state[1];
            cube.state[1] = a;

            Cube temp = bh.board[x + 1][y];
            bh.board[x + 1][y] = bh.board[x][y];
            bh.board[x][y] = temp;

            bh.board[x + 1][y].state[6] = x + 1;
            bh.board[x][y].state[6] = x;
        }
        bh.history = "D" + bh.history;
    }

    static void up(Cube cube, BoardHistory bh) {
        int x = cube.state[6];
        int y = cube.state[7];
        if (x == 1 || x == 2) {
            int a = cube.state[0];
            cube.state[0] = cube.state[1];
            cube.state[1] = cube.state[5];
            cube.state[5] = cube.state[3];
            cube.state[3] = a;

            Cube temp = bh.board[x - 1][y];
            bh.board[x - 1][y] = bh.board[x][y];
            bh.board[x][y] = temp;

            bh.board[x - 1][y].state[6] = x - 1;
            bh.board[x][y].state[6] = x;
        }
        bh.history = "U" + bh.history;
    }

    static void left(Cube cube, BoardHistory bh) {
        int x = cube.state[6];
        int y = cube.state[7];
        if (y == 1 || y == 2) {
            int a = cube.state[0];
            cube.state[0] = cube.state[4];
            cube.state[4] = cube.state[5];
            cube.state[5] = cube.state[2];
            cube.state[2] = a;

            Cube temp = bh.board[x][y - 1];
            bh.board[x][y - 1] = bh.board[x][y];
            bh.board[x][y] = temp;

            bh.board[x][y - 1].state[7] = y - 1;
            bh.board[x][y].state[7] = y;
        }
        bh.history = "L" + bh.history;
    }

    static void right(Cube cube, BoardHistory bh) {
        int x = cube.state[6];
        int y = cube.state[7];
        if (y == 0 || y == 1) {
            int a = cube.state[0];
            cube.state[0] = cube.state[2];
            cube.state[2] = cube.state[5];
            cube.state[5] = cube.state[4];
            cube.state[4] = a;

            Cube temp = bh.board[x][y + 1];
            bh.board[x][y + 1] = bh.board[x][y];
            bh.board[x][y] = temp;

            bh.board[x][y + 1].state[7] = y + 1;
            bh.board[x][y].state[7] = y;
        }
        bh.history = "R" + bh.history;
    }

    static BoardHistory copyConstructor(BoardHistory bh) {
        Cube[][] newBoard = new Cube[BoardSize][BoardSize];
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                newBoard[i][j] = new Cube(bh.board[i][j].state);
            }
        }
        return new BoardHistory(newBoard, bh.history);
    }

    static AbstractMap.SimpleEntry<List<String>, AbstractMap.SimpleEntry<Integer, Integer>> getPossibleMoves(Cube[][] state) {
        List<String> moves = new ArrayList<>();
        int gx = -1, gy = -1;
        // same logic as C++
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (state[i][j].state[0] == 0) {
                    gx = i;
                    gy = j;
                    break;
                }
            }
        }

        // condition mapping like C++'s if-else chain...
        if (gx == 0 && gy == 0) { moves.add("L"); moves.add("U"); }
        else if (gx == 0 && gy == 1) { moves.addAll(Arrays.asList("L","U","R")); }
        else if (gx == 0 && gy == 2) { moves.add("U"); moves.add("R"); }
        else if (gx == 1 && gy == 0) { moves.addAll(Arrays.asList("L","U","D")); }
        else if (gx == 1 && gy == 1) { moves.addAll(Arrays.asList("L","U","D","R")); }
        else if (gx == 1 && gy == 2) { moves.addAll(Arrays.asList("R","U","D")); }
        else if (gx == 2 && gy == 0) { moves.add("L"); moves.add("D"); }
        else if (gx == 2 && gy == 1) { moves.addAll(Arrays.asList("L","R","D")); }
        else if (gx == 2 && gy == 2) { moves.add("R"); moves.add("D"); }

        return new AbstractMap.SimpleEntry<>(moves, new AbstractMap.SimpleEntry<>(gx, gy));
    }

    static void applyMove(BoardHistory bh, String move) {
        int gx = -1, gy = -1;
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (bh.board[i][j].state[0] == 0) { gx = i; gy = j; }
            }
        }
        switch (move) {
            case "U": up(bh.board[gx + 1][gy], bh); break;
            case "D": down(bh.board[gx - 1][gy], bh); break;
            case "L": left(bh.board[gx][gy + 1], bh); break;
            case "R": right(bh.board[gx][gy - 1], bh); break;
            default: System.out.println("Invalid move");
        }
    }

    static List<BoardHistory> generateNextStates(BoardHistory bh) {
        List<BoardHistory> next = new ArrayList<>();
        var pm = getPossibleMoves(bh.board);
        List<String> moves = pm.getKey();
        String latestMove = bh.history.isEmpty() ? "" : bh.history.substring(0, 1);
        if (latestMove.equals("U")) moves.remove("D");
        else if (latestMove.equals("D")) moves.remove("U");
        else if (latestMove.equals("L")) moves.remove("R");
        else if (latestMove.equals("R")) moves.remove("L");

        for (String m : moves) {
            BoardHistory copy = copyConstructor(bh);
            applyMove(copy, m);
            next.add(copy);
        }
        return next;
    }

    static void BFSSolve(BoardHistory bh) {
        Queue<BoardHistory> q = new LinkedList<>();
        q.add(bh);
        printBoard(bh.board);

        while (!q.isEmpty()) {
            BoardHistory curr = q.poll();
            printBoard(curr.board);
            System.out.println(curr.history);

            if (isSolved(curr.board)) {
                System.out.println("Solved!");
                printBoard(curr.board);
                System.out.println(curr.history);
                return;
            }
            q.addAll(generateNextStates(curr));
        }
        System.out.println("No solution found!");
    }

    public static void main(String[] args) {
        Cube cube1 = new Cube(6,4,3,2,5,1,0,0);
        Cube cube2 = new Cube(6,4,3,2,5,1,0,1);
        Cube cube3 = new Cube(6,4,3,2,5,1,0,2);
        Cube cube4 = new Cube(6,4,3,2,5,1,1,0);
        Cube cube5 = new Cube(6,4,3,2,5,1,1,2);
        Cube cube6 = new Cube(6,4,3,2,5,1,2,0);
        Cube cube7 = new Cube(4,1,3,6,5,2,1,1);
        Cube cube8 = new Cube(6,4,3,2,5,1,2,2);
        Cube empty = new Cube(0,0,0,0,0,0,2,1);

        Cube[][] board = {
            {cube1, cube2, cube3},
            {cube4, cube7, cube5},
            {cube6, empty, cube8}
        };
        BoardHistory bh = new BoardHistory(board, "");
        BFSSolve(bh);
    }
}
