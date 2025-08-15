import java.util.*;

public class Cube {
    private byte[] state;
    private String moveHistory = "";

    public Cube(byte[] inputState) {
        this.state = Arrays.copyOf(inputState, 24);
    }

    public Cube(byte[] inputState, String move) {
        this.state = Arrays.copyOf(inputState, 24);
        this.moveHistory = move;
    }

    public byte[] getState() {
        return Arrays.copyOf(state, 24);
    }

    public String getMove() {
        return moveHistory;
    }

    public String printColor(byte color) {
        switch (color) {
            case 1: return "\033[0;97m@\033[0m";
            case 2: return "\033[0;34m@\033[0m";
            case 3: return "\033[38;5;208m@\033[0m";
            case 4: return "\033[0;31m@\033[0m";
            case 5: return "\033[0;32m@\033[0m";
            case 6: return "\033[0;93m@\033[0m";
            default: return "Invalid Color";
        }
    }

    public void printCube() {
        System.out.println("The state of the cube is as follows:");
        System.out.println("Front Face:");
        System.out.println(printColor(state[3]) + " " + printColor(state[0]));
        System.out.println(printColor(state[6]) + " " + printColor(state[9]) + "\n");

        System.out.println("Right Face:");
        System.out.println(printColor(state[2]) + " " + printColor(state[16]));
        System.out.println(printColor(state[10]) + " " + printColor(state[14]) + "\n");

        System.out.println("Left Face:");
        System.out.println(printColor(state[20]) + " " + printColor(state[4]));
        System.out.println(printColor(state[22]) + " " + printColor(state[8]) + "\n");

        System.out.println("Up Face:");
        System.out.println(printColor(state[19]) + " " + printColor(state[17]));
        System.out.println(printColor(state[5]) + " " + printColor(state[1]) + "\n");

        System.out.println("Down Face:");
        System.out.println(printColor(state[7]) + " " + printColor(state[11]));
        System.out.println(printColor(state[23]) + " " + printColor(state[13]) + "\n");

        System.out.println("Back Face:");
        System.out.println(printColor(state[15]) + " " + printColor(state[18]));
        System.out.println(printColor(state[12]) + " " + printColor(state[21]) + "\n");
    }

    public boolean isValid() {
        return true;
    }

    public boolean isSolved() {
        byte[] solvedState2 = {1, 3, 2, 1, 5, 3, 1, 4, 5, 1, 2, 4, 6, 4, 2, 6, 2, 3, 6, 3, 5, 6, 5, 4};
        for (int i = 0; i < 24; i++) {
            if (solvedState2[i] != state[i]) {
                return false;
            }
        }
        return true;
    }

    // ====== Move Methods (R, Rdash, R2, F, Fdash, F2, U, Udash, U2) ======
    // Example for one move (R) - The rest follow same pattern

    public Cube R() {
        byte[] newState = new byte[24];
        newState[17] = state[0];
        newState[15] = state[1];
        newState[16] = state[2];

        newState[1] = state[9];
        newState[2] = state[10];
        newState[0] = state[11];

        newState[11] = state[12];
        newState[9] = state[13];
        newState[10] = state[14];

        newState[13] = state[15];
        newState[14] = state[16];
        newState[12] = state[17];

        for (int i : new int[]{3,4,5,6,7,8,18,19,20,21,22,23})
            newState[i] = state[i];

        return new Cube(newState, "R" + moveHistory);
    }

    // Implement Rdash(), R2(), F(), Fdash(), F2(), U(), Udash(), U2() similarly...

    public static void BFSSolve(Cube cube) {
        System.out.println("Hold the cube such that the unmoved cubelet has back left down = yellow, left down back = green, down back left = red.");

        Queue<Cube> q = new LinkedList<>();
        q.add(cube);

        while (!q.isEmpty()) {
            Cube currentCube = q.poll();
            System.out.print(currentCube.getMove() + " ");

            if (currentCube.isSolved()) {
                System.out.println("\n\nThe cube has been solved!");
                currentCube.printCube();
                System.out.println("The moves to solve the cube are (Go from back to start): " + currentCube.getMove());
                return;
            } else {
                String move = currentCube.getMove();
                if (move.startsWith("Rdash")) {
                    q.add(currentCube.F());
                    q.add(currentCube.U());
                    q.add(currentCube.U2());
                    q.add(currentCube.Fdash());
                    q.add(currentCube.F2());
                    q.add(currentCube.Udash());
                }
                // Add rest of BFS branching conditions here...
            }
        }
        System.out.println("The queue got empty!");
    }

    public static void main(String[] args) {
        byte[] initialState = { /* fill with your cube state */ };
        Cube c = new Cube(initialState);
        BFSSolve(c);
    }
}
