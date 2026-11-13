
import java.util.*;

class Solution {
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;

        Deque<long[]> deque = new ArrayDeque<>();

        long cost = 0;
        long ans = 0;

        int right = n - 1;

        for (int i = n - 1; i >= 0; i--) {

            long val = nums[i];
            long count = 1;

            // Maintain decreasing groups of suffix maximums
            while (!deque.isEmpty() &&
                   deque.peekLast()[0] < val) {

                long[] last = deque.pollLast();

                cost += (val - last[0]) * last[1];
                count += last[1];
            }

            // Merge equal values
            if (!deque.isEmpty() &&
                deque.peekLast()[0] == val) {

                deque.peekLast()[1] += count;
            } else {
                deque.offerLast(new long[]{val, count});
            }

            // Shrink window while cost exceeds k
            while (cost > k) {

                long[] first = deque.peekFirst();

                cost -= first[0] - nums[right];

                first[1]--;

                if (first[1] == 0) {
                    deque.pollFirst();
                }

                right--;
            }

            // Count valid subarrays ending at i
            ans += right - i + 1;
        }

        return ans;
    }
}
