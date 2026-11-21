
import java.util.*;

class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        List<List<Integer>> children = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }

        for (int i = 1; i < n; i++) {
            children.get(parent[i]).add(i);
        }

        int[] newParent = parent.clone();
        int[] last = new int[26];
        int[] previous = new int[n];

        Arrays.fill(last, -1);

        // Iterative DFS on original tree
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{0, 0});

        while (!stack.isEmpty()) {
            int[] curr = stack.pop();

            int node = curr[0];
            int state = curr[1];

            int ch = s.charAt(node) - 'a';

            if (state == 0) {
                previous[node] = last[ch];

                if (last[ch] != -1) {
                    newParent[node] = last[ch];
                }

                last[ch] = node;

                stack.push(new int[]{node, 1});

                List<Integer> list = children.get(node);

                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(new int[]{list.get(i), 0});
                }
            } else {
                last[ch] = previous[node];
            }
        }

        // Build the modified tree
        List<List<Integer>> newChildren = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            newChildren.add(new ArrayList<>());
        }

        for (int i = 1; i < n; i++) {
            newChildren.get(newParent[i]).add(i);
        }

        // Get traversal order
        int[] order = new int[n];
        int index = 0;

        Stack<Integer> st = new Stack<>();
        st.push(0);

        while (!st.isEmpty()) {
            int node = st.pop();

            order[index++] = node;

            for (int child : newChildren.get(node)) {
                st.push(child);
            }
        }

        // Calculate subtree sizes
        int[] ans = new int[n];
        Arrays.fill(ans, 1);

        for (int i = n - 1; i > 0; i--) {
            int node = order[i];
            ans[newParent[node]] += ans[node];
        }

        return ans;
    }
}
