package octree;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


public class OcTree<T> {
    final List<OcTree<T>> children = new Vector<>();
    final List<OcTreeNode<T>> objects = new Vector<>();
    final List<OcTreeNode<T>> foundObjects = new Vector<>();
    private final int capacity;
    private final int maxLevel;
    private final CollidableAABB bounds;
    private final int numDimensions;
    OcTree<T> parent;
    boolean isLeaf = true;
    private int level = 0;

    public OcTree(final CollidableAABB bound, final int capacity, final int maxLevel) {
        this.bounds = bound;
        this.capacity = capacity;
        this.maxLevel = maxLevel;
        this.numDimensions = bound.numDimensions();
        for (int i = 0; i < (1 << numDimensions); i++) {
            children.add(null);
        }
    }

    public OcTree(float[] min, float[] max, final int capacity, final int maxLevel) {
        this(new CollidableAABB(min, max), capacity, maxLevel);

    }

    public OcTree(final OcTree<T> other) {
        this(other.getBounds(), other.capacity, other.maxLevel);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public List<OcTreeNode<T>> getObjects() {
        return objects;
    }

    private void subdivide() {

        final float[] min = new float[numDimensions];
        final float[] newLengths = new float[numDimensions];
        for (int d = 0; d < numDimensions; d++) {
            min[d] = (float) getBounds().realMin(d);
            newLengths[d] = (float) ((getBounds().realMax(d) - getBounds().realMin(d)) * 0.5f);
        }
        for (int i = 0; i < (1 << numDimensions); i++) {
            final float[] thisMin = min.clone();
            final float[] thisMax = new float[numDimensions];
            for (int e = 0; e < numDimensions; e++) {
                if ((i & (1 << e)) == e + 1) {
                    thisMin[e] += newLengths[e];
                }
                thisMax[e] = thisMin[e] + newLengths[e];
            }

            final OcTree<T> newNode = new OcTree<>(new CollidableAABB(thisMin, thisMax), capacity, maxLevel);
            newNode.level = level + 1;
            newNode.parent = this;
            children.set(i, newNode);

        }
        isLeaf = false;
    }

    OcTree<T> getChild(final Collidable bound) {
        int i = 0;
        for (int d = 0; d < numDimensions; d++) {
            final float center = (float) ((getBounds().realMin(d) + getBounds().realMax(d)) * .5f);
            if (bound.realMax(d) > center && bound.realMin(d) < center) {
                return null;
            }
            if (bound.realMin(d) > center) {
                i |= 1 << d;
            }
        }
        return children.get(i);
    }

    private void discardEmptyBuckets() {
        if (objects.size() == 0) return;
        if (!isLeaf) {
            for (final OcTree<T> child : children)
                if (!child.isLeaf || child.objects.size() > 0)
                    return;
        }
        clear();
        if (parent != null) {
            parent.discardEmptyBuckets();
        }
    }

    public boolean add(final OcTreeNode<T> obj) {
        if (obj.bound.numDimensions() != numDimensions) {
            throw new IllegalArgumentException("Can only insert objects of the same number of dimension");
        }
        if (obj.parent != null) {
            System.out.printf("Trying to add %s, but already has a parent%n", obj);
            return false;
        }
        //if it has children, recurse continuously until we get to end of branch
        if (!isLeaf) {
            final OcTree<T> child = getChild(obj.bound);
            if (child != null) {
                return child.add(obj);
            }
        }
        objects.add(obj);
        obj.parent = this;

        if (isLeaf && level < maxLevel && objects.size() >= capacity) {
            subdivide();
            update(obj);
        }
        return true;
    }

    public boolean add(final CollidableAABB bounds, T object) {
        return add(new OcTreeNode<T>(bounds, object));
    }

    public boolean remove(final OcTreeNode<T> obj) {
        if (obj.parent == null) {
            return false;
        }
        if (obj.parent != this) {
            return obj.parent.remove(obj);
        }
        objects.remove(obj);
        obj.parent = null;
        discardEmptyBuckets();
        return true;
    }

    public boolean update(final OcTreeNode<T> obj) {
        if (!remove(obj)) return false;

        if (parent != null && !getBounds().containsBoundingBox(obj.bound)) {
            return parent.add(obj);
        }
        if (!isLeaf) {
            final OcTree<T> child = getChild(obj.bound);
            if (child != null) {
                return child.add(obj);
            }

        }
        return add(obj);
    }

    public List<OcTreeNode<T>> getObjectsContaining(final Collidable bound) {
        foundObjects.clear();
        for (final OcTreeNode<T> obj : objects) {
            if (obj.bound != bound && obj.bound.containsBoundingBox(bound)) {
                foundObjects.add(obj);
            }
        }
        if (!isLeaf) {
            final OcTree<T> child = getChild(bound);

            if (child != null) {
                child.getObjectsContaining(bound);
                foundObjects.addAll(child.foundObjects);
            } else {
                for (final OcTree<T> leaf : children) {
                    if (leaf.getBounds().containsBoundingBox(bound)) {
                        leaf.getObjectsContaining(bound);
                        foundObjects.addAll(leaf.foundObjects);
                    }
                }
            }
        }
        return foundObjects;
    }

    public List<OcTreeNode<T>> getObjectsInBound(final Collidable bound) {
        foundObjects.clear();
        for (final OcTreeNode<T> obj : objects) {
            if (obj.bound != bound && obj.bound.intersectsBoundingBox(bound)) {
                foundObjects.add(obj);
            }
        }

        if (!isLeaf) {
            final OcTree<T> child = getChild(bound);

            if (child != null) {
                child.getObjectsInBound(bound);
                foundObjects.addAll(child.foundObjects);
            } else {
                for (final OcTree<T> leaf : children) {
                    if (leaf.getBounds().intersectsBoundingBox(bound)) {
                        leaf.getObjectsInBound(bound);
                        foundObjects.addAll(leaf.foundObjects);
                    }
                }
            }
        }
        return foundObjects;
    }

    public int totalChildren() {
        int total = 0;
        if (isLeaf) return total;
        for (final OcTree<T> child : children)
            total += child.totalChildren();
        return ((1 << numDimensions) + total);
    }

    public int totalObjects() {
        int total = objects.size();
        if (!isLeaf) {
            for (final OcTree<T> child : children)
                total += child.totalObjects();
        }
        return total;

    }

    public void clear() {
        if (objects.size() != 0) {
            for (final OcTreeNode<T> obj : objects) {
                obj.parent = null;
            }
            objects.clear();
        }
        if (!isLeaf) {
            for (final OcTree<T> child : children) {
                child.clear();
            }
            isLeaf = true;
        }
    }

    public List<OcTree<T>> getChildren() {
        return children;
    }

    public CollidableAABB getBounds() {
        return bounds;
    }

    private String buildString(int level) {

        for (final OcTreeNode<T> obj : objects) {
            System.out.printf("%s* %s%n", String.join("", Collections.nCopies(level + 1, "  ")), obj);
        }
        if (!isLeaf) {

            for (final OcTree<T> leaf : children) {
                leaf.buildString(level++);
            }

        }
        return "";
    }

    @Override
    public String toString() {

        System.out.println("* root");
        return buildString(0);
    }


}