class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();

        // pal[i][j] = true if s[i...j] is a palindrome
        boolean[][] pal = new boolean[n][n];

        // Build palindrome table
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {

                int j = i + len - 1;

                if (s.charAt(i) == s.charAt(j) &&
                    (len <= 2 || pal[i + 1][j - 1])) {
                    pal[i][j] = true;
                }
            }
        }

        // dp[i] = maximum palindromes using first i characters
        int[] dp = new int[n + 1];

        for (int r = 0; r < n; r++) {

            // Don't select a palindrome ending at r
            dp[r + 1] = Math.max(dp[r + 1], dp[r]);

            // Try every starting position
            for (int l = 0; l <= r; l++) {

                if (r - l + 1 >= k && pal[l][r]) {
                    dp[r + 1] = Math.max(dp[r + 1], dp[l] + 1);
                }
            }
        }

        return dp[n];
    }
}