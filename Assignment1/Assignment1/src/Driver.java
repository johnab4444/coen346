
public class Driver {
	public static int threadCount = 0;

	public static void main(String[] args) {
		int[] arr = {1,1,0,1,1,0,1,1};
		int[] pos = {1,2,3,4,5,6,7,8};
		BulbList bulbs = new BulbList(arr.length);
		
		FindDefective(arr,pos,bulbs);
		
		int[] burntBulbs = bulbs.getList();
		
		for(int i=0;i<burntBulbs.length;i++) {
			if(burntBulbs[i]==0) {
				break;
			}
			System.out.print(burntBulbs[i]+" ");
		}
		System.out.println(threadCount);

	}
	
	public static boolean bulbScanner(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			if(arr[i]==0) {
				return true;
			}
		}
		return false;
	}
	
	public static void FindDefective(int[] arr,int[] pos,BulbList bulbs) {
		threadCount++;
		if(arr.length == 1 && arr[0] == 0) {
			bulbs.register(pos[0]);
			return;
		}
		else if(arr.length == 1 && pos[0] != 0) {
			return;
		}
		else if(bulbScanner(arr) == false) {
			return;
		}
		else {
			int mid = arr.length/2;
			int[] leftArr = new int[mid];
			int[] rightArr = new int[mid];
			int[] leftPos = new int[mid];
			int[] rightPos = new int[mid];
			
			for(int i=0;i<mid;i++) {
				leftArr[i] = arr[i];
				leftPos[i] = pos[i];
				rightArr[i] = arr[i+mid];
				rightPos[i] = pos[i+mid];
			}
			FindDefective(leftArr,leftPos,bulbs);
			FindDefective(rightArr,rightPos,bulbs);
		}
	}

}
