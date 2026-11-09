
import java.util.*;

class Solution {
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {

        TreeSet<Integer> available = new TreeSet<>();

        // [finishTime, serverIndex]
        PriorityQueue<long[]> busy = new PriorityQueue<>(
            (a, b) -> Long.compare(a[0], b[0])
        );

        int[] count = new int[k];

        // Initially all servers are available
        for (int i = 0; i < k; i++) {
            available.add(i);
        }

        for (int i = 0; i < arrival.length; i++) {

            int time = arrival[i];

            // Release servers whose tasks are completed
            while (!busy.isEmpty() && busy.peek()[0] <= time) {
                long[] server = busy.poll();
                available.add((int) server[1]);
            }

            // If all servers are busy, drop the request
            if (available.isEmpty()) {
                continue;
            }

            // Find the next available server
            int preferred = i % k;

            Integer server = available.ceiling(preferred);

            // Wrap around if necessary
            if (server == null) {
                server = available.first();
            }

            available.remove(server);

            count[server]++;

            // Mark server as busy
            busy.offer(new long[]{
                (long) arrival[i] + load[i],
                server
            });
        }

        // Find maximum requests handled
        int max = 0;

        for (int c : count) {
            max = Math.max(max, c);
        }

        // Collect all busiest servers
        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            if (count[i] == max) {
                ans.add(i);
            }
        }

        return ans;
    }
}
