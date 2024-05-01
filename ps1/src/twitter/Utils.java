package twitter;

// This file includes some useful functions for manipulating structures like HashSet and HashMap.

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Utils {
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

    /**
     * Sort the HashMap.keySet() using its values according to corresponding compareTo function
     *
     * @param map the HashMap being sorted
     * @return the sorted keys list using the HashMap.values() according to compareTo for the class
     *
     */

    public static List<String> mapKeySort(Map<String, Integer> map){
        List<String> sortedKeys = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return sortedKeys;
    }
}
