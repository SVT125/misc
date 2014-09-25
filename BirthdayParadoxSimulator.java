import java.util.*;
import java.util.concurrent.*;

public class BirthdayParadoxSimulator {
	public static void main(String[] args) throws Exception {
		int n = Integer.parseInt(args[0]), simulations = Integer.parseInt(args[1]), sum = 0;
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		for( int i = 0; i < simulations; i++ ) {
			Future<Integer> result = exec.submit(new BirthdayParadox(n));
			results.add(result);
		}
		System.out.println("The expected number of results are: " + (1.2 * Math.sqrt(n)));
		for( Future<Integer> result : results ) {
			int attempts = result.get();
			sum += attempts;
		}
		exec.shutdown();
		System.out.println("The mean number of results are: " + (double)sum/(double)simulations);
	}
}

class BirthdayParadox implements Callable<Integer> {
	Random r = new Random();
	boolean foundCollision = false;
	List<Integer> list = new ArrayList<Integer>();
	int n, attempts;
	public BirthdayParadox(int n) { this.n = n; }
	public Integer call() {
		while(!foundCollision) {
			int generated = r.nextInt(n);
			if(search(generated)) {
				attempts = list.size();
				break;
			}
			list.add(generated);
		}
		return attempts;
	}
	private boolean search(int element) {
		for(Integer i : list )
			if(i == element)
				return true;
		
		return false;
	}
}