package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyExtractTest {
    // This test includes tests for customized util functions, which is not included in the problem set.
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much? @-one123 and " +
            "berk@mime are connecting me. @-ONE123 says that @Alice123 is talking about @Mime and @mime goes out.", d1);
    private static final Tweet tweet2 = new Tweet(2, "alyssa", "is it reasonable to talk about rivest so much?", d1);


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //
    // Testing strategies for caseInsensitiveSetUnion(set1, set2) -> result
    //
    // set1.size: 0, >0
    // set2.size: 0, >0
    // result.size: 0, >0
    //

    // This test covers set1.size=0, set2.size=0, result.size=0
    @Test
    public void testEmptySet1EmptySet2(){
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();

        assertTrue("expect empty set", Extract.caseInsensitiveSetUnion(set1, set2).isEmpty());
    }

    // This test covers set1.size>0, set2.size=0, result.size>0
    @Test
    public void testSet1EmptySet2(){
        Set<String> set1 = new HashSet<String>(Arrays.asList("Aaa", "bes", "ca"));
        Set<String> set2 = new HashSet<String>();

        assertEquals("expect a set with 3 elements", 3, Extract.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"Aaa\", \"bes\", \"ca\"", Extract.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("Aaa", "bes", "ca")));
    }

    // This test covers set1.size>0, set2.size=0, result.size>0
    @Test
    public void testEmptySet1Set2(){
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>(Arrays.asList("Aaa", "bes", "ca"));

        assertEquals("expect a set with 3 elements", 3, Extract.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"Aaa\", \"bes\", \"ca\"", Extract.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("Aaa", "bes", "ca")));
    }

    // This test covers set1.size>0, set2.size>0, result.size>0
    @Test
    public void testSet1Set2(){
        Set<String> set1 = new HashSet<String>(Arrays.asList("aaa", "Bea", "Coo", "dawn"));
        Set<String> set2 = new HashSet<String>(Arrays.asList("AAA", "beA", "cOO", "Ea"));

        assertEquals("expect a set with 3 elements", 5, Extract.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"aaa\", \"Bea\", \"Coo\", \"dawn\", \"Ea\"", Extract.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("aaa", "Bea", "Coo", "dawn", "Ea")));
    }

    //
    // Testing strategies for getMentionedUserOneTweet(tweet) -> result
    //
    // result.size: 0, >0
    //

    // This test covers result.size>0
    @Test
    public void testGetMentionedUserOneTweetMultipleResult(){
        assertEquals("expect 3 elements in result", 3, Extract.getMentionedUserOneTweet(tweet1).size());
        assertTrue("expect \"@-one123\", \"@Alice123\", \"@Mime\"", Extract.getMentionedUserOneTweet(tweet1).containsAll(Arrays.asList("@-one123", "@Alice123", "@Mime")));
    }

    // This test covers result.size=0
    @Test
    public void testGetMentionedUserOneTweetEmptyResult(){
        assertTrue("expect empty result", Extract.getMentionedUserOneTweet(tweet2).isEmpty());
    }
}
