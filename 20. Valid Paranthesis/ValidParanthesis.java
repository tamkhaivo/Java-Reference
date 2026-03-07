import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.Function;

public class ValidParanthesis {

    /**
     * Approach 1: Idiomatic Java Stack (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of the string
     * Space Complexity: O(N) in the worst case (all open brackets)
     * 
     * Trade-off: We use ArrayDeque instead of the legacy java.util.Stack class
     * because
     * Stack is synchronized (slow) and extends Vector. ArrayDeque is the modern
     * standard
     * for stack/queue algorithms in Java.
     */
    public boolean isValidDeque(String s) {
        // Fast fail: Odd length strings cannot be validly matched
        if (s.length() % 2 != 0) {
            return false;
        }

        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false; // Found a closing bracket without any openings
                }
                char open = stack.pop();
                if (c == ')' && open != '(')
                    return false;
                if (c == '}' && open != '{')
                    return false;
                if (c == ']' && open != '[')
                    return false;
            }
        }

        // If stack is empty, all parentheses were matched perfectly
        return stack.isEmpty();
    }

    /**
     * Approach 2: Primitive Array as Stack (Highly Optimized Time)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * 
     * Trade-off: Instead of using Java objects (Character) which carry memory and
     * autoboxing overhead, we simulate a stack using a raw primitive char array.
     * This is consistently the fastest approach on LeetCode execution times.
     */
    public boolean isValidPrimitiveArray(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }

        char[] stack = new char[s.length()];
        int head = 0; // Points to the top of our "stack"

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                stack[head++] = c;
            } else {
                if (head == 0) {
                    return false;
                }
                char open = stack[--head]; // "Pop" the top element by moving the pointer down
                if (c == ')' && open != '(')
                    return false;
                if (c == '}' && open != '{')
                    return false;
                if (c == ']' && open != '[')
                    return false;
            }
        }

        return head == 0;
    }

    /**
     * Approach 3: String Replacement (Educational Anti-Pattern)
     * Time Complexity: O(N^2)
     * Space Complexity: O(N^2)
     * 
     * Trade-off: Visually the easiest to reason about ("just delete matching
     * pairs"),
     * but string immutability in Java means every replace creates a new string
     * object footprint.
     * This will absolutely Time Limit Exceed (TLE) on large competitive programming
     * sets.
     */
    public boolean isValidStringReplace(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }

        while (s.contains("()") || s.contains("{}") || s.contains("[]")) {
            s = s.replace("()", "");
            s = s.replace("{}", "");
            s = s.replace("[]", "");
        }
        return s.isEmpty();
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        String s;
        boolean expected;

        TestCase(String name, String s, boolean expected) {
            this.name = name;
            this.s = s;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<String, Boolean> algorithm, String s, boolean expected) {
        boolean result = algorithm.apply(s);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-42s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        ValidParanthesis solution = new ValidParanthesis();

        // 1. Define Algorithms to Test
        Map<String, Function<String, Boolean>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Idiomatic ArrayDeque Stack O(N)", solution::isValidDeque);
        algorithms.put("Solution 2: Primitive Array Stack Fast O(N)", solution::isValidPrimitiveArray);
        algorithms.put("Solution 3: String Replace Anti-pattern O(N^2)", solution::isValidStringReplace);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Simple Pair ()", "()", true),
                new TestCase("Standard Case: Multiple Pairs ()[]{}", "()[]{}", true),
                new TestCase("Standard Case: Nested Bracket {[]}", "{[]}", true),
                new TestCase("Invalid Case: Wrong Closure (]", "(]", false),
                new TestCase("Invalid Case: Missing Closure ([)]", "([)]", false),
                new TestCase("Edge Case: Closing Bracket First }{", "}{", false),
                new TestCase("Edge Case: Odd Length ((()", "((()", false),
                new TestCase("Edge Case: Exhausted Elements ((()))(", "((()))(", false)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<String, Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.s, tc.expected);
            }
            System.out.println();
        }
    }
}
