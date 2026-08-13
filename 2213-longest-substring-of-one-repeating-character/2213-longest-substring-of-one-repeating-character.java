class Solution {

    static class Node {
        int len;
        int leftChar;
        int rightChar;
        int prefix;
        int suffix;
        int best;

        Node() {}

        Node(int len, int leftChar, int rightChar,
             int prefix, int suffix, int best) {
            this.len = len;
            this.leftChar = leftChar;
            this.rightChar = rightChar;
            this.prefix = prefix;
            this.suffix = suffix;
            this.best = best;
        }
    }

    Node[] tree;
    char[] s;

    private Node merge(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;

        Node res = new Node();
        res.len = a.len + b.len;

        res.leftChar = a.leftChar;
        res.rightChar = b.rightChar;

        res.prefix = a.prefix;
        if (a.prefix == a.len && a.rightChar == b.leftChar) {
            res.prefix = a.len + b.prefix;
        }

        res.suffix = b.suffix;
        if (b.suffix == b.len && a.rightChar == b.leftChar) {
            res.suffix = b.len + a.suffix;
        }

        res.best = Math.max(a.best, b.best);

        if (a.rightChar == b.leftChar) {
            res.best = Math.max(
                res.best,
                a.suffix + b.prefix
            );
        }

        return res;
    }

    private void build(int node, int l, int r) {
        if (l == r) {
            int c = s[l] - 'a';

            tree[node] = new Node(
                1,
                c,
                c,
                1,
                1,
                1
            );

            return;
        }

        int mid = l + (r - l) / 2;

        build(node * 2, l, mid);
        build(node * 2 + 1, mid + 1, r);

        tree[node] = merge(
            tree[node * 2],
            tree[node * 2 + 1]
        );
    }

    private void update(int node, int l, int r,
                       int idx, int c) {

        if (l == r) {
            tree[node] = new Node(
                1,
                c,
                c,
                1,
                1,
                1
            );
            return;
        }

        int mid = l + (r - l) / 2;

        if (idx <= mid) {
            update(node * 2, l, mid, idx, c);
        } else {
            update(node * 2 + 1, mid + 1, r, idx, c);
        }

        tree[node] = merge(
            tree[node * 2],
            tree[node * 2 + 1]
        );
    }

    public int[] longestRepeating(
        String s,
        String queryCharacters,
        int[] queryIndices
    ) {

        this.s = s.toCharArray();

        int n = this.s.length;
        int k = queryIndices.length;

        tree = new Node[4 * n];

        build(1, 0, n - 1);

        int[] ans = new int[k];

        for (int i = 0; i < k; i++) {

            int idx = queryIndices[i];
            int c = queryCharacters.charAt(i) - 'a';

            this.s[idx] = queryCharacters.charAt(i);

            update(
                1,
                0,
                n - 1,
                idx,
                c
            );

            ans[i] = tree[1].best;
        }

        return ans;
    }
}