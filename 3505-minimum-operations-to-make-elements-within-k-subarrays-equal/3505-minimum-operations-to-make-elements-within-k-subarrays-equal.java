
import java.util.Arrays;

class Solution {

    class Fenwick {
        int n;
        long[] count;
        long[] sum;

        Fenwick(int n) {
            this.n = n;
            count = new long[n + 1];
            sum = new long[n + 1];
        }

        void add(int i, long c, long s) {
            while (i <= n) {
                count[i] += c;
                sum[i] += s;
                i += i & -i;
            }
        }

        long queryCount(int i) {
            long res = 0;
            while (i > 0) {
                res += count[i];
                i -= i & -i;
            }
            return res;
        }

        long querySum(int i) {
            long res = 0;
            while (i > 0) {
                res += sum[i];
                i -= i & -i;
            }
            return res;
        }

        int kth(long k) {
            int idx = 0;
            int bit = Integer.highestOneBit(n);

            for (; bit > 0; bit >>= 1) {
                int next = idx + bit;

                if (next <= n && count[next] < k) {
                    idx = next;
                    k -= count[next];
                }
            }

            return idx + 1;
        }
    }

    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int m = 0;
        for (int v : sorted) {
            if (m == 0 || sorted[m - 1] != v) {
                sorted[m++] = v;
            }
        }

        Fenwick bit = new Fenwick(m);

        long[] cost = new long[n - x + 1];

        long windowSum = 0;

        for (int i = 0; i < x; i++) {
            int pos = Arrays.binarySearch(sorted, 0, m, nums[i]) + 1;
            bit.add(pos, 1, nums[i]);
            windowSum += nums[i];
        }

        for (int i = 0; i <= n - x; i++) {

            int medianPos = bit.kth((x + 1L) / 2);
            long median = sorted[medianPos - 1];

            long leftCount = bit.queryCount(medianPos);
            long leftSum = bit.querySum(medianPos);

            long rightCount = x - leftCount;
            long rightSum = windowSum - leftSum;

            cost[i] = median * leftCount - leftSum
                    + rightSum - median * rightCount;

            if (i < n - x) {
                int removePos = Arrays.binarySearch(
                    sorted, 0, m, nums[i]
                ) + 1;

                bit.add(removePos, -1, -nums[i]);
                windowSum -= nums[i];

                int addPos = Arrays.binarySearch(
                    sorted, 0, m, nums[i + x]
                ) + 1;

                bit.add(addPos, 1, nums[i + x]);
                windowSum += nums[i + x];
            }
        }

        long INF = Long.MAX_VALUE / 4;

        long[] prev = new long[n + 1];
        long[] curr = new long[n + 1];

        for (int j = 1; j <= k; j++) {
            Arrays.fill(curr, INF);

            for (int i = 1; i <= n; i++) {

                curr[i] = curr[i - 1];

                if (i >= x && prev[i - x] != INF) {
                    curr[i] = Math.min(
                        curr[i],
                        prev[i - x] + cost[i - x]
                    );
                }
            }

            long[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }
}
