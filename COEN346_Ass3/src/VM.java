import javax.swing.text.ViewFactory;
import java.io.IOException;
import java.util.ArrayList;

public class VM {

    private ArrayList<Frames> mainMem;
    private int frameCount;
    private int frameCap;
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

    public VM(int memSize) throws IOException {
        this.mainMem = new ArrayList<Frames>(memSize);
        this.frameCap = memSize;
        this.frameCount =0;
    }

    public void memStore(int varID, int val, int t) throws IOException {
        if(freeFrames()){
            mainMem.set(emptyFrame(), new Frames(varID, val, t));
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(mainMem.get(oldest()).variableID,mainMem.get(oldest()).value,mainMem.get(oldest()).lastAccess,'s',varID);
            int old = oldest();
            mainMem.set(old, mainMem.get(old).updateFrame(temp));

        }
    }

    public void memFree(int varID) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem.set(a, null);
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(varID,null, null, 'r',null);
        }
    }


    public int memLookUp(int varID, int t) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem.get(a).setLastAccess(t);
            return mainMem.get(a).getValue();
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(mainMem.get(oldest()).variableID,mainMem.get(oldest()).value,mainMem.get(oldest()).lastAccess,'l',varID);
            if(temp.get(0) == -1){
                return -1;
            }else {
                int old = oldest();
                temp.set(2, t);
                mainMem.set(old, mainMem.get(old).updateFrame(temp));
                return temp.get(1);
            }
        }
    }

    public int isInMem(int varID){
        for(int i = 0;i<mainMem.size();i++){
            if(mainMem.get(i).getVariableID() == (varID)){
                return i;
            }
        }
        return -1;
    }

    public boolean freeFrames(){
        if(frameCount < frameCap){
            return true;
        }else
            return false;
    }

    public int emptyFrame(){
        for(int i=0; i<frameCap; i++){
            if(mainMem.get(i)==null){
                return i;
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

