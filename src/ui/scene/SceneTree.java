package ui.scene;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.val;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.ui.widgets.STree;
import scene.graph.GameObject;
import scene.graph.SceneGraph;

import java.util.HashMap;


public class SceneTree extends STable {

    private SceneGraph sceneGraph;
    protected STree tree;
    float indentSpacing = 20;
    float ySpacing = 4, iconSpacingLeft = 2, iconSpacingRight = 2, paddingLeft, paddingRight;

    protected HashMap<GameObject,GameObjectNode> nodeMap = new HashMap<>();

    public SceneTree(SceneGraph sceneGraph) {
        super();
        this.sceneGraph = sceneGraph;
        TitleNode title = new TitleNode("Scene Graph");
        title.setExpanded(true);
        tree = new STree() {

//            @Override
//            protected void drawBackground(Batch batch, float parentAlpha) {
//                TreeStyle style = getStyle();
//                GameObjectNode root = new GameObjectNode(sceneGraph.getRoot(),1);
//                if (style.background != null) {
//                    Color color = getColor();
//                    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//                    style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
//                   float rowHeight = title.getHeight();
//                   int numPossibleRows = (int) (getHeight()/rowHeight)-1;
//
//                   float x = getX();
//                    for (int i = 1; i < numPossibleRows; i++) {
//
//
//                        if(i%2==0){
//                            batch.setColor(0.8f,0.8f,0.8f,1);
//                        }else{
//                            batch.setColor(1,1,1,1);
//                        }
//                        float y = getY() - (i * rowHeight);
//
//
//                       style.background.draw(batch, x, y-indentSpacing/2, getWidth(), getHeight());
//
//                    }
//
//                    Array<Node> nodes = getRootNodes();
////                    for (int i = 0, n = numPossibleRows; i < n; i++) {
////
////                        Rectangle cullingArea = getCullingArea();
////                        float cullBottom = 0, cullTop = 0;
////                        if (cullingArea != null) {
////                            cullBottom = cullingArea.y;
////                            cullTop = cullBottom + cullingArea.height;
////                        }
////
////                        if(i%2==0){
////                            batch.setColor(0.8f,0.8f,0.8f,1);
////                        }else{
////                            batch.setColor(1,1,1,1);
////                        }
////
////
////
////
////                        float x = getX(), y = getY() - (i * rowHeight);
////                        float expandX = x + getIndentSpacing(), iconX = expandX + SceneTree.this.plusMinusWidth() + iconSpacingLeft;
////                        style.background.draw(batch, x,y, SceneTree.this.getWidth(), rowHeight);
////
////                    }
//
//
//
//                }
//
//            }
        };

        GameObjectNode root = new GameObjectNode(sceneGraph.root,1);
        tree.add(root);

        indentSpacing = tree.getIndentSpacing();


        traverse(sceneGraph.root ,null,1);
        add(tree).grow();
        for (int i = 0; i < 100 ; i++) {

        }
    }

    void traverse(GameObject go, STree.Node parent, int depth) {
        GameObjectNode node = new GameObjectNode(go,1);
        node.getActor().depth = depth;
        nodeMap.put(go,node);
        if(parent!=null) {
            parent.add(node);
        } else {
            tree.add(node);
        }
        if(go.getChildren()!=null) {
        for (GameObject child : go.getChildren()) {
            traverse(child,node, depth+1);
        }
        }
    }

    public void refresh() {
        nodeMap.clear();
        tree.clearChildren();
        TitleNode title = new TitleNode("Scene Graph");
        GameObjectNode root = new GameObjectNode(sceneGraph.root,1);


        traverse(sceneGraph.root,null,1);
    }

    public void select(GameObject go) {
        GameObjectNode node = nodeMap.get(go);
        if (node == null) return;
        tree.getSelection().set(node);
    }

    public void deselect(GameObject go) {
        GameObjectNode node = nodeMap.get(go);
        if (node == null) return;
        tree.getSelection().remove(node);
    }

    public void remove(GameObject go) {
        GameObjectNode node = nodeMap.get(go);
        if (node == null) return;
        nodeMap.remove(go);
        node.remove();
    }

    private float plusMinusWidth () {
        STree.TreeStyle style = tree.getStyle();
        float width = Math.max(style.plus.getMinWidth(), style.minus.getMinWidth());
        if (style.plusOver != null) width = Math.max(width, style.plusOver.getMinWidth());
        if (style.minusOver != null) width = Math.max(width, style.minusOver.getMinWidth());
        return width;
    }

    protected class GameObjectNodeTable extends STable {

        VisLabel name;
        ImageButton visible = new ImageButton(getSkin(), "eye");
        public int depth;

        public GameObjectNodeTable(GameObject go,int depth) {
            super();
            this.depth = depth;
            name = new VisLabel(go.name);
            add(name).growX().left();
            add(visible).right().size(20);
            addListener(go);
        }

        public void addListener(GameObject go ) {
            val gameObject = go;
            visible.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(visible.isChecked()) {
                        gameObject.visible = false;
                    } else {
                        gameObject.visible = true;
                    }
                }
            });
        }



        @Override
        public float getPrefWidth() {
           return SceneTree.this.getWidth()-SceneTree.this.plusMinusWidth()-(indentSpacing*depth)-10;
        }

    }
    protected class GameObjectNode extends STree.Node<GameObjectNode, GameObject,GameObjectNodeTable> {

        public GameObjectNode(GameObject value,int depth) {
            super(new GameObjectNodeTable(value,depth));
        }

    }

    private class TitleTable extends VisTable {
        public TitleTable(String title) {
            super();
            ImageButton iconify = new ImageButton(getSkin(), "iconify-window");
            add(new VisLabel(title)).growX().left();
            add(iconify).right().size(20);
        }

        @Override
        public float getPrefWidth() {
            return SceneTree.this.getWidth()-SceneTree.this.plusMinusWidth()-(indentSpacing)-10;
        }
    }


    private class TitleNode extends STree.Node<STree.Node, String, TitleTable>
    {

        public TitleNode(String value) {
            super(new TitleTable(value));
        }



    }



}
