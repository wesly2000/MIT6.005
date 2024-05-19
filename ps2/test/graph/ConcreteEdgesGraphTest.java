/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */

    //
    // Testing strategy for ConcreteEdgesGraph(vertices)
    //
    // vertices.size: 0, 1, >1
    //

    // This test covers vertices.size=0
    @Test
    public void ConcreteEdgesGraphEmptyVerticesTest(){
        Set<String> vertices = new HashSet<>();
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph(vertices);

        assertTrue("expect empty vertex set", graph.vertices().isEmpty());
    }

    // This test covers vertices.size=1
    @Test
    public void ConcreteEdgesGraphSingleVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A"));
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph(vertices);

        assertEquals("expect singleton vertex set", 1, graph.vertices().size());
    }

    // This test covers vertices.size》1
    @Test
    public void ConcreteEdgesGraphMultiVerticesTest(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph(vertices);

        assertEquals("expect a vertex set with 3 elements", 3, graph.vertices().size());
        assertTrue("expect a vertex set containing \"A\", \"B\", \"C\"", graph.vertices().containsAll(vertices));
    }

    // Testing strategy for ConcreteEdgesGraph.toString() -> graphString
    // vertices.size: 0, >0
    // edges.size: 0, >0

    // This test covers vertices.size=0, edges.size=0
    @Test
    public void testEmptyConcreteEdgesGraphToString(){
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        String graphString = "Edge graph\n" +
                "\tvertices: [],\n" +
                "\tedges: []";
        assertEquals("expect string equality", graphString, graph.toString());
    }

    // This test covers vertices.size>0, edges.size=0
    @Test
    public void testEmptyEdgeConcreteEdgesGraphToString(){
        Set<String> vertices = new HashSet<>(Arrays.asList("A", "B", "C"));
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph(vertices);
        String graphString = "Edge graph\n" +
                "\tvertices: [A, B, C],\n" +
                "\tedges: []";
        assertEquals("expect string equality", graphString, graph.toString());
    }

    // This test covers vertices.size>0, edges.size>0
    @Test
    public void testNonEmptyConcreteEdgesGraphToString(){
        String graphString = "Edge graph\n" +
                                  "\tvertices: [A, B, C],\n" +
                                  "\tedges: [A->B: 2, A->C: 4, C->B: 6]";
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("A", "C", 4);
        graph.set("C", "B", 6);
        assertEquals("expect string equality", graphString, graph.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //   TODO
    
    // TODO tests for operations of Edge
    // This test covers the creator of Edge
    @Test
    public void testEdgeConstructor(){
        Edge e = new Edge("A", "B", 2);

        assertEquals("Source should be A", "A", e.getSource());
        assertEquals("Target should be B", "B", e.getTarget());
        assertEquals("Weight should be 2", 2, e.getWeight());
    }

    // This test covers edge toString()
    @Test
    public void testEdgeToString(){
        Edge e = new Edge("A", "B", 2);
        assertEquals("expect string equality", "A->B: 2", e.toString());
    }

    // This test covers checkRep()
    @Test
    public void testCheckRep(){
        AssertionError e = assertThrows(AssertionError.class,
                () -> new Edge("A", "B", -2)
        );

        assertEquals("Illegal weight in edge: " + "A->B: -2" + ", edge weight should be positive", e.getMessage());
    }
}
