import java.util.Arrays;

public class PuzzleUtils {

    // Functional interface for processing integer selections
    @FunctionalInterface
    public interface SelectionProcessor {
        void process(int[] selection);
    }

    // Functional interface for processing string splits
    @FunctionalInterface
    public interface SplitProcessor {
        void process(String split);
    }

    // Generate k-selections from array a[0..n-1] in lexicographic order
    public static void generateSelections(int[] a, int k, SelectionProcessor processor) {
        int[] b = new int[k];
        generateSelectionsRecursive(a, a.length, k, 0, 0, b, processor);
    }

    private static void generateSelectionsRecursive(int[] a, int n, int k, int index, int selected, int[] b, SelectionProcessor processor) {
        if (selected == k) {
            processor.process(Arrays.copyOf(b, k));
            return;
        }
        if (index >= n) return;

        // Include current element
        b[selected] = a[index];
        generateSelectionsRecursive(a, n, k, index + 1, selected + 1, b, processor);

        // Skip current element
        generateSelectionsRecursive(a, n, k, index + 1, selected, b, processor);
    }

    // Binary search for a word in a sorted dictionary
    private static boolean binarySearch(String word, String[] dictionary) {
        int left = 0, right = dictionary.length - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int cmp = word.compareTo(dictionary[mid]);
            if (cmp == 0) return true;
            if (cmp < 0) right = mid - 1;
            else left = mid + 1;
        }
        return false;
    }

    // Generate all valid splits from source using dictionary
    public static void generateSplits(String source, String[] dictionary, SplitProcessor processor) {
        generateSplitsRecursive(source, dictionary, "", processor);
    }

    private static void generateSplitsRecursive(String source, String[] dictionary, String current, SplitProcessor processor) {
        if (source.isEmpty()) {
            processor.process(current.trim());
            return;
        }
        for (int i = 1; i <= source.length(); i++) {
            String prefix = source.substring(0, i);
            if (binarySearch(prefix, dictionary)) {
                generateSplitsRecursive(source.substring(i), dictionary, current + prefix + " ", processor);
            }
        }
    }

    // Swap helper for permutations
    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Reverse helper
    private static void reverse(int[] a, int start, int end) {
        while (start < end) {
            swap(a, start++, end--);
        }
    }

    // Compute previous permutation
    public static void previousPermutation(int[] a) {
        int n = a.length;
        int i = n - 2;
        while (i >= 0 && a[i] <= a[i + 1]) {
            i--;
        }
        if (i < 0) return;

        int j = n - 1;
        while (a[j] >= a[i]) {
            j--;
        }
        swap(a, i, j);
        reverse(a, i + 1, n - 1);
    }

    // Example test usage
    public static void main(String[] args) {
        int[] arr = {2, 1, 6, 5};
        System.out.println("Selections of size 2:");
        generateSelections(arr, 2, sel -> System.out.println(Arrays.toString(sel)));

        String[] dict = {"art", "artist", "is", "oil", "toil"};
        System.out.println("\nSplits for 'artistoil':");
        generateSplits("artistoil", dict, split -> System.out.println(split));

        int[] perm = {1, 5, 6, 2, 3, 4};
        previousPermutation(perm);
        System.out.println("\nPrevious permutation: " + Arrays.toString(perm));
    }
}
