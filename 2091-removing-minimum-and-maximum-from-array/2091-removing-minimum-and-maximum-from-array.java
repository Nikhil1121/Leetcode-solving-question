class Solution {
    public int minimumDeletions(int[] nums) {
        int n = nums.length;

        int minIndex = 0;
        int maxIndex = 0;

        // Find minimum and maximum indices
        for (int i = 1; i < n; i++) {
            if (nums[i] < nums[minIndex]) {
                minIndex = i;
            }

            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }

        // Put min and max in sorted index order
        int left = Math.min(minIndex, maxIndex);
        int right = Math.max(minIndex, maxIndex);

        // 1. Remove both from front
        int fromFront = right + 1;

        // 2. Remove both from back
        int fromBack = n - left;

        // 3. Remove left from front and right from back
        int bothSides = (left + 1) + (n - right);

        return Math.min(fromFront, Math.min(fromBack, bothSides));
    }
}