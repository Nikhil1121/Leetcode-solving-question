class Solution {
    public int[] validSequence(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // next[i] = number of characters from the end of word2
        // that can be matched using word1[i...]
        int[] suf = new int[n + 1];

        int j = m - 1;

        for (int i = n - 1; i >= 0; i--) {
            suf[i] = suf[i + 1];

            if (j >= 0 && word1.charAt(i) == word2.charAt(j)) {
                suf[i] = Math.max(suf[i], m - j);
                j--;
            }
        }

        int[] ans = new int[m];

        int p = 0;
        boolean changed = false;

        for (int i = 0; i < n && p < m; i++) {

            if (word1.charAt(i) == word2.charAt(p)) {
                ans[p++] = i;
            } 
            else if (!changed) {

                int need = m - p - 1;

                if (need == 0 || suf[i + 1] >= need) {
                    ans[p++] = i;
                    changed = true;
                }
            }
        }

        if (p != m) {
            return new int[0];
        }

        return ans;
    }
}