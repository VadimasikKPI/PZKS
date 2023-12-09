import java.util.*;
import java.util.stream.Collectors;

public class Matrix {
    public static final int PLUS_VALUE = 2;
    public static final int MINUS_VALUE = 3;
    public static final int MULTIPLY_VALUE = 4;
    public static final int DIVIDE_VALUE = 8;
    public static final int FUNCTION_VALUE = 6;
    public int operationNumber = 1;
    public List<List<Integer>> doneOperations = new ArrayList<>();

    public List<OperationInformation> operationInformation = new ArrayList<>();

    public void manageOperations(TreeNode root){
        List<String> result = new ArrayList<>();
        bottomUpUtil(root, result);
        System.out.println();
        for(String s: result){
            System.out.print(s + " | ");
        }

        //fillOperations(root);
        System.out.println();


    }
    public void bottomUpUtil(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }

        // Process left subtree
        bottomUpUtil(node.left, result);

        // Process right subtree
        bottomUpUtil(node.right, result);
        if(node.getValue()!=null && isFunction(node.getCharValue()) || isOperator(node.getCharValue().get(0))){
            result.add(node.getValue());
        }

    }

    public List<Integer> fillOperations(TreeNode node){
        if (node == null) {
            return new ArrayList<>(Arrays.asList(-1));
        }
        fillOperations(node.left);
        fillOperations(node.right);
        if(node.getValue()!=null && isFunction(node.getCharValue()) || isOperator(node.getCharValue().get(0))){

            operationInformation.add(new OperationInformation(operationNumber, node.getValue()));
            operationNumber++;
        }

        return new ArrayList<>(Arrays.asList(-1));
    }
    public int processTree(TreeNode root) {//main method
        Map<Integer, OperationInformation> operationsMap = new HashMap<>();
        IntTree inttree = tree(root);
        operationNumber = 1;
        postOrderTraversal(inttree, operationsMap, root);
        System.out.println();
        for (Map.Entry<Integer, OperationInformation> entry : operationsMap.entrySet()) {
            System.out.println(entry.getValue().number + " | " + entry.getValue().operation + " | " + entry.getValue().requieredOperations);
        }
        List<Integer> sortedOperations = new ArrayList<>();
        sortedOperations = topologicalSort(operationsMap);
//        System.out.println("Topological order: " + sortedOperations);
        List<List<String>> result = mapAlgorithm(operationsMap, sortedOperations);
        System.out.println("Найгірший час виконання: " + worstTime(operationsMap));
        System.out.println("Час виконання: " + calculateTime(result));
        System.out.println("Коефіцієнт прискорення: " + (double)worstTime(operationsMap)/calculateTime(result));
        System.out.println("Коефіцієнт ефективності: " + (double)worstTime(operationsMap)/(calculateTime(result)*8));
        return calculateTime(result);
    }

    private int worstTime(Map<Integer,OperationInformation> operationsMap) {
        int result = 0;
        for(Map.Entry<Integer, OperationInformation> entry: operationsMap.entrySet()){
            if(entry.getValue().getOperation().equals("+")){
                result+=PLUS_VALUE;
            }
            else if(entry.getValue().getOperation().equals("-")){
                result+=MINUS_VALUE;
            }
            else if(entry.getValue().getOperation().equals("*")){
                result+=MULTIPLY_VALUE;
            }
            else if(entry.getValue().getOperation().equals("/")){
                result+=DIVIDE_VALUE;
            }
            else if(isFunction(entry.getValue().getOperation())){
                result+=FUNCTION_VALUE;
            }
        }
        return result;
    }


    private int postOrderTraversal(IntTree node, Map<Integer, OperationInformation> operationsMap, TreeNode root) {
        if (node == null) {
            return 0;
        }

        int leftOp = postOrderTraversal(node.left, operationsMap, root.left);
        int rightOp = postOrderTraversal(node.right, operationsMap, root.right);
        Set<Integer> requiredOps = new HashSet<>(Arrays.asList(leftOp, rightOp));
        requiredOps.remove(0);
        OperationInformation operationInfo = new OperationInformation(node.value, root.value, requiredOps);
        operationsMap.put(node.value, operationInfo);


        return node.value;
    }


    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c=='^';
    }

    public boolean isFunction(List<Character> exp){
        if(exp.size()==1){
            return false;
        }
        for(int i = 0;i<exp.size();i++){
            if(exp.get(i)=='('){
                return true;
            }
            else if(isOperator(exp.get(i))){
                return false;
            }
        }
        return false;
    }
    public boolean isFunction(String exp){
        if(exp.length()==1){
            return false;
        }
        for(int i = 0;i<exp.length();i++){
            if(exp.charAt(i)=='('){
                return true;
            }
            else if(isOperator(exp.charAt(i))){
                return false;
            }
        }
        return false;
    }

    public IntTree tree(TreeNode root){
        IntTree result = new IntTree();

        if(root!=null && root.getValue()!=null && (isFunction(root.getCharValue()) || isOperator(root.getCharValue().get(0)))){
        result.setValue(operationNumber);
        operationNumber++;
        result.setLeft(tree(root.left));
        result.setRight(tree(root.right));
        }
        else{
            return null;
        }
        return result;
    }

    public List<Integer> topologicalSort(Map<Integer, OperationInformation> operationsMap) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        for (Map.Entry<Integer, OperationInformation> entry : operationsMap.entrySet()){
            if(entry.getValue().getRequieredOperations().isEmpty()){
                result.add(entry.getKey());
                visited.add(entry.getKey());
            }
        }
        for (Map.Entry<Integer, OperationInformation> entry : operationsMap.entrySet()) {
            int operationNumber = entry.getKey();
            if (!visited.contains(operationNumber)) {
                topologicalSortUtil(operationsMap, operationNumber, visited, result);
            }
        }



        return result;
    }

    public void topologicalSortUtil(Map<Integer, OperationInformation> operationsMap, int operationNumber,
                                            Set<Integer> visited, List<Integer> result) {
        visited.add(operationNumber);

        OperationInformation operationInfo = operationsMap.get(operationNumber);
        for (int reqOperation : operationInfo.getRequieredOperations()) {
            if (!visited.contains(reqOperation)) {
                topologicalSortUtil(operationsMap, reqOperation, visited, result);
            }
        }

        result.add(operationNumber);
    }

    public List<List<String>> mapAlgorithm(Map<Integer, OperationInformation> operationsMap, List<Integer> sorted){
        int resultTime = Integer.MAX_VALUE;
        List<List<String>> processors = new ArrayList<>();
        List<Integer> functions = new ArrayList<>();
        List<String> functionProcessor = new ArrayList<>();
        List<Integer> priority = new ArrayList<>();
        List<Integer> doneOperations = new ArrayList<>();
        boolean isInserted = false, isRequireFunction = false;
        for(int i = 0; i<8;i++){
            processors.add(new ArrayList<>());
        }
        for(int i = 1; i<processors.size()-1;i++){
            priority.add(i);
        }
        for(int i = 0;i<sorted.size();i++){
            if(isFunction(operationsMap.get(sorted.get(i)).getOperation())){
                functions.add(sorted.get(i));
                sorted.remove(i);
                i--;
            }
        }
        for(int k = 0;k<calculateNumberOfInputs(operationsMap.get(sorted.get(0)).getOperation());k++){
            processors.get(1).add(sorted.get(0).toString());
        }

        for(int i = 0;i<functions.size();i++){
            for(int j = 0;j<calculateNumberOfInputs(operationsMap.get(functions.get(i)).getOperation());j++){
                functionProcessor.add(functions.get(i).toString());
            }
        }
        System.out.println("Functions: " + functions);
        int processorNumber = 1, taskNumber = sorted.get(0), previousTaskNumber = sorted.get(0);
        for(int i = 1;i<sorted.size();i++){
            int time = Integer.MAX_VALUE, tempTime = Integer.MAX_VALUE;
            for(int j = 0;j<processors.size();j++){
                isInserted = false;
                isRequireFunction = false;
                List<List<String>> temp = new ArrayList<>();
                coppy(processors, temp);
                if(operationsMap.get(sorted.get(i)).getRequieredOperations().isEmpty()){
                    if(previousTaskNumber!=sorted.get(i)){
                        if(operationsMap.get(taskNumber).getOperation().equals(operationsMap.get(sorted.get(i)).getOperation())){
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                temp.get(j).add(sorted.get(i).toString());
                            }
                            time=calculateTime(temp);
                        }
                        else{
                            for(int k = 0;k<processors.size();k++){
                                if(processors.get(k).size()!=calculateTime(temp)){
                                    while(processors.get(k).size()!=calculateTime(temp)){
                                        processors.get(k).add("Empty");
                                    }
                                }
                            }
                            temp = new ArrayList<>();
                            coppy(processors, temp);
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                temp.get(j).add(sorted.get(i).toString());
                            }
                            time=calculateTime(temp);
                        }
                    }
                    else{
//                        if(processors.get(j).size()<calculateTime(temp)){
//                            while(processors.get(j).size()!=calculateTime(processors)){
//                                processors.get(j).add("Empty");
//                            }
//                        }
                        for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                            temp.get(j).add(sorted.get(i).toString());
                        }
                        time=calculateTime(temp);
                    }
                }
                else{
                    //if has required operations
                    List<Integer> requiredOperationProcessors = findRequiredOperationProcessors(processors, operationsMap.get(sorted.get(i)).getRequieredOperations(), functions, operationsMap, sorted.get(i));
                    if(requiredOperationProcessors.size()==1){
                        int firstProcessor = requiredOperationProcessors.get(0);
                        if(processors.get(firstProcessor).size()<calculateTime(processors)){
                            while(processors.get(firstProcessor).size()!=calculateTime(processors)){
                                processors.get(firstProcessor).add("Empty");
                            }
                        }
                        for(int z = 0;z<functions.size();z++){
                            if(operationsMap.get(sorted.get(i)).getRequieredOperations().contains(functions.get(z))){
                                if(processors.get(firstProcessor).size()<calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                    while(processors.get(firstProcessor).size()!=calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                        processors.get(firstProcessor).add("Empty");
                                    }
                                    isRequireFunction = true;
                                }
                            }
                        }
                        if(operationsMap.get(taskNumber).getOperation().equals(operationsMap.get(sorted.get(i)).getOperation()) && !operationsMap.get(sorted.get(i)).getRequieredOperations().contains(taskNumber) && !isRequireFunction ) {
                            for(int l = 0;l<processors.size();l++){
                                temp = new ArrayList<>();
                                coppy(processors, temp);
                                if(temp.get(l).size()<calculateTime(temp)){
                                    while(temp.get(l).size()!=calculateTime(temp)){
                                        temp.get(l).add("Empty");
                                    }
                                }
                                for(int r = 0;r<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());r++){
                                    temp.get(l).add(sorted.get(i).toString());
                                }
                                time=calculateTime(temp);
                                if(time<tempTime && priority.contains(l)){
                                    tempTime = time;
                                    processorNumber = l;
                                    taskNumber = sorted.get(i);
                                    previousTaskNumber = sorted.get(i);
                                    resultTime = time;
                                }
                                else if(time==tempTime && priority.contains(l)){
                                    if(processors.get(l).size()<processors.get(processorNumber).size()){
                                        tempTime = time;
                                        processorNumber = l;
                                        taskNumber = sorted.get(i);
                                        previousTaskNumber = sorted.get(i);
                                        resultTime = time;
                                    }
                                }
                            }
                            isInserted = false;
                        }else{
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                processors.get(firstProcessor).add(sorted.get(i).toString());
                            }
                            isInserted = true;
                            processorNumber = firstProcessor;
                            taskNumber = sorted.get(i);
                        }
