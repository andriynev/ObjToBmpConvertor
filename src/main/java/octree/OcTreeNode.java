package octree;

import geometry.Triangle;

public class OcTreeNode<T> {
    public final CollidableAABB bound;
    public boolean isSelected = false;
    public final T data;
    OcTree<T> parent;

    public OcTreeNode(CollidableAABB bounds, T data) {
        this.bound = bounds;
        this.data = data;
    }

    public Triangle getData() {
        return (Triangle) data;
    }

    @Override
    public String toString() {
        return String.format("NTreeNode containing %s", data == null ? null : data.toString());
    }

    public void update() {
        if(parent==null){
            return;
        }
        parent.update(this);
    }

}