import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.function.BiFunction;

public class Anagram {

    /**
     * Approach 1: Frequency Array (Optimal Time & Space)
     * Assumes the input string only contains lowercase English letters.
     */
    public boolean isAnagram(String s, String t) {
        // Fast fail: if lengths differ, they can't be anagrams
        if (s.length() != t.length()) {
            return false;
        }

        // Use an array to keep track of character counts
        int[] counts = new int[26];

        // Increment for string s, decrement for string t
        // We can do this in a single pass since we already know the lengths match
        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++;
            counts[t.charAt(i) - 'a']--;
        }

        // If they are anagrams, all counts should balance out to exactly 0
        for (int count : counts) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Approach 2: HashMap (Time-Optimized for Unicode)
     * Good if Unicode characters are allowed and we can't use a fixed-size array.
     */
    public boolean isAnagramMap(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        Map<Character, Integer> seenLetters = new HashMap<>();

        // Build frequency map
        for (char letter : s.toCharArray()) {
            seenLetters.put(letter, seenLetters.getOrDefault(letter, 0) + 1);
        }

        // Validate against the second string
        for (char letter : t.toCharArray()) {
            if (!seenLetters.containsKey(letter) || seenLetters.get(letter) == 0) {
                return false;
            }
            seenLetters.put(letter, seenLetters.get(letter) - 1);
        }

        return true;
    }

    /**
     * Approach 3: Original Solution (HashMap with 2 passes)
     * The original, straightforward hash map implementation.
     */
    public boolean isAnagramOriginal(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        HashMap<Character, Integer> seenLetters = new HashMap<>();
        for (char letter : s.toCharArray()) {
            if (!seenLetters.containsKey(letter)) {
                seenLetters.put(letter, 0);
            }
            seenLetters.put(letter, seenLetters.get(letter) + 1);
        }

        for (char letter : t.toCharArray()) {
            if (!seenLetters.containsKey(letter) || seenLetters.get(letter) == 0) {
                return false;
            }
            seenLetters.put(letter, seenLetters.get(letter) - 1);
        }

        return true;
    }

    /**
     * Approach 4: Sorting (Space-Optimized context)
     */
    public boolean isAnagramSort(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();

        Arrays.sort(sChars);
        Arrays.sort(tChars);

        return Arrays.equals(sChars, tChars);
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        String s;
        String t;
        boolean expected;

        TestCase(String name, String s, String t, boolean expected) {
            this.name = name;
            this.s = s;
            this.t = t;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, BiFunction<String, String, Boolean> algorithm, String s, String t,
            boolean expected) {

        boolean result = algorithm.apply(s, t);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-40s (Expected: %-5b): %-5b %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        Anagram solution = new Anagram();

        // 1. Define Algorithms to Test
        Map<String, BiFunction<String, String, Boolean>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Frequency Array (Optimal)", solution::isAnagram);
        algorithms.put("Solution 2: HashMap (For Unicode)", solution::isAnagramMap);
        algorithms.put("Solution 3: Original HashMap (2 Passes)", solution::isAnagramOriginal);
        algorithms.put("Solution 4: Sorting array comparison", solution::isAnagramSort);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Valid anagram", "anagram", "nagaram", true),
                new TestCase("Standard Case: Invalid anagram", "rat", "car", false),
                new TestCase("Edge Case: Different lengths", "a", "ab", false),
                new TestCase("Edge Case: Empty strings", "", "", true),
                new TestCase("Edge Case: Single identical characters", "z", "z", true),
                new TestCase("Edge Case: Palindromes vs non-palindromes", "racecar", "carrace", true)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, BiFunction<String, String, Boolean>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.s, tc.t, tc.expected);
            }
            System.out.println();
        }
    }
}
