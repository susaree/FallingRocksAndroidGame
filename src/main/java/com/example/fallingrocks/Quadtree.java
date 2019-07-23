package com.example.fallingrocks;


import android.graphics.Rect;



import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    private int MAX_OBJECTS = 5;
    private int MAX_LEVELS = 2;

    private int level;
    private List<SpriteBase> objects;
    private Rect bounds;
    private Quadtree[] nodes;

    /*
     * Constructor
     */
    public Quadtree(int pLevel, Rect pBounds) {
        level = pLevel;
        objects = new ArrayList<>();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    /*
     * Clears the quadtree
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    /*
     * Splits the node into 4 subnodes
     */
    private void split() {

        nodes[0] = new Quadtree(level+1, new Rect(bounds.centerX(),bounds.top,bounds.right,bounds.centerY()));
        nodes[1] = new Quadtree(level+1, new Rect(bounds.left, bounds.top, bounds.centerX(), bounds.centerY()));
        nodes[2] = new Quadtree(level+1, new Rect(bounds.left, bounds.centerY(), bounds.centerX(), bounds.bottom));
        nodes[3] = new Quadtree(level+1, new Rect(bounds.centerX(), bounds.centerY(), bounds.right, bounds.bottom));
    }
    /*
     * Determine which node the object belongs to. -1 means
     * object cannot completely fit within a child node and is part
     * of the parent node
     */
    private int getIndex(SpriteBase sprite) {
        int index = -1;
        double verticalMidpoint = bounds.left + (bounds.centerX());
        double horizontalMidpoint = bounds.top + (bounds.centerY());

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (sprite.getY() < horizontalMidpoint && sprite.getY() + sprite.getH() < horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (sprite.getY() > horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (sprite.getX() < verticalMidpoint && sprite.getX() + sprite.getW() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            }
            else if (bottomQuadrant) {
                index = 2;
            }
        }
        // Object can completely fit within the right quadrants
        else if (sprite.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            }
            else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    /*
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(SpriteBase sprite) {
        if (nodes[0] != null) {
            int index = getIndex(sprite);

            if (index != -1) {
                nodes[index].insert(sprite);

                return;
            }
        }

        objects.add(sprite);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }

    /*
     * Return all objects that could collide with the given object
     */
    public List<SpriteBase> retrieve(List<SpriteBase> returnObjects, SpriteBase sprite) {


        int index = getIndex(sprite);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, sprite);
        }

       returnObjects.addAll(objects);

        return returnObjects;
    }






}