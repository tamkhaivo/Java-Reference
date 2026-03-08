import java.util.Map;
import java.util.function.BiFunction;
import java.util.Arrays;

public class SearchA2DMatrix {

    /**
     * Approach 1: Treat as 1D Array + Binary Search (Optimal Time & Space)
     * Time Complexity: O(log(m * n)) -> Logarithmic time exactly as requested.
     * Space Complexity: O(1) auxiliary space.
     * 
     * Trade-off: The standard and definitive solution. Because each row is sorted,
     * and the first integer of each row is strictly greater than the last of the
     * previous,
     * the entire matrix can be conceptually flattened into one perfectly sorted 1D
     * array.
     * We run standard Binary Search, dynamically mapping 1D mid-indices back into
     * 2D Coordinates.
     */
    public boolean searchMatrixOptimal(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // "Flatten" indices
        int left = 0;
        int right = rows * cols - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Map 1D index back to 2D matrix coordinates
            int midCurrentValue = matrix[mid / cols][mid % cols];

            if (midCurrentValue == target) {
                return true;
            } else if (midCurrentValue < target) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }

        return false;
    }

    /**
     * Approach 2: Double Binary Search (Row then Col)
     * Time Complexity: O(log(m) + log(n)) -> This evaluates identically to O(log(m
     * * n))
     * Space Complexity: O(1) auxiliary space.
     * 
     * Trade-off: Instead of dealing with the math of mapping 1D indices `mid/cols`
     * and `mid%cols`,
     * you run Binary Search twice. First, BS over the rows to find WHICH row it
     * must be in.
     * Second, BS inside that specific target row. Very readable and interview safe.
     */
    public boolean searchMatrixDoubleBS(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        // --- Step 1: Binary Search to find the correct row ---
        int top = 0;
        int bottom = matrix.length - 1;
        int targetRow = -1;

        while (top <= bottom) {
            int midRow = top + (bottom - top) / 2;

            if (target < matrix[midRow][0]) {
                // Target is smaller than the smallest item in this row -> go UP
                bottom = midRow - 1;
            } else if (target > matrix[midRow][matrix[0].length - 1]) {
                // Target is larger than the largest item in this row -> go DOWN
                top = midRow + 1;
            } else {
                // Target is within the bounds of this row!
                targetRow = midRow;
                break;
            }
        }

        // Target doesn't fit in any row bounds
        if (targetRow == -1) {
            return false;
        }

        // --- Step 2: Standard Binary Search on that specific target sequence ---
        int left = 0;
        int right = matrix[targetRow].length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (matrix[targetRow][mid] == target) {
                return true;
            } else if (matrix[targetRow][mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    /**
     * Approach 3: Brute Force Traverse
     * Time Complexity: O(m * n)
     * Space Complexity: O(1)
     * 
     * Trade-off: Completely ignores the two sorted properties provided in the
     * prompt.
     * Highly unoptimized, strictly serves as our testing baseline.
     */
    public boolean searchMatrixBruteForce(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int val : row) {
                if (val == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[][] matrix;
        int target;
        boolean expected;

        TestCase(String name, int[][] matrix, int target, boolean expected) {
            this.name = name;
            this.matrix = matrix;
            this.target = target;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<int[][], Integer, Boolean> algorithm, int[][] matrix,
            int target, boolean expected) {
        boolean result = algorithm.apply(matrix, target);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-38s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        SearchA2DMatrix solution = new SearchA2DMatrix();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<int[][], Integer, Boolean>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: 1D Mapped Binary Search O(log(m*n))", solution::searchMatrixOptimal);
        algorithms.put("Solution 2: Double Binary Search (Row + Col)", solution::searchMatrixDoubleBS);
        algorithms.put("Solution 3: Brute Force Traversal O(m * n)", solution::searchMatrixBruteForce);

        // 2. Define Test Cases
        int[][] standardMatrix = {
                { 1, 3, 5, 7 },
                { 10, 11, 16, 20 },
                { 23, 30, 34, 60 }
        };

        int[][] tightMatrix = {
                { 1, 3 }
        };

        TestCase[] testCases = {
                new TestCase("Standard Case: Exists in middle", standardMatrix, 3, true),
                new TestCase("Standard Case: Does not exist (hole)", standardMatrix, 13, false),
                new TestCase("Edge Case: Target is smaller than strictly all", standardMatrix, 0, false),
                new TestCase("Edge Case: Target is larger than strictly all", standardMatrix, 100, false),
                new TestCase("Edge Case: Exact Top-Left element", standardMatrix, 1, true),
                new TestCase("Edge Case: Exact Bot-Right element", standardMatrix, 60, true),
                new TestCase("Edge Case: Single Row (Exists)", tightMatrix, 3, true),
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<int[][], Integer, Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.matrix, tc.target, tc.expected);
            }
            System.out.println();
        }
    }
}
