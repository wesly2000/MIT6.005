/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets == null) throw new NullPointerException("tweets is null");
        if (tweets.isEmpty()) throw new IllegalArgumentException("tweets is empty");

        Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();
        for (Tweet tweet : tweets) {
            Instant currentTs = tweet.getTimestamp();
            if(currentTs.isBefore(start)){
                start = currentTs;
            }else if(currentTs.isAfter(end)){
                end = currentTs;
            }
        }
        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<String>();
        for (Tweet tweet : tweets) {
            // NOTE: caseInsensitiveSetUnion is not commutative, it reserves the first pattern encountered
            // For example, ("Add", "minus") UNION ("add", "divide") = ("Add", "minus", "divide")
            // But          ("add", "divide") UNION ("Add", "minus") = ("add", "minus", "divide")
            mentionedUsers = caseInsensitiveSetUnion(mentionedUsers, getMentionedUserOneTweet(tweet));
        }
        return mentionedUsers;
    }

    /**
     * Find all the usernames in a tweet, all the usernames with the same lower-case form are recorded only once.
     *
     * @param tweet the tweet to extract usernames within
     * @return all the mentioned user from a tweet, without duplicates which have the same lower-case form
     *
     */
    public static Set<String> getMentionedUserOneTweet(Tweet tweet){
        String regex = "(?<=\\s|^)@[-\\w]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tweet.getText());
        Set<String> mentionedUsers = new HashSet<>();
        Set<String> mentionedUsersLowerCase = new HashSet<>();

        String mentionedUser = "";
        while (m.find()) {
            mentionedUser = m.group();
            if (! mentionedUsersLowerCase.contains(mentionedUser.toLowerCase())) {
                mentionedUsers.add(mentionedUser);
                mentionedUsersLowerCase.add(mentionedUser.toLowerCase());
            }
        }
        return mentionedUsers;
    }

    /**
     * Union two string set with case-insensitive manner.
     *
     * @param set1 String set without case-different duplicates
     * @param set2 String set without case-different duplicates
     * @return the union set of set1, set2 ignoring case-different duplicates
     */
    public static Set<String> caseInsensitiveSetUnion(Set<String> set1, Set<String> set2) {
        Logger logger = Logger.getLogger("twitter");
        if(set1 == null && set2 == null){
            throw new IllegalArgumentException("All sets are null");
        }else if(set1 == null){
            logger.warning("Set 1 is null, return Set 2");
            return set2;
        }else if(set2 == null){
            logger.warning("Set 2 is null, return Set 1");
            return set1;
        }

        Set<String> unionSet = new HashSet<>();
        // Use a lower case set to filter duplicates with the same lower-case form.
        Set<String> set1LowerCase = lowerCaseSet(set1);
        for (String s : set1) {
            unionSet.add(s);
            set1LowerCase.add(s.toLowerCase());
        }
        for (String s : set2){
            if(! set1LowerCase.contains(s.toLowerCase())){
                unionSet.add(s);
            }
        }
        return unionSet;
    }

    /**
     * lower all the elements in a set of strings.
     *
     * @param set a set of strings.
     * @return a new set whose elements are lower-case form of the corresponding elements in the input set
     *
     */
    public static Set<String> lowerCaseSet(Set<String> set) {
        Set<String> lowerCaseSet = new HashSet<>();
        for (String s : set) {
            lowerCaseSet.add(s.toLowerCase());
        }
        return lowerCaseSet;
    }
}
