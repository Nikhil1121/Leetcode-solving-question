
import java.util.*;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;

        // Count frequency of each factor
        int[] freq = new int[n + 1];

        int minPrice = Integer.MAX_VALUE;

        for (int[] item : items) {
            freq[item[0]]++;
            minPrice = Math.min(minPrice, item[1]);
        }

        // Count distinct free-item opportunities per item type
        int[] freeCount = new int[n + 1];

        for (int f = 1; f <= n; f++) {
            for (int multiple = f; multiple <= n; multiple += f) {
                freeCount[f] += freq[multiple];
            }
            freeCount[f]--;
        }

        // Sort item types by price
        Integer[] order = new Integer[n];

        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) ->
            Integer.compare(items[a][1], items[b][1])
        );

        long remaining = budget;
        long bonusCopies = 0;

        // Each bonus purchase gives:
        // 1 purchased item + 1 free item
        for (int idx : order) {
            int factor = items[idx][0];
            int price = items[idx][1];

            // A bonus purchase is useful only if price <= 2 * minPrice
            if (2L * minPrice < price) {
                break;
            }

            long count = Math.min(
                freeCount[factor],
                remaining / price
            );

            bonusCopies += count;
            remaining -= count * price;
        }

        // Use remaining budget for cheapest-price purchases
        long answer = 2L * bonusCopies + remaining / minPrice;

        return (int) answer;
    }
}
