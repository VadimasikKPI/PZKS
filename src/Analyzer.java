import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Analyzer {
    private int brackets = 0;
    private int figureBrackets = 0;
    private int squareBrackets = 0;
    private Stack<Character> variables = new Stack<>();
    private List<String> errors = new ArrayList<>();
    private List<Integer> errorPositions = new ArrayList<>();
    public final String ANSI_RESET = "\u001B[0m";

    public final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public List<String> analyze(String expression) {
        List<Character> characters = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            characters.add(c);
        }

        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i) == '(') {
                if(i+1!=characters.size() && isOperator(characters.get(i+1))){
                    errors.add("Операція " + characters.get(i+1) + " після відкритої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(i-1!=-1 && !isOperator(characters.get(i-1))){
                    errors.add("Перед '(' не має знаку операції. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                brackets += 1;
            }
            else if (characters.get(i) == ')') {
                if(i==0){
                    errors.add("Душка ')' на початку виразу. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(brackets == 0){
                    errors.add("Душка ')' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(characters.get(i-1)=='('){
                    errors.add("Порожні душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(isOperator(characters.get(i-1))){
                    errors.add("Операція " + characters.get(i-1) + " перед ')'. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(i+2<characters.size() && !isOperator(characters.get(i+1)) && !isOperator(characters.get(i+2)) && i+2!=characters.size()){
                    if(characters.get(i+1)==')'){
                        continue;
                    }
                    else{
                    errors.add("Після ')' не має операції. Позиція - " + (i+1));
                    errorPositions.add(i);
                    }
                }
                brackets -=1;
            }
            else if(characters.get(i)==' '){
                if(i+1!=characters.size() && characters.get(i+1)==' '){
                    errors.add("Забагато пробілів. Позиція - " + (i+1));
                    errorPositions.add(i);
                    errorPositions.add(i+1);
                }
            }
            else if(characters.get(i)==';'){
                errors.add("Символ ';' в не правильному місці. Позиція - " + (i+1));
                errorPositions.add(i);
            }
            else if(characters.get(i)=='{'){
                figureBrackets+=1;
            }
            else if(characters.get(i)=='}'){
                if(figureBrackets!=0){
                    figureBrackets-=1;
                }
                else {
                    errors.add("Душка '}' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
            }
            else if(characters.get(i)=='['){
                squareBrackets+=1;
            }
            else if(characters.get(i)==']'){
                if(squareBrackets!=0){
                    squareBrackets-=1;
                }
                else {
                    errors.add("Душка ']' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
            }
            else if(isSpecialCharacter(characters.get(i))){
                errors.add("В тілі функції помилка. Позиція - " + i);
                errorPositions.add(i);
            }
            else if (isOperator(characters.get(i))) {
                if(i==0){
                    if(characters.get(i)=='-' && isFunctionOrVariable(characters, i+1)) continue;
                    else{
                        errors.add("Знак " + characters.get(i) + " на початку виразу. Позиція - " + (i+1));
                        errorPositions.add(i);
                    }

                }
                else if(i==characters.size()-1){
                    errors.add("Знак " + characters.get(i) + " на кінці виразу. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if (isOperator(characters.get(i+1))) {
                    errors.add("Подвійні операції " + characters.get(i) + " та " + characters.get(i+1)+ ". Позиція - " + (i+1) + " та "+ (i+2));
                    errorPositions.add(i);
                    errorPositions.add(i+1);
                }

            }
            else if (Character.isDigit(characters.get(i))){
                int nextI = isConstant(characters, i);
                i = nextI;
            }
            else if(Character.isAlphabetic(characters.get(i))){
                if(isFunctionOrVariable(characters, i)){
                    int nextI =isFunction(characters, i);
                    i = nextI;
                }
                else {
                    int nextI = isVariable(characters, i);
                    i = nextI;
                }
            }
        }
        if(amount(expression)!=0){
            errors.add("Нерівна кількість відкритих та закритих дужок в основному виразі");
        }
        if (figureBrackets!=0){
            errors.add("Нерівна кількість фігурних дужок");
        }
        if(squareBrackets!=0){
            errors.add("Нерівна кількість квадратиних дужок");
        }

        return errors;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c=='^';
    }

    private int isVariable(List<Character> characters, int startPosition){
        String pattern = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        int endPosition = startPosition;
        StringBuilder builder = new StringBuilder();
        for(int i = startPosition;i<characters.size();i++){
            if(Character.isAlphabetic(characters.get(i)) || Character.isDigit(characters.get(i))){
                builder.append(characters.get(i));
            }
            else{
                endPosition = i-1;
                break;
            }
        }
        if(!Pattern.matches(pattern, builder)){
            errors.add("Ім'я змінної задано не вірно. Позиція - " + startPosition);
            for(int j = startPosition;j<=endPosition;j++){
                errorPositions.add(j);
            }
        }
        return endPosition;
    }

    private int isConstant(List<Character> characters, int startPosition){
        int endPosition = startPosition;
        StringBuilder builder = new StringBuilder();
        for(int i = startPosition;i<characters.size();i++){
            if (Character.isDigit(characters.get(i)) || characters.get(i)=='.'){
                builder.append(characters.get(i));
            }
            else{
                endPosition = i-1;
                break;
            }
        }
        try {
            double value = Double.parseDouble(String.valueOf(builder));

        } catch (NumberFormatException e) {
            errors.add("Константа задана не вірно. Позиція - " + startPosition);
            for(int j = startPosition;j<=endPosition;j++){
                errorPositions.add(j);
            }
        }
//        if(!Pattern.matches(pattern, builder)){
//            errors.add("Константа задана не вірно. Позиція - " + startPosition);
//            for(int j = startPosition;j<=endPosition;j++){
//                errorPositions.add(j);
//            }
//        }
        return endPosition;
    }

    private int isFunction(List<Character> characters, int startPosition){
        //check list of characters is function name
        String functionPattern = "^[a-z_][a-zA-Z0-9_]*\\([a-zA-Z_][a-zA-Z0-9_]*\\)$";
        int localBrackets = 0;
        String namePattern = "^[a-z_][a-zA-Z0-9_]*$";
        String variablePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        int startBodyPosition = startPosition;
        int endPosition = startPosition;
        StringBuilder functionName = new StringBuilder();
        StringBuilder functionBody = new StringBuilder();
        for(int i = startPosition;i<characters.size();i++){
            if(characters.get(i)!='('){
                functionName.append(characters.get(i));

            }
            else if(characters.get(i)=='('){
                startBodyPosition = i;
                break;
            }
        }
//        for(int i = startBodyPosition;i<characters.size();i++){
//            if(characters.get(i)!=')' && localBrackets!=0){
//                functionBody.append(characters.get(i));
//            }
//            else if(characters.get(i)==')'){
//                localBrackets+=1;
//            }
//            else if(characters.get(i)==')' && localBrackets!=0){
//                localBrackets-=1;
//                functionBody.append(characters.get(i));
//            }
//            else{
//                functionBody.append(characters.get(i));
//                endPosition = i;
//                break;
//            }
//        }
        if(!Pattern.matches(namePattern, functionName)){
            errors.add("Ім'я функції задано не вірно. Позиція початку - " + startPosition);
            errorPositions.add(startPosition);

        }
        endPosition = checkBody(characters, startBodyPosition, localBrackets);
        return endPosition;
    }

    public boolean isFunctionOrVariable(List<Character> characters, int startPosition){
        boolean isFunc = false;
        for(int i = startPosition;i<characters.size();i++){
            if(characters.get(i+1)=='(' && !isOperator(characters.get(i+1)) && i+1!=characters.size()){
                isFunc = true;
                break;
            }
            else if(characters.get(i+1)==')' || isOperator(characters.get(i+1)) || characters.get(i+1)==','){
                break;
            }

        }
        return isFunc;
    }

    public int checkBody(List<Character> characters, int startBodyPosition, int localBrackets){
        StringBuilder builder = new StringBuilder();
        String variablePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        int end = startBodyPosition;
        for(int i=startBodyPosition;i<characters.size();i++){
            if (characters.get(i) == '(') {
                if(i+1!=characters.size() && isOperator(characters.get(i+1))){
                    errors.add("Операція " + characters.get(i+1) + " після відкритої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(i!=startBodyPosition){
                    end = checkBody(characters, i, 0);
                    i = end;
                }
//                else if(i-1!=-1 && !isOperator(characters.get(i-1))){
//                    if(characters.get(i-1)=='-'){
//                        continue;
//                    }
//                    else {
//                        errors.add("Перед '(' не має знаку операції. Позиція - " + (i + 1));
//                        errorPositions.add(i);
//                    }
//                }
                else{
                    localBrackets += 1;
                }
            }
            else if (characters.get(i) == ')') {
                if(i==0){
                    errors.add("Душка ')' на початку виразу. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(localBrackets == 0){
                    errors.add("Душка ')' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(characters.get(i-1)=='('){
                    errors.add("Порожні душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(isOperator(characters.get(i-1))){
                    errors.add("Операція " + characters.get(i-1) + " перед ')'. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                else if(i+2<characters.size() && !isOperator(characters.get(i+1)) && !isOperator(characters.get(i+2))){
                    errors.add("Після ')' не має операції. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
                localBrackets -=1;
                if(localBrackets!=0){
                    errors.add("Відкриваюча душка не має закриваючої душки" + characters.get(startBodyPosition-1));
                    errorPositions.add(startBodyPosition);
                }
                end = i;
                return end;
            }
            else if(characters.get(i)==' '){
                if(i+1!=characters.size() && characters.get(i+1)==' '){
                    errors.add("Забагато пробілів. Позиція - " + (i+1));
                    errorPositions.add(i);
                    errorPositions.add(i+1);
                }
            }
            else if(characters.get(i)==';'){
                errors.add("Символ ';' в не правильному місці. Позиція - " + (i+1));
                errorPositions.add(i);
            }
            else if(characters.get(i)=='{'){
                figureBrackets+=1;
            }
            else if(characters.get(i)=='}'){
                if(figureBrackets!=0){
                    figureBrackets-=1;
                }
                else {
                    errors.add("Душка '}' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
            }
            else if(characters.get(i)=='['){
                squareBrackets+=1;
            }
            else if(characters.get(i)==']'){
                if(squareBrackets!=0){
                    squareBrackets-=1;
                }
                else {
                    errors.add("Душка ']' не має відкриваючої душки. Позиція - " + (i+1));
                    errorPositions.add(i);
                }
            }
            else if(isSpecialCharacter(characters.get(i))){
                errors.add("В тілі функції помилка. Символ - " + characters.get(i));
                errorPositions.add(i);
            }
            else if (isOperator(characters.get(i))) {
                if(i==0){
                    if(characters.get(i)=='-' && isFunctionOrVariable(characters, i+1)) continue;
                    else{
                        errors.add("Знак " + characters.get(i) + " на початку виразу. Позиція - " + (i+1));
                        errorPositions.add(i);
                    }

                }
                else if(i==characters.size()-1){
                    errors.add("Знак " + characters.get(i) + " на кінці виразу. Позиція - " + characters.get(i));
                    errorPositions.add(i);
                }
                else if (isOperator(characters.get(i+1))) {
                    errors.add("Подвійні операції " + characters.get(i) + " та " + characters.get(i+1)+ ". Позиція - " + (i+1) + " та "+ (i+2));
                    errorPositions.add(i);
                    errorPositions.add(i+1);
                }

            }
            else if (Character.isDigit(characters.get(i))){
                int nextI = isConstant(characters, i);
                i = nextI;
            }
            else if(Character.isAlphabetic(characters.get(i))){
                if(isFunctionOrVariable(characters, i)){
                    int nextI =isFunction(characters, i);
                    i = nextI;
                }
                else {
                    int nextI = isVariable(characters, i);
                    i = nextI;
                }
            }
        }
        return end;
    }


    public void printCorrectExpression(String expression, List<Integer> errors){
        List<Character> arr = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            arr.add(c);
        }
        for(int i = 0; i<arr.size();i++){
            if(errors.contains(i)){
                System.out.print(ANSI_RED_BACKGROUND
                        + arr.get(i)
                        + ANSI_RESET);
            }
            else {
                System.out.print(arr.get(i));
            }
        }
    }
    public boolean isSpecialCharacter(char c){
        return c == '&' || c == '|' || c == '%' || c == '$' || c=='#' ||
                c == '!' || c == '@' || c == '?' || c == '{' || c=='}' || c==';';
    }
    public boolean isDouble(String str){
        try {
            double value = Double.parseDouble(str);


        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public int amount(String str){
        int number = 0;
        for(char r: str.toCharArray()){
            if(r=='('){
                number+=1;
            }
            else if(r==')'){
                number-=1;
            }
        }
        return number;
    }

    public List<Integer> getErrorPositions() {
        return errorPositions;
    }
}
