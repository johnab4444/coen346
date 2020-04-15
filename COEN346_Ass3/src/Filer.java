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

    public ArrayList<Integer> vmWR(Integer varID, Integer val, Integer t, char com, Integer returnID) throws IOException {
        switch(com){
            case 's':
                if(vmSize ==0){
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
                                fileWriter.write(vars.get(j) + ' ' + vals.get(j) + ' ' + accesst.get(j) + '\n');
                            }
                            fileWriter.flush();
                            fileWriter.close();
                            return temp;
                        }
                    }
                    fileWriter = new FileWriter("vm.txt", true);
                }
                fileWriter.write(varID + ' ' + val + ' ' + t + '\n');
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
                            fileWriter.write(vars.get(i) + ' ' + vals.get(i) + ' ' + accesst.get(i) + '\n');
                        }
                    }
                    fileWriter.flush();
                    fileWriter.close();
                }
                return null;

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
                        fileWriter.write(vars.get(j) + ' ' + vals.get(j) + ' ' + accesst.get(j) + '\n');
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

    public void fileWriter(String[] data){

    }

    /* frames = 2
    var1 5 1300
    var2 7 2000

    store var3 8 3000

    vmWR(var1, 5, 1300)
    vm.txt add (var1, 5, 1300)
    result:
    var3 8 3000
    var2 7 2000

    vm.txt:
    1 5 1300

    store var4 17 3500
    vm.txt.add(var2 7 2000)

    result:
    var3 8 3000
    var4 17 3500

    vm.txt:
    1 5 1300
    2 7 2000

    store var1 23 4000

    vm.txt (swap var3 var1)

    result:
    var1 23 4000
    var4 17 3500

    vm.txt:
    3 8 3000
    2 7 2000

    free var4
    result
    var1 23 4000
    ________

    vm.txt
    3 8 3000
    2 7 2000

    store(lookup) var3 99 5000

    result
    var1 23 4000
    var3 99 5000 (var3 8 5000)

    vm.txt:
    2 7 2000
     */



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

    /* vm.txt
    1 4 2000
    3 5 6000
     */

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
