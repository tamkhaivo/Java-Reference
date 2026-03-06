import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.function.Function;

public class ValidSudoku {

    /**
     * Approach 1: Bitmasking (Optimal Space & Time)
     * Time Complexity: O(N^2) -> O(81) -> O(1)
     * Space Complexity: O(N) -> O(27) integers -> O(1)
     * 
     * Trade-off: Theoretically the fastest possible approach. Instead of HashSets,
     * we use an array of 9 integers (or shorts) for rows, cols, and boxes.
     * The bits of the integer represent which numbers we have seen.
     * E.g., if we see '4', we flip the 4th bit of that row's integer to 1.
     */
    public boolean isValidSudokuBitmask(char[][] board) {
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] boxes = new int[9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    continue;
                }

                int val = board[r][c] - '0';
                int bitShift = 1 << val; // Bit representation of the number

                // Calculate which of the 9 boxes we are currently sitting in
                int boxIdx = (r / 3) * 3 + (c / 3);

                // Check if the bit is already set (meaning we've seen the number)
                if ((rows[r] & bitShift) != 0 ||
                        (cols[c] & bitShift) != 0 ||
                        (boxes[boxIdx] & bitShift) != 0) {
                    return false;
                }

                // Turn on the bit to mark the number as seen
                rows[r] |= bitShift;
                cols[c] |= bitShift;
                boxes[boxIdx] |= bitShift;
            }
        }
        return true;
    }

    /**
     * Approach 2: One-Pass String HashSets (Clean & Idiomatic)
     * Time Complexity: O(N^2)
     * Space Complexity: O(N^2) for Hash strings
     * 
     * Trade-off: Very readable and hard to mess up during interviews.
     * We encode the row, col, and box into unique Strings.
     */
    public boolean isValidSudokuStringHash(char[][] board) {
        Set<String> seen = new HashSet<>();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char number = board[r][c];
                if (number != '.') {
                    // HashSet.add() returns false if the item was already present!
                    if (!seen.add(number + " in row " + r) ||
                            !seen.add(number + " in column " + c) ||
                            !seen.add(number + " in block " + r / 3 + "-" + c / 3)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Approach 3: Your Original Solution
     * Time Complexity: O(3 * N^2)
     * Space Complexity: O(N) max 9 items in the set at any time
     */
    public boolean isValidSudokuOriginalFixed(char[][] board) {
        int ROWS = board.length;
        int COLS = board[0].length;

        // 1. Check Rows
        HashSet<Character> seenRowNum = new HashSet<>();
        for (int row = 0; row < ROWS; row++) {
            seenRowNum.clear();
            for (int col = 0; col < COLS; col++) {
                char current = board[row][col];
                if (current == '.')
                    continue;
                if (seenRowNum.contains(current)) {
                    return false; // Found a duplicate
                }
                seenRowNum.add(current);
            }
        }

        // 2. Check Cols
        HashSet<Character> seenColNum = new HashSet<>();
        for (int col = 0; col < COLS; col++) {
            seenColNum.clear();
            for (int row = 0; row < ROWS; row++) {
                char current = board[row][col];
                if (current == '.')
                    continue;
                if (seenColNum.contains(current)) {
                    return false; // Found a duplicate
                }
                seenColNum.add(current);
            }
        }

        // 3. Check 3x3 Boxes
        HashSet<Character> seenBoxNum = new HashSet<>();
        // Iterate over the 9 top-left corners of the boxes (0, 3, 6)
        for (int rowStart = 0; rowStart < ROWS; rowStart += 3) {
            for (int colStart = 0; colStart < COLS; colStart += 3) {
                seenBoxNum.clear();
                // Check the 3x3 grid
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        char current = board[rowStart + r][colStart + c];
                        if (current == '.')
                            continue;
                        if (seenBoxNum.contains(current)) {
                            return false; // Found a duplicate
                        }
                        seenBoxNum.add(current);
                    }
                }
            }
        }

        return true;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        char[][] board;
        boolean expected;

        TestCase(String name, char[][] board, boolean expected) {
            this.name = name;
            this.board = board;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<char[][], Boolean> algorithm, char[][] board,
            boolean expected) {
        boolean result = algorithm.apply(board);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-36s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        ValidSudoku solution = new ValidSudoku();

        // 1. Define Algorithms to Test
        Map<String, Function<char[][], Boolean>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Bitmasking O(1) Space", solution::isValidSudokuBitmask);
        algorithms.put("Solution 2: String Hashing (Clean Code)", solution::isValidSudokuStringHash);
        algorithms.put("Solution 3: Multi-Pass Original (Fixed)", solution::isValidSudokuOriginalFixed);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Valid Board", new char[][] {
                        { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
                        { '6', '.', '.', '1', '9', '5', '.', '.', '.' },
                        { '.', '9', '8', '.', '.', '.', '.', '6', '.' },
                        { '8', '.', '.', '.', '6', '.', '.', '.', '3' },
                        { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
                        { '7', '.', '.', '.', '2', '.', '.', '.', '6' },
                        { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
                        { '.', '.', '.', '4', '1', '9', '.', '.', '5' },
                        { '.', '.', '.', '.', '8', '.', '.', '7', '9' }
                }, true),
                new TestCase("Invalid Row (Two 8s in Row 2)", new char[][] {
                        { '8', '3', '.', '.', '7', '.', '.', '.', '.' },
                        { '6', '.', '.', '1', '9', '5', '.', '.', '.' },
                        { '.', '9', '8', '.', '.', '.', '.', '8', '.' }, // Two 8s here
                        { '8', '.', '.', '.', '6', '.', '.', '.', '3' },
                        { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
                        { '7', '.', '.', '.', '2', '.', '.', '.', '6' },
                        { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
                        { '.', '.', '.', '4', '1', '9', '.', '.', '5' },
                        { '.', '.', '.', '.', '8', '.', '.', '7', '9' }
                }, false),
                new TestCase("Invalid Column (Two 5s in Col 0)", new char[][] {
                        { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
                        { '5', '.', '.', '1', '9', '5', '.', '.', '.' }, // Col 0 has 5 again
                        { '.', '9', '8', '.', '.', '.', '.', '6', '.' },
                        { '8', '.', '.', '.', '6', '.', '.', '.', '3' },
                        { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
                        { '7', '.', '.', '.', '2', '.', '.', '.', '6' },
                        { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
                        { '.', '.', '.', '4', '1', '9', '.', '.', '5' },
                        { '.', '.', '.', '.', '8', '.', '.', '7', '9' }
                }, false),
                new TestCase("Invalid 3x3 Box (Two 3s in Box 0)", new char[][] {
                        { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
                        { '6', '.', '3', '1', '9', '5', '.', '.', '.' }, // 3 is top-left box again
                        { '.', '9', '8', '.', '.', '.', '.', '6', '.' },
                        { '8', '.', '.', '.', '6', '.', '.', '.', '3' },
                        { '4', '.', '.', '8', '.', '3', '.', '.', '1' },
                        { '7', '.', '.', '.', '2', '.', '.', '.', '6' },
                        { '.', '6', '.', '.', '.', '.', '2', '8', '.' },
                        { '.', '.', '.', '4', '1', '9', '.', '.', '5' },
                        { '.', '.', '.', '.', '8', '.', '.', '7', '9' }
                }, false)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<char[][], Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.board, tc.expected);
            }
            System.out.println();
        }
    }
}
