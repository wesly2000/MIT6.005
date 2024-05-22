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
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   a graph represented by this.vertices, each of which possesses a
    //   sources and targets map representing the inbound and outbound
    //   edges.
    // Representation invariant:
    //   Rep invariant of ConcreteVerticesGraph includes two points:
    //   1. Rep invariant of each vertex, i.e., all weights in its
    //      sources and targets map are positive;
    //   2. If A.sources contains B with weight w, then B.targets contains A with
    //      weight w, and vise versa.(inbound/outbound consistence)
    // Safety from rep exposure:
    //   All fields are private;
    //   vertices are mutable list, so vertices() only return the label set
    //   of the vertices, which will not affect this.vertices.

    public ConcreteVerticesGraph() {}

    public ConcreteVerticesGraph(Set<String> vertices) {
        for (String vertex : vertices) {
            this.vertices.add(new Vertex(vertex));
        }
    }

    /**
     * Check the rep invariant of a ConcreteVerticesGraph, which includes 2 steps:
     * 1. rep invariant of all vertices;
     * 2. If A.sources contains B with weight w, then B.targets contains A with
     *    weight w, and vise versa.(inbound/outbound consistence)
     */
    private void checkRep(){
        this.vertices.forEach(Vertex::checkRep);
        // Check vertex inbound/outbound consistence for each vertex
        this.vertices.forEach(vertex -> {
            // Check inbound consistence
            vertex.sources().forEach((label, weight) -> {
                Vertex sourceVertex = getVertex(label);
                // Check for link consistence
                assert sourceVertex.targets().containsKey(vertex.getLabel()): String.format("Missing target: %s has source %s, but" +
                                "%s has no target %s.",
                        vertex.getLabel(), label, sourceVertex.getLabel(), vertex.getLabel());
                // Check for weight consistence
                assert sourceVertex.targets().get(vertex.getLabel()).equals(weight) : String.format("Inbound and outbound weights mismatch:" +
                        "%s inbound weight is %d, but %s outbound weight is %d.",
                        vertex.getLabel(), weight, sourceVertex.getLabel(), sourceVertex.targets().get(vertex.getLabel())
                        );
            });
            // Check outbound consistence
            vertex.targets().forEach((label, weight) -> {
                Vertex targetVertex = getVertex(label);
                // Check for link consistence
                assert targetVertex.sources().containsKey(vertex.getLabel()): String.format("Missing target: %s has target %s, but" +
                        "%s has no source %s.",
                        vertex.getLabel(), label, targetVertex.getLabel(), vertex.getLabel());
                // Check for weight consistence
                assert targetVertex.sources().get(vertex.getLabel()).equals(weight) : String.format("Inbound and outbound weights mismatch:" +
                                "%s outbound weight is %d, but %s inbound weight is %d.",
                        vertex.getLabel(), weight, targetVertex.getLabel(), targetVertex.sources().get(vertex.getLabel())
                );
            });
        });
    }
    
    @Override public boolean add(String vertex) {
        if(this.vertices().contains(vertex)) return false;

        this.vertices.add(new Vertex(vertex));
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        int prevWeight = 0;
        if(weight > 0){
            // Add or update
            this.add(source);
            this.add(target);

            prevWeight = getVertex(source).addTarget(target, weight);
            getVertex(target).addSource(source, weight);
        }else{
            // If the edge exists, remove it
            if(this.vertices().contains(source) && this.vertices().contains(target)){
                // Note that when weight <= 0, if the edge exists, addTarget(addSource)
                // will remove that edge;
                // if the edge does not exist, do nothing
                prevWeight = getVertex(source).addTarget(target, weight);
                getVertex(target).addSource(source, weight);
            }
        }
        checkRep();
        return prevWeight;
    }
    
    @Override public boolean remove(String vertex) {
        if(!this.vertices().contains(vertex)) return false;
        // remove using set with weight=0
        this.vertices.forEach((v) -> {
            if(v.sources().containsKey(vertex))
                this.set(vertex, v.getLabel(), 0);
            if(v.targets().containsKey(vertex))
                this.set(v.getLabel(), vertex, 0);
        });
        this.vertices.remove(getVertex(vertex));

        checkRep();
        return true;
    }
    
    @Override public Set<String> vertices() {
        Set<String> verticesString = new HashSet<>();
        for (Vertex vertex : vertices) {
            verticesString.add(vertex.getLabel());
        }
        return verticesString;
    }

    /**
     *
     * @param vertex the label of an exising vertex
     * @return the underlying vertex
     */
    private Vertex getVertex(String vertex) {
        for(Vertex v: vertices)
            if(v.getLabel().equals(vertex)) return v;

        throw new RuntimeException("Vertex not found");
    }
    
    @Override public Map<String, Integer> sources(String target) {
        if(!vertices().contains(target)) throw new IllegalArgumentException("No such vertex: " + target);
        Vertex targetVertex = getVertex(target);
        return targetVertex.sources();
    }
    
    @Override public Map<String, Integer> targets(String source) {
        if(!vertices().contains(source)) throw new IllegalArgumentException("No such vertex: " + source);
        Vertex sourceVertex = getVertex(source);
        return sourceVertex.targets();
    }

    /**
     * A graph could be converted to a string, here is the format
     * "Vertex graph\n
     *      Vertices: [A, B, C],\n
     *      Edges:\n
     *      vertex A: Src: {B=3, C=4}, Dst: {};\n
     *      vertex B: Src: {}, Dst: {A=3};\n
     *      vertex C: Src: {}, Dst: {A=4};\n
     * "
     * @return a string that records vertices and edges
     */
    @Override public String toString(){
        String verticesString = this.vertices().toString() + ",\n";
        List<String> edges = new ArrayList<>();
        this.vertices.forEach(v -> {
            edges.add("\t" + v.toString() + ";\n");
        });
        return "Vertices graph\n" +
                "\tVertices: " + verticesString +
                "\tEdges:\n" +
                String.join("", edges);
    }
}

