import java.util.*;

class Solution {
    public String smallestNumber(String num, long t) {
        // Factor t into powers of 2,3,5,7
        int a = 0, b = 0, c = 0, d = 0;
        long tt = t;
        while (tt % 2 == 0) { tt /= 2; a++; }
        while (tt % 3 == 0) { tt /= 3; b++; }
        while (tt % 5 == 0) { tt /= 5; c++; }
        while (tt % 7 == 0) { tt /= 7; d++; }
        if (tt != 1) return "-1"; // other prime factors -> impossible

        int n = num.length();

        // Try same length: find smallest string >= num, zero-free, satisfying (a,b,c,d)
        String same = trySameLength(num, a, b, c, d);
        if (same != null) return same;

        // Otherwise go to length n+1 (always possible since digit 8 gives huge power of 2,
        // digit 9 gives power of 3, etc. — but must confirm feasibility for large a,b,c,d)
        // Use length n+1, n+2, ... until we find minimal length that can hold required digits.
        int len = n + 1;
        while (true) {
            String res = buildMinimalOfLength(len, a, b, c, d);
            if (res != null) return res;
            len++;
            // Safety bound: t <= 1e14, max exponents bounded, so this terminates quickly.
            if (len > n + 60) return "-1"; // safety guard, should not trigger
        }
    }

