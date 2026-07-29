class Solution {
    static final long LIMIT = 1_000_001L;

    public String smallestPalindrome(String s, int k) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        int[] half = new int[26];
        String mid = "";
        int halfLen = 0;

        for (int i = 0; i < 26; i++) {
            half[i] = freq[i] / 2;
            halfLen += half[i];
            if ((freq[i] & 1) == 1)
                mid = String.valueOf((char) ('a' + i));
        }

        long total = countWays(half, halfLen);
        if (k > total) return "";

        StringBuilder left = new StringBuilder();

        for (int pos = 0; pos < halfLen; pos++) {

            for (int c = 0; c < 26; c++) {

                if (half[c] == 0) continue;

                half[c]--;

                long ways = countWays(half, halfLen - pos - 1);

                if (k > ways) {
                    k -= ways;
                    half[c]++;
                } else {
                    left.append((char) ('a' + c));
                    break;
                }
            }
        }

        String right = new StringBuilder(left).reverse().toString();
        return left.toString() + mid + right;
    }

    private long countWays(int[] cnt, int total) {

        long ans = 1;

        int remain = total;

        for (int x : cnt) {

            if (x == 0) continue;

            ans *= comb(remain, x);

            if (ans >= LIMIT) return LIMIT;

            remain -= x;
        }

        return Math.min(ans, LIMIT);
    }

    private long comb(int n, int r) {

        if (r > n) return 0;

        r = Math.min(r, n - r);

        long res = 1;

        for (int i = 1; i <= r; i++) {

            res = res * (n - r + i) / i;

            if (res >= LIMIT) return LIMIT;
        }

        return res;
    }
}