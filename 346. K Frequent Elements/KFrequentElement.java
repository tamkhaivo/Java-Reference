import java.util.*;
import java.util.function.BiFunction;

public class KFrequentElement {

    /**
     * Approach 1: Bucket Sort (Optimal Time)
     * Time Complexity: O(N) where N is the number of elements in nums.
     * Space Complexity: O(N) for HashMap and Bucket Array.
     * 
     * Trade-off: Uses an array of Lists (buckets) where the index represents the
     * frequency.
     * Since max frequency is N, we need an array of size N+1.
     */
    public int[] topKFrequentBucket(int[] nums, int k) {
        // Step 1: Count element frequencies
        Map<Integer, Integer> frequencyTable = new HashMap<>();
        for (int num : nums) {
            frequencyTable.put(num, frequencyTable.getOrDefault(num, 0) + 1);
        }

        // Step 2: Create buckets where index = frequency
        List<Integer>[] buckets = new List[nums.length + 1];
        for (Map.Entry<Integer, Integer> entry : frequencyTable.entrySet()) {
            int element = entry.getKey();
            int freq = entry.getValue();
            if (buckets[freq] == null) {
                buckets[freq] = new ArrayList<>();
            }
            buckets[freq].add(element);
        }

        // Step 3: Gather the top k elements starting from the highest frequency
        int[] result = new int[k];
        int pos = 0;
        for (int i = buckets.length - 1; i >= 0 && pos < k; i--) {
            if (buckets[i] != null) {
                for (int element : buckets[i]) {
                    result[pos++] = element;
                    if (pos == k) {
                        return result;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Approach 2: Min-Heap / PriorityQueue
     * Time Complexity: O(N log K) since we maintain a heap of size K.
     * Space Complexity: O(N + K) for HashMap and Heap.
     * 
     * Trade-off: Very space efficient if K is small. Slower than Bucket Sort if K
     * approaches N.
     */
    public int[] topKFrequentHeap(int[] nums, int k) {
        Map<Integer, Integer> frequencyTable = new HashMap<>();
        for (int num : nums) {
            frequencyTable.put(num, frequencyTable.getOrDefault(num, 0) + 1);
        }

        // Min-Heap ordered by frequency
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> frequencyTable.get(a) - frequencyTable.get(b));

        // Loop over every unique number we counted in our frequency table
        for (int element : frequencyTable.keySet()) {
            // Add the element to the Min-Heap.
            // The heap will automatically use our comparator to re-sort itself.
            heap.add(element);

            // If the heap grows larger than exactly 'K' elements
            if (heap.size() > k) {
                // We remove (poll) the top element of the heap.
                // Because this is a Min-Heap sorted by frequency, the element sitting
                // exactly at the top is guaranteed to be the one with the lowest frequency.
                // This ensures we ONLY ever keep the K most frequent elements in memory.
                heap.poll();
            }
        }

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = heap.poll();
        }
        return result;
    }

    /**
     * Approach 3: Your Original Solution (Fixed compilation & logic)
     * Time Complexity: O(N * K)
     * Space Complexity: O(N) for the HashMap
     */
    public int[] topKFrequentOriginal(int[] nums, int k) {
        HashMap<Integer, Integer> frequencyTable = new HashMap<>();
        int[] kLargest = new int[k];
        int[] kLargestItem = new int[k];
        int counter = 0;

        for (int num : nums) {
            if (!frequencyTable.containsKey(num)) {
                frequencyTable.put(num, 0);
            }
            frequencyTable.put(num, frequencyTable.get(num) + 1);
        }

        for (var item : frequencyTable.keySet()) { // FIXED: keys() -> keySet()
            if (counter < k) {
                kLargestItem[counter] = item;
                kLargest[counter++] = frequencyTable.get(item);
            } else {
                int minIdx = 0;
                for (int idx = 0; idx < k; idx++) {
                    if (kLargest[minIdx] > kLargest[idx]) {
                        minIdx = idx;
                    }
                }
                if (kLargest[minIdx] < frequencyTable.get(item)) {
                    kLargestItem[minIdx] = item;
                    kLargest[minIdx] = frequencyTable.get(item); // FIXED: missing update to the frequency array
                }
            }
        }
        return kLargestItem;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] nums;
        int k;
        int[] expected;

        TestCase(String name, int[] nums, int k, int[] expected) {
            this.name = name;
            this.nums = nums;
            this.k = k;
            this.expected = expected;
        }
    }

    // Top K frequent elements can be returned in any order, so we must sort both to
    // compare.
    private static void runTest(String testName, BiFunction<int[], Integer, int[]> algorithm, int[] nums, int k,
            int[] expected) {
        int[] numsCopy = Arrays.copyOf(nums, nums.length);

        int[] result = algorithm.apply(numsCopy, k);

        // Sort both because LeetCode accepts output in any order
        Arrays.sort(result);
        Arrays.sort(expected);

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
        KFrequentElement solution = new KFrequentElement();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<int[], Integer, int[]>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Bucket Sort O(N)", solution::topKFrequentBucket);
        algorithms.put("Solution 2: Min-Heap O(N log K)", solution::topKFrequentHeap);
        algorithms.put("Solution 3: Original Setup O(N * K)", solution::topKFrequentOriginal);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: k=2", new int[] { 1, 1, 1, 2, 2, 3 }, 2, new int[] { 1, 2 }),
                new TestCase("Standard Case: k=1", new int[] { 1 }, 1, new int[] { 1 }),
                new TestCase("Edge Case: Negative Numbers", new int[] { -1, -1 }, 1, new int[] { -1 }),
                new TestCase("Multiple Elements, Same Frequency", new int[] { 4, 1, -1, 2, -1, 2, 3 }, 2,
                        new int[] { -1, 2 }),
                new TestCase("K == N", new int[] { 1, 2, 3 }, 3, new int[] { 1, 2, 3 })
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<int[], Integer, int[]>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.nums, tc.k, tc.expected);
            }
            System.out.println();
        }
    }
}
