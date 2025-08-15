import java.util.*;

public class TriangleLineGame {

    static final char EMPTY = '.';
    static final char RED = 'R';
    static final char BLUE = 'B';
    static final int BOARD_SIZE = 15;

    static class MoveScore {
        int line;  // 0 to 14
        int score; // -1 for loss, 0 for draw, 1 for win

        MoveScore(int line, int score) {
            this.line = line;
            this.score = score;
        }
    }

    static void initiateBoard(char[] board) {
        Arrays.fill(board, EMPTY);
    }

    static void playMove(char[] board, int move, char player) {
        board[move] = player;
    }

    static boolean isValidMove(char[] board, int move) {
        return move >= 0 && move < BOARD_SIZE && board[move] == EMPTY;
    }

    static boolean hasLost(char[] board, char player) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 - i; j++) {
                for (int k = 0; k < 4 - i - j; k++) {
                    int a1 = (i * (11 - i) / 2) + j + 1;
                    int a2 = a1 + k + 1;
                    int a3 = 6 + k + (i + j) * (9 - i - j) / 2;
                    a1--; a2--; a3--;
                    if (board[a1] == player && board[a2] == player && board[a3] == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static boolean isFull(char[] board) {
        for (char c : board) {
            if (c == EMPTY) return false;
        }
        return true;
    }

    static MoveScore bestMove(char[] board, char player, int alpha, int beta, int depth, boolean isMaximizing) {
        MoveScore best = new MoveScore(-1, isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE);

        if (hasLost(board, player)) return new MoveScore(-1, -1);
        if (hasLost(board, player == RED ? BLUE : RED)) return new MoveScore(-1, 1);

        if (isMaximizing) {
            for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                if (isValidMove(board, col)) {
                    playMove(board, col, player);
                    MoveScore eval = bestMove(board, player, alpha, beta, depth + 1, false);
                    board[col] = EMPTY;

                    if (eval.score > best.score) {
                        best.score = eval.score;
                        best.line = col;
                    }
                    alpha = Math.max(alpha, best.score);
                    if (alpha >= beta) break;
                }
            }
        } else {
            for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                if (isValidMove(board, col)) {
                    playMove(board, col, player == RED ? BLUE : RED);
                    MoveScore eval = bestMove(board, player, alpha, beta, depth + 1, true);
                    board[col] = EMPTY;

                    if (eval.score < best.score) {
                        best.score = eval.score;
                        best.line = col;
                    }
                    beta = Math.min(beta, best.score);
                    if (alpha >= beta) break;
                }
            }
        }
        return best;
    }

    static void printBoard(char[] board) {
        for (char c : board) System.out.print(c + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] board = new char[BOARD_SIZE];
        initiateBoard(board);

        System.out.println("Initializing Board...");
        printBoard(board);

        System.out.println("Red plays first.");
        System.out.print("Which color do you choose? (R or B): ");
        char user = sc.next().toUpperCase().charAt(0);
        while (user != RED && user != BLUE) {
            System.out.print("Invalid choice. Please select 'R' or 'B': ");
            user = sc.next().toUpperCase().charAt(0);
        }

        char computer = (user == RED) ? BLUE : RED;
        char current = RED;

        while (true) {
            if (current == user) {
                System.out.print("Enter your move (1 to 15): ");
                int move = sc.nextInt();
                while (!isValidMove(board, move - 1)) {
                    System.out.print("Not a valid move. Enter again: ");
                    move = sc.nextInt();
                }
                playMove(board, move - 1, current);
            } else {
                System.out.println("Computer is thinking...");
                MoveScore computerMove = bestMove(board, current, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, true);
                playMove(board, computerMove.line, current);
            }

            printBoard(board);

            if (hasLost(board, current)) {
                if (current == user) {
                    System.out.println("OOPS! Computer Won");
                } else {
                    System.out.println("HURRAY! You Won");
                }
                break;
            }

            current = (current == user) ? computer : user;
        }

        sc.close();
    }
}
