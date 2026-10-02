class Solution {
    public List<String> generateParenthesis(int n) {

        // solving using the Brute force method
        List<String> result = new ArrayList<>();
        generateAll(new char[2 * n], 0, result);
        return result;
    }

    private void generateAll(char[] current, int pos, List<String> result){
        //base condition 
        if(pos == current.length){
            if(isValid(current)){
                result.add(new String(current));
            }
            return;
        }
        //using recursion making a equal branch of this
        current[pos] = '(';
        generateAll(current,pos + 1, result);

        current[pos] = ')';
        generateAll(current,pos + 1, result);
    }

    private boolean isValid(char[] current){
        //  End me '(' aur ')' count equal hone chahiye
        //   (balance == 0)
        int balance = 0;
        for (char c : current){
            if(c == '(') balance++;
            else balance--;
            if(balance < 0) return false;
        }
        return balance == 0;
    }
}