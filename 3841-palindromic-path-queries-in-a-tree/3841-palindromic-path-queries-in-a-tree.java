
import java.util.*;

class Solution {
    private int n, timer, LOG;
    private List<Integer>[] adj;
    private int[] tin, tout, depth, parent, bit;
    private int[][] up;
    private char[] chars;

    public List<Boolean> palindromePath(int n, int[][] edges,
                                        String s, String[] queries) {
        this.n = n;
        chars = s.toCharArray();

        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        LOG = 1;
        while ((1 << LOG) <= n) LOG++;

        tin = new int[n];
        tout = new int[n];
        depth = new int[n];
        parent = new int[n];
        up = new int[LOG][n];
        bit = new int[n + 2];

        buildEulerTour();

        for (int k = 1; k < LOG; k++) {
            for (int v = 0; v < n; v++) {
                up[k][v] = up[k - 1][up[k - 1][v]];
            }
        }

        for (int v = 0; v < n; v++) {
            int mask = 1 << (chars[v] - 'a');
            add(tin[v], mask);
            add(tout[v] + 1, mask);
        }

        List<Boolean> result = new ArrayList<>();

        for (String q : queries) {
            String[] p = q.split(" ");

            if (p[0].equals("update")) {
                int u = Integer.parseInt(p[1]);
                char c = p[2].charAt(0);

                int oldMask = 1 << (chars[u] - 'a');
                int newMask = 1 << (c - 'a');

                if (oldMask != newMask) {
                    int delta = oldMask ^ newMask;
                    add(tin[u], delta);
                    add(tout[u] + 1, delta);
                    chars[u] = c;
                }
            } else {
                int u = Integer.parseInt(p[1]);
                int v = Integer.parseInt(p[2]);

                int w = lca(u, v);

                int mask = prefix(tin[u])
                         ^ prefix(tin[v])
                         ^ (1 << (chars[w] - 'a'));

                result.add((mask & (mask - 1)) == 0);
            }
        }

        return result;
    }

    private void buildEulerTour() {
        int[] stack = new int[2 * n + 5];
        int[] iter = new int[n];
        int top = 0;

        stack[top++] = 0;
        parent[0] = 0;
        depth[0] = 0;

        while (top > 0) {
            int u = stack[top - 1];

            if (iter[u] == 0) {
                tin[u] = ++timer;
                up[0][u] = parent[u];
            }

            if (iter[u] < adj[u].size()) {
                int v = adj[u].get(iter[u]++);

                if (v == parent[u]) continue;

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            } else {
                tout[u] = timer;
                top--;
            }
        }
    }

    private void add(int i, int val) {
        for (; i <= n + 1; i += i & -i) {
            bit[i] ^= val;
        }
    }

    private int prefix(int i) {
        int mask = 0;

        for (; i > 0; i -= i & -i) {
            mask ^= bit[i];
        }

        return mask;
    }

    private int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int t = u;
            u = v;
            v = t;
        }

        int diff = depth[u] - depth[v];

        for (int k = 0; k < LOG; k++) {
            if ((diff & (1 << k)) != 0) {
                u = up[k][u];
            }
        }

        if (u == v) return u;

        for (int k = LOG - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }

        return parent[u];
    }
}
