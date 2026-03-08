import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 155. Min Stack
 * 
 * 1. Understand & Approach:
 * The goal is to design a stack that supports push, pop, top, and retrieving
 * the minimum
 * element in constant time O(1).
 * 
 * Approach 1 (Optimal Standard): Linked List with Minimum
 * We can create a custom `Node` class where each node stores its value, the
 * minimum value up
 * to that point, and a pointer to the next node. This avoids the overhead of
 * Java
 * Collections and boxing/unboxing wrappers entirely, making it extremely fast
 * and memory efficient.
 * 
 * Approach 2 (Two Stacks): Use two java.util.ArrayDeque
 * One stack stores the values, and the other stores the minimums. ArrayDeque is
 * preferred over
 * java.util.Stack because Stack extends Vector and is synchronized (adding
 * unnecessary locking overhead).
 */
public class MinStack {

    // ==========================================
    // 2. Optimal Solution Code (Linked List Node)
    // ==========================================

    private class Node {
        int val;
        int min;
        Node next;

        private Node(int val, int min, Node next) {
            this.val = val;
            this.min = min;
            this.next = next;
        }
    }

    private Node head;

    public MinStack() {
        this.head = null;
    }

    public void push(int val) {
        if (head == null) {
            head = new Node(val, val, null);
        } else {
            head = new Node(val, Math.min(val, head.min), head);
        }
    }

    public void pop() {
        if (head != null) {
            head = head.next;
        }
    }

    public int top() {
        // Constraints specify that pop, top, and getMin are always called on non-empty
        // stacks.
        return head.val;
    }

    public int getMin() {
        return head.min;
    }

    /**
     * 3. Complexity Analysis:
     * - Time Complexity: O(1) for all operations (push, pop, top, getMin).
     * We just update the head pointer and compute the min using the current value
     * and previous min.
     * - Space Complexity: O(N) where N is the number of elements in the stack.
     * Every element requires a Node object storing value, minimum, and a pointer.
     */

    // ==========================================
    // 4. Deep Dive / Alternatives
    // ==========================================

    /**
     * Alternative 1: Two ArrayDeque Approach
     * Trade-off: Uses Java's Collections framework instead of custom nodes.
     * - Time: O(1) amortized.
     * - Space: O(N).
     * Why ArrayDeque? It's faster than java.util.Stack (which is synchronized) and
     * LinkedList
     * (which has more cache misses due to individual node allocations for
     * doubly-linked lists).
     * We optimally push to minStack only if the new value <= current min to save
     * space.
     */
    public static class MinStackTwoDeques {
        private Deque<Integer> stack;
        private Deque<Integer> minStack;

        public MinStackTwoDeques() {
            stack = new ArrayDeque<>();
            minStack = new ArrayDeque<>();
        }

        public void push(int val) {
            stack.push(val);
            // Push to minStack if empty or if new value is <= current minimum.
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = stack.pop();
            // Only pop the minStack if we are popping the current minimum
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // ==========================================
    // 5. Modular Testing Framework
    // ==========================================

    public static void main(String[] args) {
        // Test Optimal Implementation
        System.out.println("Testing Solution 1: Linked List Node (Optimal):");
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        runAssert("Standard: getMin() after pushes", minStack.getMin(), -3);
        minStack.pop();
        runAssert("Standard: top() after pop", minStack.top(), 0);
        runAssert("Standard: getMin() after pop", minStack.getMin(), -2);

        // Test TwoDeques Implementation
        System.out.println("\nTesting Solution 2: Two ArrayDeques (Alternative):");
        MinStackTwoDeques minStackTwo = new MinStackTwoDeques();
        minStackTwo.push(-2);
        minStackTwo.push(0);
        minStackTwo.push(-3);
        runAssert("Standard: getMin() after pushes", minStackTwo.getMin(), -3);
        minStackTwo.pop();
        runAssert("Standard: top() after pop", minStackTwo.top(), 0);
        runAssert("Standard: getMin() after pop", minStackTwo.getMin(), -2);

        // Edge Case: Handling duplicate minimums correctly (very important for
        // Alternative 1)
        System.out.println("\nTesting Solution 2: Edge Case (Duplicate Minimums):");
        MinStackTwoDeques minStackEdge = new MinStackTwoDeques();
        minStackEdge.push(2);
        minStackEdge.push(1);
        minStackEdge.push(1); // Second 1 pushed
        minStackEdge.pop(); // First 1 popped
        runAssert("Edge Case: getMin() after duplicate min pop", minStackEdge.getMin(), 1);
    }

    private static void runAssert(String testName, int actual, int expected) {
        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        boolean passed = (actual == expected);
        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-42s (Expected: %d): %d %s",
                testName, expected, actual, status));
    }
}