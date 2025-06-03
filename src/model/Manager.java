package model;

public class Manager {

    private String username;
    private String password;
    private String name;
    private ProcessingUnit assignedPU;

    public Manager(String username, String password, String name, ProcessingUnit assignedPU) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.assignedPU = assignedPU;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public ProcessingUnit getPU() {
        return assignedPU;
    }
}
