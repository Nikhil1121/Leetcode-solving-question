class Solution {
    public int minSumOfLengths(int[] arr, int target) {

        int n = arr.length;
        int INF = 1000000000;

        // best[i] = minimum length of a valid subarray
        // ending at or before index i
        int[] best = new int[n];

        Arrays.fill(best, INF);

        int left = 0;
        int sum = 0;
        int ans = INF;
        int minLen = INF;

        for (int right = 0; right < n; right++) {

            sum += arr[right];

            // Shrink window if sum becomes too large
            while (sum > target) {
                sum -= arr[left];
                left++;
            }

            // Current window has sum = target
            if (sum == target) {

                int len = right - left + 1;

                // Check previous non-overlapping subarray
                if (left > 0 && best[left - 1] != INF) {
                    ans = Math.min(ans, len + best[left - 1]);
                }

                // Keep shortest subarray found so far
                minLen = Math.min(minLen, len);
            }

            // Best answer up to current index
            best[right] = minLen;
        }

        return ans == INF ? -1 : ans;
    }
}