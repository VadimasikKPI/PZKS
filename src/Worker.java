import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Worker {
    public List<String> generateComutations(String expression) {
        List<String> comutations = new ArrayList<>();
        comutations.add(expression);
        List<String> terms = findTerms(expression);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            for (int j = i + 1; j < terms.size(); j++) {
                for (int k = 0; k < terms.size(); k++) {
                    if (k == i) {
                        if (k == 0 && !containsOperator(terms.get(j)))
                            sb.append(terms.get(j));
                        else if (!containsOperator(terms.get(j))) {
                            sb.append("+");
                            sb.append(terms.get(j));
                        } else if (k == 0 && containsOperator(terms.get(j))) {
                            sb.append(terms.get(j).substring(1));
                        } else
                            sb.append(terms.get(j));

                    } else if (k == j) {
                        if (k == 0 && !containsOperator(terms.get(i)))
                            sb.append(terms.get(i));
                        else if (!containsOperator(terms.get(i))) {
                            sb.append("+");
                            sb.append(terms.get(i));
                        } else
                            sb.append(terms.get(i));

                    } else {
                        sb.append(terms.get(k));
                    }
                }
                comutations.add(sb.toString());
                sb.setLength(0);
            }
        }
        return comutations;
    }

    public String generateComutations1(String expression) {
        String expAfterPreprocessing = preprocess(expression);
        List<String> terms = findTerms(expAfterPreprocessing);
        StringBuilder sb = new StringBuilder();
        Collections.sort(terms, (a, b) -> Integer.compare(a.length(), b.length()));
        int number = 0;
        for (String term : terms) {
            if (term.contains("(") && term.contains(")")) {
                int startBracket = findOpenBracket(term);
                int endBracket = findClosingBracket(term, startBracket);
                sb.append(term.substring(0, startBracket) + "(");
                sb.append(generateComutations1(term.substring(startBracket + 1, endBracket)));
                sb.append(")");
                if(term.length()>endBracket+1){
                    sb.append(term.substring(endBracket+1));
                }
            } else {
                if (number == 0 && term.charAt(0) == '+') {
                    sb.append(term.substring(1));
                } else if (number != 0 && Character.isLetterOrDigit(term.charAt(0))) {
                    sb.append("+");
                    sb.append(term);
                } else
                    sb.append(term);
            }
            number++;
        }
        return sb.toString();
    }

    public String preprocess(String exp) {
        StringBuilder sb = new StringBuilder();
        StringBuilder expression = new StringBuilder();
        expression.append(exp);
        for(int i = 0; i<expression.length();i++){
            if(expression.charAt(i)=='('){
                int end = findClosingBracket(expression.toString(), i);
                if(expression.substring(i+1, end).contains("(")){
                    String temp = preprocess(expression.substring(i + 1, end));
                    if(temp.equals(expression.substring(i + 1, end)) ){
                        sb.append(expression.substring(i+1, end));
                        i = end;
                        //break;
                    }
                    else{
                        expression.replace(i + 1, end, temp);
                        i=i-1;
                    }
//                    expression.replace(i+1, end, preprocess(expression.substring(i+1, end)));
//                    i = i-1;
                }
                else if(i>0){
                    char previousOperator = expression.charAt(i-1);
                    if(previousOperator=='+'){
                        for(int j = i+1;j<end;j++){
                            sb.append(expression.charAt(j));
                        }
                        i = end;
                    }
                    else if(previousOperator=='-'){
                        for(int j = i+1;j<end;j++){
                            if(expression.charAt(j)=='-'){
                                sb.append('+');
                            }
                            else if(expression.charAt(j)=='+'){
                                sb.append('-');
                            }
                            else{
                                sb.append(expression.charAt(j));
                            }
                        }
                        i = end;
                    }
                    else{
                        sb.append(expression.charAt(i));
                    }
                }
                else{
                    for(int j = i+1;j<end;j++){
                        sb.append(expression.charAt(j));
                    }
                    i = end;
                }
            }
            else{
                sb.append(expression.charAt(i));
            }
        }
        return sb.toString();
    }

    public int findOpenBracket(String term) {
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '(') {
                return i;
            }
        }
        return -1;
    }

    public int findVar(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                return i;
            }
        }
        return -1;
    }

    public List<String> findTerms(String expression) {
        List<String> terms = new ArrayList<>();
        int i = 0;
        while (i < expression.length()) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                terms.add(expression.substring(0, i));
                expression = expression.substring(i);
                i = 0;
            } else if (expression.charAt(i) == '(') {
                int closingIndex = findClosingBracket(expression, i);
                if (closingIndex + 1 < expression.length() && (expression.charAt(closingIndex + 1) == '/' || expression.charAt(closingIndex + 1) == '*')) {
                    int temp = closingIndex;
                    for (int l = closingIndex; l < expression.length(); l++) {
                        if (expression.charAt(l) == '+' || expression.charAt(l) == '-') {
                            l = closingIndex;
                            break;
                        }
                    }
                    closingIndex = expression.length() - 1;

                }
                terms.add(expression.substring(0, closingIndex + 1));
                expression = expression.substring(closingIndex + 1);
                i = 0;
            } else if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                int j = i + 1;
                if (expression.charAt(j) == '(') {
                    j = findClosingBracket(expression, j);

                }
                while (j < expression.length() && expression.charAt(j) != '+' && expression.charAt(j) != '-') {
                    if (expression.charAt(j) == '(') {
                        j = findClosingBracket(expression, j);

                    }else{
                        j++;
                    }
                    //j++;
                }
                terms.add(expression.substring(0, j));
                expression = expression.substring(j);
                i = 0;
            } else if (expression.charAt(i) == '^') {
                int j = i + 1;
                while (j < expression.length() && expression.charAt(j) != '+' && expression.charAt(j) != '-' && expression.charAt(j) != '*' && expression.charAt(j) != '/') {
                    j++;
                }
                terms.add(expression.substring(0, j));
                expression = expression.substring(j);
                i = 0;
            } else if (i == expression.length() - 1) {
                terms.add(expression);
                i++;
            }

            i++;

        }
        return terms;
    }

    public String postProcess(String distrib){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<distrib.length();i++){

            if(isOperator(distrib.charAt(i)) && isOperator(distrib.charAt(i+1))){
                sb.append(distrib.charAt(i+1));
                i++;
            }
            else{
                if(i==0 && isOperator(distrib.charAt(i))){
                    if(distrib.charAt(i)=='-'){
                        sb.append(distrib.charAt(i));
                    }
                }
                else{
                    sb.append(distrib.charAt(i));
                }
                //sb.append(distrib.charAt(i));
            }
        }
        return sb.toString();
    }

    public String distrib(String test) {
        StringBuilder expression = new StringBuilder();
        expression.append(test);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                int end = findClosingBracket(expression.toString(), i);
                if (expression.substring(i + 1, end).contains("(")) {
                    String temp = distrib(expression.substring(i + 1, end));
                    if(temp.equals(expression.substring(i + 1, end)) ){
                        sb.append(expression.substring(i+1, end));
                        i = end;
                        //break;
                    }
                    else{
                        expression.replace(i + 1, end, temp);
                        i=i-1;
                    }

                    //System.out.println(temp);

                } else {
                    if (i != 0) {
                        char previousOperator = expression.charAt(i - 1);
                        if (end < expression.length() - 1) {
                            char nextOperator = expression.charAt(end + 1);
                            if (!isPriorityOperator(previousOperator) && !isPriorityOperator(nextOperator)) {//якщо минула і наступна не пріоритетні
                                if (previousOperator == '-') {
                                    for (int j = i + 1; j < end; j++) {
                                        if (Character.isLetterOrDigit(expression.charAt(j))) {
                                            if (j == i + 1) {
                                                sb.append('-');
                                            }
                                            sb.append(expression.charAt(j));
                                        } else if (isOperator(expression.charAt(j))) {
                                            if (expression.charAt(j) == '-') {
                                                sb.append('+');
                                            } else if (expression.charAt(j) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(expression.charAt(j));
                                            }
                                        }
                                    }
                                } else {
                                    sb.append(previousOperator);
                                    for (int j = i + 1; j < end; j++) {
                                        sb.append(expression.charAt(j));
                                    }
                                }
                                i = end + 1;
                            } else if (!isPriorityOperator(previousOperator) && isPriorityOperator(nextOperator)) {
                                if (nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        sb.append(makeMultiply(first, second));
                                        i = end1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), end + 2));
                                        sb.append(makeMultiply(first, second));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        String second = expression.substring(end + 2, end1 + 1);
                                        sb.append(makeDivision(first, second));
                                        i = end1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        String second = findNextVariable(expression.toString(), end + 2);
                                        sb.append(makeDivision(first, second));
                                        i = end + 2 + second.length();
                                    }
                                }
                            } else if (isPriorityOperator(previousOperator) && isPriorityOperator(nextOperator)) {
                                if (previousOperator == '*' && nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        //це місце де може бути !!!!!!!!!!!
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), i + 2));
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (previousOperator == '*' && nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = expression.substring(end + 2, end1 + 1);
                                        String temp = makeDivision(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = findNextVariable(expression.toString(), i + 2);
                                        String temp = makeDivision(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        i = end + 2 + second.length();
                                    }
                                } else if (previousOperator == '/' && nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        sb.append(makeDivisionVariable(var, third));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), i + 2));
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        sb.append(makeDivisionVariable(var, third));
                                        i = end + 2 + second.get(0).length();
                                    }
                                }
                                //тут два ділення підряд але я хз чи це правльно
                            }
                        } else {
                            if (!isPriorityOperator(previousOperator)) {
                                sb.append(previousOperator);
                                if (previousOperator == '-') {
                                    for (int j = i + 1; j < end; j++) {
                                        if (Character.isLetterOrDigit(expression.charAt(j))) {
                                            sb.append(expression.charAt(j));
                                        } else if (isOperator(expression.charAt(j))) {
                                            if (expression.charAt(j) == '-') {
                                                sb.append('+');
                                            } else if (expression.charAt(j) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(expression.charAt(j));
                                            }
                                        }
                                        i = end ;
                                    }
                                } else {
                                    for (int j = i + 1; j < end; j++) {
                                        sb.append(expression.charAt(j));
                                    }
                                    i = end;
                                }
                            } else if (isPriorityOperator(previousOperator)) {
                                if (previousOperator == '*') {
                                    List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                    List<String> second = new ArrayList<>();
                                    second.add(findPreviousVariable(expression.toString(), i - 2));
                                    sb.append(makeMultiply(first, second));
                                    i = end + 1;
                                } else if (previousOperator == '/') {
                                    String first = expression.substring(i, end);
                                    String second = findPreviousVariable(expression.toString(), i - 2);
                                    sb.append(second);
                                    sb.append('/');
                                    sb.append(first);
                                    i = end + 1;

                                }
                            }
                        }
                    } else {//якщо i=0
                        if (end + 1 < expression.length()) {
                            char nextOperator = expression.charAt(end + 1);
                            if (isPriorityOperator(nextOperator)) {
                                if (nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        sb.append(makeMultiply(first, second));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), end + 2));
                                        sb.append(makeMultiply(first, second));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = expression.substring(end + 2, end1 + 1);
                                        sb.append(makeDivision(first, second));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = findNextVariable(expression.toString(), end + 2);
                                        sb.append(makeDivision(first, second));
                                        i = end + 2 + second.length();
                                    }
                                }
                            }
                        } else {
                            for (int j = i + 1; j < end; j++) {
                                sb.append(expression.charAt(j));
                            }
                            i = end;
                        }
                    }
                }
            } else if (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.') {
                if(isFunc(expression.toString(), i)){
                    int endOfVariable = findEndOfVariable(expression.toString(), i);
                    if(endOfVariable>=expression.length()){

                        if (i == 0) {

                        } else {
                            String operator = findOperatorForVariable(expression.toString(), i - 1);
                            sb.append(operator);

                        }
                        for(int j = i;j<expression.length();j++){
                            sb.append(expression.charAt(j));
                        }
                        i = expression.length()-1;
                    }

                    //break;
                }
                else {
                    String variable = findNextVariable(expression.toString(), i);
                    int endOfVariable = findEndOfVariable(variable, i);
                    if (isNextMultiplyOrDivideAndBrackets(expression.toString(), endOfVariable)) {
                        if (expression.charAt(endOfVariable) == '*') {
                            int end = findClosingBracket(expression.toString(), endOfVariable + 2);
                            if (expression.substring(endOfVariable + 2, end).contains("(")) {
                                String temp = distrib(expression.substring(endOfVariable + 2, end));
                                expression.replace(endOfVariable + 2, end, temp);
                                i = endOfVariable - 1;
                                //System.out.println(temp);

                            } else {
                                List<String> first = findVariablesAndOperations(expression.toString(), endOfVariable + 2, end);
                                List<String> second = new ArrayList<>();
                                if (i == 0) {
                                    variable = "+" + variable;
                                } else {
                                    variable = findOperatorForVariable(expression.toString(), i - 1) + variable;
                                }
                                second.add(variable);
                                sb.append(makeMultiply(first, second));
                                i = end;
                            }
//                        List<String> first = findVariablesAndOperations(expression.toString(), endOfVariable + 2, end);
//                        List<String> second = new ArrayList<>();
//                        if (i == 0) {
//
//                        } else {
//                            variable = findOperatorForVariable(expression.toString(), i - 1) + variable;
//                        }
//                        second.add(variable);
//                        sb.append(makeMultiply(first, second));
//                        i = end;
                        } else if (expression.charAt(endOfVariable) == '/') {
                            int end = findClosingBracket(expression.toString(), endOfVariable + 2);
                            if (expression.substring(endOfVariable + 2, end).contains("(")) {
                                String temp = distrib(expression.substring(endOfVariable + 2, end));
                                expression.replace(endOfVariable + 2, end, temp);
                                i = endOfVariable - 1;
                                //System.out.println(temp);

                            } else {
                                String first = expression.substring(endOfVariable + 2, end);
                                String second = variable;
                                sb.append(makeDivisionVariableBrackets(second, first));
                                i = end;
                            }
//                        String first = expression.substring(endOfVariable + 2, end);
//                        String second = variable;
//                        sb.append(makeDivisionVariableBrackets(second, first));
//                        i = end;
                        }
                    } else {
                        if (i == 0) {

                        } else {
                            String operator = findOperatorForVariable(expression.toString(), i - 1);
                            if(operator.equals("null")){
                                sb.append("+");
                            }
                            else {
                                variable = operator + variable;
                            }

                        }
                        sb.append(variable);
                        i = endOfVariable;

                    }
                }
            } else if (isOperator(expression.charAt(i))) {
            } else if(expression.charAt(i)==')'){

            }
            else {
                sb.append(expression.charAt(i));

            }
        }
        return sb.toString();
    }

    public boolean containsFunction(String substring) {
        for (int i = 0; i < substring.length(); i++) {
            if (substring.charAt(i) == '(' && isFunction(substring.toCharArray(), i)) {
                return true;
            }
        }
        return false;
    }

    public String makeDivisionVariableBrackets(String first, String second){
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        sb.append('(');
        sb.append(second);
        sb.append(')');
        return sb.toString();
    }
    public int findEndOfFunction(char[] charArray, int i) {
        int numberOfOpens = 1;
        for (int j = i; j < charArray.length; j++) {
            if (charArray[j] == '(') {
                numberOfOpens++;
            } else if (charArray[j] == ')') {
                numberOfOpens--;
                if (numberOfOpens == 0) {
                    return j + 1;
                }
            }
        }
        return -1;
    }


    public boolean isFunction(char[] charArray, int i) {
        boolean isFunction = false;
        while (i >= 0) {
            if (Character.isLetterOrDigit(charArray[i])) {
                i++;
            } else {
                if (charArray[i] == '(') {
                    isFunction = true;
                    break;
                } else {
                    isFunction = false;
                    break;
                }

            }
        }
        return isFunction;
    }


    public String findOperatorForVariable(String expression, int start) {
        for (int i = start; i > 0; i--) {
            if (isOperator(expression.charAt(i))) {
                return String.valueOf(expression.charAt(i));
            }
        }
        return "+";
    }

    public int findClosingBracket(String expr, int startIndex) {
        int count = 1;
        for (int i = startIndex + 1; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                count++;
            } else if (expr.charAt(i) == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int findEndOfVariable(String s, int start) {
        return start + s.length() - 1;
    }

    public boolean isNextMultiplyOrDivideAndBrackets(String expression, int start) {
        for (int i = start; i < expression.length(); i++) {
            if (isOperator(expression.charAt(i))) {
                if ((expression.charAt(i) == '*' || expression.charAt(i) == '/') && expression.charAt(i + 1) == '(')
                    return true;
                else return false;
            }

        }
        return false;
    }

    public List<String> findVariablesAndOperations(String expression, int start, int end) {
        List<String> variablesAndOperations = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; ) {
            if (Character.isLetterOrDigit(expression.charAt(i))) {
                sb.append('+');
                while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                    sb.append(expression.charAt(i));
                    i++;
                }
                variablesAndOperations.add(sb.toString());
                sb.setLength(0);

            } else if (isOperator(expression.charAt(i))) {
                if (expression.charAt(i) == '-') {
                    //variablesAndOperations.add(sb.toString());
                    //sb.setLength(0);
                    sb.append(expression.charAt(i));
                    i++;
                    while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);
                } else if (expression.charAt(i) == '+') {
                    //variablesAndOperations.add(sb.toString());
                    //sb.setLength(0);
                    sb.append(expression.charAt(i));
                    i++;
                    while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);

                }
//                else if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
//                    sb.append(expression.charAt(i));
//                    i++;
//                    if (expression.charAt(i) == '(') {
//                        int end1 = findClosingBracket(expression.toCharArray(), i);
//                        for (int j = i + 1; j < end1 - 1; j++) {
//                            if (Character.isLetterOrDigit(expression.charAt(j))) {
//                                sb.append(expression.charAt(j));
//                            } else if (isOperator(expression.charAt(j))) {
//                                if (expression.charAt(j) == '-') {
//                                    sb.append('+');
//                                } else if (expression.charAt(j) == '+') {
//                                    sb.append('-');
//                                } else {
//                                    sb.append(expression.charAt(j));
//                                }
//                            }
//                        }
//                        i = end1;
//                        variablesAndOperations.add(sb.toString());
//                        sb.setLength(0);
//                    } else if (Character.isLetterOrDigit(expression.charAt(i))) {
//                        while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
//                            sb.append(expression.charAt(i));
//                            i++;
//                        }
//                        variablesAndOperations.add(sb.toString());
//                        sb.setLength(0);
//                    }
//                }

            } else {
                i++;
            }
        }
        //variablesAndOperations.add(sb.toString());
        return variablesAndOperations;
    }

    public List<String> findVariableInBracketsWhenVariablePreviously(String expression, int start, int end) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (isOperator(expression.charAt(i))) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(expression.charAt(i));
            }
        }
        result.add(sb.toString());
        return result;
    }

    public String makeDivisionVariable(String var, List<String> second) {
        StringBuilder sb = new StringBuilder();
        for (String t : second) {
            sb.append(var);
            sb.append("/");
            sb.append(t);
        }
        return sb.toString();
    }

    public List<String> findAllTermsWithBrackets(String expression) {
        List<String> termsWithBrackets = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                int closingIndex = findClosingBracket(expression, i);
                termsWithBrackets.add(expression.substring(i, closingIndex + 1));
            }
        }
        return termsWithBrackets;
    }


    public String findPreviousVariable(String expression, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i >= 0; i--) {
            if (Character.isLetterOrDigit(expression.charAt(i)) || isOperator(expression.charAt(i))) {
                sb.append(expression.charAt(i));
            } else {
                break;
            }
        }
        return sb.reverse().toString();
    }

    public String findNextVariable(String expression, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < expression.length(); i++) {
            if (Character.isLetterOrDigit(expression.charAt(i)) || isPriorityOperator(expression.charAt(i)) || expression.charAt(i)=='.') {
                sb.append(expression.charAt(i));
            } else {
                break;
            }
        }
        return sb.toString();
    }

    public String makeMultiply(List<String> first, List<String> second) {
        StringBuilder sb = new StringBuilder();
        for (String t : first) {
            for (String t1 : second) {
                if (t.contains("-") && t1.contains("-")) {
                    sb.append("+");
                    sb.append(t1.substring(1));
                    sb.append("*");
                    sb.append(t.substring(1));
                } else if (t.contains("-") && !t1.contains("-")) {
                    sb.append('-');
                    sb.append(t1);
                    sb.append("*");
                    sb.append(t.substring(1));
                } else if (!t.contains("-") && t1.contains("-")) {
                    sb.append("-");
                    sb.append(t1.substring(1));
                    sb.append("*");
                    sb.append(t.substring(1));
                } else if (t.contains("/")) {
                    sb.append("+");
                    sb.append(t1);
                    sb.append("*");
                    sb.append(t);
                } else if (t1.contains("/")) {
                    sb.append("+");
                    sb.append(t);
                    sb.append("*");
                    sb.append(t1);
                } else {
                    sb.append("+");
                    sb.append(t1);
                    sb.append("*");
                    sb.append(t);
                }

            }
        }
        String temp = sb.toString();
        while (hasDoubleOperations(temp)){
            temp = replace(temp);
        }
        return temp;
    }

    public String replace(String s){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '*' && s.charAt(i + 1) == '+') {
                sb.append(s.charAt(i));
                i += 1;
            } else if (s.charAt(i) == '-' && s.charAt(i + 1) == '+') {
                sb.append(s.charAt(i));
                i += 1;
            } else if (s.charAt(i) == '*' && s.charAt(i + 1) == '*') {
                sb.append(s.charAt(i));
                i += 1;
            }
            else if(isOperator(s.charAt(i)) && s.charAt(i) == s.charAt(i+1)){
                sb.append(s.charAt(i));
                i += 1;
            }
