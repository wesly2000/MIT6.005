/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   TODO
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    // This test covers add 1-2 vertices to the graph
    @Test
    public void testAddVertices() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertEquals(new HashSet<>(Arrays.asList("A")), graph.vertices());
        graph.add("B");
        assertEquals(new HashSet<>(Arrays.asList("A", "B")), graph.vertices());

        assertFalse("Duplicate adding should return false", graph.add("A"));
        assertEquals(new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
    }

    //
    // Test strategy for set(source, target, weight) -> prevWeight:
    //
    // num of newly introduced vertices: 0, 1, 2
    // weight: 0, >0
    //

    @Test
    public void testSetUpdatesEdge(){

    }
}
