class Solution {
    public boolean stoneGameIX(int[] stones) {
        int[] cnt = new int[3];

        for (int x : stones) {
            cnt[x % 3]++;
        }

        // If there are no remainder-1 and remainder-2 stones,
        // Alice can never make the sum divisible by 3 on her turn.
        if (cnt[1] == 0 && cnt[2] == 0) {
            return false;
        }

        // If remainder-0 stones are even, the game behaves
        // like the basic alternating 1/2 remainder game.
        if (cnt[0] % 2 == 0) {
            return cnt[1] > 0 && cnt[2] > 0;
        }

        // If remainder-0 stones are odd, Alice needs one side
        // to have at least two more stones than the other.
        return Math.abs(cnt[1] - cnt[2]) > 2;
    }
}