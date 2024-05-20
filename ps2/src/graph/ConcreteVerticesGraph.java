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
    //   TODO
    // Representation invariant:
    //   TODO
    // Safety from rep exposure:
    //   TODO
    
    // TODO constructor
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
     *    weight w, and vise versa.
     */
    private void checkRep(){
        this.vertices.forEach(Vertex::checkRep);

    }
    
    @Override public boolean add(String vertex) {
        if(this.vertices().contains(vertex)) return false;

        this.vertices.add(new Vertex(vertex));
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        this.add(source);
        this.add(target);

        Vertex sourceVertex = getVertex(source);
        Vertex targetVertex = getVertex(target);

        int weightSrcToDst = sourceVertex.addTarget(target, weight);
        int weightDstFromSrc = targetVertex.addSource(source, weight);

        assert weightSrcToDst == weightDstFromSrc: "The weight outbound and inbound is not equal";

        checkRep();
        return weightDstFromSrc;
    }
    
    @Override public boolean remove(String vertex) {
        throw new RuntimeException("not implemented");
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
        Vertex targetVertex = getVertex(target);
        return targetVertex.sources();
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Vertex sourceVertex = getVertex(source);
        return sourceVertex.targets();
    }
    
    // TODO toString()
    
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * A vertex contains a label(String), sources and targets(Map)
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {
    // TODO fields
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
    
    // TODO constructor
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
    
    // TODO methods
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
