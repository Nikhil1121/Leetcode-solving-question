class Solution {
    public boolean sumGame(String num) {
        int n = num.length();
        int half = n / 2;

        int sumLeft = 0;
        int sumRight = 0;

        int qLeft = 0;
        int qRight = 0;

        for (int i = 0; i < half; i++) {
            if (num.charAt(i) == '?') {
                qLeft++;
            } else {
                sumLeft += num.charAt(i) - '0';
            }
        }

        for (int i = half; i < n; i++) {
            if (num.charAt(i) == '?') {
                qRight++;
            } else {
                sumRight += num.charAt(i) - '0';
            }
        }

        // If number of '?' is odd, Alice can always make
        // the final sums unequal.
        if ((qLeft + qRight) % 2 == 1) {
            return true;
        }

        int diff = sumLeft - sumRight;

        // Bob can force equality only when the remaining
        // question marks are balanced and the sum difference
        // can be compensated.
        return Math.abs(diff + 9 * (qLeft - qRight) / 2) != 0;
    }
}