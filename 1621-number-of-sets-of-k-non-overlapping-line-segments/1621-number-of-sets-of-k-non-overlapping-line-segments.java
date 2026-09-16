class Solution {
    static final long MOD = 1000000007;

    public int numberOfSets(int n, int k) {
        long[] dp = new long[2 * k + 1];

        dp[0] = 1;

        for (int i = 1; i <= n + k - 1; i++) {
            for (int j = Math.min(i, 2 * k); j >= 1; j--) {
                dp[j] = (dp[j] + dp[j - 1]) % MOD;
            }
        }

        return (int) dp[2 * k];
    }
}