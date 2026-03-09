import java.util.Map;
import java.util.Arrays;
import java.util.function.Function;

public class BestTimeToBuyAndSellStock {

    /**
     * Approach 1: One Pass / Min Price Tracker (Optimal Time & Space)
     * Time Complexity: O(N) where N is the length of prices array
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: This is the standard Dynamic Programming / Greedy approach.
     * We simply keep track of the lowest price we've seen SO FAR.
     * At every step, we check: "If I sell today, how much profit would I make?"
     * We record the maximum profit seen across all days.
     */
    public int maxProfitOptimal(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            // Update the minimum price dynamically if we find a cheaper day to buy
            if (price < minPrice) {
                minPrice = price;
            }
            // Otherwise, see if selling today beats our record max profit
            else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }

        return maxProfit;
    }

    /**
     * Approach 2: Brute Force (Educational Baseline)
     * Time Complexity: O(N^2)
     * Space Complexity: O(1) auxiliary space
     * 
     * Trade-off: Very easy to understand ("Check every possible buy day against
     * every possible future sell day"). However, scales quadratically, which will
     * result in Time Limit Exceeded (TLE) errors for strictly large inputs.
     */
    public int maxProfitBruteForce(int[] prices) {
        int maxProfit = 0;
        int n = prices.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxProfit) {
                    maxProfit = profit;
                }
            }
        }

        return maxProfit;
    }

    // ==========================================
    // Modular Testing Framework
    // ==========================================

    static class TestCase {
        String name;
        int[] prices;
        int expected;

        TestCase(String name, int[] prices, int expected) {
            this.name = name;
            this.prices = prices;
            this.expected = expected;
        }
    }

    private static void runTest(String testName, Function<int[], Integer> algorithm, int[] prices, int expected) {
        int[] pricesCopy = Arrays.copyOf(prices, prices.length);

        int result = algorithm.apply(pricesCopy);
        boolean passed = result == expected;

        // ANSI Color Codes
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String status = passed ? (GREEN + "[PASS]" + RESET) : (RED + "[FAIL]" + RESET);

        System.out.println(String.format("  %-40s (Expected: %-3d): %-3d %s",
                testName, expected, result, status));
    }

    /**
     * 5. Test Cases
     */
    public static void main(String[] args) {
        BestTimeToBuyAndSellStock solution = new BestTimeToBuyAndSellStock();

        // 1. Define Algorithms to Test
        Map<String, Function<int[], Integer>> algorithms = new java.util.LinkedHashMap<>();
        algorithms.put("Solution 1: Min Tracker O(N) Time / O(1) Space", solution::maxProfitOptimal);
        algorithms.put("Solution 2: Brute Force O(N^2) Time / O(1) Space", solution::maxProfitBruteForce);

        // 2. Define Test Cases
        TestCase[] testCases = {
                new TestCase("Standard Case: Mixed prices", new int[] { 7, 1, 5, 3, 6, 4 }, 5), // Buy at 1, Sell at 6
                new TestCase("Standard Case: Strictly decreasing", new int[] { 7, 6, 4, 3, 1 }, 0), // No profit
                                                                                                    // possible
                new TestCase("Standard Case: Strictly increasing", new int[] { 1, 2, 3, 4, 5 }, 4), // Buy at 1, Sell at
                                                                                                    // 5
                new TestCase("Edge Case: Flat prices", new int[] { 3, 3, 3, 3 }, 0),
                new TestCase("Edge Case: Two prices (profit)", new int[] { 1, 5 }, 4),
                new TestCase("Edge Case: Two prices (loss)", new int[] { 5, 1 }, 0),
                new TestCase("Edge Case: Massive spike at end", new int[] { 10, 8, 5, 7, 2, 100 }, 98)
        };

        // 3. Run all test cases against all algorithms
        for (Map.Entry<String, Function<int[], Integer>> entry : algorithms.entrySet()) {
            System.out.println("Testing " + entry.getKey() + ":");
            for (TestCase tc : testCases) {
                runTest(tc.name, entry.getValue(), tc.prices, tc.expected);
            }
            System.out.println();
        }
    }
}
