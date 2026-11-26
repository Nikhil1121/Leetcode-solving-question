
class Solution {
    public boolean canAliceWin(int n) {
        int remove = 10;
        boolean aliceTurn = true;

        while (n >= remove) {
            n -= remove;

            aliceTurn = !aliceTurn;
            remove--;
        }

        // Current player cannot move, so the other player wins.
        return !aliceTurn;
    }
}
