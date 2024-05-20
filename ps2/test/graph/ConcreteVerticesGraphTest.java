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

    //
    // Testing strategies for add(vertex) -> result
    // vertex: existent or not
    // result: true, false
    //

    // This test covers adding new vertices to a graph
    @Test
    public void testAddNewVertices(){
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();

        assertTrue(graph.add("A"));
        assertTrue(graph.add("B"));

        assertEquals("expect vertices \"A\", \"B\"", new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
    }

    // This test covers adding existing vertices to a graph
    @Test
    public void testAddExistVertices(){
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();

        assertTrue(graph.add("A"));
        assertTrue(graph.add("B"));
        assertFalse(graph.add("B"));

        assertEquals("expect vertices \"A\", \"B\"", new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
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

    @Test
    public void testAddSource(){
        Vertex v = new Vertex("A");
        // Add and update
        assertEquals(0, v.addSource("B", 1));
        assertEquals(1, v.addSource("B", 2));
        assertEquals(0, v.addSource("C", 3));
        assertEquals(3, v.addSource("C", 4));
        // Remove B from A's sources
        assertEquals(2, v.addSource("B", 0));
        assertEquals(0, v.addSource("B", 2));
        // Robustness test: weight < 0 should trigger edge removal
        assertEquals(2, v.addSource("B", -100));
        assertEquals(0, v.addSource("B", 2));
    }

    //
    // Testing strategies for addTarget(target, weight) -> result
    //
    // target: existent or non-existent sources
    // weight: <=0, >0
    //
    @Test
    public void testAddTarget(){
        Vertex v = new Vertex("A");
        // Add and update
        assertEquals(0, v.addTarget("B", 1));
        assertEquals(1, v.addTarget("B", 2));
        assertEquals(0, v.addTarget("C", 3));
        assertEquals(3, v.addTarget("C", 4));
        // Remove B from A's sources
        assertEquals(2, v.addTarget("B", 0));
        assertEquals(0, v.addTarget("B", 2));
        // Robustness test: weight < 0 should trigger edge removal
        assertEquals(2, v.addTarget("B", -100));
        assertEquals(0, v.addTarget("B", 2));
    }

    //
    // Testing strategies for toString() -> info
    // Note that empty vertex info was tested in the constructor tests
    //
    // sources.size: 0, 1, >1
    // targets.size: 0, 1, >1
    //

    // This test covers sources.size=1, targets.size=1
    @Test
    public void testSingleSourceSingleTargets(){
        Vertex v = new Vertex("A");
        assertEquals(0, v.addSource("C", 1));
        assertEquals(0, v.addTarget("B", 3));
        String vInfo = "Vertex A: Src: {C=1}, Dst: {B=3}";
        assertEquals("expect string equality", vInfo, v.toString());
    }

    // This test covers sources.size>1, targets.size>1
    @Test
    public void testMultipleSourceMultipleTargets(){
        Vertex v = new Vertex("A");
        assertEquals(0, v.addSource("B", 1));
        assertEquals(0, v.addSource("C", 2));
        assertEquals(0, v.addTarget("D", 3));
        assertEquals(0, v.addTarget("E", 4));
        assertEquals(0, v.addTarget("F", 5));
        String vInfo = "Vertex A: Src: {B=1, C=2}, Dst: {D=3, E=4, F=5}";
        assertEquals("expect string equality", vInfo, v.toString());
    }
    
}
