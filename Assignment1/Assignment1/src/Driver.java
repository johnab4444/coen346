
public class Driver {
	public static int threadCount = 0;

	public static void main(String[] args) throws InterruptedException {
		int[] arr = {1,1,0,1,1,0,1,1,0,1,1,0,0,1,0,0};
		int[] pos = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		BulbList bulbs = new BulbList(arr.length);
		
		FindDefective(arr,pos,bulbs);
		
		int[] burntBulbs = bulbs.getList();
		
		if(burntBulbs.length == 0) {
			System.out.println("There are no defective bulbs");
		}
		else if(burntBulbs.length == 1) {
			System.out.println("Bulb #" + burntBulbs[0] + " is defective");
		}else {
			System.out.print("The defective bulbs are: ");
			for(int i=0;i<burntBulbs.length;i++) {
				if(burntBulbs[i]==0) {
					break;
				}
				if(burntBulbs[i+1] == 0) {
					System.out.println("and #" + burntBulbs[i]);
				}
				else {
					System.out.print("#" + burntBulbs[i]+", ");
				}
			}
		}
		System.out.println("The thread count is " + threadCount);

	}
	
	/*verify if at least one defective bulb in list*/
	public static boolean bulbScanner(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			if(arr[i]==0) {
				return true;
			}
		}
		return false;
	}
	
	/*find indexes of defective bulbs*/
	public static void FindDefective(int[] arr,int[] pos,BulbList bulbs) throws InterruptedException {
		
		/*count number of threads called*/
		threadCount++;
		
		/*when array of length 1 and contains a 0 then add its index into array*/
		if(arr.length == 1 && arr[0] == 0) {
			bulbs.register(pos[0]);
			return;
		}
		
		/*if array of length 1 and contains a 1 just return*/
		else if(arr.length == 1 && pos[0] != 0) {
			return;
		}
		
		/*if no defective bulb found in array, return*/
		else if(bulbScanner(arr) == false) {
			return;
		}
		
		/*if defective bulb found*/
		else {
			
			/*separate array into 2 arrays of equal length*/
			int mid = arr.length/2;
			int[] leftArr = new int[mid];
			int[] rightArr = new int[mid];
			int[] leftPos = new int[mid];
			int[] rightPos = new int[mid];
			
			/*insert values of arrays into new arrays*/
			for(int i=0;i<mid;i++) {
				leftArr[i] = arr[i];
				leftPos[i] = pos[i];
				rightArr[i] = arr[i+mid];
				rightPos[i] = pos[i+mid];
			}
			
			/*this thread will handle the left array*/
			Thread thread1 = new Thread(new Runnable() {
				public void run() {
					try {
						FindDefective(leftArr,leftPos,bulbs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread1.start();
			thread1.join();
			
			/*this thread will handle right array*/
			Thread thread2 = new Thread(new Runnable() {
				public void run() {
					try {
						FindDefective(rightArr,rightPos,bulbs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread2.start();
			thread2.join();
		}
	}

}
