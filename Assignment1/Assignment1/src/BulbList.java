
public class BulbList {
	private int bulbCount=0;
	private int[] bulbNum;
	
	BulbList(int a){
		bulbNum = new int[a];
	}
	
	public void register(int pos) {
		bulbNum[bulbCount++] = pos;
	}
	
	public int[] getList() {
		return bulbNum;
	}

}
