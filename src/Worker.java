import java.util.ArrayList;
import java.util.List;

public class Worker {
    public List<String> generateComutations(String expression) {
        List<String> comutations = new ArrayList<>();
        List<String> terms = findTerms(expression);
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i< terms.size();i++){
            for(int j = i+1;j<terms.size();j++){
                for(int k = 0;k<terms.size();k++){
                    if(k==i){
                        if(k==0 && !containsOperator(terms.get(j)))
                            sb.append(terms.get(j));
                        else if(!containsOperator(terms.get(j))){
                            sb.append("+");
                            sb.append(terms.get(j));
                        }
                        else if(k==0 && containsOperator(terms.get(j))){
                            sb.append(terms.get(j).substring(1));
                        }
                        else
                            sb.append(terms.get(j));

                    }
                    else if(k==j){
                        if(k==0 && !containsOperator(terms.get(i)))
                            sb.append(terms.get(i));
                        else if(!containsOperator(terms.get(i))){
                            sb.append("+");
                            sb.append(terms.get(i));
                        }
                        else
                            sb.append(terms.get(i));

                    }
                    else{
                        sb.append(terms.get(k));
                    }
                }
                comutations.add(sb.toString());
                sb.setLength(0);
            }
        }
        //permute(expression.toCharArray(), 0, permutations);
        return comutations;
    }

    public List<String> findTerms(String expression){
        List<String> terms = new ArrayList<>();
        int i = 0;
        while(i < expression.length()){
            if(expression.charAt(i) == '+' || expression.charAt(i) == '-'){
                terms.add(expression.substring(0, i));
                expression = expression.substring(i);
                i = 0;
            }
            else if(expression.charAt(i)=='('){
                int closingIndex = findClosingBracket(expression.toCharArray(), i);
                terms.add(expression.substring(0, closingIndex+1));
                expression = expression.substring(closingIndex+1);
                i = 0;
            }
            else if(expression.charAt(i)=='*' || expression.charAt(i)=='/'){
                int j = i+1;
                while(j < expression.length() && expression.charAt(j)!='+' && expression.charAt(j)!='-' && expression.charAt(j)!='*' && expression.charAt(j)!='/'){
                    j++;
                }
                terms.add(expression.substring(0, j));
                expression = expression.substring(j);
                i = 0;
            }
            else if(expression.charAt(i)=='^'){
                int j = i+1;
                while(j < expression.length() && expression.charAt(j)!='+' && expression.charAt(j)!='-' && expression.charAt(j)!='*' && expression.charAt(j)!='/'){
                    j++;
                }
                terms.add(expression.substring(0, j));
                expression = expression.substring(j);
                i = 0;
            }
            else if(i==expression.length()-1){
                terms.add(expression);
                i++;
            }

            i++;

        }
        return terms;
    }

    public void permute(char[] expr, int index, List<String> permutations) {
        if (index == expr.length - 1) {
            permutations.add(new String(expr));
        } else {
            for (int i = index; i < expr.length; i++) {
                swap(expr, index, i);
                permute(expr, index + 1, permutations);
                swap(expr, index, i);
            }
        }
    }

    public void swap(char[] expr, int i, int j) {
        char temp = expr[i];
        expr[i] = expr[j];
        expr[j] = temp;
    }

    public List<String> generateDistributions(String expression) {
        List<String> distributions = new ArrayList<>();
        distribute(expression.toCharArray(), 0, distributions);
        return distributions;
    }

    public void distribute(char[] expr, int index, List<String> distributions) {
        if (index == expr.length - 1) {
            distributions.add(new String(expr));
        } else if (expr[index] == '(') {
            int closingIndex = findClosingBracket(expr, index);
            distribute(expr, closingIndex + 1, distributions);
            distribute(expr, index + 1, distributions);
            for (int i = index + 1; i <= closingIndex - 2; i++) {
                swap(expr, index + 1, i);
                distribute(expr, closingIndex + 1, distributions);
                swap(expr, index + 1, i);
            }
        } else {
            distribute(expr, index + 1, distributions);
        }
    }

    public int findClosingBracket(char[] expr, int startIndex) {
        int count = 1;
        for (int i = startIndex + 1; i < expr.length; i++) {
            if (expr[i] == '(') {
                count++;
            } else if (expr[i] == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    public boolean containsOperator(String term){
        for(int i = 0;i<term.length();i++){
            if(term.charAt(i)=='+' || term.charAt(i)=='-' || term.charAt(i)=='*' || term.charAt(i)=='/' || term.charAt(i)=='^'){
                return true;
            }
        }
        return false;
    }
}
