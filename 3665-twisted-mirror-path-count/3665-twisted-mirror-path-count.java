
class Solution {
    public int uniquePaths(int[][] grid) {

        int m = grid.length;
        int n = grid[0].length;

        int MOD = 1_000_000_007;

        int[][] right = new int[m][n];
        int[][] down = new int[m][n];

        // Starting position
        right[0][0] = 1;

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                int r = right[i][j];
                int d = down[i][j];

                // Empty cell
                if (grid[i][j] == 0) {

                    // Destination reached
                    if (i == m - 1 && j == n - 1) {
                        return (r + d) % MOD;
                    }

                    int ways = (r + d) % MOD;

                    // Move right
                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + ways) % MOD;
                    }

                    // Move down
                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + ways) % MOD;
                    }

                } 
                // Mirror cell
                else {

                    // Entered from left -> move down
                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + r) % MOD;
                    }

                    // Entered from top -> move right
                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + d) % MOD;
                    }
                }
            }
        }

        return 0;
    }
}
