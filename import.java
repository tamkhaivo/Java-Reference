import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Stack;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Optional;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.DoubleStream;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

import java.math.BigInteger;
import java.math.BigDecimal;


class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] minCoinExchange = new int[amount + 1];
        Arrays.fill(minCoinExchange, amount + 1);
        minCoinExchange[0] = 0;
        for(int coin: coins){
            for(int idx = coin; idx <= amount; idx++){
                minCoinExchange[idx] = Math.min(minCoinExchange[idx], minCoinExchange[idx - coin] + 1);
            }   
        }
        return minCoinExchange[amount] > amount ? -1: minCoinExchange[amount];
    }
}

class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }
    private void backtrack(List<List<Integer>> list, List<Integer> temp, int[] nums, int startIdx){
        list.add(new ArrayList<>(temp));
        for(int idx = startIdx; idx < nums.length; idx++){
            temp.add(nums[idx]);
            backtrack(list, temp, nums, idx + 1);
            temp.remove(temp.size() - 1);
        }
    }
}

class Solution {
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
    private int findK(int[] nums, int k){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for(int num: nums){
            minHeap.add(num);
            if(minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();

    }
}

class Solution {
    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        int count = 0;
        TreeNode currentNode = root;
        while(currentNode != null || !stack.isEmpty()){
            if(currentNode != null){
                stack.push(currentNode);
                currentNode = currentNode.left;
            } else {
                currentNode = stack.pop();
                count++;
                if(count == k) return currentNode.val;
                currentNode = currentNode.right;
            }
        }
        return -1;
    }
}

class Solution {
    public int searchRotatedArray(int[] nums, int target) {
        int left = 0; 
        int right = nums.length - 1;
        while(left <= right){
            int mid = (left + right) / 2;
            int midNum = nums[mid];
            int leftNum = nums[left];
            int rightNum = nums[right];

            if(midNum == target) return mid;
            if(midNum >= leftNum){ // Left-Side Sorted
                if(leftNum <= target && target < midNum) right = mid - 1;
                else left = mid + 1;
            }
            else {
                if(midNum < target && target <= rightNum) left = mid + 1;
                else right = mid - 1;
            }
        }
        return -1;
    }
}

class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int topRow = 0;
        int bottomRow = matrix.length - 1;

        int leftCol = 0; 
        int rightCol = matrix[0].length - 1;

        while(topRow <= bottomRow){
            int middleRow = (topRow + bottomRow) / 2;
            int startRange = matrix[middleRow][leftCol];
            int endRange = matrix[middleRow][rightCol];
            if(startRange <= target && target <= endRange){
                while(leftCol <= rightCol){
                    int middleCol = (leftCol + rightCol) / 2;
                    int currentNumber = matrix[middleRow][middleCol];
                    if(target == currentNumber){
                        return true;
                    }
                    if(target > currentNumber) leftCol = middleCol + 1;
                    else rightCol = middleCol - 1;
                }
            }
            if(target < startRange) bottomRow = middleRow - 1;
            else topRow = middleRow + 1;
        }
        return false;
    }
}

class Solution {
    public boolean isValidSudoku(char[][] board) {
        final int ROWS = board.length;
        final int COLS = board[0].length;
        HashSet<String> seenNumbers = new HashSet<>();
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                char number = board[row][col];
                if(number == '.') continue;
                if(!isValidDigitOnBoard(seenNumbers, row, col, number)) return false;
            }
        }
        return true;
        
    }
    private boolean isValidDigitOnBoard(HashSet<String> seenNumbers, int row, int col, char number){
        return (seenNumbers.add("ROW" + row + "Number" + number) && 
                 seenNumbers.add("COL" + col + "Number" + number) && 
                 seenNumbers.add("BOX" + (row / 3) + "-" + (col / 3) + "Number" + number));
    }
}

class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] minCoinExchange = new int[amount + 1];
        Arrays.fill(minCoinExchange, amount + 1);
        minCoinExchange[0] = 0;
        for(int coin: coins){
            for(int idx = coin; idx <= amount; idx++){
                minCoinExchange[idx] = Math.min(minCoinExchange[idx], minCoinExchange[idx - coin] + 1);
            }   
        }
        return minCoinExchange[amount] > amount ? -1: minCoinExchange[amount];
    }
}