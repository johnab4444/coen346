
public class BulbList {
	private int bulbCount=0; 		//holds the array index for the array of defective bulbs
	private int[] bulbNum;			//int array that will be passed the positions of the burnt bulbs
	
	BulbList(int a){
		bulbNum = new int[a];		//overloaded constructor creates an int array who's size is passed
	}
	
	public void register(int pos) {
		bulbNum[bulbCount++] = pos;	//any bulb identified as "burnt" will have its position noted (index is also incremented)
	}
	
	public int[] getList() {
		return bulbNum;			//return the array of burnt bulb positions to caller
	}
	
	public int burntCount(){
		return bulbCount;		//return number of burnt bulbs
	}

}
