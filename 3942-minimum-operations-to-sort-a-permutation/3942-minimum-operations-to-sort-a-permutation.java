
import java.util.*;

class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        int total = 2 * n;

        int[] dist = new int[total];
        Arrays.fill(dist, -1);

        Queue<Integer> q = new LinkedList<>();
        dist[0] = 0;
        q.offer(0);

        while (!q.isEmpty()) {
            int state = q.poll();
            int k = state % n;
            int rev = state / n;

            if (isSorted(nums, k, rev)) {
                return dist[state];
            }

            // Rotate left by one
            int next = rev * n + (k + 1) % n;
            if (dist[next] == -1) {
                dist[next] = dist[state] + 1;
                q.offer(next);
            }

            // Reverse the entire array
            next = (1 - rev) * n + (n - k) % n;
            if (dist[next] == -1) {
                dist[next] = dist[state] + 1;
                q.offer(next);
            }
        }

        return -1;
    }

    private boolean isSorted(int[] nums, int k, int rev) {
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            int idx = (i + k) % n;

            int val;
            if (rev == 0) {
                val = nums[idx];
            } else {
                val = nums[n - 1 - idx];
            }

            if (val != i) {
                return false;
            }
        }

        return true;
    }
}




