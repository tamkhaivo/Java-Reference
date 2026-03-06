import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class ProductOfArrayExceptSelf {

    /**
     * Approach 1: Optimal Prefix & Postfix Running Products
     * Time Complexity: O(N)
     * Space Complexity: O(1) auxiliary space (the result array is usually not
     * counted as extra space for the problem requirements).
     * 
     * Trade-off: Theoretically the best solution. We avoid the division operator
     * entirely
     * and compute the product of all elements to the left (prefix) and to the right
     * (postfix).
     */
    public int[] productExceptSelfOptimal(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // 1. Calculate prefix products and store them directly in the result array
        // (This saves us the O(N) space of a dedicated 'left' array)
        int prefix = 1;
        for (int i = 0; i < n; i++) {
            result[i] = prefix;
            prefix *= nums[i];
        }

        // 2. Calculate postfix products on the fly, and multiply them into the result
        // array
        int postfix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= postfix;
            postfix *= nums[i];
        }

        return result;
    }

    /**
     * Approach 2: Explicit Left and Right Arrays (Educational step towards Optimal)
     * Time Complexity: O(N)
     * Space Complexity: O(N) for maintaining explicit left and right arrays.
     * 
     * Trade-off: Much easier to understand how the prefix/postfix mathematically
     * works,
     * but we sacrifice O(N) auxiliary space.
     */
    public int[] productExceptSelfArrays(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        int[] result = new int[n];

        // Fill left array (product of everything to the left of i)
        left[0] = 1;
        for (int i = 1; i < n; i++) {
            left[i] = left[i - 1] * nums[i - 1];
        }

        // Fill right array (product of everything to the right of i)
        right[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            right[i] = right[i + 1] * nums[i + 1];
        }

        // Result is the product of everything to the left * everything to the right
        for (int i = 0; i < n; i++) {
            result[i] = left[i] * right[i];
        }

        return result;
    }

    /**
     * Approach 3: Your Original Solution (Brute Force)
     * Time Complexity: O(N^2)
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: This will time out (TLE) on LeetCode for large inputs due to
     * O(N^2) time constraints,
     * but logic is perfectly sound for smaller arrays!
     */
    public int[] productExceptSelfOriginal(int[] nums) {
        int[] result = new int[nums.length];
        for (int currentIdx = 0; currentIdx < nums.length; currentIdx++) {
            int product = 1;
            for (int productIdx = 0; productIdx < nums.length; productIdx++) {
                if (currentIdx == productIdx)
                    continue;
                product *= nums[productIdx];
            }
            result[currentIdx] = product;
        }
        return result;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] nums;
        int[] expected;

        TestCase(String name, int[] nums, int[] expected) {
            this.name = name;
            this.nums = nums;
            this.expected = expected;
        }
    }

    // Taking Function<int[], int[]> since the method signatures take a single int[]
    // parameter
    private static void runTest(String testName, Function<int[], int[]> algorithm, int[] nums, int[] expected) {
        int[] numsCopy = Arrays.copyOf(nums, nums.length);

        int[] result = algorithm.apply(numsCopy);
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
        ProductOfArrayExceptSelf solution = new ProductOfArrayExceptSelf();

        // 1. Define Algorithms to Test
        Map<String, Function<int[], int[]>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: In-Place Prefix & Postfix O(N) Time / O(1) Space",
                solution::productExceptSelfOptimal);
        algorithms.put("Solution 2: Left & Right Arrays O(N) Time / O(N) Space", solution::productExceptSelfArrays);
        algorithms.put("Solution 3: Original Setup O(N^2) Time / O(1) Space", solution::productExceptSelfOriginal);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case", new int[] { 1, 2, 3, 4 }, new int[] { 24, 12, 8, 6 }),
                new TestCase("Edge Case: Contains Zeros", new int[] { -1, 1, 0, -3, 3 }, new int[] { 0, 0, 9, 0, 0 }),
                new TestCase("Edge Case: Two Zeros", new int[] { 0, 4, 0 }, new int[] { 0, 0, 0 }),
                new TestCase("Edge Case: Two Elements", new int[] { 10, 5 }, new int[] { 5, 10 })
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], int[]>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.nums, tc.expected);
            }
            System.out.println();
        }
    }
}
