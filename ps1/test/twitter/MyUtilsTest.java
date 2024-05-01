package twitter;

import org.junit.Test;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyUtilsTest {

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

        assertTrue("expect empty set", Utils.caseInsensitiveSetUnion(set1, set2).isEmpty());
    }

    // This test covers set1.size>0, set2.size=0, result.size>0
    @Test
    public void testSet1EmptySet2(){
        Set<String> set1 = new HashSet<String>(Arrays.asList("Aaa", "bes", "ca"));
        Set<String> set2 = new HashSet<String>();

        assertEquals("expect a set with 3 elements", 3, Utils.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"Aaa\", \"bes\", \"ca\"", Utils.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("Aaa", "bes", "ca")));
    }

    // This test covers set1.size>0, set2.size=0, result.size>0
    @Test
    public void testEmptySet1Set2(){
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>(Arrays.asList("Aaa", "bes", "ca"));

        assertEquals("expect a set with 3 elements", 3, Utils.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"Aaa\", \"bes\", \"ca\"", Utils.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("Aaa", "bes", "ca")));
    }

    // This test covers set1.size>0, set2.size>0, result.size>0
    @Test
    public void testSet1Set2(){
        Set<String> set1 = new HashSet<String>(Arrays.asList("aaa", "Bea", "Coo", "dawn"));
        Set<String> set2 = new HashSet<String>(Arrays.asList("AAA", "beA", "cOO", "Ea"));

        assertEquals("expect a set with 3 elements", 5, Utils.caseInsensitiveSetUnion(set1, set2).size());
        assertTrue("expect a set of \"aaa\", \"Bea\", \"Coo\", \"dawn\", \"Ea\"", Utils.caseInsensitiveSetUnion(set1, set2).containsAll(Arrays.asList("aaa", "Bea", "Coo", "dawn", "Ea")));
    }

    //
    // Testing strategies for mapKeySort(map) -> result
    //
    // map.size: 0, 1, >1
    // result.size: 0, 1, >1
    //

    // This test covers map.size=0, result.size=0
    @Test
    public void testMapKeySortEmptyMapEmptyResult(){
        Map<String, Integer> map = new HashMap<>();

        assertTrue("expect empty sorted key list", Utils.mapKeySort(map).isEmpty());
    }

    // This test covers map.size=1, result.size=1
    @Test
    public void testMapKeySortSingleMapSingleResult(){
        Map<String, Integer> map = new HashMap<>();
        map.put("Key 1", 0);

        assertEquals("expect a singleton map", 1, Utils.mapKeySort(map).size());
        assertEquals("expect \"Key 1\" in the result", Arrays.asList("Key 1"), Utils.mapKeySort(map));
    }

    // This test covers map.size>1, result.size>1
    @Test
    public void testMapKeySortMultipleMapMultipleResult(){
        Map<String, Integer> map = new HashMap<>();
        map.put("Key 1", 0);
        map.put("Key 2", 2);
        map.put("Key 3", 1);
        map.put("Key 4", 1);

        assertEquals("expect [\"Key 1\", \"Key 3\", \"Key 4\", \"Key 2\"]", Arrays.asList("Key 1", "Key 3", "Key 4", "Key 2"), Utils.mapKeySort(map));
    }
}
