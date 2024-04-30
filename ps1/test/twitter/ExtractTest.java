/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T13:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "aba", "Hi, @-ONE123, I'm @two666, is xYz@one123 yours email? I like @ symbol.", d3);
    private static final Tweet tweet4 = new Tweet(4, "xyz", "I'm @-one123, 456@-one123 is my email, not Xyz@one123.", d4);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Testing strategy:
     *
     * Partition for Extract.getTimespan(tweets) -> result:
     *
     * tweets.size: 0, 1, >1,
     * when tweets.size is 0, an exception is expected.
     * result.start = result.end, result.start < result.end
     *
     */

    // This test covers tweets.size=0, throwing an exception.
    @Test
    public void testGetTimespanEmptyTweets() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Extract.getTimespan(Arrays.asList())
        );

        assertEquals("tweets is empty", e.getMessage());
    }
    // This test covers tweets.size=1, result.start = result.end
    @Test
    public void testGetOneTweets(){
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected same start and end", timespan.getEnd(), timespan.getStart());
    }
    // This test covers tweets.size > 1, result.start < result.end
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    //
    // Testing strategy:
    //
    // Partition for getMentionedUsers(tweets) -> result:
    // tweets.size: 0, 1, >1
    // tweets whose text contains abc@xyz, @-abc, @, @123abc
    // tweets whose text contains duplicates with the same lower-case form.
    // result.size: 0, >0
    //

    // This test covers tweets.size=0, result.size=0
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    // This test covers tweets.size>0, result.size>0
    // tweets have duplicates with the same lower-case form
    // and patterns like abc@xyz, @-abc, @, @123abc.
    @Test
    public void testGetMentionedUsersMention(){
        Set<String> mentionedUsers = new HashSet<>(Arrays.asList("-ONE123", "two666"));
        assertEquals("expected the same mentioned users", mentionedUsers, Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2, tweet3, tweet4)));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
