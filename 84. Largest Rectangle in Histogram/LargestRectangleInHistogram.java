import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Arrays;
import java.util.function.Function;

public class LargestRectangleInHistogram {

    /**
     * Approach 1: Monotonic Stack (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of heights array
     * Space Complexity: O(N) auxiliary space for the stack
     * 
     * Trade-off: This is the definitive O(N) solution. We maintain a Monotonically
     * INCREASING stack.
     * The stack stores the INDICES of the heights.
     * When we encounter a bar that is SHORTER than the bar at the top of the stack,
     * we know the
     * taller bar cannot be extended any further to the right. We pop it, calculate
     * its maximum area,
     * and repeat until the stack conditions are satisfied.
     */
    public int largestRectangleAreaDeque(int[] heights) {
        int maxArea = 0;
        int n = heights.length;

        // Stack will store INDICES of the heights array.
        // It maintains indices in a way that heights[stack[i]] is strictly increasing.
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i <= n; i++) {
            // We append a pseudo-height of 0 at the very end to force the stack to flush
            // everything
            int currentHeight = (i == n) ? 0 : heights[i];

            // While stack is not empty and the current bar breaks the increasing order
            while (!stack.isEmpty() && currentHeight < heights[stack.peek()]) {
                // We've found the right-boundary for the bar at the top of the stack
                int height = heights[stack.pop()];

                // Calculate width:
                // If stack is empty, it means this popped bar was the smallest seen so far,
                // so it extends from the very beginning (width = i).
                // Otherwise, it extends back to the index currently at the top of the stack.
                int width = stack.isEmpty() ? i : (i - stack.peek() - 1);

                maxArea = Math.max(maxArea, height * width);
            }

            // Push the current index onto the stack
            stack.push(i);
        }

        return maxArea;
    }

    /**
     * Approach 2: Primitive Array Stack (Highly Optimized Time)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * 
     * Trade-off: Exact same logic as Approach 1, but completely bypasses
     * java.util.ArrayDeque and
     * autoboxing constraints. We maintain a native int array and a pointer acting
     * as our stack.
     * This consistently achieves 99%+ execution times on LeetCode.
     */
    public int largestRectangleAreaPrimitive(int[] heights) {
        int maxArea = 0;
        int n = heights.length;

        int[] stack = new int[n + 1]; // +1 to handle the pseudo-push
        int top = -1;

        for (int i = 0; i <= n; i++) {
            int currentHeight = (i == n) ? 0 : heights[i];

            while (top >= 0 && currentHeight < heights[stack[top]]) {
                int height = heights[stack[top--]]; // Pop

                int width = (top == -1) ? i : (i - stack[top] - 1);

                int area = height * width;
                if (area > maxArea) {
                    maxArea = area;
                }
            }
            stack[++top] = i; // Push
        }

        return maxArea;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] heights;
        int expected;

        TestCase(String name, int[] heights, int expected) {
            this.name = name;
            this.heights = heights;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<int[], Integer> algorithm, int[] heights, int expected) {
        int[] heightsCopy = Arrays.copyOf(heights, heights.length);

        int result = algorithm.apply(heightsCopy);
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
        LargestRectangleInHistogram solution = new LargestRectangleInHistogram();

        // 1. Define Algorithms to Test
        Map<String, Function<int[], Integer>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Monotonic Stack (ArrayDeque) O(N)", solution::largestRectangleAreaDeque);
        algorithms.put("Solution 2: Primitive Array Stack O(N)", solution::largestRectangleAreaPrimitive);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Valley", new int[] { 2, 1, 5, 6, 2, 3 }, 10), // 5 and 6 form a 10-area
                                                                                           // rect
                new TestCase("Standard Case: Flat Array", new int[] { 2, 4 }, 4), // 2 and 4 form a 4-area rect
                new TestCase("Standard Case: All equal", new int[] { 2, 2, 2, 2 }, 8),
                new TestCase("Edge Case: Strictly Increasing", new int[] { 1, 2, 3, 4, 5 }, 9), // 3, 4, 5 forms 3*3=9
                new TestCase("Edge Case: Strictly Decreasing", new int[] { 5, 4, 3, 2, 1 }, 9), // similar concept
                new TestCase("Edge Case: Empty or Zeros", new int[] { 0, 0, 0, 0 }, 0),
                new TestCase("Edge Case: Single Element", new int[] { 5 }, 5),
                new TestCase("Edge Case: Alternating heights", new int[] { 2, 1, 2, 1, 2 }, 5) // width of 5 * height 1
                                                                                               // = 5
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], Integer>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.heights, tc.expected);
            }
            System.out.println();
        }
    }
}
