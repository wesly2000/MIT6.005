/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.*;

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

        assertTrue("expect empty map", graph.sources("D").isEmpty());
        assertTrue("expect empty map", graph.sources("A").isEmpty());
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

        assertTrue("expect empty map", graph.targets("D").isEmpty());
        assertTrue("expect empty map", graph.targets("B").isEmpty());
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

    // This test integrates add, set, remove, sources and targets
    @Test
    public void testConcreteEdgesGraphIntegrated(){
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.set("C", "B", 2);

        assertEquals("expect vertices \"A\", \"B\", \"C\"", new HashSet<>(Arrays.asList("A", "B", "C")), graph.vertices());

        int prevWeight = graph.set("C", "B", 4);
        assertEquals("expect 2 previous weight", 2, prevWeight);

        graph.set("A", "B", 2);
        graph.set("A", "C", 6);

        Map<String, Integer> mapSources = new HashMap<>();
        mapSources.put("A", 2);
        mapSources.put("C", 4);
        assertEquals("expect map {\"A\": 2, \"C\": 4}", mapSources, graph.sources("B"));

        Map<String, Integer> mapTargets = new HashMap<>();
        mapTargets.put("B", 2);
        mapTargets.put("C", 6);
        assertEquals("expect map {\"B\": 2, \"C\": 6}", mapTargets, graph.targets("A"));
    }
}
