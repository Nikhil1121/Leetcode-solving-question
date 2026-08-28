class Solution {
    public String lexPalindromicPermutation(String s, String target) {

        int n = s.length();
        int[] freq = new int[26];

        // Count characters
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        // Check if palindrome is possible
        int odd = 0;
        int middle = -1;

        for (int i = 0; i < 26; i++) {
            if (freq[i] % 2 == 1) {
                odd++;
                middle = i;
            }
        }

        // More than one odd frequency -> no palindrome
        if (odd > 1) {
            return "";
        }

        int halfLen = n / 2;

        // Frequency of characters in first half
        int[] halfFreq = new int[26];

        for (int i = 0; i < 26; i++) {
            halfFreq[i] = freq[i] / 2;
        }

        char[] half = new char[halfLen];

        if (!build(half, 0, halfFreq, target, middle)) {
            return "";
        }

        return makePalindrome(half, middle);
    }

    private boolean build(char[] half, int pos, int[] freq,
                          String target, int middle) {

        if (pos == half.length) {
            String palindrome = makePalindrome(half, middle);

            return palindrome.compareTo(target) > 0;
        }

        int targetChar = target.charAt(pos) - 'a';

        // Try characters from smallest to largest
        for (int c = 0; c < 26; c++) {

            if (freq[c] == 0) {
                continue;
            }

            freq[c]--;
            half[pos] = (char) ('a' + c);

            // If already greater than target,
            // remaining characters should be smallest.
            if (c > targetChar) {

                int index = pos + 1;

                for (int x = 0; x < 26; x++) {
                    while (freq[x] > 0) {
                        half[index++] = (char) ('a' + x);
                        freq[x]--;
                    }
                }

                return true;
            }

            // If equal, continue to next position
            if (c == targetChar) {

                if (build(half, pos + 1, freq, target, middle)) {
                    return true;
                }
            }

            // Restore
            freq[c]++;
        }

        return false;
    }

    private String makePalindrome(char[] half, int middle) {

        StringBuilder result = new StringBuilder();

        // First half
        result.append(half);

        // Middle character
        if (middle != -1) {
            result.append((char) ('a' + middle));
        }

        // Reverse first half
        for (int i = half.length - 1; i >= 0; i--) {
            result.append(half[i]);
        }

        return result.toString();
    }
}