import java.util.*;

class Solution {

    int n;
    int[][] arr;
    int[][] next;
    
    long[][] dp;
    List<Integer>[][] best;

    public int[] maximumWeight(List<List<Integer>> intervals) {

        n = intervals.size();

        // [left, right, weight, originalIndex]
        arr = new int[n][4];

        for (int i = 0; i < n; i++) {
            arr[i][0] = intervals.get(i).get(0);
            arr[i][1] = intervals.get(i).get(1);
            arr[i][2] = intervals.get(i).get(2);
            arr[i][3] = i;
        }

        // Sort by left endpoint
        Arrays.sort(arr, (a, b) -> {
            if (a[0] != b[0])
                return Integer.compare(a[0], b[0]);

            return Integer.compare(a[1], b[1]);
        });

        // Find next non-overlapping interval
        next = new int[n][1];

        for (int i = 0; i < n; i++) {

            int left = i + 1;
            int right = n;

            while (left < right) {

                int mid = left + (right - left) / 2;

                // Need arr[mid][0] > arr[i][1]
                if (arr[mid][0] > arr[i][1]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            next[i][0] = left;
        }

        /*
         * dp[i][k] = maximum score
         * from i onwards using at most k intervals
         */
        dp = new long[n + 1][5];

        @SuppressWarnings("unchecked")
        List<Integer>[][] temp =
                (ArrayList<Integer>[][]) new ArrayList[n + 1][5];

        best = temp;

        for (int i = 0; i <= n; i++) {
            for (int k = 0; k <= 4; k++) {
                best[i][k] = new ArrayList<>();
            }
        }

        // Build DP from right to left
        for (int i = n - 1; i >= 0; i--) {

            for (int k = 1; k <= 4; k++) {

                // Option 1: Skip current interval
                long skipScore = dp[i + 1][k];
                List<Integer> skipList = best[i + 1][k];

                // Option 2: Take current interval
                int j = next[i][0];

                long takeScore =
                        arr[i][2] + dp[j][k - 1];

                List<Integer> takeList =
                        new ArrayList<>();

                takeList.add(arr[i][3]);
                takeList.addAll(best[j][k - 1]);

                // Sort indices because answer must be lexicographically
                // compared in increasing original-index order
                Collections.sort(takeList);

                if (takeScore > skipScore) {

                    dp[i][k] = takeScore;
                    best[i][k] = takeList;

                } else if (takeScore < skipScore) {

                    dp[i][k] = skipScore;
                    best[i][k] = new ArrayList<>(skipList);

                } else {

                    // Same score -> lexicographically smaller
                    if (compare(takeList, skipList) < 0) {

                        dp[i][k] = takeScore;
                        best[i][k] = takeList;

                    } else {

                        dp[i][k] = skipScore;
                        best[i][k] = new ArrayList<>(skipList);
                    }
                }
            }
        }

        List<Integer> answer = best[0][4];

        int[] result = new int[answer.size()];

        for (int i = 0; i < answer.size(); i++) {
            result[i] = answer.get(i);
        }

        return result;
    }

    // Lexicographical comparison
    private int compare(List<Integer> a, List<Integer> b) {

        int size = Math.min(a.size(), b.size());

        for (int i = 0; i < size; i++) {

            if (!a.get(i).equals(b.get(i))) {
                return Integer.compare(a.get(i), b.get(i));
            }
        }

        return Integer.compare(a.size(), b.size());
    }
}