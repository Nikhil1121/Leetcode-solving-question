class Solution {
    public long[] resultArray(int[] nums, int k) {

        long[] dp = new long[k];
        long[] ans = new long[k];

        for (int num : nums) {

            long[] next = new long[k];

            int value = num % k;

            // Current element alone
            next[value]++;

            // Extend previous subarrays
            for (int r = 0; r < k; r++) {

                if (dp[r] > 0) {
                    int newRemainder = (r * value) % k;
                    next[newRemainder] += dp[r];
                }
            }

            // Add current subarrays to answer
            for (int r = 0; r < k; r++) {
                ans[r] += next[r];
            }

            dp = next;
        }

        return ans;
    }
}