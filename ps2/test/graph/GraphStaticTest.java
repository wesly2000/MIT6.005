/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // TODO test other vertex label types in Problem 3.2
    //
    // Testing strategies for add(vertex) -> result
    // vertex: existent or not
    // result: true, false
    //
    @Test
    public void testIntegerLabelGraph(){
        Graph<Integer> graph = Graph.empty();

        graph.add(1);
        assertEquals(new HashSet<>(Arrays.asList(1)), graph.vertices());
        graph.add(2);
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), graph.vertices());

        assertFalse("Duplicate adding should return false", graph.add(1));
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), graph.vertices());
    }

    //
    // Test strategy for set(source, target, weight) -> prevWeight:
    //
    // num of newly introduced vertices: 0, 1, 2
    // weight: 0, >0
    // prevWeight: 0, >0
    //

    // This test covers adding new edge to a graph, with 1-2 new vertices;
    // and we add an edge with 0 weight, which should have no effect.
    @Test
    public void testSetAddEdge(){
        Graph<Integer> graph = Graph.empty();
        int prevWeight = graph.set(1, 2, 2);

        assertEquals("expect vertices 1, 2", new HashSet<>(Arrays.asList(1, 2)), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set(3, 2, 4);

        assertEquals("expect vertices 1, 2, 3", new HashSet<>(Arrays.asList(1, 2, 3)), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set(4, 5, 0);

        assertEquals("expect vertices 1, 2, 3", new HashSet<>(Arrays.asList(1, 2, 3)), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set(4, 5, 5);

        assertEquals("expect vertices 1, 2, 3, 4, 5", new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);
    }
}
