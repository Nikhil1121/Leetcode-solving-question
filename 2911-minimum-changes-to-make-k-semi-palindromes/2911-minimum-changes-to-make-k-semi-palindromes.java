
class Solution {
    public int minimumChanges(String s, int k) {
        int n = s.length();
        int INF = 1_000_000_000;

        int[][] cost = new int[n][n];

        // Step 1: Precompute minimum changes
        // to make every substring semi-palindrome
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                int len = r - l + 1;
                cost[l][r] = INF;

                // Find proper divisors of substring length
                for (int d = 1; d < len; d++) {
                    if (len % d != 0) {
                        continue;
                    }

                    int changes = 0;

                    // Check palindrome in each group
                    for (int offset = 0; offset < d; offset++) {
                        int left = 0;
                        int right = (len - 1 - offset) / d;

                        while (left < right) {
                            char a = s.charAt(l + offset + left * d);
                            char b = s.charAt(l + offset + right * d);

                            if (a != b) {
                                changes++;
                            }

                            left++;
                            right--;
                        }
                    }

                    cost[l][r] = Math.min(cost[l][r], changes);
                }
            }
        }

        // Step 2: DP
        // dp[i][j] = minimum changes to partition
        // first i characters into j semi-palindromes
        int[][] dp = new int[n + 1][k + 1];

        for (int[] row : dp) {
            java.util.Arrays.fill(row, INF);
        }

        dp[0][0] = 0;

        for (int parts = 1; parts <= k; parts++) {
            for (int end = 2 * parts; end <= n; end++) {
                for (int start = 2 * (parts - 1);
                     start <= end - 2; start++) {

                    if (dp[start][parts - 1] == INF) {
                        continue;
                    }

                    dp[end][parts] = Math.min(
                        dp[end][parts],
                        dp[start][parts - 1] + cost[start][end - 1]
                    );
                }
            }
        }

        return dp[n][k];
    }
}
