package at.ac.ait.speedr.workspace;

/**
 *
 * @author visnei
 */
public class RObject {

    private RObject parent;
    private String name;
    private String type;

    public RObject(RObject parent, String name, String type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getRSymbol() {
        if (parent != null && parent.getType().equals("list")) {
            if (getName().matches("\\d+")) {
                return parent.getRSymbol() + "[[" + getName() + "]]";
            } else {
                return parent.getRSymbol() + "[[\"" + getName() + "\"]]";
            }
        } else {
            return getName();
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + "(" + type + ")";
    }
}
