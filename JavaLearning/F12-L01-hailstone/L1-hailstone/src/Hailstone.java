import java.util.ArrayList;
import java.util.List;

class Tortoise{
	// Different from Python,
	// Accessing class variable through instance and changing it
	// also affect the class variable.
	public static int someStaticVariable = 0;
	public int position = 1;

	public Tortoise() {}

	public Tortoise(int position){
		this.position = position;
	}
	public void forward(){
		position += 1;
	}
}

public class Hailstone {

    /**
     * Compute the hailstone sequence.
     * See http://en.wikipedia.org/wiki/Collatz_conjecture#Statement_of_the_problem
     * @param n starting number of sequence
     * @return the hailstone sequence starting at n and ending with 1.
     * For example, hailstone(3)=[3,10,5,16,8,4,2,1].
     */
	// n changes its reference.
    public static List<Integer> hailstone(int n) {
		// Add final to l is OK, since l always refer to the same address (the head of the array)
        final List<Integer> l = new ArrayList<Integer>();
        
        while (n != 1) {
            l.add(n);
            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
        }        
        
        l.add(n); // don't forget the final 1
        return l;
    }

	/**
	 * Find the largest element in a list.
	 * @param l list of elements.  Requires l to be nonempty 
	 * and all elements to be nonnegative.
	 * @return the largest element in l
	 */
	public static int max(List<Integer> l) {
		int m = 0;
		for (int x : l) {
			if (x > m) {
				m = x;
			}
		}
		return m;
	}

	public static String getType(Object o){
		return o.getClass().toString();
	}

	public static int LONG_WORD_LENGTH = 5;
	public static String longestWord;

	public static void countLongWords(final List<String> words) {
		int n = 0;
		longestWord = "";
		for (String word: words) {
			if (word.length() > LONG_WORD_LENGTH) ++n;
			if (word.length() > longestWord.length()) longestWord = word;
		}
		System.out.println(n);
	}
	
	/**
	 * Main program.  Print the peak of the hailstone
	 * sequence for a range of starting n's.
	 */
	public static void main(String[] args) {
//		for (int n = 1; n < 100; n++) {
//		    List<Integer> l = hailstone(n);
//		    System.out.println(n + " >> " + max(l));
//		}
//		List<List<Character>> grid = new ArrayList<>();
		List<String> words = new ArrayList<>();
		words.add("Hello");
		words.add("World");
		countLongWords(words);
	}
}