    // Build smallest zero-free string of exactly `len` digits achieving product with
    // 2^a * 3^b * 5^c * 7^d dividing it. Padding with '1's allowed. Returns null if impossible
    // (i.e., len is not enough room for mandatory 5s (c) and 7s (d) at minimum, or generally can't fit).
    private String buildMinimalOfLength(int len, int a, int b, int c, int d) {
        // mandatory digits: c fives, d sevens => need at least c+d slots
        if (c + d > len) return null;
        int remaining = len - c - d; // slots left for covering (a,b) via digits from {1,2,3,4,6,8,9}
        // Find minimal number of "meaningful" digits (non-1) from set covering exponent a of 2, b of 3,
        // using at most `remaining` digits total (rest filled with '1').
        int[] combo = minDigitsForAB(a, b, remaining);
        if (combo == null) return null; // can't achieve within remaining slots

        // combo is array of digit counts for digits 2,3,4,6,8,9 chosen (count of each)
        // Now assemble the number of length len:
        //  - fill with '1's for padding (remaining - usedSlots) times
        //  - then the chosen 2/3/4/6/8/9 digits sorted ascending (to keep number small) 
        //  - then required 5's (c of them)
        //  - then required 7's (d of them)
        // We want smallest number: leading positions smallest. '1' is smallest possible digit,
        // so all padding '1's go first (most significant). Among the meaningful non-1 digits chosen
        // for a,b we should order them ascending after the 1's, then followed by 5's, then 7's,
        // BUT actually to keep minimal value we want the entire suffix (non-1 digits) sorted ascending
        // as a whole, mixing 2/3/4/5/6/7/8/9 in ascending order, since any arrangement must include all
        // mandatory non-one digits; sorting them ascending gives lexicographically smallest suffix.
        List<Integer> nonOne = new ArrayList<>();
        int[] digitsArr = {2,3,4,6,8,9};
        int usedSlots = 0;
        for (int i = 0; i < digitsArr.length; i++) {
            for (int k = 0; k < combo[i]; k++) {
                nonOne.add(digitsArr[i]);
                usedSlots++;
            }
        }
        for (int k = 0; k < c; k++) nonOne.add(5);
        for (int k = 0; k < d; k++) nonOne.add(7);
        Collections.sort(nonOne);

        int padCount = len - nonOne.size();
        if (padCount < 0) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padCount; i++) sb.append('1');
        for (int v : nonOne) sb.append((char)('0' + v));
        return sb.toString();
    }

    // Find minimal-count combination of digits {2,3,4,6,8,9} (with counts) covering
    // at least 'a' factors of 2 and 'b' factors of 3, using at most 'maxSlots' digits total.
    // Returns int[6] counts for [2,3,4,6,8,9] or null if impossible within maxSlots.
    // We choose the combination minimizing total digit count first (to leave more '1' padding,
    // which keeps the number smaller), and among equal counts we don't need to pick specific
    // digits here since final sorting handles ordering; but different digit choices with same
    // count can differ in value composition — however since we sort ascending at the end anyway,
    // what matters is achieving the exponents with fewest digits (favor 8 for factor of 2^3, 9 for 3^2, etc.)
    private int[] minDigitsForAB(int a, int b, int maxSlots) {
        // dp over (exponent of 2 covered, exponent of 3 covered) -> min digits used
        // cap a and b to avoid huge DP; a can be up to ~47 (since t<=1e14 => 2^a <=1e14 => a<=46)
        // b up to ~29 (3^b <=1e14 => b<=29)
        int capA = a, capB = b;
        // digit contributions: value -> (d2,d3)
        int[][] opts = {
            {2,1,0}, {3,0,1}, {4,2,0}, {6,1,1}, {8,3,0}, {9,0,2}
        };
        int INF = Integer.MAX_VALUE / 2;
        int[][] dp = new int[capA+1][capB+1];
        int[][] choice = new int[capA+1][capB+1]; // which digit index used to reach this state optimally (for reconstruction via BFS/DP order)
        for (int[] row : dp) Arrays.fill(row, INF);
        dp[0][0] = 0;
        // We do a DP similar to unbounded knapsack: process states in order of increasing digits used (BFS by layers)
        // Since we want minimum digit count reaching >= a and >= b, do a BFS/Dijkstra-like with digits as "items", unbounded use.
        // Use iterative relaxation since it's like shortest path with all edge weights = 1 (unbounded knapsack minimizing count).
        // We'll do this via multiple passes (like Bellman-Ford) since values are small.
        boolean updated = true;
        int maxIter = capA + capB + 5;
        for (int iter = 0; iter < maxIter && updated; iter++) {
            updated = false;
            for (int i = 0; i <= capA; i++) {
                for (int j = 0; j <= capB; j++) {
                    if (dp[i][j] == INF) continue;
                    for (int[] opt : opts) {
                        int ni = Math.min(capA, i + opt[1]);
                        int nj = Math.min(capB, j + opt[2]);
                        if (dp[i][j] + 1 < dp[ni][nj]) {
                            dp[ni][nj] = dp[i][j] + 1;
                            updated = true;
                        }
                    }
                }
            }
        }
        int best = dp[capA][capB];
        if (best == INF || best > maxSlots) return null;

        // Reconstruct one optimal combination using greedy re-derivation:
        // Re-run DP but track parent choices explicitly with single pass BFS-style using counts as levels.
        int[][] parentDigit = new int[capA+1][capB+1];
        int[][] parentI = new int[capA+1][capB+1];
        int[][] parentJ = new int[capA+1][capB+1];
        int[][] dist = new int[capA+1][capB+1];
        for (int[] row : dist) Arrays.fill(row, -1);
        dist[0][0] = 0;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{0,0});
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int i = cur[0], j = cur[1];
            if (dist[i][j] > best) continue;
            for (int oi = 0; oi < opts.length; oi++) {
                int[] opt = opts[oi];
                int ni = Math.min(capA, i + opt[1]);
                int nj = Math.min(capB, j + opt[2]);
                if (dist[ni][nj] == -1) {
                    dist[ni][nj] = dist[i][j] + 1;
                    parentDigit[ni][nj] = opt[0];
                    parentI[ni][nj] = i;
                    parentJ[ni][nj] = j;
                    queue.add(new int[]{ni, nj});
                }
            }
        }
        // Reconstruct path from (capA,capB) back to (0,0)
        int[] counts = new int[6]; // for digits 2,3,4,6,8,9 in that order
        int[] digitToIdx = new int[10];
        digitToIdx[2]=0; digitToIdx[3]=1; digitToIdx[4]=2; digitToIdx[6]=3; digitToIdx[8]=4; digitToIdx[9]=5;
        int ci = capA, cj = capB;
        while (ci != 0 || cj != 0) {
            int dgt = parentDigit[ci][cj];
            int pi = parentI[ci][cj];
            int pj = parentJ[ci][cj];
            counts[digitToIdx[dgt]]++;
            ci = pi; cj = pj;
        }
        int total = 0;
        for (int c : counts) total += c;
        if (total > maxSlots) return null;
        return counts;
    }

    // Try to find smallest zero-free string of the SAME length as num, >= num, satisfying exponents.
    private String trySameLength(String num, int a, int b, int c, int d) {
        int n = num.length();
        int[] digits = new int[n];
        for (int i = 0; i < n; i++) digits[i] = num.charAt(i) - '0';

        // Try: keep prefix [0, pos) equal to num, at position pos place a digit > num[pos] (zero-free),
        // fill rest optimally (greedy minimal) to satisfy remaining exponent requirements.
        // Also try pos = n meaning num itself (all digits zero-free) satisfies requirement, or we need
        // to increase digits within same value... Actually first check if num itself is zero-free and
        // satisfies divisibility -- if so return num directly (case pos = -1 sentinel).

        if (isZeroFree(digits) && satisfies(digits, a, b, c, d)) {
            return num;
        }

        // For pos from n-1 down to 0, try increasing digit at pos (keeping prefix same),
        // fill suffix minimally. We want the SMALLEST resulting number >= num, so we should try
        // pos from RIGHTMOST first? No — standard "next number" construction: to get smallest number >= num,
        // we try the longest possible matching prefix (i.e., pos as large as possible / rightmost),
        // increasing digit at pos, then fill suffix with the minimal zero-free combination achieving
        // remaining requirement. If multiple pos work, the one with larger pos (closer to the end) gives
        // a smaller resulting number (since prefix stays equal to num for longer).
        // So iterate pos from n-1 downto 0, and return the first success (that's the smallest such number).

        for (int pos = n - 1; pos >= 0; pos--) {
            int origDigit = digits[pos];
            for (int newDigit = origDigit + 1; newDigit <= 9; newDigit++) {
                // prefix [0,pos) = original digits (must be zero-free! since equal to num's digits which
                // might contain 0 — if prefix contains a 0 before pos, this branch is invalid)
                boolean prefixOk = true;
                for (int k = 0; k < pos; k++) {
                    if (digits[k] == 0) { prefixOk = false; break; }
                }
                if (!prefixOk) continue; // if prefix has a zero, no same-length solution keeping this prefix
                // compute exponents contributed by prefix + newDigit
                int[] exp = computeExp(digits, pos, newDigit);
                int remA = Math.max(0, a - exp[0]);
                int remB = Math.max(0, b - exp[1]);
                int remC = Math.max(0, c - exp[2]);
                int remD = Math.max(0, d - exp[3]);
                int suffixLen = n - pos - 1;
                String suffix = buildMinimalOfLength(suffixLen, remA, remB, remC, remD);
                if (suffix != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int k = 0; k < pos; k++) sb.append((char)('0' + digits[k]));
                    sb.append((char)('0' + newDigit));
                    sb.append(suffix);
                    return sb.toString();
                }
            }
        }
        return null;
    }

    private int[] computeExp(int[] digits, int pos, int newDigitAtPos) {
        // exponents of 2,3,5,7 from digits[0..pos-1] plus newDigitAtPos
        int a=0,b=0,c=0,d=0;
        for (int k = 0; k < pos; k++) {
            int[] e = digitExp(digits[k]);
            a += e[0]; b += e[1]; c += e[2]; d += e[3];
        }
        int[] e = digitExp(newDigitAtPos);
        a += e[0]; b += e[1]; c += e[2]; d += e[3];
        return new int[]{a,b,c,d};
    }

    private int[] digitExp(int digit) {
        switch (digit) {
            case 2: return new int[]{1,0,0,0};
            case 3: return new int[]{0,1,0,0};
            case 4: return new int[]{2,0,0,0};
            case 5: return new int[]{0,0,1,0};
            case 6: return new int[]{1,1,0,0};
            case 7: return new int[]{0,0,0,1};
            case 8: return new int[]{3,0,0,0};
            case 9: return new int[]{0,2,0,0};
            default: return new int[]{0,0,0,0}; // 1 or 0
        }
    }

    private boolean isZeroFree(int[] digits) {
        for (int d : digits) if (d == 0) return false;
        return true;
    }

    private boolean satisfies(int[] digits, int a, int b, int c, int d) {
        int A=0,B=0,C=0,D=0;
        for (int dg : digits) {
            int[] e = digitExp(dg);
            A += e[0]; B += e[1]; C += e[2]; D += e[3];
        }
        return A >= a && B >= b && C >= c && D >= d;
    }
}