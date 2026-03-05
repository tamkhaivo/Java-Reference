
// Lists and Arrays
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// Queues and Stacks
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

// Maps and Sets
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

// Heaps
import java.util.PriorityQueue;

// Other Utilities
import java.util.BitSet;
import java.lang.Math;
import java.math.BigInteger;

// Functional Programming
import java.util.function.Function;

public class ContainsDuplicate {

    /**
     * Optimal Solution: Using a HashSet
     * 
     * 1. Understand & Approach:
     * The problem asks if any value appears at least twice in the array.
     * We can iterate through the array, keeping track of the numbers we've seen so
     * far.
     * A HashSet is perfect for this, as it offers O(1) average time complexity for
     * both
     * insertions and lookups. If we encounter a number that is already in our set,
     * we know a duplicate exists.
     */
    public boolean containsDuplicate(int[] nums) {
        // Space-Optimized Approach Trade-off: Use HashSet for O(1) lookups
        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            // If the element is already in the set, we found a duplicate
            // The add() method gracefully handles checking for duplicates,
            // returning false if it was already present.
            if (!seen.add(num)) {
                return true;
            }
        }

        // If we finish the loop without finding a duplicate, all elements are unique
        return false;
    }

    public boolean containsDuplicate2(int[] nums) {
        // Sort the array
        Arrays.sort(nums);

        // Check for adjacent duplicates
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper class to store test cases
     */
    static class TestCase {
        String name;
        int[] input;
        boolean expected;

        TestCase(String name, int[] input, boolean expected) {
            this.name = name;
            this.input = input;
            this.expected = expected;
        }
    }

    /**
     * Helper method to execute and validate test cases by passing the function as
     * an argument.
     * Demonstrates Functional Programming concepts.
     */
    private static void runTest(String testName, Function<int[], Boolean> algorithm, int[] input,
            boolean expected) {
        // Create a copy of the input array so algorithms that modify it (like sort)
        // don't affect others
        int[] inputCopy = Arrays.copyOf(input, input.length);
        boolean result = algorithm.apply(inputCopy);

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        boolean passed = (result == expected);
        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-36s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        ContainsDuplicate solution = new ContainsDuplicate();

        // 1. Define Algorithms to Test
        // Map of Algorithm Name -> Function
        Map<String, Function<int[], Boolean>> algorithms = new LinkedHashMap<>();
        algorithms.put("Solution 1: HashSet", solution::containsDuplicate);
        algorithms.put("Solution 2: Sorting", solution::containsDuplicate2);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Duplicates exist", new int[] { 1, 2, 3, 1 }, true),
                new TestCase("Standard Case: No duplicates", new int[] { 1, 2, 3, 4 }, false),
                new TestCase("Standard Case: Multiple duplicates", new int[] { 1, 1, 1, 3, 3, 4, 3, 2, 4, 2 }, true),
                new TestCase("Edge Case: Empty array", new int[] {}, false),
                new TestCase("Edge Case: Single element", new int[] { 5 }, false),
                new TestCase("Edge Case: Negative numbers", new int[] { -1, -2, -3, -1 }, true)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.input, tc.expected);
            }
            System.out.println();
        }
    }
}
