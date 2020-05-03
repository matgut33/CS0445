public class QuickSort<T extends Comparable<? super T>>

implements Sorter<T>

{

      private Partitionable<T> partAlgo;

      private int MIN_SIZE;  // min size to recurse, use InsertionSort

                             // for smaller sizes to complete sort

      public QuickSort(Partitionable<T> part)

      {

            partAlgo = part;

            MIN_SIZE = 3;

      }

      // remaining code in QuickSort class not shown

      // You must complete this class – in particular the methods

      // sort() and setMin()  You will use partAlgo for partition

// within the sort() method.

      public void setMin(int m) {
            MIN_SIZE = m;
      }

      public void sort(T[] arr, int size) {
            if (size <= MIN_SIZE) insertionSort(arr);
            else quickSort(arr, 0, size-1);
      }

      public void quickSort(T arr[], int low, int high)
      {
          if (low < high)
          {
            int pi = partAlgo.partition(arr,low,high);;

            quickSort(arr, low, pi - 1);  // Before pi
            quickSort(arr, pi + 1, high); // After pi
          }
      }

      public void insertionSort(T arr[]) 
      { 
        int n = arr.length; 
        for (int i = 1; i < n; ++i) { 
            T key = arr[i]; 
            int j = i - 1; 
            while (j >= 0 && arr[j].compareTo(key)>1) { 
                arr[j + 1] = arr[j]; 
                j = j - 1; 
            } 
            arr[j + 1] = key; 
        } 
      } 

}