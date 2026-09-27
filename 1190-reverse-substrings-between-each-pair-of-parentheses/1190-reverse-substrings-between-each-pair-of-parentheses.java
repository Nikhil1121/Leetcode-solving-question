class Solution {
    public String reverseParentheses(String s) {

        Stack<StringBuilder> stack = new Stack<>();

        StringBuilder current = new StringBuilder();

        for (char ch : s.toCharArray()) {

            if (ch == '(') {

                // Save current string
                stack.push(current);

                // Start a new string
                current = new StringBuilder();

            } 
            else if (ch == ')') {

                // Reverse current substring
                current.reverse();

                // Get previous string
                StringBuilder previous = stack.pop();

                // Append reversed substring
                previous.append(current);

                current = previous;

            } 
            else {

                current.append(ch);
            }
        }

        return current.toString();
    }
}