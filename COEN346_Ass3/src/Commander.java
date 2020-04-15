import java.util.ArrayList;

public class Commander {
    private ArrayList<Commander> commandList = new ArrayList<Commander>();
    private Character command;
    private Integer var;
    private Integer value;

    public Commander(Character command, Integer var, Integer value){
        this.command = command;
        this.var = var;
        this.value = value;
    }

    public Commander(char command, int var) {
        this.command = command;
        this.var = var;
    }

    public Commander(ArrayList<Character> orders, ArrayList<Integer> vars, ArrayList<Integer> vals){
        int valcount =0;

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
