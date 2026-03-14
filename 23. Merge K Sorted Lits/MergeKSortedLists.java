import java.util.PriorityQueue;
import java.util.Map;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class MergeKSortedLists {

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
     * Approach 1: Priority Queue (Min-Heap) (Optimal Time & Space)
     * Time Complexity: O(N log K) where N is total nodes, K is number of lists.
     * Space Complexity: O(K) for the Priority Queue storing at most K nodes.
     * 
     * Trade-off: Easily the most intuitive standard library solution. We toss the
     * head of every single list into a Min-Heap. The heap automatically sorts them.
     * We constantly `poll()` the smallest node out, link it to our master list,
     * and if that popped node had a `next`, we push its `next` into the heap!
     */
    public ListNode mergeKListsHeap(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;

        // Min-Heap ordered by node value
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);

        // Add the head of each list to the minimum heap
        for (ListNode list : lists) {
            if (list != null) {
                pq.add(list);
            }
        }

        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;

        // Continually pop the absolute smallest remaining node
        while (!pq.isEmpty()) {
            ListNode smallestNode = pq.poll();
            current.next = smallestNode;
            current = current.next;

            // If the list we just pulled from has more nodes, add its next into the heap!
            if (smallestNode.next != null) {
                pq.add(smallestNode.next);
            }
        }

        return dummyHead.next;
    }

    /**
     * Approach 2: Divide and Conquer (Merge Sort Style)
     * Time Complexity: O(N log K)
     * Space Complexity: O(1) auxiliary space (O(log K) memory if using recursive
     * stack)
     * 
     * Trade-off: Instead of keeping a dynamic heap of K elements, this pairs up the
     * lists (0 with 1, 2 with 3...) and merges them in-place. It loops this process
     * (merging the merged lists) until exactly 1 list remains. Highly efficient,
     * saves
     * the O(K) space overhead of the Priority Queue at the cost of trickier
     * iterative logic.
     */
    public ListNode mergeKListsDivide(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;

        int interval = 1;
        while (interval < lists.length) {
            for (int i = 0; i + interval < lists.length; i = i + interval * 2) {
                lists[i] = mergeTwoLists(lists[i], lists[i + interval]);
            }
            interval *= 2;
        }

        return lists[0];
    }

    // Standard routine to merge exactly two sorted lists (used by Divide and
    // Conquer)
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }

        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // ==========================================
    // Modular Testing Framework (with Linked List Helpers)
    // ==========================================

    private static ListNode arrayToList(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int val : arr) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next;
    }

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
        int[][] listsArray;
        List<Integer> expected;

        TestCase(String name, int[][] listsArray, List<Integer> expected) {
            this.name = name;
            this.listsArray = listsArray;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<ListNode[], ListNode> algorithm, int[][] listsArray,
            List<Integer> expected) {
        // Construct fresh lists to avoid mutant references across different algorithms
        ListNode[] lists = new ListNode[listsArray.length];
        for (int i = 0; i < listsArray.length; i++) {
            lists[i] = arrayToList(listsArray[i]);
        }

        ListNode resultNode = algorithm.apply(lists);
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
        MergeKSortedLists solution = new MergeKSortedLists();

        // 1. Define Algorithms to Test
        Map<String, Function<ListNode[], ListNode>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Priority Queue (Min-Heap) O(N log K)", solution::mergeKListsHeap);
        algorithms.put("Solution 2: Divide and Conquer / In-Place Merge O(N log K)", solution::mergeKListsDivide);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Multiple Lists",
                        new int[][] { { 1, 4, 5 }, { 1, 3, 4 }, { 2, 6 } },
                        Arrays.asList(1, 1, 2, 3, 4, 4, 5, 6)),
                new TestCase("Edge Case: Empty Array of Lists",
                        new int[][] {},
                        Arrays.asList()),
                new TestCase("Edge Case: Array with single Empty List",
                        new int[][] { {} },
                        Arrays.asList()),
                new TestCase("Edge Case: One List heavily loaded",
                        new int[][] { { 1, 2, 3, 4, 5 }, {} },
                        Arrays.asList(1, 2, 3, 4, 5)),
                new TestCase("Edge Case: All negative numbers",
                        new int[][] { { -5, -2, -1 }, { -8, -3 } },
                        Arrays.asList(-8, -5, -3, -2, -1))
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<ListNode[], ListNode>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.listsArray, tc.expected);
            }
        }
    }
}
