class Solution {
    public String lexGreaterPermutation(String s, String target) {
        int[] freq = new int[26];

        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        char[] ans = new char[s.length()];

        if (build(target, 0, freq, ans)) {
            return new String(ans);
        }

        return "";
    }

    private boolean build(String target, int pos, int[] freq, char[] ans) {

        if (pos == target.length()) {
            return false;
        }

        int t = target.charAt(pos) - 'a';

        // Try same character first
        if (freq[t] > 0) {
            freq[t]--;
            ans[pos] = (char) ('a' + t);

            if (build(target, pos + 1, freq, ans)) {
                return true;
            }

            freq[t]++;
        }

        // Try the smallest character greater than target[pos]
        for (int c = t + 1; c < 26; c++) {
            if (freq[c] > 0) {

                freq[c]--;
                ans[pos] = (char) ('a' + c);

                int idx = pos + 1;

                // Put remaining characters in sorted order
                for (int x = 0; x < 26; x++) {
                    while (freq[x] > 0) {
                        ans[idx++] = (char) ('a' + x);
                        freq[x]--;
                    }
                }

                return true;
            }
        }

        return false;
    }
}