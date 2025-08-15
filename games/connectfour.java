import java.util.Scanner;

public class ConnectFour5x4 {

    // Board config
    private static final int ROWS = 4;
    private static final int COLS = 5;

    // Pieces
    private static final char EMPTY = '.';
    private static final char RED   = 'R';
    private static final char BLUE  = 'B';

    // Board type
    private final char[][] board = new char[ROWS][COLS];

    public ConnectFour5x4() {
        initiateBoard();
    }

    /* ------------------- Core Board Utilities ------------------- */

    private void initiateBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = EMPTY;
            }
        }
    }

    private void dropPiece(int row, int col, char player) {
        board[row][col] = player;
    }

    private boolean isValidLocation(int col) {
        return board[0][col] == EMPTY;
    }

    private int nextOpenRow(int col) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == EMPTY) return r;
        }
        return -1;
    }

    private void printBoard() {
        System.out.println();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isDraw() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == EMPTY) return false;
            }
        }
        return true;
    }

    private boolean hasWon(char player) {
        // Horizontal
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == player && board[r][c + 1] == player &&
                    board[r][c + 2] == player && board[r][c + 3] == player) {
                    return true;
                }
            }
        }
        // Vertical
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == player && board[r + 1][c] == player &&
                    board[r + 2][c] == player && board[r + 3][c] == player) {
                    return true;
                }
            }
        }
        // Positive diagonal (r+, c+)
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == player && board[r + 1][c + 1] == player &&
                    board[r + 2][c + 2] == player && board[r + 3][c + 3] == player) {
                    return true;
                }
            }
        }
        // Negative diagonal (r-, c+)
        for (int r = 3; r < ROWS; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (board[r][c] == player && board[r - 1][c + 1] == player &&
                    board[r - 2][c + 2] == player && board[r - 3][c + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    /* ------------------- Minimax + Alpha-Beta ------------------- */

    private static class MoveScore {
        int col;
        int score;
        MoveScore(int col, int score) {
            this.col = col;
            this.score = score;
        }
    }

    private char opponentOf(char p) {
        return (p == RED) ? BLUE : RED;
    }

    /**
     * Returns the best move (column) and score from current board for 'player'.
     * maxTurn == true => we choose the best for 'player'
     * maxTurn == false => opponent turn (minimize 'player' score)
     */
    private MoveScore bestMove(char player, int alpha, int beta, int depth, boolean maxTurn) {
        // Terminal checks
        if (hasWon(player)) {
            // Win for current perspective player
            return new MoveScore(-1, 1);
        } else if (hasWon(opponentOf(player))) {
            // Opponent already has a win
            return new MoveScore(-1, -1);
        } else if (isDraw()) {
            return new MoveScore(-1, 0);
        }

        int bestCol = -1;

        if (maxTurn) {
            int bestScore = Integer.MIN_VALUE;
            for (int c = 0; c < COLS; c++) {
                if (!isValidLocation(c)) continue;
                int r = nextOpenRow(c);
                dropPiece(r, c, player);
                MoveScore ms = bestMove(player, alpha, beta, depth + 1, false);
                board[r][c] = EMPTY;

                if (ms.score > bestScore) {
                    bestScore = ms.score;
                    bestCol = c;
                }
                alpha = Math.max(alpha, bestScore);
                if (alpha >= beta) break; // beta cut-off
            }
            return new MoveScore(bestCol, bestScore);
        } else {
            // Opponent turn: they try to minimize 'player'
            int bestScore = Integer.MAX_VALUE;
            char opp = opponentOf(player);
            for (int c = 0; c < COLS; c++) {
                if (!isValidLocation(c)) continue;
                int r = nextOpenRow(c);
                dropPiece(r, c, opp);
                MoveScore ms = bestMove(player, alpha, beta, depth + 1, true);
                board[r][c] = EMPTY;

                if (ms.score < bestScore) {
                    bestScore = ms.score;
                    bestCol = c;
                }
                beta = Math.min(beta, bestScore);
                if (alpha >= beta) break; // alpha cut-off
            }
            return new MoveScore(bestCol, bestScore);
        }
    }

    /* ------------------- Game Loop ------------------- */

    public void play() {
        printBoard();

        Scanner sc = new Scanner(System.in);
        char userChoice;

        // Ask user color
        System.out.print("Do you want to start as Red (R) or Blue (B)? ");
        userChoice = sc.next().trim().toUpperCase().charAt(0);
        while (userChoice != RED && userChoice != BLUE) {
            System.out.print("Invalid choice. Please select 'R' or 'B': ");
            userChoice = sc.next().trim().toUpperCase().charAt(0);
        }

        char computerChoice = opponentOf(userChoice);
        char currentPlayer = RED; // same as C: Red always starts

        while (true) {
            if (currentPlayer == userChoice) {
                // User move
                int move;
                while (true) {
                    System.out.print("Choose your move (1-" + COLS + "): ");
                    while (!sc.hasNextInt()) {
                        System.out.print("Invalid input. Enter a number (1-" + COLS + "): ");
                        sc.next();
                    }
                    move = sc.nextInt();
                    if (move >= 1 && move <= COLS && isValidLocation(move - 1)) break;
                    System.out.println("Invalid choice. Please select a valid move.");
                }
                int row = nextOpenRow(move - 1);
                dropPiece(row, move - 1, userChoice);
            } else {
                // Computer move
                System.out.println("\nComputer is thinking..");
                MoveScore best = bestMove(computerChoice, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, true);
                int col = best.col;
                if (col == -1) {
                    // No valid move (should mean draw)
                    System.out.println("No valid moves left.");
                    printBoard();
                    break;
                }
                int row = nextOpenRow(col);
                dropPiece(row, col, computerChoice);
            }

            printBoard();

            // Win check for the player who just moved
            if (hasWon(currentPlayer)) {
                if (currentPlayer == userChoice) {
                    System.out.println("You Won");
                } else {
                    System.out.println("Computer Won");
                }
                break;
            }

            // Draw?
            if (isDraw()) {
                System.out.println("Game Draw");
                break;
            }

            // Switch turn
            currentPlayer = opponentOf(currentPlayer);
        }

        sc.close();
    }

    /* ------------------- Entry Point ------------------- */

    public static void main(String[] args) {
        new ConnectFour5x4().play();
    }
}
