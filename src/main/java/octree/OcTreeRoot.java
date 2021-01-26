package octree;

import java.util.HashMap;
import java.util.Map;

public class OcTreeRoot<T> extends OcTree<T> {
    public final Map<T, OcTreeNode<T>> allObjects = new HashMap<>();

    public OcTreeRoot(CollidableAABB bound, int capacity, int maxLevel) {
        super(bound, capacity, maxLevel);
    }

    public OcTreeRoot(float[] min, float[] max, int capacity, int maxLevel) {
        this(new CollidableAABB(min, max), capacity, maxLevel);
    }

    public OcTreeNode<T> getNode(T object) {
        return allObjects.get(object);
    }

    public void update(T object, CollidableAABB newBoundingBox) {
        final OcTreeNode<T> node = getNode(object);
        assert node.bound.numDimensions() == newBoundingBox.numDimensions();
        for (int i = 0; i < node.bound.min.length; i++) {
            node.bound.min[i] = newBoundingBox.min[i];
            node.bound.max[i] = newBoundingBox.max[i];
        }
        update(node);
    }

    @Override
    public boolean add(final OcTreeNode<T> obj) {
        if (super.add(obj)) {
            allObjects.put(obj.data, obj);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(final OcTreeNode<T> obj) {
        if (super.remove(obj)) {
            allObjects.remove(obj.data);
            return true;
        }
        return false;
    }

    public boolean remove(T obj) {
        return remove(getNode(obj));
    }
}