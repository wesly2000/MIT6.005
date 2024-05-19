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
    
    // TODO checkRep
    
    @Override public boolean add(String vertex) {
        if(this.vertices().contains(vertex)) return false;

        this.vertices.add(new Vertex(vertex));
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        throw new RuntimeException("not implemented");
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
    
    @Override public Map<String, Integer> sources(String target) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Map<String, Integer> targets(String source) {
        throw new RuntimeException("not implemented");
    }
    
    // TODO toString()
    
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * A vertex contains a label(String) and date(Date)
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
    //   Since vertex is mutable, we allow mutators on vertex;
    //   label and size are immutable, so defensive copy is not needed;
    //   for date, in creator, observer and mutator we do defensive copies
    //   to avoid accidental modification.
    
    // TODO constructor
    public Vertex(String label) { this.label = label; }
    
    // TODO checkRep
    
    // TODO methods
    public String getLabel() { return label; }

    public Map<String, Integer> getSources() { return sources; }

    public Map<String, Integer> getTargets() { return targets; }

//    public boolean addSource(String source, int weight) {
//        // Here we follow the traditional get-put workflow to get the boolean return,
//        // which could be used directly in set() of graph
//        if (!sources.containsKey(source)) {}
//        return true;
//    }
//
//    public boolean addTarget(String target, int weight) {
//        if (!targets.containsKey(target)) {}
//        return true;
//    }

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
