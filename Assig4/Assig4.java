//Matthew Gutkin
//CS0445 Assigment 4

import java.util.*;
import java.math.BigDecimal;

public class Assig4 {
	private static Random rand = new Random();
	private static ArrayList<Sorter<Integer>> sorts;
	private static Integer [] A;
	private static long[][][] times;	
	private static int size, tries, seed = 123456;
	private static boolean presorted;

	public static void main(String args[]) {
		try {
			size = Integer.parseInt(args[0]);
			tries = Integer.parseInt(args[1]);
			presorted = Boolean.parseBoolean(args[2]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Check your command line args.");
		} //get all command line args and check for validity.

		A = new Integer[size];
		times = new long[4][tries][15];
		sorts = new ArrayList<Sorter<Integer>>();
		sorts.add(new QuickSort<Integer>(new SimplePivot<Integer>()));
		sorts.add(new QuickSort<Integer>(new MedOfThree<Integer>()));
		sorts.add(new QuickSort<Integer>(new RandomPivot<Integer>()));
		sorts.add(new MergeSort<Integer>());

		int m = 0;
		for (int j = 0; j < 4; j++) {
			for (int k = 3; k < 74; k+=5) {
				sorts.get(j).setMin(k);
				for (int i = 0; i < tries; i++) {
					rand.setSeed(seed);
					fillArray();
					long start = System.nanoTime();
					sorts.get(j).sort(A, A.length);
					long end = System.nanoTime();
					long time = end-start;
					times[j][i][((k-3)/5)] = time;
				}
			}
		}

		showData();
	}

	public static void fillArray()
	{
		if (!presorted) {
			for (int i = 0; i < A.length; i++)
			{
				// Values will be 0 <= X < 1 billion
				A[i] = new Integer(rand.nextInt(1000000000));
			}
		} else {
			for (int i = 0; i < A.length; i++)
			{
				A[i] = new Integer(i+1);
			}
		}
	}

	public static void showData() {
		System.out.println("Initialization data:");
		System.out.println("\tArray size: " + size);
		System.out.println("\tNumber of runs per test: " + tries);
		if (presorted) System.out.println("\tInitial Data: Sorted");
		else System.out.println("\tIntial Data: Random");

		int[] tmp = new int[2];
		tmp = getMinIndex(times);
		System.out.println("After the tests, here is the best setup:");
		int jkl = tmp[0];
		if (jkl == 0) System.out.println("\tAlgorithm: Simple Pivot QuickSort");
		else if (jkl == 1) System.out.println("\tAlgorithm: Median of Three QuickSort");
		else if (jkl == 2) System.out.println("\tAlgorithm: Random Pivot QuickSort");
		else if (jkl == 3) System.out.println("\tAlgorithm: MergeSort");
		if (presorted) System.out.println("\tData Status: Sorted");
		else System.out.println("\tData Status: Random");
		System.out.println("\tMin Recurse: " + ((tmp[2]*5)+3));
		System.out.println("\tAverage: " + convertToSec(getAvgOfTries(times, tmp[0], tmp[2])));

		tmp = getMaxIndex(times);
		System.out.println("After the tests, here is the worst setup:");
		jkl = tmp[0];
		if (jkl == 0) System.out.println("\tAlgorithm: Simple Pivot QuickSort");
		else if (jkl == 1) System.out.println("\tAlgorithm: Median of Three QuickSort");
		else if (jkl == 2) System.out.println("\tAlgorithm: Random Pivot QuickSort");
		else if (jkl == 3) System.out.println("\tAlgorithm: MergeSort");
		if (presorted) System.out.println("\tData Status: Sorted");
		else System.out.println("\tData Status: Random");
		System.out.println("\tMin Recurse: " + ((tmp[2]*5)+3));
		System.out.println("\tAverage: " + convertToSec(getAvgOfTries(times, tmp[0], tmp[2])));

		System.out.println();
		System.out.println("Here are the per algorithm results:");
		for (int al = 0; al < 4; al++) {
			System.out.println();
			if (al == 0) System.out.println("Algorithm: Simple Pivot QuickSort");
			else if (al == 1) System.out.println("Algorithm: Median of Three QuickSort");
			else if (al == 2) System.out.println("Algorithm: Random Pivot QuickSort");
			else if (al == 3) System.out.println("Algorithm: MergeSort");
			System.out.println("\tBest Result: ");
			tmp = getMinIndex(times[al]);
			System.out.println("\t\tMin Recurse: " + ((tmp[1]*5)+3));
			System.out.println("\t\tAverage: " + convertToSec(getAvgOfTries(times, al, tmp[1])));
			System.out.println("\tWorst Result: ");
			tmp = getMaxIndex(times[al]);
			System.out.println("\t\tMin Recurse: " + ((tmp[1]*5)+3));
			System.out.println("\t\tAverage: " + convertToSec(getAvgOfTries(times, al, tmp[1])));
		}

	}

	public static String convertToSec(long n) {
		double b = (double)n/1E9;
		String s = String.format("%.10f", b) + " sec";
		return s;
	}

	public static long getAvgOfTries(long[][][] arr, int algorithm, int recurse) {
		long sum = 0;
		int t;
		for (t = 0; t < tries; t++) {
			sum += arr[algorithm][t][recurse];
		}
		sum /= t;
		return sum;
	}

	public static int[] getMinIndex(long[][][] arr) {
		int[] m = new int[3];
		for (int j = 0; j < arr.length; j++) 
			for (int i = 0; i < arr[j].length; i++)
				for (int l = 0; l < arr[j][i].length; l++) {
					if (arr[j][i][l] < arr[m[0]][m[1]][m[2]]) {
						m[0] = j;
						m[1] = i;
						m[2] = l;
					}
				}
		return m;
	}

	public static int[] getMaxIndex(long[][][] arr) {
		int[] m = new int[3];
		for (int j = 0; j < arr.length; j++) 
			for (int i = 0; i < arr[j].length; i++)
				for (int l = 0; l < arr[j][i].length; l++) {
					if (arr[j][i][l] > arr[m[0]][m[1]][m[2]]) {
						m[0] = j;
						m[1] = i;
						m[2] = l;
					}
				}
		return m;
	}

	public static int[] getMinIndex(long[][] arr) {
		int[] ind = new int[2];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (arr[i][j] < arr[ind[0]][ind[1]]) {
					ind[0] = i;
					ind[1] = j;
				}
			}
		}
		return ind;
	}
	public static int[] getMaxIndex(long[][] arr) {
		int[] ind = new int[2];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (arr[i][j] > arr[ind[0]][ind[1]]) {
					ind[0] = i;
					ind[1] = j;
				}
			}
		}
		return ind;
	}
}