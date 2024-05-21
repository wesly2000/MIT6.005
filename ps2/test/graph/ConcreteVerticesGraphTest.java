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
        Graph<String> graph = emptyInstance();

        assertTrue(graph.add("A"));
        assertTrue(graph.add("B"));

        assertEquals("expect vertices \"A\", \"B\"", new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
    }

    // This test covers adding existing vertices to a graph
    @Test
    public void testAddExistVertices(){
        Graph<String> graph = emptyInstance();

        assertTrue(graph.add("A"));
        assertTrue(graph.add("B"));
        assertFalse(graph.add("B"));

        assertEquals("expect vertices \"A\", \"B\"", new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
    }

    //
    // Testing strategy for graph sources and targets
    // sources.size: 0, 1, >1
    // targets.size: 0, 1, >1
    //

    @Test
    public void graphSources(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B"));
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph(vertices);

        assertTrue(graph.sources("A").isEmpty());
        assertTrue(graph.sources("B").isEmpty());
    }

    //
    // Testing strategies for set(source, target, weight) -> prevWeight
    //
    // num of newly introduced vertices: 0, 1, 2
    // weight: 0, >0
    // prevWeight: 0, >0
    //

    // This test covers adding new edge to a graph, with 1-2 new vertices;
    // and we add an edge with 0 weight, which should have no effect.
    @Test
    public void testSetAddEdge(){
        Graph<String> graph = emptyInstance();
        int prevWeight = graph.set("A", "B", 2);

        assertEquals("expect vertices \"A\", \"B\"", new HashSet<>(Arrays.asList("A", "B")), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set("C", "B", 4);

        assertEquals("expect vertices \"A\", \"B\", \"C\"", new HashSet<>(Arrays.asList("A", "B", "C")), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set("D", "E", 0);

        assertEquals("expect vertices \"A\", \"B\", \"C\"", new HashSet<>(Arrays.asList("A", "B", "C")), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set("D", "E", 5);

        assertEquals("expect vertices \"A\", \"B\", \"C\", \"D\", \"E\"", new HashSet<>(Arrays.asList("A", "B", "C", "D", "E")), graph.vertices());
        assertEquals("expect 0 previous weight", 0, prevWeight);
    }

    // This test covers edge weight update
    @Test
    public void testSetUpdateEdge(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);

        int prevWeight = graph.set("A", "B", 3);
        assertEquals("expect 2 previous weight", 2, prevWeight);

        prevWeight = graph.set("A", "B", 7);
        assertEquals("expect 3 previous weight", 3, prevWeight);

        // Test if the edge is directed, this test should return 0
        prevWeight = graph.set("B", "C", 1);
        assertEquals("expect 0 previous weight", 0, prevWeight);

        prevWeight = graph.set("C", "B", 1);
        assertEquals("expect 4 previous weight", 4, prevWeight);
    }

    // This test covers edge removal
    @Test
    public void testSetRemoveEdge(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);

        int prevWeight = graph.set("A", "B", 0);
        assertEquals("expect 2 previous weight", 2, prevWeight);
        assertEquals("expect vertices \"A\", \"B\", \"C\"", new HashSet<>(Arrays.asList("A", "B", "C")), graph.vertices());

        prevWeight = graph.set("A", "B", 2);
        assertEquals("expect 0 previous weight", 0, prevWeight);
    }


    //
    // Test strategies for remove(vertex) -> result:
    //
    // vertex: exist or not in the graph
    // result: true, false
    //

    @Test
    public void testRemove(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        assertTrue("expect successful removal", graph.remove("B"));
        assertEquals("expect only \"A\", \"C\" in the vertices", new HashSet<>(Arrays.asList("A", "C")), graph.vertices());
        // Test if the edge is removed from the edges
        assertEquals("expect 0 previous weight", 0, graph.set("A", "B", 0));
        assertEquals("expect only \"A\", \"C\" in the vertices", new HashSet<>(Arrays.asList("A", "C")), graph.vertices());
        // Test that the edge A->C(6) is not affected
        assertEquals("expect 6 previous weight", 6, graph.set("A", "C", 2));
    }


    //
    // Testing strategies for sources(target) -> map:
    //
    // target: in or not in graph.vertices
    // map.size: 0, 1, >1
    //

    // This test covers target in and not in graph.vertices, map.size=0
    @Test
    public void testSourcesEmptySources(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        // This test should throw IllegalArgumentException since D does not exist
        // assertTrue("expect empty map", graph.targets("D").isEmpty());
        assertTrue("expect empty map", graph.sources("A").isEmpty());
        assertThrows("expect IllegalArgumentException",
                IllegalArgumentException.class,
                () -> graph.sources("D"));
    }

    // This test covers target in graph.vertices, map.size=1
    @Test
    public void testSourcesSingleSourceVertex(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        Map<String, Integer> map = new HashMap<>();
        map.put("A", 6);
        assertEquals("expect map {\"A\": 6}", map, graph.sources("C"));
    }

    // This test covers target in graph.vertices, map.size>1
    @Test
    public void testSourcesMultipleSourceVertex(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        Map<String, Integer> map = new HashMap<>();
        map.put("A", 2);
        map.put("C", 4);
        assertEquals("expect map {\"A\": 2, \"C\": 4}", map, graph.sources("B"));
    }

    //
    // Testing strategies for targets(source) -> map:
    //
    // source: in or not in graph.vertices
    // map.size: 0, 1, >1
    //

    // This test covers source in and not in graph.vertices, map.size=0
    @Test
    public void testTargetsEmptyTargets(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        // This test should throw IllegalArgumentException since D does not exist
        // assertTrue("expect empty map", graph.targets("D").isEmpty());
        assertTrue("expect empty map", graph.targets("B").isEmpty());
        assertThrows("expect IllegalArgumentException",
                IllegalArgumentException.class,
                () -> graph.targets("D"));
    }

    // This test covers source in graph.vertices, map.size=1
    @Test
    public void testTargetsSingleTargetVertex(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        Map<String, Integer> map = new HashMap<>();
        map.put("B", 4);
        assertEquals("expect map {\"B\": 4}", map, graph.targets("C"));
    }

    // This test covers source in graph.vertices, map.size>1
    @Test
    public void testTargetsMultipleTargetVertex(){
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("C", "B", 4);
        graph.set("A", "C", 6);

        Map<String, Integer> map = new HashMap<>();
        map.put("B", 2);
        map.put("C", 6);
        assertEquals("expect map {\"B\": 2, \"C\": 6}", map, graph.targets("A"));
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

    //
    // Testing strategy for sources and targets
    // sources.size: 0, 1, >1
    // targets.size: 0, 1, >1
    //

    @Test
    public void testSources(){
        Vertex v = new Vertex("A");
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
        Vertex v = new Vertex("A");
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
        Vertex v = new Vertex("A");
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
