
import java.math.BigInteger;

class Solution {
    public int maxLength(int[] nums) {
        int n = nums.length;
        int ans = 0;

        for (int i = 0; i < n; i++) {
            BigInteger product = BigInteger.ONE;
            BigInteger gcd = BigInteger.ZERO;
            BigInteger lcm = BigInteger.ONE;

            for (int j = i; j < n; j++) {
                BigInteger num = BigInteger.valueOf(nums[j]);

                // Calculate product
                product = product.multiply(num);

                // Calculate GCD
                gcd = gcd.gcd(num);

                // Calculate LCM
                lcm = lcm.divide(lcm.gcd(num)).multiply(num);

                // Check product equivalent condition
                if (product.equals(gcd.multiply(lcm))) {
                    ans = Math.max(ans, j - i + 1);
                }
            }
        }

        return ans;
    }
}
