import java.util.List;
import java.util.Set;

public class OperationInformation {
    public int number;
    public String operation;
    public Set<Integer> requieredOperations;

    public OperationInformation(int number, String operation, Set<Integer> requieredOperations){
        this.number = number;
        this.operation = operation;
        this.requieredOperations = requieredOperations;
    }

    public OperationInformation(int number, String operation) {
        this.number = number;
        this.operation = operation;
    }

    public OperationInformation() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Set<Integer> getRequieredOperations() {
        return requieredOperations;
    }

    public void setRequieredOperations(Set<Integer> requieredOperations) {
        this.requieredOperations = requieredOperations;
    }

    public void addRequieredOperation(int requieredOperation){
        this.requieredOperations.add(requieredOperation);
    }
}
