import javax.swing.text.ViewFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class VM extends Thread {

    private Frames[] mainMem;
    private int frameCount;
    private int frameCap;
    private boolean notDone;
    private boolean firstFill;
    private int[] results = new int[2];
    private Filer vmFiler = new Filer("vm.txt",0);
    private int clock;
    private int lastClock;
    private int bumps;
    private int bump =1000;
    private int execTime;
    private int proCount;
    private int commandCount = 0;

    public class Frames{
        private int variableID;
        private int value;
        private int lastAccess;

        public Frames(int varID, int val, int t){
            this.variableID = varID;
            this.value = val;
            this.lastAccess = t;
        }

        public Frames(){
            this.variableID = 0;
            this.lastAccess =0;
            this.value =0;
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

    public synchronized int[] executeC(char c, int var, Integer val) throws IOException {
        results[0] = -2;
        results[1] = -2;
        commandCount++;
        execTime = ticTock(clock,execTime);
        clock = execTime;
        passTime();
        switch(c){
            case 's':
                return memStore(var, val, execTime);


            case 'r':
               return memFree(var, execTime);

            case 'l':
               return memLookUp(var, execTime);


            default:
                return null;
        }
    }

    public VM(int memSize, int p, int c) throws IOException {
        this.mainMem = new Frames[memSize];
        this.frameCap = memSize;
        this.frameCount =0;
        this.clock =1000;
        this.lastClock = 1000;
        this.execTime =0;
        this.proCount = p;
        this.commandCount = c;
        this.notDone = true;
        this.firstFill = true;
    }

    public void allDone(){
        this.notDone = false;
    }

    @Override
    public void run() {
        while(proCount > 0 && commandCount > 0){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized int[] memStore(int varID, int val, int t) throws IOException {
        if(frameCount < frameCap && firstFill) {
            mainMem[frameCount] = new Frames(varID, val, t);
            frameCount++;
            fillCheck();
        }else if (frameCount < frameCap){
            mainMem[emptyFrame()].variableID = varID;
            mainMem[emptyFrame()].value = val;
            mainMem[emptyFrame()].lastAccess = t;
            frameCount++;
        } else {
            int old = oldest();
            ArrayList<Integer> temp = vmFiler.vmWR(mainMem[old].variableID, mainMem[old].value, mainMem[old].lastAccess, 's', varID);
            if(temp==null){
                int temporary = mainMem[old].variableID;
                mainMem[old].variableID = varID;
                mainMem[old].value = val;
                mainMem[old].lastAccess = t;
                results[0] = temporary;
                results[1] = -1;
                return results;
            }else{
                results[0] = mainMem[old].variableID;
                results[1] = 0;
                mainMem[old].variableID = temp.get(0);
                mainMem[old].value = temp.get(1);
                mainMem[old].lastAccess = t;
                return results;
            }
        }

        results[0]=-1;
        results[1]=-1;
        return results;
    }

    public synchronized int[] memFree(int varID, int t) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem[a].variableID =0;
            frameCount--;
            results[0]=-1;
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(varID,null, null, 'r',null);
            results[0]=temp.get(0);
        }

        results[1]=-1;
        return results;
    }


    public synchronized int[] memLookUp(int varID, int t) throws IOException {
        int a = isInMem(varID);
        if(a>=0){
            mainMem[a].lastAccess = t;
            results[0] = -2;
            results[1] = mainMem[a].value;
        }else{
            ArrayList<Integer> temp = vmFiler.vmWR(mainMem[oldest()].variableID,mainMem[oldest()].value,mainMem[oldest()].lastAccess,'l',varID);
            if(temp.get(0) == -1){
                results[0] = -1;
                results[1] = -1;
            }else {
                int old = oldest();

                mainMem[old].variableID = temp.get(0);
                mainMem[old].value = temp.get(1);
                mainMem[old].lastAccess =t;
                results[0] = temp.get(0);
                results[1] = temp.get(1);
            }
        }
        return results;
    }

    public int isInMem(int varID){
        for(int i = 0;i<mainMem.length;i++){
            if(mainMem[i].variableID == (varID)){
                return i;
            }
        }
        return -1;
    }


    public int emptyFrame(){
        for(int i=0; i<mainMem.length; i++){
            if(mainMem[i].variableID == 0){
                return i;
            }
        }
        return -1;
    }

    public void fillCheck(){
        if(firstFill && frameCount==frameCap){
            firstFill =false;
        }
    }

    public int oldest(){
        int id = 0;

        for(int i=1; i < mainMem.length; i++){
            if(mainMem[i].lastAccess < mainMem[id].lastAccess){
                id = i;
            }
        }
        return id;
    }


    public synchronized int chronological(){
        if(execTime > clock){
            return execTime;
        }
        else return clock;
    }

    public synchronized int readClock(){
        if(execTime < clock){
            execTime = clock;
        }
     return clock;
    }

    private synchronized int ticTock(int t, int x){
        Random rand = new Random();
        bumps++;
        if(x == 0){ return t + x + rand.nextInt(500);}
        else {
            x -= t;
            return t + x + rand.nextInt(500-x);
        }
    }


    private synchronized void passTime(){
        if(bumps%2 == 0){
            clock = lastClock + bump;
            lastClock = clock;
        }
    }

    public void checkCommand(int c){
        commandCount = c;
    }

    public void yeetProcess(){
        proCount--;
    }

    public boolean notFinished(){
        if(commandCount > 0 && proCount > 0){return true;}
        else {return false;}
    }
}

