/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        // We use the implementation that if a user doesn't follow anyone, its following set
        // is empty, i.e., Map[A].isEmpty() is true.

        // We only implement the following relationship that if an author mentioned
        // someone in its tweet, the author follows him/her.
        Map<String, Set<String>> graph = new HashMap<>();
        Set<String> userOneTweet = new HashSet<>();
        for (Tweet tweet : tweets) {
            // We make all the usernames to their lower-cased form.
            userOneTweet = Utils.lowerCaseSet(Extract.getMentionedUserOneTweet(tweet));
            graph.merge(tweet.getAuthor().toLowerCase(), userOneTweet, Utils::caseInsensitiveSetUnion);
        }
        return graph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();
        for(String username : followsGraph.keySet()) {
            followerCount.put(username, followersOfOneUser(followsGraph, username).size());
        }

        List<String> influencers = Utils.mapKeySort(followerCount);
        Collections.reverse(influencers);
        return influencers;
    }

    /**
     * Find all the followers of a given user in a social network
     *
     * @param followsGraph a social network (as defined above)
     * @param username name of the user we find its distinct followers
     * @return a list of all distinct followers of the user
     *
     */
    public static Set<String> followersOfOneUser(Map<String, Set<String>> followsGraph, String username) {
        Set<String> followers = new HashSet<>();
        followsGraph.forEach(
                (user, mentionedUsers) -> {
                    if(! user.equals(username)) { // Self-mention is not counted
                        if (mentionedUsers.contains(username)) {
                            followers.add(user);
                        }
                    }
                }
        );
        return followers;
    }
}
