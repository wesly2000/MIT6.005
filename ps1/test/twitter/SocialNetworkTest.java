/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;

public class SocialNetworkTest {

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
    private static final Tweet tweet3 = new Tweet(3, "ABA", "Hi, @-ONE123, I'm @TWO666, is xYz@one123 yours email? I like @ symbol.", d3);
    private static final Tweet tweet4 = new Tweet(4, "aba", "I'm @-one123, 456@-one123 is my email, not Xyz@one123.", d4);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //
    // Testing strategies for guessFollowsGraph(tweets) -> result:
    //
    // tweets.size: 0, >0
    // result.size: 0, >0
    // result[key].size: 0, >0
    //

    // This test covers tweets.size=0, result.size=0
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // This test covers tweets.size>0, result.size>0, result[key]=0 and result[key]>0
    @Test
    public void testGuessFollowsGraph(){
        Map<String, Set<String>> trueGraph = new HashMap<>();
        trueGraph.put("alyssa", new HashSet<>());
        trueGraph.put("bbitdiddle", new HashSet<>());
        trueGraph.put("aba", new HashSet<>(Arrays.asList("-one123", "two666")));
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4));

        assertEquals("expect 3 authors", 3, followsGraph.size());
        assertTrue("expect alyssa, bbitdiddle, aba in the key set", followsGraph.keySet().containsAll(Arrays.asList("alyssa", "bbitdiddle", "aba")));

        assertEquals("expect equal following users", followsGraph, trueGraph);
    }

    //
    // Testing strategy for influencers(followsGraph) -> result:
    //
    // followsGraph.size: 0, 1, >1
    // result.size: 0, 1, >1
    //

    // This test covers followGraph.size=0, result.size=0
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // This test covers followGraph.size=1, result.size=1
    @Test
    public void testInfluencersSingleResult(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", new HashSet<>(Arrays.asList("alyssa")));

        assertEquals("expect list [\"alyssa\"]", Arrays.asList("alyssa"), SocialNetwork.influencers(followsGraph));
    }

    // This test covers followGraph.size>1, result.size>1
    @Test
    public void testInfluencersMultipleResults(){
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", new HashSet<>(Arrays.asList("alyssa", "cindy")));
        followsGraph.put("bob", new HashSet<>(Arrays.asList("alyssa", "cindy")));
        followsGraph.put("cindy", new HashSet<>(Arrays.asList("bob", "eve")));
        followsGraph.put("dave", new HashSet<>(Arrays.asList("alyssa", "eve")));
        followsGraph.put("eve", new HashSet<>());

        assertEquals("expect list [\"cindy\", \"eve\", \"alyssa\", \"bob\", \"dave\"]",
                Arrays.asList("cindy", "eve", "alyssa", "bob", "dave"),
                SocialNetwork.influencers(followsGraph));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
