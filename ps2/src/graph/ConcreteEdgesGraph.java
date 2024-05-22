/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   represents a graph whose vertices and edges are defined by
    //   vertices and edges, respectively.
    // Representation invariant:
    //   edge weight must be positive
    // Safety from rep exposure:
    //   All fields are private;
    //   vertices is mutable Set, so vertices() returns defensive copies of the vertices.

    public ConcreteEdgesGraph() {}

    public ConcreteEdgesGraph(Set<String> vertices) {
        this.vertices.addAll(vertices);

        checkRep();
    }

    // WARNING: Class 'Edge' is exposed outside its defined visibility scope
//    public ConcreteEdgesGraph(List<Edge> edges) {
//        for (Edge edge : edges) {
//            this.set(edge.getSource(), edge.getTarget(), edge.getWeight());
//        }
//    }
    

    private void checkRep(){
        edges.forEach(Edge::checkRep);
    }
    
    @Override public boolean add(String vertex) {
        if (vertices.contains(vertex)) return false;
        vertices.add(vertex);

        checkRep();

        return true;
    }

    @Override public int set(String source, String target, int weight) {
        for(int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            // The edge already exists.
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                int prevWeight = e.getWeight();
                if(weight > 0)
                    edges.set(i, new Edge(source, target, weight));
                else
                    edges.remove(e);

                checkRep();

                return prevWeight;
            }
        }
        // There is no such edge.
        if(weight > 0){
            // First add the end points of the edge if they don't exist.
            vertices.add(source);
            vertices.add(target);
            Edge e = new Edge(source, target, weight);
            edges.add(e);
        }

        checkRep();

        return 0;
    }
    
    @Override public boolean remove(String vertex) {
        // First remove all the edges which source from or target to the vertex
        if(!vertices.contains(vertex)) return false;
        for(int i = edges.size() - 1; i >= 0; i--) {
            Edge e = edges.get(i);
            if(e.getSource().equals(vertex))
                this.set(vertex, e.getTarget(), 0);
            else if (e.getTarget().equals(vertex))
                this.set(e.getSource(), vertex, 0);
        }
        vertices.remove(vertex);

        checkRep();

        return true;
    }
    
    @Override public Set<String> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override public Map<String, Integer> sources(String target) {
        if(!vertices().contains(target)) throw new IllegalArgumentException("No such vertex: " + target);
        Map<String, Integer> map = new HashMap<>();
        edges.stream()
                .filter(edge -> edge.getTarget().equals(target))
                .forEach((e) -> map.put(e.getSource(), e.getWeight()));
        return map;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        if(!vertices().contains(source)) throw new IllegalArgumentException("No such vertex: " + source);
        Map<String, Integer> map = new HashMap<>();
        edges.stream()
                .filter(edge -> edge.getSource().equals(source))
                .forEach((e) -> map.put(e.getTarget(), e.getWeight()));
        return map;
    }
    

    /**
     * A graph could be converted to a string, here is the format
     * "Edge graph\n
     *      vertices: [A, B, C],\n
     *      edges: [A->B: 2, A->C: 4, C->A: 6]
     * "
     * @return a string that records vertices and edges
     */
    @Override public String toString() {
        String vertices = vertices().toString();
        List<String> edges = new ArrayList<>();
        this.edges.forEach((e) -> edges.add(e.toString()));
        return "Edge graph\n" +
                "\tvertices: " + vertices + ",\n" +
                "\tedges: " + edges.toString();
    }
}

/**
 *
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {
    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   represents a directed edge in graph, from source vertex to the target vertex
    // Representation invariant:
    //   weight > 0
    // Safety from rep exposure:
    //   All fields are private;
    //   source and target are Strings, so are immutable;
    //   weight is int, which is immutable and could be change only through setWeight()
    

    public Edge(String source, String target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;

        checkRep();
    }

    public void checkRep(){
        assert weight > 0: "Illegal weight in edge: " + toString() + ", edge weight should be positive";
    }

    public String getSource() { return source; }
    public String getTarget() { return target; }
    public int getWeight() { return weight; }

    /**
     * An edge could be formated in src->dst: weight
     *
     * @return a string describing an edge
     */
    public String toString() {
        return (source + "->" + target + ": " + weight).replace("\"", "");
    }
}
