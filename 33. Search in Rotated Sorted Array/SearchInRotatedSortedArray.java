import java.util.Map;
import java.util.Arrays;
import java.util.function.BiFunction;

public class SearchInRotatedSortedArray {

    /**
     * Approach 1: Modified Binary Search (Optimal Time & Space)
     * Time Complexity: O(log N) where N is the length of the array
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: Instead of linear search, we use the property that in a rotated
     * sorted array, at least one half of the array (left or right) is ALWAYS
     * strictly sorted.
     * We figure out which half is sorted, check if our target falls within the
     * bounds of
     * that sorted half. If it does, we search that half. If it doesn't, we search
     * the other half.
     */
    public int searchOptimal(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return -1;

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            }

            // Core Logic: Check if the LEFT half is perfectly sorted
            if (nums[left] <= nums[mid]) {
                // If the target is within the bounds of this sorted left half
                if (target >= nums[left] && target < nums[mid]) {
                    // Target must be in the left half
                    right = mid - 1;
                } else {
                    // Target must be in the right half
                    left = mid + 1;
                }
            }
            // If the left half ISN'T sorted, the RIGHT half MUST be sorted
            else {
                // If the target is within the bounds of this sorted right half
                if (target > nums[mid] && target <= nums[right]) {
                    // Target must be in the right half
                    left = mid + 1;
                } else {
                    // Target must be in the left half
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

    /**
     * Approach 1b: Your Optimized Binary Search (Optimal Time & Space)
     * Time Complexity: O(log N)
     * Space Complexity: O(1)
     * 
     * Trade-off: Functionally identical to Approach 1, but utilizes local variables
     * (leftNum, rightNum, midNum) to reduce array lookups, which can
     * microscopically
     * improve constant-time bounds in tight CPU execution. The conditional logic
     * cleanly checks
     * `midNum >= leftNum` to track sorted halves with maximum efficiency.
     */
    public int searchOptimal2(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int midNum = nums[mid];
            int leftNum = nums[left];
            int rightNum = nums[right];

            if (midNum == target)
                return mid;
            if (midNum >= leftNum) { // Left-Side Sorted
                if (leftNum <= target && target < midNum)
                    right = mid - 1;
                else
                    left = mid + 1;
            } else {
                if (midNum < target && target <= rightNum)
                    left = mid + 1;
                else
                    right = mid - 1;
            }
        }
        return -1;

    }

    /**
     * Approach 2: Brute Force (Linear Scan)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * 
     * Trade-off: Perfectly intuitive layout, but it fails the specific O(log N)
     * runtime complexity requirement stated by the prompt. Provided for
     * completeness.
     */
    public int searchBruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] nums;
        int target;
        int expected;

        TestCase(String name, int[] nums, int target, int expected) {
            this.name = name;
            this.nums = nums;
            this.target = target;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<int[], Integer, Integer> algorithm, int[] nums, int target,
            int expected) {
        int[] numsCopy = Arrays.copyOf(nums, nums.length);

        int result = algorithm.apply(numsCopy, target);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-45s (Expected: %-2d): %-2d %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        SearchInRotatedSortedArray solution = new SearchInRotatedSortedArray();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<int[], Integer, Integer>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Modified Binary Search O(log N) / O(1)", solution::searchOptimal);
        algorithms.put("Solution 1b: Your Optimized Binary Search O(log N)", solution::searchOptimal2);
        algorithms.put("Solution 2: Brute Force O(N) / O(1)", solution::searchBruteForce);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Rotated, target exists", new int[] { 4, 5, 6, 7, 0, 1, 2 }, 0, 4),
                new TestCase("Standard Case: Rotated, target doesnt exist", new int[] { 4, 5, 6, 7, 0, 1, 2 }, 3, -1),
                new TestCase("Standard Case: Normal Sorted Array", new int[] { 0, 1, 2, 4, 5, 6, 7 }, 4, 3), // rotation=0
                new TestCase("Edge Case: Single element array (exists)", new int[] { 1 }, 1, 0),
                new TestCase("Edge Case: Single element array (doesnt exist)", new int[] { 2 }, 3, -1),
                new TestCase("Edge Case: Target at the exact unsorted pivot", new int[] { 5, 1, 2, 3, 4 }, 1, 1),
                new TestCase("Edge Case: Target is the first element", new int[] { 4, 5, 6, 7, 0, 1, 2 }, 4, 0),
                new TestCase("Edge Case: Target is the last element", new int[] { 4, 5, 6, 7, 0, 1, 2 }, 2, 6)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<int[], Integer, Integer>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.nums, tc.target, tc.expected);
            }
            System.out.println();
        }
    }
}
