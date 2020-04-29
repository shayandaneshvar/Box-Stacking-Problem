package ir.shayandaneshvar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class LIS {
    /* lis() returns the length of the longest increasing
       subsequence in arr[] of size n */
    static int lis(int arr[], int n) {
        int i, j, max = 0;
        int lis[] = new int[n];
        List<Integer>[] lists = new ArrayList[n];
        /* Initialize LIS values for all indexes */
        for (i = 0; i < n; i++) {
            lis[i] = 1;
            lists[i] = new ArrayList<>();
            lists[i].add(arr[i]);
        }

        /* Compute optimized LIS values in bottom up manner */
        for (i = 1; i < n; i++)
            for (j = 0; j < i; j++)
                if (arr[i] > arr[j] && lis[i] < lis[j] + 1) {
                    lis[i]++;
                    lists[i].add(lists[i].size() - 1, arr[j]);
                }

        /* Pick maximum of all LIS values */
        j = 0;
        for (i = 0; i < n; i++)
            if (max < lis[i]) {
                max = lis[i];
                j = i;
            }

        System.out.println(Arrays.deepToString(lists));
        return max;
    }

    public static void main(String args[]) {
        for (int k = 1; k < 4; k++)
            for (int i = 0; i < 4 - k; i++) System.err.println(i + "," + (i+k));
        int arr[] = {10, 22, 9, 33, 21, 50, 41, 60};
        List<Integer> l = new ArrayList<>();
        Arrays.stream(arr).forEach(z -> l.add(z));

        l.sort(Comparator.comparingInt(Integer::intValue));
        l.stream().forEach(System.out::println);
        System.out.println("Length of lis is " + lis(arr, arr.length) + "\n");
    }
}