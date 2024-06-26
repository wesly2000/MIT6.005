/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {
    final static double halfCircle = 180.0;
    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        int side = 4;
        drawRegularPolygon(turtle, side, sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        if(sides < 3){
            throw new IllegalArgumentException("A polygon must have at least three sides");
        }

        return (sides - 2) * halfCircle / sides; // Polygon inner angle formula.
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        if(angle <= 0 || angle >= 180.0){
            throw new IllegalArgumentException("Angle must be between 0 and 180.");
        }
        return (int)Math.round(2*halfCircle/(halfCircle-angle));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double interiorAngle = calculateRegularPolygonAngle(sides);
        double turn = halfCircle - interiorAngle;
        for(int i = 0; i < sides; i++){
            turtle.forward(sideLength);
            turtle.turn(turn);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        double diffX = targetX - currentX;
        double diffY = targetY - currentY;
        if(diffX == 0 && diffY == 0){return 0;} // No moving is needed.
        double angleXAxis = Math.atan2(diffY, diffX) * halfCircle / Math.PI; // The angle of the position vector for Cartesian X-axis.
        double angleNorth = convertXAisAngleToNorthAngle(angleXAxis);
        double angleDiff = angleNorth - currentHeading;
        return angleDiff < 0 ? 2*halfCircle - Math.abs(angleDiff) : angleDiff;
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        int numOfCoords = xCoords.size();
        List<Double> adjustments = new ArrayList<>();
        if (numOfCoords <= 1) return adjustments; // Return an empty list.

        double adjustment;
        double currentHeading = 0;

        Integer currentX = xCoords.get(0);
        Integer currentY = yCoords.get(0);
        Integer targetX, targetY;
        for(int i = 1; i < numOfCoords; i++){
            targetX = xCoords.get(i);
            targetY = yCoords.get(i);
            adjustment = calculateHeadingToPoint(currentHeading, currentX, currentY, targetX, targetY);
            adjustments.add(adjustment);
            currentHeading = (adjustment + currentHeading) % (2 * halfCircle); // Recalculate currentHeading
            currentX = targetX;
            currentY = targetY;
        }
        return adjustments;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        throw new RuntimeException("implement me!");
    }

    /**
     * Convert the angle to positive X-axis to the angle as clockwise to the North.
     *
     * @param angleXAxis the angle to positive X-axis, which belongs to (-180, 180].
     * @return angleNorth the angle to the North, which must be 0 <= angleNorth < 360.0
     */
    public static double convertXAisAngleToNorthAngle(double angleXAxis){
        assert angleXAxis <= 180 &&  angleXAxis > -180: "angleXAxis must be in (-180, 180].";
        if(angleXAxis > 90 && angleXAxis <= 180)
            return 2.5*halfCircle - angleXAxis;
        else
            return 90-angleXAxis;
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawSquare(turtle, 40);
//        drawRegularPolygon(turtle, 4, 50);
        // draw the window
        turtle.draw();
    }

}
