import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Arrays;
import java.util.function.Function;

public class DailyTemperature {

    /**
     * Approach 1: Monotonic Stack (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of temperatures
     * Space Complexity: O(N) auxiliary space for the stack
     * 
     * Trade-off: The standard data structure solution. We use a Monotonically
     * Decreasing
     * stack, which means the elements in the stack are strictly decreasing. We
     * store
     * the INDICES in the stack, not the temperatures themselves. Because each
     * element
     * is pushed and popped exactly once, time scales to O(N).
     */
    public int[] dailyTemperaturesDeque(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];

        // Stack stores the INDICES of the days
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            int currentTemp = temperatures[i];

            // While the stack isn't empty, and today's temperature is WARMER
            // than the temperature of the day recorded at the top of the stack
            while (!stack.isEmpty() && currentTemp > temperatures[stack.peek()]) {
                // We found a warmer day! Pop it off the stack and calculate the difference.
                int prevDayIndex = stack.pop();
                answer[prevDayIndex] = i - prevDayIndex;
            }

            // Push today's candidate onto the stack to wait for a warmer day
            stack.push(i);
        }

        return answer;
    }

    /**
     * Approach 2: Primitive Array Stack (Highly Optimized Time)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * 
     * Trade-off: Exact same algorithm as Approach 1, but we bypass the overhead of
     * Java Collections (ArrayDeque) and autoboxing primitive ints into Integer
     * objects.
     * Simulating a stack with an int[] and a top pointer is the fastest way to
     * crack
     * Monotonic stack problems in a competitive setting.
     */
    public int[] dailyTemperaturesPrimitiveStack(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];

        // Manually simulate a stack
        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            int currentTemp = temperatures[i];

            // While stack is not empty and today is warmer
            while (top >= 0 && currentTemp > temperatures[stack[top]]) {
                int prevDayIndex = stack[top--]; // Read top and decrement pointer (Pop)
                answer[prevDayIndex] = i - prevDayIndex;
            }
            // Add today to stack (Push)
            stack[++top] = i;
        }

        return answer;
    }

    /**
     * Approach 3: Brute Force (Educational Anti-Pattern)
     * Time Complexity: O(N^2)
     * Space Complexity: O(1) auxiliary space (excluding the output array)
     * 
     * Trade-off: Intuitive and easy to write. However, for a given start day, it
     * linearly
     * scans the remainder of the array. For perfectly decreasing lists, this is
     * disastrous
     * and guarantees Time Limit Exceeded (TLE) errors for n=10^5 arrays.
     */
    public int[] dailyTemperaturesBruteForce(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (temperatures[j] > temperatures[i]) {
                    answer[i] = j - i;
                    break;
                }
            }
        }

        return answer;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] temperatures;
        int[] expected;

        TestCase(String name, int[] temperatures, int[] expected) {
            this.name = name;
            this.temperatures = temperatures;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<int[], int[]> algorithm, int[] temperatures, int[] expected) {
        // Deep copy the array to prevent side-effects if solutions mutate input
        int[] tempCopy = Arrays.copyOf(temperatures, temperatures.length);

        int[] result = algorithm.apply(tempCopy);
        boolean passed = Arrays.equals(result, expected);

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-40s: %s", testName, status));
        System.out.println("    Expected: " + Arrays.toString(expected));
        System.out.println("    Result  : " + Arrays.toString(result) + "\n");
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        DailyTemperature solution = new DailyTemperature();

        // 1. Define Algorithms to Test
        Map<String, Function<int[], int[]>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Monotonic Stack (ArrayDeque) O(N)", solution::dailyTemperaturesDeque);
        algorithms.put("Solution 2: Primitive Array Stack O(N)", solution::dailyTemperaturesPrimitiveStack);
        algorithms.put("Solution 3: Brute Force O(N^2)", solution::dailyTemperaturesBruteForce);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Mixed trends", new int[] { 73, 74, 75, 71, 69, 72, 76, 73 },
                        new int[] { 1, 1, 4, 2, 1, 1, 0, 0 }),
                new TestCase("Standard Case: Strictly increasing", new int[] { 30, 40, 50, 60 },
                        new int[] { 1, 1, 1, 0 }),
                new TestCase("Standard Case: Strictly decreasing", new int[] { 30, 28, 26, 24 },
                        new int[] { 0, 0, 0, 0 }),
                new TestCase("Edge Case: Single element", new int[] { 30 }, new int[] { 0 }),
                new TestCase("Edge Case: Same temperatures", new int[] { 30, 30, 30 }, new int[] { 0, 0, 0 })
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], int[]>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.temperatures, tc.expected);
            }
        }
    }
}
