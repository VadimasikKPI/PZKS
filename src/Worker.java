import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Worker {
    public List<String> generateComutations(String expression) {
        List<String> comutations = new ArrayList<>();
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

    public List<String> findTerms(String expression) {
        List<String> terms = new ArrayList<>();
        int i = 0;
        while (i < expression.length()) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                terms.add(expression.substring(0, i));
                expression = expression.substring(i);
                i = 0;
            } else if (expression.charAt(i) == '(') {
                int closingIndex = findClosingBracket(expression.toCharArray(), i);
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
                    j = findClosingBracket(expression.toCharArray(), j);

                }
                while (j < expression.length() && expression.charAt(j) != '+' && expression.charAt(j) != '-' && expression.charAt(j) != '*' && expression.charAt(j) != '/') {
                    j++;
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

    public List<String> generateDistributions(String expression) {
        List<String> distributions = new ArrayList<>();
        List<String> termsWithBrackets = findAllTermsWithBrackets(expression);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < termsWithBrackets.size(); j++) {
            int bracketCount = 1;
            int bracketNumber = 0;
            for (int k = 0; k < expression.length(); k++) {
                if (expression.charAt(k) == '(' && bracketCount > 0 && bracketNumber == j) {
                    bracketNumber++;
                    bracketCount--;
                    int end = findClosingBracket(expression.toCharArray(), k);
                    for (int l = k + 1; l <= end - 1; l++) {
                        if (k - 1 >= 0 && expression.charAt(k - 1) == '-') {
                            if (Character.isLetterOrDigit(expression.charAt(l))) {
                                sb.append(expression.charAt(l));
                            } else if (isOperator(expression.charAt(l))) {
                                if (expression.charAt(l) == '-') {
                                    sb.append('+');
                                } else if (expression.charAt(l) == '+') {
                                    sb.append('-');
                                } else {
                                    sb.append(expression.charAt(l));
                                }
                            }
                        } else if (expression.charAt(l) == '(' && bracketCount > 0) {
                            int end1 = findClosingBracket(expression.toCharArray(), l);
                            bracketCount--;
                            for (int m = l + 1; m <= end1 - 1; m++) {
                                if (Character.isLetterOrDigit(expression.charAt(m))) {
                                    sb.append(expression.charAt(l));
                                } else if (isOperator(expression.charAt(m))) {
                                    if (expression.charAt(m) == '-') {
                                        sb.append('+');
                                    } else if (expression.charAt(m) == '+') {
                                        sb.append('-');
                                    } else {
                                        sb.append(expression.charAt(m));
                                    }
                                } else {
                                    sb.append(expression.charAt(l));
                                }
                            }
                            l = end1;
                        } else {
                            sb.append(expression.charAt(l));
                        }
                    }
                    k = end;

                } else if (expression.charAt(k) == '(') {
                    sb.append(expression.charAt(k));
                    bracketNumber++;
                } else {
                    sb.append(expression.charAt(k));
                }
            }
            distributions.add(sb.toString());
            sb.setLength(0);
        }
        if (termsWithBrackets.size() > 2) {
            for (int i = 0; i < termsWithBrackets.size(); i++) {
                for (int j = i + 1; j < termsWithBrackets.size(); j++) {
                    for (int k = 0; k < expression.length(); k++) {
                        if (expression.charAt(k) == '(') {
                            int end = findClosingBracket(expression.toCharArray(), k);
                            if (expression.substring(k, end + 1).equals(termsWithBrackets.get(i)) || expression.substring(k, end + 1).equals(termsWithBrackets.get(j))) {
                                for (int l = k + 1; l <= end - 1; l++) {
                                    if (k - 1 >= 0 && expression.charAt(k - 1) == '-') {
                                        if (Character.isLetterOrDigit(expression.charAt(l))) {
                                            sb.append(expression.charAt(l));
                                        } else if (isOperator(expression.charAt(l))) {
                                            if (expression.charAt(l) == '-') {
                                                sb.append('+');
                                            } else if (expression.charAt(l) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(expression.charAt(l));
                                            }
                                        }
                                    } else if (expression.charAt(l) == '(') {
                                        int end1 = findClosingBracket(expression.toCharArray(), l);
                                        if (expression.substring(l, end1 + 1).equals(termsWithBrackets.get(i)) || expression.substring(l, end1 + 1).equals(termsWithBrackets.get(j))) {
                                            for (int m = l + 1; m <= end1 - 1; m++) {
                                                if (Character.isLetterOrDigit(expression.charAt(m))) {
                                                    sb.append(expression.charAt(l));
                                                } else if (isOperator(expression.charAt(m))) {
                                                    if (expression.charAt(m) == '-') {
                                                        sb.append('+');
                                                    } else if (expression.charAt(m) == '+') {
                                                        sb.append('-');
                                                    } else {
                                                        sb.append(expression.charAt(m));
                                                    }
                                                } else {
                                                    sb.append(expression.charAt(l));
                                                }
                                            }
                                            l = end1;
                                        } else {
                                            sb.append(expression.charAt(l));
                                        }
                                    } else {
                                        sb.append(expression.charAt(l));
                                    }
                                }
                                k = end;
                            } else {
                                sb.append(expression.charAt(k));
                            }
                        } else {
                            sb.append(expression.charAt(k));
                        }
                    }
                    distributions.add(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        if (termsWithBrackets.size() > 3) {
            for (int i = 0; i < termsWithBrackets.size(); i++) {
                for (int k = 0; k < expression.length(); k++) {
                    if (expression.charAt(k) == '(') {
                        int end = findClosingBracket(expression.toCharArray(), k);
                        if (!expression.substring(k, end + 1).equals(termsWithBrackets.get(i))) {
                            for (int l = k + 1; l <= end - 1; l++) {
                                if (k - 1 >= 0 && expression.charAt(k - 1) == '-') {
                                    if (Character.isLetterOrDigit(expression.charAt(l))) {
                                        sb.append(expression.charAt(l));
                                    } else if (isOperator(expression.charAt(l))) {
                                        if (expression.charAt(l) == '-') {
                                            sb.append('+');
                                        } else if (expression.charAt(l) == '+') {
                                            sb.append('-');
                                        } else {
                                            sb.append(expression.charAt(l));
                                        }
                                    }
                                } else if (expression.charAt(l) == '(') {
                                    int end1 = findClosingBracket(expression.toCharArray(), l);
                                    if (!expression.substring(l, end1 + 1).equals(termsWithBrackets.get(i))) {
                                        for (int m = l + 1; m <= end1 - 1; m++) {
                                            if (Character.isLetterOrDigit(expression.charAt(m))) {
                                                sb.append(expression.charAt(l));
                                            } else if (isOperator(expression.charAt(m))) {
                                                if (expression.charAt(m) == '-') {
                                                    sb.append('+');
                                                } else if (expression.charAt(m) == '+') {
                                                    sb.append('-');
                                                } else {
                                                    sb.append(expression.charAt(m));
                                                }
                                            } else {
                                                sb.append(expression.charAt(l));
                                            }
                                        }
                                        l = end1;
                                    } else {
                                        sb.append(expression.charAt(l));
                                    }
                                } else {
                                    sb.append(expression.charAt(l));
                                }
                            }
                            k = end;
                        } else {
                            sb.append(expression.charAt(k));
                        }
                    } else {
                        sb.append(expression.charAt(k));
                    }
                }
                distributions.add(sb.toString());
                sb.setLength(0);
            }
        }
        for (int k = 0; k < expression.length(); k++) {
            if (expression.charAt(k) == '(') {
                int end = findClosingBracket(expression.toCharArray(), k);
                for (int l = k + 1; l <= end - 1; l++) {
                    if (k - 1 >= 0 && expression.charAt(k - 1) == '-') {
                        if (Character.isLetterOrDigit(expression.charAt(l))) {
                            sb.append(expression.charAt(l));
                        } else if (isOperator(expression.charAt(l))) {
                            if (expression.charAt(l) == '-') {
                                sb.append('+');
                            } else if (expression.charAt(l) == '+') {
                                sb.append('-');
                            } else {
                                sb.append(expression.charAt(l));
                            }
                        }
                    } else if (expression.charAt(l) == '(') {
                        int end1 = findClosingBracket(expression.toCharArray(), l);
                        for (int m = l + 1; m <= end1 - 1; m++) {
                            if (Character.isLetterOrDigit(expression.charAt(m))) {
                                sb.append(expression.charAt(m));
                            } else if (isOperator(expression.charAt(m)) && expression.charAt(l - 1) == '-') {
                                if (expression.charAt(m) == '-') {
                                    sb.append('+');
                                } else if (expression.charAt(m) == '+') {
                                    sb.append('-');
                                } else {
                                    sb.append(expression.charAt(m));
                                }
                            } else {
                                sb.append(expression.charAt(m));
                            }
                        }
                        l = end1;
                    } else {
                        sb.append(expression.charAt(l));
                    }
                }
                k = end;

            } else {
                sb.append(expression.charAt(k));
            }
        }
        distributions.add(sb.toString());
        sb.setLength(0);
        return distributions;
    }

    public List<String> generateDistributions1(String expression) {
        List<String> distributions = new ArrayList<>();
        distributions.add(expression);
        List<String> terms = findTerms(expression);
        StringBuilder sb = new StringBuilder();
        boolean isEnd = false;
        while (!isEnd) {
            int i = 0;
            int amountOfOpens = 1;
            String s = distributions.get(distributions.size() - 1);
            while (i < s.length()) {
                if (s.charAt(i) == '(') {
                    int end = findClosingBracket(s.toCharArray(), i);

                    if (s.substring(i + 1, end - 1).contains("(")) {
                        int end1 = findClosingBracket(s.toCharArray(), i + 1);
                        for (int j = i + 1; j < end - 1; j++) {
                            if (s.charAt(j) == '(' && !isPriorityOperator(s.charAt(j - 1)) && !isPriorityOperator(s.charAt(end + 1))) {//tut
                                for (int k = j + 1; j < end1 - 1; j++) {
                                    if (s.charAt(j - 1) == '-') {
                                        if (Character.isLetterOrDigit(s.charAt(k))) {
                                            sb.append(s.charAt(k));
                                        } else if (isOperator(s.charAt(k))) {
                                            if (s.charAt(k) == '-') {
                                                sb.append('+');
                                            } else if (s.charAt(k) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(s.charAt(k));
                                            }
                                        }
                                    } else {
                                        sb.append(s.charAt(k));
                                    }
                                    amountOfOpens = 0;
                                    j = end1 + 1;
                                }
                            } else if (s.charAt(j) == '(' && !isPriorityOperator(s.charAt(j - 1)) && isPriorityOperator(s.charAt(end + 1)) && s.charAt(end + 2) == '(' && end + 2 < s.length()) {
                                //nastupna prioritetna i dali tez dushka
                                int end2 = findClosingBracket(s.toCharArray(), end + 2);
                                if (s.charAt(end + 1) == '*') {
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(j-1)=='-'){
                                        for(String str:first){
                                            if(str.charAt(0)=='+'){
                                                str.replace('+','-');
                                            }
                                            else if(str.charAt(0)=='-'){
                                                str.replace('-','+');
                                            }
                                            else{
                                                str = "-"+str;
                                            }
                                        }
                                    }
                                    List<String> second = findVariablesAndOperations(s, end + 2, end2);
                                    sb.append(makeMultiply(first, second));
                                    i = end2 + 1;
                                    amountOfOpens = 0;
                                } else if (s.charAt(end + 1) == '/') {
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(j-1)=='-'){
                                        for(String str:first){
                                            if(str.charAt(0)=='+'){
                                                str.replace('+','-');
                                            }
                                            else if(str.charAt(0)=='-'){
                                                str.replace('-','+');
                                            }
                                            else{
                                                str = "-"+str;
                                            }
                                        }
                                    }
                                    String second = s.substring(end + 2, end2 + 1);
                                    sb.append(makeDivision(first, second));
                                    i = end2 + 1;
                                    amountOfOpens = 0;
                                }
                            } else if (s.charAt(j) == '(' && !isPriorityOperator(s.charAt(j - 1)) && !isPriorityOperator(s.charAt(end + 1))) {
                                for (int k = j + 1; k < end; k++) {
                                    if (s.charAt(j - 1) == '-') {
                                        if (Character.isLetterOrDigit(s.charAt(k))) {
                                            sb.append(s.charAt(k));
                                        } else if (isOperator(s.charAt(k))) {
                                            if (s.charAt(k) == '-') {
                                                sb.append('+');
                                            } else if (s.charAt(k) == '+') {
                                                sb.append('-');
                                            } else {
                                                sb.append(s.charAt(k));
                                            }
                                        }
                                    } else {
                                        sb.append(s.charAt(k));
                                    }
                                }
                                amountOfOpens = 0;
                                j = end + 1;
                            }
                        }
                    } else {
                        if (i == 0 && !isPriorityOperator(s.charAt(end + 1))) {
                            for (int j = i + 1; j < end - 1; j++) {
                                sb.append(s.charAt(j));
                            }
                            i = end + 1;
                        } else if (i == 0 && isPriorityOperator(s.charAt(end + 1))) {
                            if (s.charAt(end + 1) == '*') {
                                int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                List<String> first = findVariablesAndOperations(s, i, end);
                                List<String> second = findVariablesAndOperations(s, end + 2, end1);
                                sb.append(makeMultiply(first, second));
                                i = end1 + 1;
                                amountOfOpens = 0;
                            } else if (s.charAt(end + 1) == '/') {
                                int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                List<String> first = findVariablesAndOperations(s, i, end);
                                String second = s.substring(end + 2, end1 + 1);
                                sb.append(makeDivision(first, second));
                                i = end1 + 1;
                                amountOfOpens = 0;
                            }
                        } else if (i != 0 && !isPriorityOperator(s.charAt(i - 1)) && !isPriorityOperator(s.charAt(end + 1)) && end + 1 < s.length()) {
                            if (s.charAt(i - 1) == '-') {
                                for (int j = i + 1; j < end; j++) {
                                    if (Character.isLetterOrDigit(s.charAt(j))) {
                                        sb.append(s.charAt(j));
                                    } else if (isOperator(s.charAt(j))) {
                                        if (s.charAt(j) == '-') {
                                            sb.append('+');
                                        } else if (s.charAt(j) == '+') {
                                            sb.append('-');
                                        } else {
                                            sb.append(s.charAt(j));
                                        }
                                    }
                                }
                            } else {
                                for (int j = i + 1; j < end; j++) {
                                    sb.append(s.charAt(j));
                                }
                            }
                            i = end + 1;
                            amountOfOpens = 0;
                        } else if (i != 0 && isPriorityOperator(s.charAt(i - 1)) && !isPriorityOperator(s.charAt(end + 1)) && end + 1 < s.length()) {
                            if (s.charAt(i - 1) == '*') {
                                int end1 = findClosingBracket(s.toCharArray(), i - 2);
                                List<String> first = findVariablesAndOperations(s, end1, i - 2);
                                List<String> second = new ArrayList<>();
                                second.add(findVariable(s, i - 2));
                                sb.append(makeMultiply(first, second));
                                i = end1 + 1;
                                amountOfOpens = 0;
                            } else if (s.charAt(i - 1) == '/') {
                                int end1 = findClosingBracket(s.toCharArray(), i - 2);
                                List<String> first = findVariablesAndOperations(s, end1, i - 2);
                                String second = findVariable(s, i - 1);
                                sb.append(makeDivisionVariable(second, first));
                                i = end1 + 1;
                                amountOfOpens = 0;
                            } else {
                                for (int j = i; j < end - 1; j++) {
                                    sb.append(s.charAt(j));
                                }
                                i = end + 1;
                                amountOfOpens = 0;
                            }
                        } else if (i != 0 && !isPriorityOperator(s.charAt(i - 1)) && isPriorityOperator(s.charAt(end + 1)) && end + 1 < s.length()) {
                            if (s.charAt(end + 1) == '*') {
                                if (end + 2 < s.length() && s.charAt(end + 2) == '(') {
                                    int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(i-1)=='-'){
                                        for(int l =0;l<first.size();l++){
                                            if(first.get(0).charAt(0)=='+'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("-"+temp.substring(1));
                                            }
                                            else if(first.get(0).charAt(0)=='-'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("+"+temp.substring(1));
                                            }
                                        }
                                    }
                                    List<String> second = findVariablesAndOperations(s, end + 2, end1);
                                    sb.append(makeMultiply(first, second));
                                    i = end1 + 1;
                                    amountOfOpens = 0;
                                } else {
                                    int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(i-1)=='-'){
                                        for(int l =0;l<first.size();l++){
                                            if(first.get(0).charAt(0)=='+'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("-"+temp.substring(1));
                                            }
                                            else if(first.get(0).charAt(0)=='-'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("+"+temp.substring(1));
                                            }
                                        }
                                    }
                                    List<String> second = new ArrayList<>();
                                    second.add(findVariable(s, end + 2));
                                    sb.append(makeMultiply(first, second));
                                    i = end1 + 1;
                                    amountOfOpens = 0;
                                }
                            } else if (s.charAt(end + 1) == '/') {
                                if (end + 2 < s.length() && s.charAt(end + 2) == '(') {
                                    int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(i-1)=='-'){
                                        for(int l =0;l<first.size();l++){
                                            if(first.get(0).charAt(0)=='+'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("-"+temp.substring(1));
                                            }
                                            else if(first.get(0).charAt(0)=='-'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("+"+temp.substring(1));
                                            }
                                        }
                                    }
                                    String second = s.substring(end + 2, end1 + 1);
                                    sb.append(makeDivision(first, second));
                                    i = end1 + 1;
                                    amountOfOpens = 0;
                                } else {
                                    int end1 = findClosingBracket(s.toCharArray(), end + 2);
                                    List<String> first = findVariablesAndOperations(s, i, end);
                                    if(s.charAt(i-1)=='-'){
                                        for(int l =0;l<first.size();l++){
                                            if(first.get(0).charAt(0)=='+'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("-"+temp.substring(1));
                                            }
                                            else if(first.get(0).charAt(0)=='-'){
                                                String temp = first.get(0);
                                                first.remove(0);
                                                first.add("+"+temp.substring(1));
                                            }
                                        }
                                    }
                                    String second = findVariable(s, end + 2);
                                    sb.append(makeDivisionVariable(second, first));
                                    i = end1 + 1;
                                    amountOfOpens = 0;
                                }
                            }
                        }
                    }
                } else if (Character.isLetterOrDigit(s.charAt(i))) {
                    if (isNextMultiplyOrDivideAndBrackets(s, i)) {
                        int endOfVariable = findEndOfVariable(s, i);
                        String variable = s.substring(i, endOfVariable);
                        if(i==0){
                            variable = "+"+variable;
                        }
                        else{
                            variable = findOperatorForVariable(s, i-1)+variable;
                        }
                        if (s.charAt(endOfVariable) == '*') {
                            int end = findClosingBracket(s.toCharArray(), endOfVariable + 2);
                            List<String> first = findVariablesAndOperations(s, endOfVariable + 2, end);
                            List<String> second = new ArrayList<>();
                            second.add(variable);
                            if(!second.get(0).contains("-")){
                                second.remove(0);
                                second.add("+"+variable);
                            }
                            sb.append(makeMultiply(first, second));
                            i = end + 1;
                        } else if (s.charAt(endOfVariable) == '/') {
                            int end = findClosingBracket(s.toCharArray(), endOfVariable + 2);
                            for(int k=i;k<end+1;k++){
                                sb.append(s.charAt(k));
                            }
                            i = end + 1;
                        }
                    } else {
                        sb.append(s.charAt(i));
                        i++;
                    }
                }
                else if(isOperator(s.charAt(i))){
                    i++;
                }
                else {
                    sb.append(s.charAt(i));
                    i++;
                }
                //distributions.add(sb.toString());
                //sb.setLength(0);
                if (i == s.length()) {
                    isEnd = true;
                    distributions.add(sb.toString());
                }

            }
        }
        return distributions;
    }

    public String findOperatorForVariable(String expression, int start) {
        for (int i = start; i >0; i--) {
            if (isOperator(expression.charAt(i))) {
                return String.valueOf(expression.charAt(i));
            }
        }
        return null;
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

    public int findEndOfVariable(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                return i;
            }
        }
        return s.length();
    }

    public boolean isNextMultiplyOrDivideAndBrackets(String expression, int start) {
        for (int i = start; i < expression.length(); i++) {
            if (isOperator(expression.charAt(i))) {
                if (expression.charAt(i) == '*' || expression.charAt(i) == '/' && expression.charAt(i + 1) == '(')
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
                while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i)=='.') {
                    sb.append(expression.charAt(i));
                    i++;
                }

            } else if (isOperator(expression.charAt(i))) {
                if (expression.charAt(i) == '-') {
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);
                    sb.append(expression.charAt(i));
                    i++;
                    while (Character.isLetterOrDigit(expression.charAt(i))) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);
                } else if (expression.charAt(i) == '+') {
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);
                    sb.append(expression.charAt(i));
                    i++;
                    while (Character.isLetterOrDigit(expression.charAt(i))) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    variablesAndOperations.add(sb.toString());
                    sb.setLength(0);

                } else if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                    sb.append(expression.charAt(i));
                    i++;
                    if (expression.charAt(i) == '(') {
                        int end1 = findClosingBracket(expression.toCharArray(), i);
                        for (int j = i + 1; j < end1 - 1; j++) {
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
                        }
                        i = end1;
                        variablesAndOperations.add(sb.toString());
                        sb.setLength(0);
                    }
                    else if(Character.isLetterOrDigit(expression.charAt(i))){
                        while (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i)=='.') {
                            sb.append(expression.charAt(i));
                            i++;
                        }
                    }

                }
            } else {
                i++;
            }
        }
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
                int closingIndex = findClosingBracket(expression.toCharArray(), i);
                termsWithBrackets.add(expression.substring(i, closingIndex + 1));
            }
        }
        return termsWithBrackets;
    }


    public String findVariable(String expression, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i > 0; i--) {
            if (Character.isLetterOrDigit(expression.charAt(i))) {
                sb.append(expression.charAt(i));
            } else {
                break;
            }
        }
        return sb.reverse().toString();
    }

    public String makeMultiply(List<String> first, List<String> second) {
        StringBuilder sb = new StringBuilder();
        for (String t : first) {
            for (String t1 : second) {
                if (t.contains("-") && t1.contains("-")) {
                    sb.append("+");
                    sb.append(t.substring(1));
                    sb.append("*");
                    sb.append(t1.substring(1));
                } else if (t.contains("-") && !t1.contains("-")) {
                    sb.append(t);
                    sb.append("*");
                    sb.append(t1.substring(1));
                } else if (!t.contains("-") && t1.contains("-")) {
                    sb.append("-");
                    sb.append(t.substring(1));
                    sb.append("*");
                    sb.append(t1.substring(1));
                } else {
                    sb.append(t);
                    sb.append("*");
                    sb.append(t1.substring(1));
                }

            }
        }
        return sb.toString();
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

    public List<String> generateMatrix() {
        List<String> a = new ArrayList<>();
        a.add("a");
        boolean c = true;
        while (c) {
            String b = a.get(a.size() - 1);
            a.add(b);
            if (a.size() == 5) {
                c = false;
            }
        }
        return a;
    }
}