//            else if (i == 0 && s.charAt(i) == '+') {
//                continue;
//            }
            else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
    public boolean hasDoubleOperations(String s){
        boolean hasDoubleOperations = false;
        for(int i =0; i<s.length();i++){
            if(isOperator(s.charAt(i))){
                if(i+1<s.length() && isOperator(s.charAt(i+1))){
                    hasDoubleOperations = true;
                    break;
                }
            }
        }
        return hasDoubleOperations;
    }

    public String makeDivision(List<String> first, String second) {
        StringBuilder sb = new StringBuilder();
        for (String t : first) {
            sb.append(t);
            sb.append("/");
            sb.append(second);
        }
        return sb.toString();
    }



    public boolean containsOperator(String term) {
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '+' || term.charAt(i) == '-' || term.charAt(i) == '*' || term.charAt(i) == '/' || term.charAt(i) == '^') {
                return true;
            }
        }
        return false;
    }

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public boolean isPriorityOperator(char c) {
        return c == '*' || c == '/' || c == '^';
    }

    public List<String> distribWithSteps(String test) {
        List<String> result = new ArrayList<>();
        StringBuilder expression = new StringBuilder();
        expression.append(test);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                int end = findClosingBracket(expression.toString(), i);
                if (expression.substring(i + 1, end).contains("(")) {
                    String temp = distrib(expression.substring(i + 1, end));
                    if(temp.equals(expression.substring(i + 1, end))){
                        sb.append(expression.substring(i+1, end));
                        result.add(makeStep(sb.toString(), expression.toString(), end));
                        i = end;
                        break;
                    }
                    expression.replace(i + 1, end, temp);
                    result.add(makeStep(sb.toString(), expression.toString(), end));
                    i=i-1;
                    //System.out.println(temp);

                } else {
                    if (i != 0) {
                        char previousOperator = expression.charAt(i - 1);
                        if (end < expression.length() - 1) {
                            char nextOperator = expression.charAt(end + 1);
                            if (!isPriorityOperator(previousOperator) && !isPriorityOperator(nextOperator)) {//якщо минула і наступна не пріоритетні
                                if (previousOperator == '-') {
                                    for (int j = i + 1; j < end; j++) {
                                        if (Character.isLetterOrDigit(expression.charAt(j))) {
                                            if (j == i + 1) {
                                                sb.append('-');
                                            }
                                            sb.append(expression.charAt(j));
                                        } else if (isOperator(expression.charAt(j))) {
                                            if (expression.charAt(j) == '-') {
                                                sb.append('+');
                                            } else if (expression.charAt(j) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(expression.charAt(j));
                                            }
                                        }
                                    }
                                } else {
                                    for (int j = i + 1; j < end; j++) {
                                        sb.append(expression.charAt(j));
                                    }
                                }
                                result.add(makeStep(sb.toString(), expression.toString(), end + 1));
                                i = end + 1;
                            } else if (!isPriorityOperator(previousOperator) && isPriorityOperator(nextOperator)) {
                                if (nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        sb.append(makeMultiply(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1));
                                        i = end1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), end + 2));
                                        sb.append(makeMultiply(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.get(0).length()));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        String second = expression.substring(end + 2, end1 + 1);
                                        sb.append(makeDivision(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1));
                                        i = end1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        if (previousOperator == '-') {
                                            for (int l = 0; l < first.size(); l++) {
                                                if (first.get(0).charAt(0) == '+') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("-" + temp.substring(1));
                                                } else if (first.get(0).charAt(0) == '-') {
                                                    String temp = first.get(0);
                                                    first.remove(0);
                                                    first.add("+" + temp.substring(1));
                                                }
                                            }
                                        }
                                        String second = findNextVariable(expression.toString(), end + 2);
                                        sb.append(makeDivision(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.length()));
                                        i = end + 2 + second.length();
                                    }
                                }
                            } else if (isPriorityOperator(previousOperator) && isPriorityOperator(nextOperator)) {
                                if (previousOperator == '*' && nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        //це місце де може бути !!!!!!!!!!!
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1 + 1));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), i + 2));
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.get(0).length()));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (previousOperator == '*' && nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = expression.substring(end + 2, end1 + 1);
                                        String temp = makeDivision(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1 + 1));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = findNextVariable(expression.toString(), i + 2);
                                        String temp = makeDivision(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        List<String> fourth = new ArrayList<>();
                                        fourth.add(var);
                                        sb.append(makeMultiply(third, fourth));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.length()));
                                        i = end + 2 + second.length();
                                    }
                                } else if (previousOperator == '/' && nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        sb.append(makeDivisionVariable(var, third));
                                        result.add(makeStep(sb.toString(), expression.toString(),  end1 + 1));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), i + 2));
                                        String temp = makeMultiply(first, second);
                                        String var = findPreviousVariable(expression.toString(), i - 2);
                                        List<String> third = findVariablesAndOperations(temp, 0, temp.length());
                                        sb.append(makeDivisionVariable(var, third));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.get(0).length()));
                                        i = end + 2 + second.get(0).length();
                                    }
                                }
                                //тут два ділення підряд але я хз чи це правльно
                            }
                        } else {
                            if (!isPriorityOperator(previousOperator)) {
                                sb.append(previousOperator);
                                if (previousOperator == '-') {
                                    for (int j = i + 1; j < end; j++) {
                                        if (Character.isLetterOrDigit(expression.charAt(j))) {
                                            sb.append(expression.charAt(j));
                                        } else if (isOperator(expression.charAt(j))) {
                                            if (expression.charAt(j) == '-') {
                                                sb.append('+');
                                            } else if (expression.charAt(j) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(expression.charAt(j));
                                            }
                                        }
                                        result.add(makeStep(sb.toString(), expression.toString(), end));
                                        i = end ;
                                    }
                                } else {
                                    for (int j = i + 1; j < end; j++) {
                                        sb.append(expression.charAt(j));
                                    }
                                    result.add(makeStep(sb.toString(), expression.toString(), end));
                                    i = end;
                                }
                            } else if (isPriorityOperator(previousOperator)) {
                                if (previousOperator == '*') {
                                    List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                    List<String> second = new ArrayList<>();
                                    second.add(findPreviousVariable(expression.toString(), i - 2));
                                    sb.append(makeMultiply(first, second));
                                    result.add(makeStep(sb.toString(), expression.toString(), end + 1));
                                    i = end + 1;
                                } else if (previousOperator == '/') {
                                    String first = expression.substring(i, end);
                                    String second = findPreviousVariable(expression.toString(), i - 2);
                                    sb.append(second);
                                    sb.append('/');
                                    sb.append(first);
                                    result.add(makeStep(sb.toString(), expression.toString(), end + 1));
                                    i = end + 1;

                                }
                            }
                        }
                    } else {//якщо i=0
                        if (end + 1 < expression.length()) {
                            char nextOperator = expression.charAt(end + 1);
                            if (isPriorityOperator(nextOperator)) {
                                if (nextOperator == '*') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = findVariablesAndOperations(expression.toString(), end + 2, end1);
                                        sb.append(makeMultiply(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1 + 1));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        List<String> second = new ArrayList<>();
                                        second.add(findNextVariable(expression.toString(), end + 2));
                                        sb.append(makeMultiply(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.get(0).length()));
                                        i = end + 2 + second.get(0).length();
                                    }
                                } else if (nextOperator == '/') {
                                    if (end + 2 < expression.length() && expression.charAt(end + 2) == '(') {
                                        int end1 = findClosingBracket(expression.toString(), end + 2);
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = expression.substring(end + 2, end1 + 1);
                                        sb.append(makeDivision(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end1 + 1));
                                        i = end1 + 1;
                                    } else {
                                        List<String> first = findVariablesAndOperations(expression.toString(), i, end);
                                        String second = findNextVariable(expression.toString(), end + 2);
                                        sb.append(makeDivision(first, second));
                                        result.add(makeStep(sb.toString(), expression.toString(), end + 2 + second.length()));
                                        i = end + 2 + second.length();
                                    }
                                }
                            }
                        } else {
                            for (int j = i + 1; j < end; j++) {
                                sb.append(expression.charAt(j));
                            }
                            result.add(makeStep(sb.toString(), expression.toString(), end));
                            i = end;
                        }
                    }
                }
            } else if (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '.') {
                if(isFunc(expression.toString(), i)){
                    int endOfVariable = findEndOfVariable(expression.toString(), i);
                    if(endOfVariable>=expression.length()){

                        if (i == 0) {

                        } else {
                            String operator = findOperatorForVariable(expression.toString(), i - 1);
                            sb.append(operator);

                        }
                        for(int j = i;j<expression.length();j++){
                            sb.append(expression.charAt(j));
                        }
                        i = expression.length()-1;
                    }

                    //break;
                }
                else{


                String variable = findNextVariable(expression.toString(), i);
                int endOfVariable = findEndOfVariable(variable, i);
                if (isNextMultiplyOrDivideAndBrackets(expression.toString(), endOfVariable)) {
                    if (expression.charAt(endOfVariable) == '*') {
                        int end = findClosingBracket(expression.toString(), endOfVariable + 2);
                        if (expression.substring(endOfVariable + 2, end).contains("(")) {
                            String temp = distrib(expression.substring(endOfVariable + 2, end));
                            expression.replace(endOfVariable + 2, end, temp);
                            result.add(expression.toString());
                            i=endOfVariable-1;
                            //System.out.println(temp);

                        } else {
                            List<String> first = findVariablesAndOperations(expression.toString(), endOfVariable + 2, end);
                            List<String> second = new ArrayList<>();
                            if (i == 0) {
                                variable = "+" + variable;
                            } else {
                                variable = findOperatorForVariable(expression.toString(), i - 1) + variable;
                            }
                            second.add(variable);
                            sb.append(makeMultiply(first, second));
                            result.add(makeStep(sb.toString(), expression.toString(), end));
                            i = end;
                        }
//                        List<String> first = findVariablesAndOperations(expression.toString(), endOfVariable + 2, end);
//                        List<String> second = new ArrayList<>();
//                        if (i == 0) {
//
//                        } else {
//                            variable = findOperatorForVariable(expression.toString(), i - 1) + variable;
//                        }
//                        second.add(variable);
//                        sb.append(makeMultiply(first, second));
//                        i = end;
                    } else if (expression.charAt(endOfVariable) == '/') {
                        int end = findClosingBracket(expression.toString(), endOfVariable + 2);
                        if (expression.substring(endOfVariable + 2, end).contains("(")) {
                            String temp = distrib(expression.substring(endOfVariable + 2, end));
                            expression.replace(endOfVariable + 2, end, temp);
                            result.add(makeStep(sb.toString(), expression.toString(), endOfVariable-1));
                            i=endOfVariable-1;
                            //System.out.println(temp);

                        } else {
                            String first = expression.substring(endOfVariable + 2, end);
                            String second = variable;
                            sb.append(makeDivisionVariableBrackets(second, first));
                            result.add(makeStep(sb.toString(), expression.toString(), end));
                            i = end;
                        }
//                        String first = expression.substring(endOfVariable + 2, end);
//                        String second = variable;
//                        sb.append(makeDivisionVariableBrackets(second, first));
//                        i = end;
                    }
                } else {
                    if (i == 0) {

                    } else {
                        variable = findOperatorForVariable(expression.toString(), i - 1) + variable;
                    }
                    sb.append(variable);
                    //result.add(makeStep(sb.toString(), expression.toString(), endOfVariable));
                    i = endOfVariable;

                }}
            } else if (isOperator(expression.charAt(i))) {
            } else {
                sb.append(expression.charAt(i));

            }
        }
        return result;
    }

    public String makeStep(String sb, String expression, int start){
        StringBuilder result = new StringBuilder();
        result.append(sb.toString());
        for(int i = start+1; i<expression.length();i++){
            result.append(expression.charAt(i));
        }
        return result.toString();
    }

    public boolean isFunc(String exp, int start){
        if(exp.length()==1){
            return false;
        }
        for(int i = start;i<exp.length();i++){
            if(exp.charAt(i)=='('){
                return true;
            }
            else if(isOperator(exp.charAt(i))){
                return false;
            }
        }
        return false;
    }

}
