package at.ac.ait.speedr.workspace;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

/**
 *
 * @author visnei
 */
public class Workspace {

    private DefaultTreeModel workspace;
    private DefaultMutableTreeNode root;

    public Workspace() {
        root = new DefaultMutableTreeNode("Data Objects");
        workspace = new DefaultTreeModel(root);
    }

    public DefaultTreeModel getWorkspace() {
        return workspace;
    }

    public void reload() throws REngineException, REXPMismatchException {
        root.removeAllChildren();

        REXP exp = RConnection.eval(".reloadworkspace()");
        if (exp != null && !exp.isNull()) {
            if (exp.isList()) {
                RList metadata = exp.asList();
                addNodes(root, metadata);
            } else {
                System.err.println("isNotList");
            }
        }
        workspace.reload();
    }

    private void addNodes(DefaultMutableTreeNode node, RList metadata) throws REXPMismatchException {
        addNodes(node, metadata, null);
    }

    private void addNodes(DefaultMutableTreeNode node, RList metadata, RObject parent) throws REXPMismatchException {
        String[] var_name = metadata.at("var_name").asStrings();
        String[] var_cls = metadata.at("var_cls").asStrings();

        RObject robj;

        for (int i = 0; i < var_name.length; i++) {
            robj = new RObject(parent, var_name[i], var_cls[i]);
            if (robj.getType().equals("list")) {
                DefaultMutableTreeNode listNode = new DefaultMutableTreeNode(robj, true);
                listNode.add(new DefaultMutableTreeNode("dummy"));
                node.add(listNode);
            } else {
                node.add(new DefaultMutableTreeNode(robj, false));
            }
        }
    }

    public REXP loadContent(String name) throws REngineException, REXPMismatchException {
        return RConnection.eval(name);
    }

    public void addListChildren(DefaultMutableTreeNode node) throws REngineException, REXPMismatchException {
        RObject ro = (RObject) node.getUserObject();
        REXP exp = RConnection.eval(".collectListOjectMetaData(" + ro.getName() + ")");
        if (exp.isList()) {
            RList metadata = exp.asList();
            node.removeAllChildren();
            addNodes(node, metadata, ro);
        } else {
            System.err.println("isNotList");
        }
    }

    public boolean hasVariable(String varname) {
        int count = workspace.getChildCount(root);
        RObject var;
        for (int i = 0; i < count; i++) {
            var = (RObject) ((DefaultMutableTreeNode) workspace.getChild(root, i)).getUserObject();
            if (varname.equalsIgnoreCase(var.getName())) {
                return true;
            }
        }
        return false;
    }
}
