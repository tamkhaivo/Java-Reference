import java.util.Map;
import java.util.Arrays;
import java.util.function.BiFunction;

public class PermutationInString {

    /**
     * Approach 1: Sliding Window + Array Comparison (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of s2
     * Space Complexity: O(1) auxiliary space (Fixed size 26 arrays)
     * 
     * Trade-off: Instead of generating all permutations of s1 (which is O(N!)),
     * we recognize that what makes a string a permutation of another is an EXACT
     * match
     * of character frequencies. We use two frequency arrays (size 26) and slide a
     * window
     * of size `s1.length()` across `s2`. At each step, we update the counts and
     * compare arrays.
     */
    public boolean checkInclusionOptimal(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;

        int[] s1Count = new int[26];
        int[] s2Count = new int[26];

        // Populate the initial window
        for (int i = 0; i < s1.length(); i++) {
            s1Count[s1.charAt(i) - 'a']++;
            s2Count[s2.charAt(i) - 'a']++;
        }

        // Compare the first window before we begin sliding
        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (s1Count[i] == s2Count[i]) {
                matches++;
            }
        }

        // Slide the window through the rest of s2
        int left = 0;
        for (int right = s1.length(); right < s2.length(); right++) {
            if (matches == 26)
                return true; // Perfect Match Found!

            // ---- ADD the new character entering the window ----
            int indexRight = s2.charAt(right) - 'a';
            s2Count[indexRight]++;
            // Did adding this character fix a mismatch?
            if (s1Count[indexRight] == s2Count[indexRight]) {
                matches++;
            }
            // Did adding this character BREAK a previously matching count?
            else if (s1Count[indexRight] + 1 == s2Count[indexRight]) {
                matches--;
            }

            // ---- REMOVE the old character leaving the window ----
            int indexLeft = s2.charAt(left) - 'a';
            s2Count[indexLeft]--;
            // Did removing this character fix a mismatch?
            if (s1Count[indexLeft] == s2Count[indexLeft]) {
                matches++;
            }
            // Did removing this character BREAK a previously matching count?
            else if (s1Count[indexLeft] - 1 == s2Count[indexLeft]) {
                matches--;
            }

            left++; // Shrink the window
        }

        // Final check for the very last window position
        return matches == 26;
    }

    /**
     * Approach 2: Sliding Window + Full Array Scan (Slightly Slower Baseline)
     * Time Complexity: O(26 * N) -> simplifies to O(N)
     * Space Complexity: O(1)
     * 
     * Trade-off: Instead of the highly optimized `matches` tracking variable from
     * Approach 1,
     * this implementation simply relies on `Arrays.equals()` at every single step
     * of the window.
     * It is much easier to write and read, at the cost of running a 26-element
     * array scan
     * every single iteration through `s2`. Still officially O(N).
     */
    public boolean checkInclusionArrayScan(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;

        int[] s1Count = new int[26];
        int[] s2Count = new int[26];

        // First Window
        for (int i = 0; i < s1.length(); i++) {
            s1Count[s1.charAt(i) - 'a']++;
            s2Count[s2.charAt(i) - 'a']++;
        }

        if (Arrays.equals(s1Count, s2Count))
            return true;

        // Slide the window
        for (int i = s1.length(); i < s2.length(); i++) {
            // Add rightmost
            s2Count[s2.charAt(i) - 'a']++;
            // Remove leftmost
            s2Count[s2.charAt(i - s1.length()) - 'a']--;

            // O(26) scan at every step
            if (Arrays.equals(s1Count, s2Count))
                return true;
        }

        return false;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        String s1;
        String s2;
        boolean expected;

        TestCase(String name, String s1, String s2, boolean expected) {
            this.name = name;
            this.s1 = s1;
            this.s2 = s2;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<String, String, Boolean> algorithm, String s1, String s2,
            boolean expected) {
        boolean result = algorithm.apply(s1, s2);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-45s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        PermutationInString solution = new PermutationInString();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<String, String, Boolean>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Sliding Window + Matches tracking O(N)", solution::checkInclusionOptimal);
        algorithms.put("Solution 2: Sliding Window + Array Scan O(26*N)", solution::checkInclusionArrayScan);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Exists in middle", "ab", "eidbaooo", true), // "ba" is permutation
                new TestCase("Standard Case: Does not exist", "ab", "eidboaoo", false),
                new TestCase("Edge Case: Exact Match", "abc", "bca", true),
                new TestCase("Edge Case: s1 longer than s2", "hello", "hi", false),
                new TestCase("Edge Case: Single element match", "a", "a", true),
                new TestCase("Edge Case: Single element no match", "a", "b", false),
                new TestCase("Edge Case: Repeated characters match", "adc", "dcda", true), // "cda" has all from "adc"
                new TestCase("Edge Case: Exists exactly at the end", "xyz", "abczyx", true)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<String, String, Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.s1, tc.s2, tc.expected);
            }
            System.out.println();
        }
    }
}
