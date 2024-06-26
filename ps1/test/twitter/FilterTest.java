/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-03-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "rivest talk in 30 minutes #hype", d3);
    private static final Tweet tweet4 = new Tweet(4, "BbitdiDDle", "rivest talk in 30 minutes #hype", d4);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "SOFTWARE construction is INTERESting! A TALK", d1);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "Is it good to write Java? Talking about C.", d2);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //
    // Testing strategy for writtenBy(tweets, username) -> result
    //
    // tweets.size: 0, >0
    // username: already exists in tweets or not, duplicates in lower-case form
    // result.size: 0, 1, >1
    //

    // Test covers tweets.size=0, result.size=0
    @Test
    public void testWrittenByEmptyTweetsEmptyResult(){
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    // Test covers tweets.size>0, existing username, result.size=1
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    // Test covers tweets.size>1, username exists, result.size>1
    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4), "BbitdiDDle");

        assertEquals("expected 3 tweets list", 3, writtenBy.size());
        assertTrue("expected list to contain tweet 2", writtenBy.contains(tweet2));
        assertTrue("expected list to contain tweet 3", writtenBy.contains(tweet3));
        assertTrue("expected list to contain tweet 4", writtenBy.contains(tweet4));
    }

    // Test covers tweets.size>1, username doesn't exist, result.size=0
    @Test
    public void testWrittenByMultipleTweetsEmptyResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "xyz");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    //
    // Testing strategy for inTimespan(tweets, timespan) -> result:
    //
    // result.size: 0, 1, >1
    //

    // This test covers result.size=0
    @Test
    public void testInTimespanMultipleTweetsEmptyResults(){
        Instant testStart = Instant.parse("2016-02-17T11:20:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));

        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    // This test covers result.size=1
    @Test
    public void testInTimespanMultipleTweetsSingleResults(){
        Instant testStart = Instant.parse("2016-02-17T10:20:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));

        assertEquals("expected singleton list", 1, inTimespan.size());
        assertTrue("expected list to contain tweet 2", inTimespan.contains(tweet2));
    }

    // This test covers result.size>1
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2, tweet3)));
        assertEquals("expected same order", 1, inTimespan.indexOf(tweet2));
    }

    //
    // Testing strategy for containing(tweets, words) -> result:
    //
    // words: including duplicates or not, case-insensitive or not
    // words.size: 0, 1, >1
    // result.size: 0, 1, >1
    //

    // Test covers no duplicates, words.size=1, result.size>1
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    // Test covers no duplicates(also test case-sensitivity), words.size>1, result.size>1
    @Test
    public void testContainingNoDuplicatesCaseMultipleWordsMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet5, tweet6), Arrays.asList("Talk", "software", "Construction"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet5)));
        assertEquals("expected same order", 2, containing.indexOf(tweet5));
    }

    // Test covers duplicates(also test case-sensitivity), words.size>1, result.size>1
    @Test
    public void testContainingDuplicatesCaseMultipleWordsMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet5, tweet6), Arrays.asList("Is", "is"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet5, tweet6)));
        assertEquals("expected same order", 1, containing.indexOf(tweet5));
    }

    // Test covers no duplicates, words.size>1, result.size=0
    @Test
    public void testContainingNoDuplicatesCaseSingleWordsNoResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet5, tweet6), Arrays.asList("Python"));

        assertTrue("expected non-empty list", containing.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */



}
