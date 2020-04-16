import javax.swing.text.ViewFactory;
import java.io.IOException;
import java.util.ArrayList;

public class VM extends Thread {

    private ArrayList<Frames> mainMem;
    private int frameCount;
    private int frameCap;
    private boolean notDone;
    private boolean firstFill;
    private Integer[] results = new Integer[2];
    private Filer vmFiler = new Filer("vm.txt",0);

    public class Frames{
        private int variableID;
        private int value;
        private int lastAccess;

        public Frames(int varID, int val, int t){
            this.variableID = varID;
            this.value = val;
            this.lastAccess = t;
        }

        public Frames updateFrame(ArrayList<Integer> temp){
            return new Frames(temp.get(0), temp.get(1), temp.get(2));
        }

        public int getVariableID() {
            return variableID;
        }

        public void setVariableID(int variableID) {
            this.variableID = variableID;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getLastAccess() {
            return lastAccess;
        }

        public void setLastAccess(int lastAccess) {
            this.lastAccess = lastAccess;
        }
    }

    public int getFrameCount(){
        return frameCount;
    }

    public synchronized Integer[] executeC(char c, int var, Integer val, Integer t) throws IOException {
        results[0] = null;
        results[1] = null;
        switch(c){
            case 's':
                return memStore(var, val, t);


            case 'r':
               return memFree(var);

            case 'l':
               return memLookUp(var, t);


            default:
                return null;
        }
    }

    public VM(int memSize) throws IOException {
        this.mainMem = new ArrayList<Frames>(memSize);
        this.frameCap = memSize;
        this.frameCount =0;
        this.notDone = true;
        this.firstFill = true;
    }

    public void allDone(){
        this.notDone = false;
    }

    @Override
    public void run() {
        while(notDone){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Frames> getMainMem() {
        return mainMem;
    }

    public synchronized Integer[] memStore(int varID, int val, int t) throws IOException {
        if(frameCount < frameCap && !firstFill){
            mainMem.add(new Frames(varID, val, t));
            frameCount++;
            if(frameCount == frameCap && firstFill){
                firstFill = false;
            } else if (frameCount < frameCap){
                mainMem.set(emptyFrame(),new Frames(varID, val, t));
                frameCount++;
            } else {
                ArrayList<Integer> temp = vmFiler.vmWR(mainMem.get(oldest()).variableID, mainMem.get(oldest()).value, mainMem.get(oldest()).lastAccess, 's', varID);
                results[0] = mainMem.get(oldest()).variableID;
                results[1] = oldest();
                mainMem.set(results[1], mainMem.get(results[1]).updateFrame(temp));
                return results;
            }
        }
        results[0]=-1;
        results[1]=-1;
        return results;
    }

    public synchronized Integer[] memFree(int varID) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem.set(a, null);
            frameCount--;
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(varID,null, null, 'r',null);
        }
        results[0]=-1;
        results[1]=-1;
        return results;
    }


    public synchronized Integer[] memLookUp(int varID, int t) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem.get(a).setLastAccess(t);
            results[0] = -2;
            results[1] = mainMem.get(a).value;
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(mainMem.get(oldest()).variableID,mainMem.get(oldest()).value,mainMem.get(oldest()).lastAccess,'l',varID);
            if(temp.get(0) == -1){
                results[0] = -1;
                results[1] = -1;
            }else {
                int old = oldest();
                temp.set(2, t);
                mainMem.set(old, mainMem.get(old).updateFrame(temp));
                results[0] = temp.get(0);
                results[1] = temp.get(1);
            }
        }
        return results;
    }

    public int isInMem(int varID){
        for(int i = 0;i<mainMem.size();i++){
            if(mainMem.get(i).getVariableID() == (varID)){
                return i;
            }
        }
        return -1;
    }


    public int emptyFrame(){
        if(mainMem.isEmpty()){
            return 0;
        }else {
            for (int i = 0; i < frameCap; i++) {
                if (mainMem.get(i) == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int oldest(){
        int id = 0;

        for(int i=1; i < mainMem.size(); i++){
            if(mainMem.get(i).getLastAccess() < mainMem.get(id).getLastAccess()){
                id = i;
            }
        }
        return id;
    }
}