//                        for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
//                            processors.get(firstProcessor).add(sorted.get(i).toString());
//                        }
                        j = processors.size()-1;
                        //isInserted = false;

                    }
                    else{
                        int firstProcessorNumber = requiredOperationProcessors.get(0);
                        int secondProcessorNumber = requiredOperationProcessors.get(1);
                        int closerProcessor = findProcessorCloserToCenter(firstProcessorNumber, secondProcessorNumber, processors);
                        if(closerProcessor==secondProcessorNumber){
                            if(processors.get(firstProcessorNumber).size()<processors.get(secondProcessorNumber).size()){
                                while (processors.get(firstProcessorNumber).size()!=processors.get(secondProcessorNumber).size()){
                                    processors.get(firstProcessorNumber).add("Empty");
                                }
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(firstProcessorNumber).size()){
                                        while(processors.get(k).size()!=processors.get(firstProcessorNumber).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            else if(processors.get(firstProcessorNumber).size()>processors.get(secondProcessorNumber).size()){
                                while(processors.get(firstProcessorNumber).size()!=processors.get(secondProcessorNumber).size()){
                                    processors.get(secondProcessorNumber).add("Empty");
                                }
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(secondProcessorNumber).size()){
                                        while(processors.get(k).size()!=processors.get(secondProcessorNumber).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            for(int z = 0;z<functions.size();z++){
                                if(operationsMap.get(sorted.get(i)).getRequieredOperations().contains(functions.get(z))){
                                    if(processors.get(closerProcessor).size()<calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                        while(processors.get(closerProcessor).size()!=calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                            processors.get(closerProcessor).add("Empty");
                                        }
                                    }
                                }
                            }
                            Set<Integer> oper = operationsMap.get(sorted.get(i)).getRequieredOperations();
                            int operationNumberValie = 0;
                            for(int m = 0;m<sorted.size();m++){
                                if(oper.contains(sorted.get(m) ) && processors.get(firstProcessorNumber).contains(sorted.get(m).toString())){
                                    operationNumberValie = sorted.get(m);
                                }
                            }
                            for(int l = firstProcessorNumber;l<secondProcessorNumber;l++){

                                processors.get(l).add("S[" + operationNumberValie + "]");
                                processors.get(l+1).add("R[" + operationNumberValie+ "]");
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(l).size()){
                                        while(processors.get(k).size()!=processors.get(l).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                processors.get(secondProcessorNumber).add(sorted.get(i).toString());
                            }
                            time = calculateTime(processors);
                            processorNumber = secondProcessorNumber;
                            taskNumber = sorted.get(i);
                            previousTaskNumber = sorted.get(i);
                            j = processors.size()-1;
                            isInserted = true;
                        }
                        else if(closerProcessor==firstProcessorNumber){

                            if(processors.get(firstProcessorNumber).size()<processors.get(secondProcessorNumber).size()){
                                while (processors.get(firstProcessorNumber).size()!=processors.get(secondProcessorNumber).size()){
                                    processors.get(firstProcessorNumber).add("Empty");
                                }
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(firstProcessorNumber).size()){
                                        while(processors.get(k).size()!=processors.get(firstProcessorNumber).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            else if(processors.get(firstProcessorNumber).size()>processors.get(secondProcessorNumber).size()){
                                while(processors.get(firstProcessorNumber).size()!=processors.get(secondProcessorNumber).size()){
                                    processors.get(secondProcessorNumber).add("Empty");
                                }
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(secondProcessorNumber).size()){
                                        while(processors.get(k).size()!=processors.get(secondProcessorNumber).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            for(int z = 0;z<functions.size();z++){
                                if(operationsMap.get(sorted.get(i)).getRequieredOperations().contains(functions.get(z))){
                                    if(processors.get(closerProcessor).size()<calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                        while(processors.get(closerProcessor).size()!=calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                            processors.get(closerProcessor).add("Empty");
                                        }
                                    }
                                }
                            }
                            Set<Integer> oper = operationsMap.get(sorted.get(i)).getRequieredOperations();
                            int operationNumberValie = 0;
                            for(int m = 0;m<sorted.size();m++){
                                if(oper.contains(sorted.get(m) ) && processors.get(secondProcessorNumber).contains(sorted.get(m).toString())){
                                    operationNumberValie = sorted.get(m);
                                }
                            }
                            for(int l = secondProcessorNumber;l>firstProcessorNumber;l--){
                                processors.get(l).add("S[" + operationNumberValie + "]");
                                processors.get(l-1).add("R[" + operationNumberValie+ "]");
                                for(int k = 0;k<processors.size();k++){
                                    if(processors.get(k).size()<processors.get(l).size()){
                                        while(processors.get(k).size()!=processors.get(l).size()){
                                            processors.get(k).add("Empty");
                                        }
                                    }
                                }
                            }
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                processors.get(firstProcessorNumber).add(sorted.get(i).toString());
                            }
                            time = calculateTime(processors);
                            processorNumber = firstProcessorNumber;
                            taskNumber = sorted.get(i);
                            previousTaskNumber = sorted.get(i);
                            j = processors.size()-1;
                            isInserted = true;

                        }
                        else{
                            for(int z = 0;z<functions.size();z++){
                                if(operationsMap.get(sorted.get(i)).getRequieredOperations().contains(functions.get(z))){
                                    if(processors.get(closerProcessor).size()<calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                        while(processors.get(closerProcessor).size()!=calculateLastTimeOfOperationInFunctionProcessor(functionProcessor, functions.get(z))){
                                            processors.get(closerProcessor).add("Empty");
                                        }
                                    }
                                }
                            }
                            for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                                processors.get(firstProcessorNumber).add(sorted.get(i).toString());
                            }
                            time = calculateTime(processors);
                            processorNumber = firstProcessorNumber;
                            taskNumber = sorted.get(i);
                            previousTaskNumber = sorted.get(i);
                            j = processors.size();
                            isInserted = true;
                        }

                    }
                }
                if(time<tempTime && priority.contains(j)){
                    tempTime = time;
                    processorNumber = j;
                    taskNumber = sorted.get(i);
                    previousTaskNumber = sorted.get(i);
                    resultTime = time;
                }
                else if(time==tempTime && priority.contains(j)){
                    if(processors.get(j).size()<processors.get(processorNumber).size()){
                        tempTime = time;
                        processorNumber = j;
                        taskNumber = sorted.get(i);
                        previousTaskNumber = sorted.get(i);
                        resultTime = time;
                    }
                }
                doneOperations.add(sorted.get(i));
            }
            if(!isInserted){
//                if(processors.get(processorNumber).size()<time-calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation())){
//                    while(processors.get(processorNumber).size()!=time-calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation())){
//                        processors.get(processorNumber).add("Empty");
//                    }
//                }
                for(int l = 0;l<calculateNumberOfInputs(operationsMap.get(sorted.get(i)).getOperation());l++){
                    processors.get(processorNumber).add(sorted.get(i).toString());
                }
                isInserted = false;
            }

        }
        for(int k = 0;k<processors.size();k++){
            if(processors.get(k).size()<calculateTime(processors)){
                while(processors.get(k).size()!=calculateTime(processors)){
                    processors.get(k).add("Empty");
                }
            }
        }
        int cellWidth = 7;
        while(functionProcessor.size()!=calculateTime(processors)){
            functionProcessor.add("Empty");
        }
        for(int i = 0;i<calculateTime(processors);i++){
            if(processors.get(0).get(i).equals("Empty") && processors.get(1).get(i).equals("Empty") && processors.get(2).get(i).equals("Empty") && processors.get(3).get(i).equals("Empty") && processors.get(4).get(i).equals("Empty") && processors.get(5).get(i).equals("Empty") && processors.get(6).get(i).equals("Empty") && processors.get(7).get(i).equals("Empty") && functionProcessor.get(i).equals("Empty")){
                processors.get(0).remove(i);
                processors.get(1).remove(i);
                processors.get(2).remove(i);
                processors.get(3).remove(i);
                processors.get(4).remove(i);
                processors.get(5).remove(i);
                processors.get(6).remove(i);
                processors.get(7).remove(i);
                functionProcessor.remove(i);
                i--;
            }
        }
        for(int i = 0;i<calculateTime(processors);i++){
            //I want to write it in a better way

            System.out.println(String.format("%" + cellWidth + "s", i+1) + String.format("| %" + cellWidth + "s", processors.get(0).get(i)) + String.format("| %" + cellWidth + "s", processors.get(1).get(i))+ String.format("| %" + cellWidth + "s", processors.get(2).get(i))+ String.format("| %" + cellWidth + "s", processors.get(3).get(i))+ String.format("| %" + cellWidth + "s", processors.get(4).get(i))+ String.format("| %" + cellWidth + "s", processors.get(5).get(i))+ String.format("| %" + cellWidth + "s", processors.get(6).get(i))+ String.format("| %" + cellWidth + "s", processors.get(7).get(i)) + String.format("| %" + cellWidth + "s", functionProcessor.get(i)));
        }
        return processors;
    }


    public boolean canBePlacedHere(List<Integer> doneOperations, Map<Integer, OperationInformation> operationsMap, int operationNumber){

        return true;
    }
    public int findProcessorCloserToCenter(int proc1, int proc2, List<List<String>> processors){

        if ((proc1 <= 2 && proc2 <= 2)) {
            // Повертаємо той процесор, який має більший номер
            return Math.max(proc1, proc2);
        } else {
            // Якщо процесори належать до різних половин,
            // повертаємо той, що ближчий до центру (менший номер)
            return Math.min(proc1, proc2);
        }
    }
//    public List<Integer> findBestProcessor(List<List<Integer>> processors, OperationInformation operationsMap, List<Integer> priority, String previousOperation){
//        List<Integer> result = new ArrayList<>();
//        int minTime = Integer.MAX_VALUE;
//        int processorNumber = 0;
//        for(int i = 0;i<processors.size();i++){
//            //int time = calculateTime(processors);
//            if(operationsMap.getRequieredOperations().isEmpty()){
//                if(previousOperation.equals(operationsMap.getOperation())){
//                    time = calculateTime(processors) + calculateNumberOfInputs(operationsMap.getOperation());
//                }
//                else{
//                    time = calculateTime(processors) + calculateNumberOfInputs(operationsMap.getOperation()) + 1;
//                }
//
//            }
//            else {
//                List<Integer> requiredOperationProcessors = findRequiredOperationProcessors(processors, operationsMap.getRequieredOperations());
//                for(Integer requiredOperationProcessor: requiredOperationProcessors){
//                    time = processors.get(requiredOperationProcessor).size() + calculateNumberOfInputs(operationsMap.getOperation());
//                    if(time<minTime && !priority.contains(i)){
//                        minTime = time;
//                        processorNumber = i;
//                    }
//                }
//            }
//            if(time<minTime && priority.contains(i)){
//                minTime = time;
//                processorNumber = i;
//            }
//        }
//        result.add(processorNumber);
//        return result;
//    }

    public int calculateLastTimeOfOperationInFunctionProcessor(List<String> functionProcessor, int operationNumber){
        int result = 0;
        for(int i = 0;i<functionProcessor.size();i++){
            if(functionProcessor.get(i).equals(String.valueOf(operationNumber))){
                result = i;
            }
        }
        return result+1;
    }
    private List<Integer> findRequiredOperationProcessors(List<List<String>> processors, Set<Integer> requieredOperations, List<Integer> functions, Map<Integer, OperationInformation> operationsMap, int operationNumber) {
        List<Integer> result = new ArrayList<>();
        int temp = 0, secondTemp = 0;
        for(int i = 0;i<functions.size();i++){
            if(requieredOperations.contains(functions.get(i))){
                for(OperationInformation operationInformation: operationsMap.values()){
                    if(operationInformation.getRequieredOperations().contains(operationNumber)){
                        for(Integer operation: operationInformation.getRequieredOperations()){
                            if(operation!=operationNumber) {
                                requieredOperations.add(operation);
                                temp = operation;
                            }
                            else {
                                secondTemp = operationInformation.getNumber();
                            }
                        }

                    }
                }
            }
        }
        for(int i = 0;i<processors.size();i++){
            for(Integer operation: requieredOperations){
                if(processors.get(i).contains(operation.toString())){
                    result.add(i);
                }
            }
        }
        if(result.size()==0){
            return findRequiredOperationProcessors(processors, requieredOperations, functions, operationsMap, secondTemp);
        }
        requieredOperations.remove(temp);
        return result;
    }


    public int calculateNumberOfInputs(String operation){
        if(operation.equals("+")){
            return PLUS_VALUE;
        }
        else if(operation.equals("-")){
            return MINUS_VALUE;
        }
        else if(operation.equals("*")){
            return MULTIPLY_VALUE;
        }
        else if(operation.equals("/")){
            return DIVIDE_VALUE;
        }
        else if(isFunction(operation)){
            return FUNCTION_VALUE;
        }
        return 0;
    }

    public int calculateTime(List<List<String>> processors){
        int result = 0;
        for(int i = 0;i<processors.size();i++){
            if(processors.get(i).size()>=result && !processors.get(i).isEmpty()){
                result = processors.get(i).size();
            }
        }

        return result;

    }

    public void coppy(List<List<String>> src, List<List<String>> dest){
        for( List<String> sublist : src) {
            dest.add(new ArrayList<>(sublist));
        }
    }
}
