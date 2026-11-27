
import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;

        int minPrice = Integer.MAX_VALUE;

        for (int[] item : items) {
            minPrice = Math.min(minPrice, item[1]);
        }

        // Calculate reward for activating each item type
        int[] value = new int[n];

        for (int i = 0; i < n; i++) {
            int freeItems = 0;

            for (int j = 0; j < n; j++) {
                if (i != j && items[j][0] % items[i][0] == 0) {
                    freeItems++;
                }
            }

            value[i] = 1 + freeItems;
        }

        // 0/1 Knapsack with exact spending
        int[] dp = new int[budget + 1];

        Arrays.fill(dp, -1000000);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            int price = items[i][1];

            for (int b = budget; b >= price; b--) {
                if (dp[b - price] != -1000000) {
                    dp[b] = Math.max(
                        dp[b],
                        dp[b - price] + value[i]
                    );
                }
            }
        }

        // Use remaining budget to buy extra copies
        int ans = 0;

        for (int spent = 0; spent <= budget; spent++) {
            if (dp[spent] != -1000000) {
                int extra = (budget - spent) / minPrice;

                ans = Math.max(ans, dp[spent] + extra);
            }
        }

        return ans;
    }
}

