/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */

    //
    // Testing strategy for ConcreteVerticesGraph(vertices)
    //
    // vertices.size: 0, 1, >1
    //

    // This test covers vertices.size=0
    @Test
    public void ConcreteVerticesGraphEmptyVerticesTest(){
        Set<String> vertices = new HashSet<>();
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph(vertices);

        assertTrue("expect empty vertex set", graph.vertices().isEmpty());
    }

    // This test covers vertices.size=1
    @Test
    public void ConcreteVerticesGraphSingleVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A"));
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph(vertices);

        assertEquals("expect singleton vertex set", 1, graph.vertices().size());
    }

    // This test covers vertices.size>1
    @Test
    public void ConcreteVerticesGraphMultiVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph(vertices);

        assertEquals("expect a vertex set with 3 elements", 3, graph.vertices().size());
        assertTrue("expect a vertex set containing \"A\", \"B\", \"C\"", graph.vertices().containsAll(vertices));
    }

    // Testing strategy for ConcreteVerticesGraph.toString()
    //   TODO
    
    // TODO tests for ConcreteVerticesGraph.toString()
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   TODO
    @Test
    public void testVertexConstructor(){
        Vertex v = new Vertex("A");
        String vInfo = "Vertex A: Src: {}, Dst: {}";

        assertEquals("expect string equality", vInfo, v.toString());
    }

    // TODO tests for operations of Vertex
    //
    // Testing strategies for addSource(source, weight) -> result
    //
    // source: existent or non-existent sources
    // weight: <=0, >0
    //

//    @Test
//    public void testAddSource(){
//        Vertex v = new Vertex("A");
//    }

    // TODO: test toString

    
}
