import java.util.ArrayList;

public class Commander {
    private ArrayList<Commander> commandList = new ArrayList<Commander>();
    private Character command;
    private Integer var;
    private Integer value;
    private int processCount;

    public Commander(Character command, Integer var, Integer value){
        this.command = command;
        this.var = var;
        this.value = value;
    }

    public Commander(char command, int var) {
        this.command = command;
        this.var = var;
    }

    public boolean isEmpty(){
        return commandList.size() == 0;
    }

    public Commander(ArrayList<Character> orders, ArrayList<Integer> vars, ArrayList<Integer> vals, int p){
        int valcount =0;
        processCount = p;

        for(int i=0; i< orders.size(); i++){
            if(orders.get(i).equals('s')){
                commandList.add(new Commander('s', vars.get(i), vals.get(valcount++)));
            } else if (orders.get(i).equals('l')){
                commandList.add(new Commander('l', vars.get(i)));
            }else{
                commandList.add(new Commander('s', vars.get(i)));
            }
        }
    }

    public int showCount(){
        return processCount;
    }

    public synchronized void yeetProcess(){
        processCount--;
    }

    public synchronized Commander nextCommand(){
        Commander temp = new Commander(commandList.get(0).command, commandList.get(0).var, commandList.get(0).value);
        commandList.remove(0);
        return temp;

    }

    public ArrayList<Commander> getCommandList() {
        return commandList;
    }

    public Character getCommand() {
        return command;
    }

    public Integer getVar() {
        return var;
    }

    public Integer getValue() {
        return value;
    }
}
