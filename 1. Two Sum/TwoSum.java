import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.Arrays;

public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        // Space-Optimized Approach Trade-off: Use HashMap for O(1) lookups
        Map<Integer, Integer> missingInteger = new HashMap<>();

        for (int idx = 0; idx < nums.length; idx++) {
            int number = nums[idx];
            int complement = target - number;

            if (missingInteger.containsKey(complement)) {
                int idx2 = missingInteger.get(complement);
                // Corrected Java syntax for array initialization
                return new int[] { idx2, idx };
            }
            // Store the current number and its index
            missingInteger.put(number, idx);
        }

        // Java requires a return statement here in case no solution is found
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     * Alternative Solution: Brute Force
     * 
     * If we wanted to avoid the O(N) space overhead of a HashMap, we could
     * do a nested loop checking every possible pair.
     * Trade-off: This sacrifices time complexity significantly.
     */
    public int[] twoSumBruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] input;
        int target;
        int[] expected;

        TestCase(String name, int[] input, int target, int[] expected) {
            this.name = name;
            this.input = input;
            this.target = target;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<int[], Integer, int[]> algorithm, int[] input, int target,
            int[] expected) {
        int[] inputCopy = Arrays.copyOf(input, input.length);

        int[] result = algorithm.apply(inputCopy, target);
        boolean passed = Arrays.equals(result, expected);

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-36s (Expected: %s): %s %s",
                testName, Arrays.toString(expected), Arrays.toString(result), status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        TwoSum solution = new TwoSum();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<int[], Integer, int[]>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: One-Pass HashMap (Your idea)", solution::twoSum);
        algorithms.put("Solution 2: Brute Force (Alternative)", solution::twoSumBruteForce);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Exists in middle", new int[] { 2, 7, 11, 15 }, 9, new int[] { 0, 1 }),
                new TestCase("Standard Case: Exists at ends", new int[] { 3, 2, 4 }, 6, new int[] { 1, 2 }),
                new TestCase("Standard Case: Same adjacent numbers", new int[] { 3, 3 }, 6, new int[] { 0, 1 }),
                new TestCase("Edge Case: Negative numbers", new int[] { -1, -2, -3, -4, -5 }, -8, new int[] { 2, 4 }),
                new TestCase("Edge Case: Zero target", new int[] { 0, 4, 3, 0 }, 0, new int[] { 0, 3 })
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<int[], Integer, int[]>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.input, tc.target, tc.expected);
            }
            System.out.println();
        }
    }
}