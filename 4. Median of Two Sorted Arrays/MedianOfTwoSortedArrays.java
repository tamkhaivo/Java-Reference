import java.util.Map;
import java.util.Arrays;
import java.util.function.BiFunction;

public class MedianOfTwoSortedArrays {

    /**
     * Approach 1: Binary Search on Partitions (Optimal Time & Space)
     * Time Complexity: O(log(min(m, n)))
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: This is the legendary "Binary Search on Partitions" algorithm.
     * To find the median, we don't need to actually sort the array. We just need to
     * find the exact partition line that divides the combined array into two equal
     * halves
     * where every element on the left is smaller than every element on the right.
     * We binary search the SMALLER array to prevent index out-of-bounds errors on
     * the larger one.
     */
    public double findMedianSortedArraysOptimal(int[] nums1, int[] nums2) {
        // Enforce Binary Search on the smaller array for efficiency and to avoid
        // out-of-bounds
        if (nums1.length > nums2.length) {
            return findMedianSortedArraysOptimal(nums2, nums1);
        }

        int x = nums1.length;
        int y = nums2.length;

        int low = 0;
        int high = x;

        // Binary search for the perfect partition partitionX
        while (low <= high) {
            int partitionX = (low + high) / 2;
            // The combined left half must have exactly half of the total elements: (x + y +
            // 1) / 2
            int partitionY = (x + y + 1) / 2 - partitionX;

            // If partitionX is 0 it means nothing is there on left side
            // Use Integer.MIN_VALUE for maxLeftX
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];

            // If partitionX is length of input then there is nothing on right side
            // Use Integer.MAX_VALUE for minRightX
            int minRightX = (partitionX == x) ? Integer.MAX_VALUE : nums1[partitionX];

            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == y) ? Integer.MAX_VALUE : nums2[partitionY];

            // Have we found the perfect partition?
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                // If the total length of combined arrays is strictly ODD
                if ((x + y) % 2 != 0) {
                    return (double) Math.max(maxLeftX, maxLeftY);
                }
                // If EVEN, we must take the average of the max left bound and min right bound
                else {
                    return ((double) Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2;
                }
            }
            // We are too far on the right side for partitionX. Go on left side.
            else if (maxLeftX > minRightY) {
                high = partitionX - 1;
            }
            // We are too far on the left side for partitionX. Go on right side.
            else {
                low = partitionX + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted.");
    }

    /**
     * Approach 2: Merge Sort Concept (Educational Baseline)
     * Time Complexity: O(m + n)
     * Space Complexity: O(1) auxiliary space (no physical merged array created)
     * 
     * Trade-off: Instead of physically merging them into an O(N) array, we just
     * step through the lists with two pointers up to the median index. While this
     * is highly intuitive and easy to trace, it's firmly O(m+n) time, meaning it
     * will
     * fail the strict O(log(m+n)) requirement of competitive bounds.
     */
    public double findMedianSortedArraysMerge(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int totalLen = m + n;

        int p1 = 0; // Pointer for nums1
        int p2 = 0; // Pointer for nums2

        int current = 0;
        int previous = 0;

        // We only cleanly iterate to the middle.
        for (int i = 0; i <= totalLen / 2; i++) {
            previous = current; // Save the old median candidate

            if (p1 < m && (p2 >= n || nums1[p1] < nums2[p2])) {
                current = nums1[p1++];
            } else {
                current = nums2[p2++];
            }
        }

        // If odd length, the "current" final position is the median.
        if (totalLen % 2 != 0) {
            return (double) current;
        }
        // If even, average the two middle most elements we just passed.
        else {
            return (previous + current) / 2.0;
        }
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] nums1;
        int[] nums2;
        double expected;

        TestCase(String name, int[] nums1, int[] nums2, double expected) {
            this.name = name;
            this.nums1 = nums1;
            this.nums2 = nums2;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<int[], int[], Double> algorithm, int[] nums1, int[] nums2,
            double expected) {
        int[] nums1Copy = Arrays.copyOf(nums1, nums1.length);
        int[] nums2Copy = Arrays.copyOf(nums2, nums2.length);

        double result = algorithm.apply(nums1Copy, nums2Copy);

        double epsilon = 0.00001;
        boolean passed = Math.abs(result - expected) < epsilon;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-45s (Expected: %-5.1f): %-5.1f %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        MedianOfTwoSortedArrays solution = new MedianOfTwoSortedArrays();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<int[], int[], Double>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Binary Search on Partitions O(log(min(m,n)))",
                solution::findMedianSortedArraysOptimal);
        algorithms.put("Solution 2: Merge Sort Two Pointers O(m+n)", solution::findMedianSortedArraysMerge);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Odd total length", new int[] { 1, 3 }, new int[] { 2 }, 2.0),
                new TestCase("Standard Case: Even total length", new int[] { 1, 2 }, new int[] { 3, 4 }, 2.5),
                new TestCase("Edge Case: nums1 empty", new int[] {}, new int[] { 1, 2, 3, 4 }, 2.5),
                new TestCase("Edge Case: nums2 empty", new int[] { 2, 5, 8 }, new int[] {}, 5.0),
                new TestCase("Edge Case: Disjoint arrays (nums1 all smaller)", new int[] { 1, 2 }, new int[] { 3, 4 },
                        2.5),
                new TestCase("Edge Case: Arrays staggered", new int[] { 1, 5, 8, 10, 18, 20 }, new int[] { 2, 3, 6, 7 },
                        6.5),
                new TestCase("Edge Case: Both single elements", new int[] { 2 }, new int[] { 4 }, 3.0)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<int[], int[], Double>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.nums1, tc.nums2, tc.expected);
            }
            System.out.println();
        }
    }
}
