/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        // Use a lower case set to filter duplicates with the same lower-case form.
        Set<String> mentionedUsersLowerCase = new HashSet<String>();
        for (Tweet tweet : tweets) {
            getMentionedUserOneTweet(tweet, mentionedUsers, mentionedUsersLowerCase);
        }
        return mentionedUsers;
    }

    /**
     * Find all the usernames in a tweet and add them to the username sets.
     *
     * @param tweet
     * @param mentionedUsers
     * @param mentionedUsersLowerCase the lower-cased version of mentionedUsers,
     *                                used to filter duplicated usernames.
     */
    public static void getMentionedUserOneTweet(Tweet tweet, Set<String> mentionedUsers, Set<String> mentionedUsersLowerCase){
        String regex = "(?<=\\s|^)@[-\\w]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tweet.getText());
        String mentionedUser = null;
        while (m.find()) {
            mentionedUser = m.group();
            if (! mentionedUsersLowerCase.contains(mentionedUser.toLowerCase())) {
                mentionedUsers.add(mentionedUser);
                mentionedUsersLowerCase.add(mentionedUser.toLowerCase());
            }
        }
    }

}
