import java.util.ArrayList;
import java.util.List;

public class ExpressionConverter {
    public List<Character> convertExpression(List<Character> correctExpression){
        List<Character> convertedExpression = new ArrayList<>();
       for(int i = 0; i<correctExpression.size();i++){
            if(correctExpression.get(i)=='-'){
                List<Character> changed = changeMinus(correctExpression, i+1);
                if(changed.size()!=0){
                    convertedExpression.add('-');
                    convertedExpression.addAll(changeMinus(correctExpression, i+1));
                    i=findMinusPosition(correctExpression, i+1);
                }
                else {
                    convertedExpression.add(correctExpression.get(i));
                }
            }
            else if(correctExpression.get(i)=='/'){
                if(changeDivide(correctExpression, i+1)!=null){
                    convertedExpression.add('/');
                    convertedExpression.addAll(changeDivide(correctExpression, i+1));
                    i=findDividePosition(correctExpression, i+1);
                }
            }
            else{
                convertedExpression.add(correctExpression.get(i));
            }
        }
        return convertedExpression;
    }

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c=='^';
    }
    public List<Character> changeMinus(List<Character> characters, int position){
        List<Character> changed = new ArrayList<>();
        boolean isMinus = false;
        for(int i = position; i<characters.size();i++){
            if(isOperator(characters.get(i))){
                if(characters.get(i)=='-'){
                    isMinus = true;
                    break;
                }
                else{
                    break;
                }
            }
            else if(characters.get(i)=='(' || characters.get(i)==')'){
                break;
            }

        }
        if(isMinus){
            changed.add('(');
            int count = 0;
            for(int j=position;j<characters.size();j++){//need to add to changed all characters from position to - then add next variable and ')'
                if(Character.isLetter(characters.get(j)) || Character.isDigit(characters.get(j))){
                   changed.add(characters.get(j));
                }
                else if(isOperator(characters.get(j)) && count==0){
                    changed.add('+');
                    count++;
                }
                else{
                    break;
                }
            }
            changed.add(')');
        }

        return changed;
    }
    public List<Character> changeDivide(List<Character> characters, int position){
        List<Character> changed = new ArrayList<>();
        for(int i=position;i<characters.size();i++){
            if(isOperator(characters.get(i))){
                if(characters.get(i)=='/') {
                changed.add('*');
                }
                else{
                    break;
                }
            }
            else{
                changed.add(characters.get(i));
            }
        }
        if(changed.size()>1){
            List<Character> changed2 = new ArrayList<>();
            changed2.add('(');
            for(int i = 0; i<changed.size();i++){
                changed2.add(changed.get(i));
            }
            changed2.add(')');
            return changed2;
        }
        return changed;
    }

    public int findMinusPosition(List<Character> characters, int position){
        int i = 0, count = 0;
        for(int j=position;j<characters.size();j++){//need to add to changed all characters from position to - then add next variable and ')'
            if(Character.isLetter(characters.get(j)) || Character.isDigit(characters.get(j))){

            }
            else if(isOperator(characters.get(j)) && count==0){
                count++;
            }
            else{
                i = j;
                break;
            }
        }
        if(i==0){
            i = characters.size();
        }
        return i-1;
    }

    public int findDividePosition(List<Character> characters, int position){
        int count = 0;
        for(int i=position;i<characters.size();i++){
            if(isOperator(characters.get(i))){
                if(characters.get(i)=='/') {
                }
                else{
                    count = i-1;
                    break;
                }
            }
            else{
            }
        }
        if(count==0){
            count = characters.size();
        }
        return count;
    }
}
