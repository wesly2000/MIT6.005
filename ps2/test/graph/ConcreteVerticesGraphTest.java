/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

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
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>(vertices);

        assertTrue("expect empty vertex set", graph.vertices().isEmpty());
    }

    // This test covers vertices.size=1
    @Test
    public void ConcreteVerticesGraphSingleVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A"));
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>(vertices);

        assertEquals("expect singleton vertex set", 1, graph.vertices().size());
    }

    // This test covers vertices.size>1
    @Test
    public void ConcreteVerticesGraphMultiVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>(vertices);

        assertEquals("expect a vertex set with 3 elements", 3, graph.vertices().size());
        assertTrue("expect a vertex set containing \"A\", \"B\", \"C\"", graph.vertices().containsAll(vertices));
    }


    //
    // Testing strategy for ConcreteVerticesGraph.toString() -> graphString
    // vertices.size: 0, >0
    // edges.size: 0, >0
    //

    // This test covers vertices.size=0, edges.size=0
    @Test
    public void testEmptyConcreteVerticesGraphToString(){
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>();
        String graphString = "Vertices graph\n" +
                "\tVertices: [],\n" +
                "\tEdges:\n";
        assertEquals("expect string equality", graphString, graph.toString());
    }

    // This test covers vertices.size>0, edges.size=0
    @Test
    public void testEmptyEdgeConcreteVerticesGraphToString(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>(vertices);
        String graphString = "Vertices graph\n" +
                "\tVertices: [A, B, C],\n" +
                "\tEdges:\n" +
                "\tVertex A: Src: {}, Dst: {};\n" +
                "\tVertex B: Src: {}, Dst: {};\n" +
                "\tVertex C: Src: {}, Dst: {};\n";
        assertEquals("expect string equality", graphString, graph.toString());
    }

    // This test covers vertices.size>0, edges.size>0
    @Test
    public void testEdgeConcreteVerticesGraphToString(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteVerticesGraph<String> graph = new ConcreteVerticesGraph<>(vertices);
        graph.set("A", "B", 2);
        graph.set("A", "C", 4);
        graph.set("C", "B", 6);
        String graphString = "Vertices graph\n" +
                "\tVertices: [A, B, C],\n" +
                "\tEdges:\n" +
                "\tVertex A: Src: {}, Dst: {B=2, C=4};\n" +
                "\tVertex B: Src: {A=2, C=6}, Dst: {};\n" +
                "\tVertex C: Src: {A=4}, Dst: {B=6};\n";
        assertEquals("expect string equality", graphString, graph.toString());
    }

    /*
     * Testing Vertex...
     */

    @Test
    public void testVertexConstructor(){
        Vertex<String> v = new Vertex<>("A");
        String vInfo = "Vertex A: Src: {}, Dst: {}";

        assertEquals("expect string equality", vInfo, v.toString());
    }



    //
    // Testing strategies for addSource(source, weight) -> result
    //
    // source: existent or non-existent sources
    // weight: <=0, >0
    //

    @Test
    public void testAddSource(){
        Vertex<String> v = new Vertex<>("A");
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
        Vertex<String> v = new Vertex<>("A");
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
        Vertex<String> v = new Vertex<>("A");
        assertEquals(0, v.addSource("C", 1));
        assertEquals(0, v.addTarget("B", 3));
        String vInfo = "Vertex A: Src: {C=1}, Dst: {B=3}";
        assertEquals("expect string equality", vInfo, v.toString());
    }

    // This test covers sources.size>1, targets.size>1
    @Test
    public void testMultipleSourceMultipleTargets(){
        Vertex<String> v = new Vertex<>("A");
        assertEquals(0, v.addSource("B", 1));
        assertEquals(0, v.addSource("C", 2));
        assertEquals(0, v.addTarget("D", 3));
        assertEquals(0, v.addTarget("E", 4));
        assertEquals(0, v.addTarget("F", 5));
        String vInfo = "Vertex A: Src: {B=1, C=2}, Dst: {D=3, E=4, F=5}";
        assertEquals("expect string equality", vInfo, v.toString());
    }

    //
    // Testing strategy for sources and targets
    // sources.size: 0, 1, >1
    // targets.size: 0, 1, >1
    //

    @Test
    public void testSources(){
        Vertex<String> v = new Vertex<>("A");
        Map<String, Integer> sources = new HashMap<>();
        // Test covers sources.size=0
        assertEquals(sources, v.sources());
        // Test covers sources.size=1
        v.addSource("B", 1);
        sources.put("B", 1);
        assertEquals(sources, v.sources());
        // Test covers sources.size>1
        v.addSource("C", 2);
        sources.put("C", 2);
        assertEquals(sources, v.sources());
    }

    @Test
    public void testTargets(){
        Vertex<String> v = new Vertex<>("A");
        Map<String, Integer> targets = new HashMap<>();
        // Test covers targets.size=0
        assertEquals(targets, v.targets());
        // Test covers targets.size=1
        v.addTarget("B", 1);
        targets.put("B", 1);
        assertEquals(targets, v.targets());
        // Test covers targets.size>1
        v.addTarget("C", 2);
        targets.put("C", 2);
        assertEquals(targets, v.targets());
    }

    // This test check if the sources() and targets() gives
    // defensive copies
    @Test
    public void testDefensiveCopies(){
        Vertex<String> v = new Vertex<>("A");
        v.addSource("B", 1);
        v.addSource("C", 2);
        v.addTarget("D", 3);
        v.addTarget("E", 4);
        Map<String, Integer> sourcesCopy = v.sources();
        Map<String, Integer> targetsCopy = v.targets();
        // Update
        sourcesCopy.put("B", 100);
        targetsCopy.put("D", 100);
        // Remove
        sourcesCopy.remove("C");
        targetsCopy.remove("E");
        // Add
        sourcesCopy.put("G", 500);
        targetsCopy.put("H", 500);

        Map<String, Integer> sourcesDefensive = new HashMap<>();
        sourcesDefensive.put("B", 1);
        sourcesDefensive.put("C", 2);

        Map<String, Integer> targetsDefensive = new HashMap<>();
        targetsDefensive.put("D", 3);
        targetsDefensive.put("E", 4);

        assertEquals(sourcesDefensive, v.sources());
        assertEquals(targetsDefensive, v.targets());
    }
}
