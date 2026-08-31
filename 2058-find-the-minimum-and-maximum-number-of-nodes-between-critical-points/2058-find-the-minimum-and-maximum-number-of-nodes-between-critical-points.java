/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public int[] nodesBetweenCriticalPoints(ListNode head) {

        int first = -1;
        int prevCritical = -1;

        int minDistance = Integer.MAX_VALUE;
        int maxDistance = -1;

        int index = 1;

        ListNode prev = head;
        ListNode curr = head.next;

        while (curr != null && curr.next != null) {

            // Local maxima
            boolean max = curr.val > prev.val &&
                          curr.val > curr.next.val;

            // Local minima
            boolean min = curr.val < prev.val &&
                          curr.val < curr.next.val;

            if (max || min) {

                if (first == -1) {
                    // First critical point
                    first = index;
                } else {
                    // Distance from previous critical point
                    minDistance = Math.min(
                        minDistance,
                        index - prevCritical
                    );

                    // Distance from first critical point
                    maxDistance = index - first;
                }

                prevCritical = index;
            }

            prev = curr;
            curr = curr.next;
            index++;
        }

        if (maxDistance == -1) {
            return new int[]{-1, -1};
        }

        return new int[]{minDistance, maxDistance};
    }
}