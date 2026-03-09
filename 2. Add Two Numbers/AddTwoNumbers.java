import java.util.Map;
import java.util.function.BiFunction;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AddTwoNumbers {

    // Definition for singly-linked list (provided by LeetCode structure).
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * Approach 1: Standard Linked List Traversal with Carry (Optimal)
     * Time Complexity: O(max(M, N)) where M and N are the lengths of l1 and l2.
     * Space Complexity: O(max(M, N)) space for the new linked list carrying the
     * sum.
     * 
     * Trade-off: Because the numbers are already stored in reverse order, we simply
     * process them from head to tail exactly like how we mathematically add two
     * numbers.
     * We keep track of a "carry" value that overflows (like 7 + 5 = 12, carry = 1)
     * and append it to the next node. Iterative dummy-head setup prevents endless
     * null checks.
     */
    public ListNode addTwoNumbersOptimal(ListNode l1, ListNode l2) {
        // A dummy head keeps our reference to the beginning of the newly constructed
        // list
        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;
        int carry = 0;

        // Loop through lists l1 and l2 until you reach both ends AND carry is 0
        while (l1 != null || l2 != null || carry != 0) {
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;

            int sum = carry + x + y;
            carry = sum / 10; // Get the overflow digit (e.g. 18 / 10 = 1)

            // Link the new node with the current digit
            current.next = new ListNode(sum % 10);
            current = current.next; // Move the pointer forward

            // Advance l1 and l2
            if (l1 != null)
                l1 = l1.next;
            if (l2 != null)
                l2 = l2.next;
        }

        return dummyHead.next; // Skip the dummy 0 node
    }

    // ==========================================
    // Modular Testing Framework (with Linked List Helpers)
    // ==========================================

    // Helper: Converts an array to a Linked List
    private static ListNode arrayToList(int[] arr) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int val : arr) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next;
    }

    // Helper: Converts a Linked List back to a List purely for easy comparison
    // output
    private static List<Integer> listToArray(ListNode node) {
        List<Integer> result = new ArrayList<>();
        while (node != null) {
            result.add(node.val);
            node = node.next;
        }
        return result;
    }

    static class TestCase {
        String name;
        int[] arr1;
        int[] arr2;
        List<Integer> expected;

        TestCase(String name, int[] arr1, int[] arr2, List<Integer> expected) {
            this.name = name;
            this.arr1 = arr1;
            this.arr2 = arr2;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<ListNode, ListNode, ListNode> algorithm, int[] arr1,
            int[] arr2, List<Integer> expected) {
        // Construct fresh lists to avoid mutant references across different algorithms
        ListNode l1 = arrayToList(arr1);
        ListNode l2 = arrayToList(arr2);

        ListNode resultNode = algorithm.apply(l1, l2);
        List<Integer> result = listToArray(resultNode);

        boolean passed = result.equals(expected);

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-40s: %s", testName, status));
        System.out.println("    Expected: " + expected);
        System.out.println("    Result  : " + result + "\n");
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        AddTwoNumbers solution = new AddTwoNumbers();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<ListNode, ListNode, ListNode>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Standard Mathematical Traversal O(max(M,N))", solution::addTwoNumbersOptimal);

        // 2. Define Test Cases
        TestCase[] testCases = {
                // 342 + 465 = 807
                new TestCase("Standard Case: Same lengths, minor carry", new int[] { 2, 4, 3 }, new int[] { 5, 6, 4 },
                        Arrays.asList(7, 0, 8)),
                // 0 + 0 = 0
                new TestCase("Standard Case: Zero bounds", new int[] { 0 }, new int[] { 0 }, Arrays.asList(0)),
                // 9999999 + 9999 = 10009998
                new TestCase("Edge Case: Massive carry chain / asymmetric", new int[] { 9, 9, 9, 9, 9, 9, 9 },
                        new int[] { 9, 9, 9, 9 }, Arrays.asList(8, 9, 9, 9, 0, 0, 0, 1)),
                // 1 + 99 = 100
                new TestCase("Edge Case: Different lengths single overflow", new int[] { 1 }, new int[] { 9, 9 },
                        Arrays.asList(0, 0, 1)),
                // 18 + 0 = 18
                new TestCase("Edge Case: One array is 0 length effectively", new int[] { 0 }, new int[] { 8, 1 },
                        Arrays.asList(8, 1))
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<ListNode, ListNode, ListNode>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.arr1, tc.arr2, tc.expected);
            }
        }
    }
}
