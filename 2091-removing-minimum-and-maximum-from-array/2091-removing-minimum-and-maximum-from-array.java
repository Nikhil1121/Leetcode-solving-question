class Solution {
    public int minimumDeletions(int[] nums) {

        int n = nums.length;

        int minIndex = 0;
        int maxIndex = 0;

        // Find index of minimum and maximum
        for (int i = 1; i < n; i++) {

            if (nums[i] < nums[minIndex]) {
                minIndex = i;
            }

            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }

        // Make minIndex the smaller index
        int left = Math.min(minIndex, maxIndex);
        int right = Math.max(minIndex, maxIndex);

        // Case 1: Remove both from front
        int front = right + 1;

        // Case 2: Remove both from back
        int back = n - left;

        // Case 3: Remove left from front and right from back
        int both = (left + 1) + (n - right);

        return Math.min(front, Math.min(back, both));
    }
}