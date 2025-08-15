import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class PuzzleUtilsTest {

    @Test
    public void testGenerateSelections() {
        int[] a = {2, 1, 6, 5};
        List<int[]> results = new ArrayList<>();
        PuzzleUtils.generateSelections(a, 2, sel -> results.add(sel));

        int[][] expected = {
            {2, 1}, {2, 6}, {2, 5},
            {1, 6}, {1, 5}, {6, 5}
        };

        assertEquals(expected.length, results.size(), "Selection count mismatch");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], results.get(i), "Mismatch at selection " + i);
        }
    }

    @Test
    public void testGenerateSplits() {
        String[] dict = {"art", "artist", "is", "oil", "toil"};
        List<String> results = new ArrayList<>();
        PuzzleUtils.generateSplits("artistoil", dict, results::add);

        List<String> expected = Arrays.asList("art is toil", "artist oil");
        assertTrue(results.containsAll(expected), "Expected splits not found");
        assertEquals(expected.size(), results.size(), "Split count mismatch");
    }

    @Test
    public void testPreviousPermutation() {
        int[] arr = {1, 5, 6, 2, 3, 4};
        PuzzleUtils.previousPermutation(arr);
        assertArrayEquals(new int[]{1, 5, 4, 6, 3, 2}, arr, "Previous permutation mismatch");
    }
}