/**
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * A vertex contains a label(String), sources and targets(Map)
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {

    private String label;
    private Map<String, Integer> sources = new HashMap<>();
    private Map<String, Integer> targets = new HashMap<>();
    
    // Abstraction function:
    //   Represent a vertex with label(String), all its sources(Map) and
    //   all its targets(Map), which consists of source(target) label
    //   along with its positive weight
    // Representation invariant:
    //   all weights must be positive
    // Safety from rep exposure:
    //   Since label is String, it is guaranteed to be immutable;
    //   the sources and targets are maps of String and Integer, which
    //   are both immutable. Therefore, a shallow copy of them is defensive
    //   copy.

    public Vertex(String label) { this.label = label; }
    
    // checkRep() checks that every weight in the sources/targets > 0
    public void checkRep(){
        // Check for sources
        sources.forEach((k, v) -> {
            assert v > 0: String.format("The weight with %s is %d <= 0", k, v);
        });
        // Check for targets
        targets.forEach((k, v) -> {
            assert v > 0: String.format("The weight with %s is %d <= 0", k, v);
        });
    }
    

    public String getLabel() { return label; }

    public Map<String, Integer> sources() { return new HashMap<>(sources); }

    public Map<String, Integer> targets() { return new HashMap<>(targets); }

    /**
     * Add a source vertex to the vertex with weight, if the source exists
     *      if weight > 0, update the weight
     *      if weight = 0, remove the source
     * if the source does not exist
     *      if weight > 0, add the source to its sources
     *      if weight < 0, do nothing
     *
     * @param source the source added
     * @param weight a non-negative weight
     * @return if the source already exists, return the previous weight,
     *          otherwise, return 0
     */
    public int addSource(String source, int weight) {
        // Here we follow the traditional get-put workflow to get the boolean return,
        // which could be used directly in set() of graph
        int prevWeight = 0;

        if(weight > 0){
            if(this.sources.containsKey(source))
                prevWeight = this.sources.get(source);
            this.sources.put(source, weight);
        }
        else{
            if(this.sources.containsKey(source)){
                prevWeight = this.sources.get(source);
                this.sources.remove(source);
            }
        }

        checkRep();
        return prevWeight;
    }

    /**
     * Add a target vertex to the vertex with weight, if the target exists
     *      if weight > 0, update the weight
     *      if weight = 0, remove the target
     * if the target does not exist
     *      if weight > 0, add the target to its targets
     *      if weight < 0, do nothing
     *
     * @param target the target added
     * @param weight a non-negative weight
     * @return if the target already exists, return the previous weight,
     *          otherwise, return 0
     */
    public int addTarget(String target, int weight) {
        int prevWeight = 0;

        if(weight > 0){
            if(this.targets.containsKey(target))
                prevWeight = this.targets.get(target);
            this.targets.put(target, weight);
        }
        else{
            if(this.targets.containsKey(target)){
                prevWeight = this.targets.get(target);
                this.targets.remove(target);
            }
        }

        checkRep();
        return prevWeight;
    }

    /**
     * The string rep of a vertex is like:
     *
     * Vertex A: Src: {B=2, C=3}, Dst: {D=4}
     *
     * @return the string that represents the vertex info
     */
    @Override public String toString(){
        return "Vertex " + label + ": " + "Src: " + sources.toString() +
                ", Dst: " + targets.toString();
    }
}
