import java.util.Map;
import java.util.Arrays;
import java.util.function.Function;

public class ContainerWithMostWater {

    /**
     * Approach 1: Two Pointers (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of height array
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: This is the definitive O(N) solution. We place one pointer at the
     * beginning (left) and one at the end (right) of the array. The area at any
     * point
     * is limited by the SHORTER line. Therefore, to maximize the area, we always
     * move
     * the pointer pointing to the shorter line inward, hoping to find a taller line
     * to compensate for the reduction in width.
     */
    public int maxAreaOptimal(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            // The area is bottlenecked by the shortest line
            int currentHeight = Math.min(height[left], height[right]);
            int width = right - left;
            int currentArea = currentHeight * width;

            // Update maxArea if current is larger
            if (currentArea > maxArea) {
                maxArea = currentArea;
            }

            // Move the pointer that is pointing to the shorter line.
            // If they are equal, moving either works (often optimizing to move both).
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    /**
     * Approach 2: Brute Force (Educational Baseline)
     * Time Complexity: O(N^2)
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: Intuitive and exhaustively checks every single possible pairing of
     * left and right boundaries. However, it scales quadratically and guarantees
     * Time Limit Exceeded (TLE) errors on larger test sets.
     */
    public int maxAreaBruteForce(int[] height) {
        int maxArea = 0;
        int n = height.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int currentHeight = Math.min(height[i], height[j]);
                int width = j - i;
                int currentArea = currentHeight * width;

                if (currentArea > maxArea) {
                    maxArea = currentArea;
                }
            }
        }

        return maxArea;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] height;
        int expected;

        TestCase(String name, int[] height, int expected) {
            this.name = name;
            this.height = height;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<int[], Integer> algorithm, int[] height, int expected) {
        int[] heightCopy = Arrays.copyOf(height, height.length);

        int result = algorithm.apply(heightCopy);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-40s (Expected: %-3d): %-3d %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        ContainerWithMostWater solution = new ContainerWithMostWater();

        // 1. Define Algorithms to Test
        Map<String, Function<int[], Integer>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Two Pointers O(N) Time / O(1) Space", solution::maxAreaOptimal);
        algorithms.put("Solution 2: Brute Force O(N^2) Time / O(1) Space", solution::maxAreaBruteForce);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Mixed heights", new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 }, 49),
                new TestCase("Edge Case: Flat Array", new int[] { 1, 1 }, 1),
                new TestCase("Edge Case: Small constraints", new int[] { 4, 3, 2, 1, 4 }, 16),
                new TestCase("Edge Case: Taller lines inner", new int[] { 1, 2, 1 }, 2),
                new TestCase("Edge Case: Tall bounds", new int[] { 1000, 1000 }, 1000)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], Integer>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.height, tc.expected);
            }
            System.out.println();
        }
    }
}
