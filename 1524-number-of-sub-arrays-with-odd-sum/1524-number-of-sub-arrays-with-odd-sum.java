class Solution {
    public int numOfSubarrays(int[] arr) {
        int mod = 1_000_000_007;

        long odd = 0;
        long even = 1;
        long ans = 0;
        int prefixSum = 0;

        for (int num : arr) {
            prefixSum += num;

            if (prefixSum % 2 == 0) {
                ans = (ans + odd) % mod;
                even++;
            } else {
                ans = (ans + even) % mod;
                odd++;
            }
        }

        return (int) ans;
    }
}