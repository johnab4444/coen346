import java.io.*;
import java.util.ArrayList;


public class Filer {
    private String name;
    private ArrayList<Character> orders = new ArrayList<Character>();
    private ArrayList<Integer> vars = new ArrayList<Integer>();
    private ArrayList<Integer> vals = new ArrayList<Integer>();
    private ArrayList<Integer> accesst = new ArrayList<Integer>();
    private int pos = 0;
    private int vmSize;
    private FileWriter fileWriter;


    public Filer(String name) throws IOException {
        this.name = name;
    }
    public Filer(String name, int size) throws IOException {
        this.name = name;
        this.vmSize = size;
    }


    //organize the commands after reading the file
    public void commandOrganizer(String[] data){
        int check = data.length;
        do{
            if(data[pos].equals("Store")){
                orders.add('s');
                pos++;
                vars.add(Integer.parseInt(data[pos++]));
                vals.add(Integer.parseInt(data[pos++]));
            } else if (data[pos].equals("Lookup")){
                orders.add('l');
                pos++;
                vars.add(Integer.parseInt(data[pos++]));
            } else {
                orders.add('r');
                pos++;
                vars.add(Integer.parseInt(data[pos++]));
            }
        }while(check > pos);
    }

    //write to vm.txt depending on the commands
    public synchronized ArrayList<Integer> vmWR(Integer varID, Integer val, Integer t, char com, Integer returnID) throws IOException {
        switch(com){
            case 's':
                if(vmSize == 0){
                    fileWriter = new FileWriter("vm.txt", false);
                }else {
                    String[] dataS = readFile().split("\\s+");
                    vmSorter(dataS);
                    for(int i=0; i<vars.size();i++){
                        if(returnID == vars.get(i)){
                            ArrayList<Integer> temp = new ArrayList<Integer>();
                            temp.add(vars.get(i));
                            temp.add(vals.get(i));
                            temp.add(accesst.get(i));
                            if(varID == null){
                                vars.remove(i);
                                vals.remove(i);
                                accesst.remove(i);
                            }else {
                                vars.set(i, varID);
                                vals.set(i, val);
                                accesst.set(i, t);
                            }
                            fileWriter = new FileWriter("vm.txt", false);
                            for(int j=0; j<vars.size(); j++){
                                fileWriter.write(vars.get(j) +  " " + vals.get(j) + " " + accesst.get(j) + '\n');
                            }
                            fileWriter.flush();
                            fileWriter.close();
                            return temp;
                        }
                    }
                    fileWriter = new FileWriter("vm.txt", true);
                }
                fileWriter.write(varID + " " + val + " " + t + '\n');
                fileWriter.flush();
                fileWriter.close();
                vmSize++;
                return null;

            case 'r':
                if(vmSize == 0){
                    return notFound();
                } else {
                    String[] dataR = readFile().split("\\s+");
                    vmSorter(dataR);
                    fileWriter = new FileWriter("vm.txt", false);
                    for (int i =0; i<vars.size(); i++){
                        if(varID == vars.get(i)){
                        }else{
                            fileWriter.write(vars.get(i) + " " + vals.get(i) + " " + accesst.get(i) + '\n');
                        }
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    vmSize--;
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    temp.add(1);
                    return temp;
                }

            case 'l':
                boolean found = false;
                ArrayList<Integer> temp = new ArrayList<Integer>();
                if(vmSize == 0){
                    return notFound();
                }else {
                    String[] dataL = readFile().split("\\s+");
                    vmSorter(dataL);
                    for(int i =0; i<vars.size(); i++){
                        if(returnID == vars.get(i)){
                            temp.add(vars.get(i));
                            temp.add(vals.get(i));
                            temp.add(accesst.get(i));
                            vars.remove(i);
                            vals.remove(i);
                            accesst.remove(i);
                            found = true;
                        }
                    }
                }
                if(found){
                    fileWriter = new FileWriter("vm.txt", false);
                    for(int j=0; j<vars.size(); j++){
                        fileWriter.write(vars.get(j) + " " + vals.get(j) + " " + accesst.get(j) + '\n');
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    return temp;
                }else{
                    return notFound();
                }
        }
        return null;
    }

    private ArrayList<Integer> notFound(){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp.add(-1);
        return temp;
    }

    private void vmSorter(String[] vm){
        int pos = 0;
        int linecount = vm.length/3;
        vars.clear();
        vals.clear();
        accesst.clear();
        for(int i =0; i<linecount; i++){
            vars.add(Integer.parseInt(vm[pos++]));
            vals.add(Integer.parseInt(vm[pos++]));
            accesst.add(Integer.parseInt(vm[pos++]));
        }

    }

    //read from file function
    public String readFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Character> getOrders() {
        return orders;
    }

    public ArrayList<Integer> getVars() {
        return vars;
    }

    public ArrayList<Integer> getVals() {
        return vals;
    }
}
