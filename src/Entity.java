import java.util.List;

/**
 * @author nanoo - created : 18/05/2020 - 16:36
 */
class Entity {

    private String name;
    private Entity parent;
    private List<Entity> child;

    public Entity(String name, Entity parent, List<Entity> child) {
        this.name = name;
        this.parent = parent;
        this.child = child;
    }

    public Entity() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public List<Entity> getChild() {
        return child;
    }

    public void setChild(List<Entity> child) {
        this.child = child;
    }

}
