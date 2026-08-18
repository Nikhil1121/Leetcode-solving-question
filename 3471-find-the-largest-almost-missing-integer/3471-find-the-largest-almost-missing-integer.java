class Solution {
    public int largestInteger(int[] nums, int k) {
        int n = nums.length;
        int[] count = new int[51];

        // Count in how many different windows
        // each number appears.
        for (int start = 0; start <= n - k; start++) {

            boolean[] seen = new boolean[51];

            for (int j = start; j < start + k; j++) {
                seen[nums[j]] = true;
            }

            for (int x = 0; x <= 50; x++) {
                if (seen[x]) {
                    count[x]++;
                }
            }
        }

        // Find largest number appearing
        // in exactly one subarray.
        for (int x = 50; x >= 0; x--) {
            if (count[x] == 1) {
                return x;
            }
        }

        return -1;
    }
}