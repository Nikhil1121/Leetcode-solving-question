
class Solution {
    static final int MOD = 1_000_000_007;

    public int sumCounts(int[] nums) {
        int n = nums.length;
        long ans = 0;

        SegmentTree st = new SegmentTree(n);
        int[] last = new int[100001];

        for (int i = 0; i < last.length; i++) {
            last[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            int x = nums[i];

            // Distinct count increases by 1 for subarrays
            // starting after the previous occurrence of x.
            st.update(last[x] + 1, i, 1);

            // Sum of squared distinct counts for subarrays
            // ending at index i.
            ans = (ans + st.query()) % MOD;

            last[x] = i;
        }

        return (int) ans;
    }

    static class SegmentTree {
        int n;
        long[] sum;
        long[] squareSum;
        long[] lazy;

        SegmentTree(int n) {
            this.n = n;
            sum = new long[4 * n];
            squareSum = new long[4 * n];
            lazy = new long[4 * n];
        }

        void apply(int node, int l, int r, long val) {
            long len = r - l + 1L;

            squareSum[node] =
                (squareSum[node] + 2L * val * sum[node]
                + len * val % MOD * val) % MOD;

            sum[node] = (sum[node] + len * val) % MOD;
            lazy[node] = (lazy[node] + val) % MOD;
        }

        void push(int node, int l, int r) {
            if (lazy[node] == 0 || l == r) return;

            int mid = l + (r - l) / 2;
            apply(node * 2, l, mid, lazy[node]);
            apply(node * 2 + 1, mid + 1, r, lazy[node]);

            lazy[node] = 0;
        }

        void update(int ql, int qr, long val) {
            if (ql > qr) return;
            update(1, 0, n - 1, ql, qr, val);
        }

        void update(int node, int l, int r,
                    int ql, int qr, long val) {
            if (ql <= l && r <= qr) {
                apply(node, l, r, val);
                return;
            }

            push(node, l, r);
            int mid = l + (r - l) / 2;

            if (ql <= mid)
                update(node * 2, l, mid, ql, qr, val);

            if (qr > mid)
                update(node * 2 + 1, mid + 1, r,
                       ql, qr, val);

            sum[node] =
                (sum[node * 2] + sum[node * 2 + 1]) % MOD;

            squareSum[node] =
                (squareSum[node * 2]
                + squareSum[node * 2 + 1]) % MOD;
        }

        long query() {
            return squareSum[1];
        }
    }
}
