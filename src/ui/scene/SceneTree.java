package ui.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.val;
import org.yunghegel.gdx.scenegraph.component.Component;
import org.yunghegel.gdx.ui.UI;
import org.yunghegel.gdx.ui.widgets.SLabel;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.ui.widgets.STree;
import org.yunghegel.gdx.ui.widgets.viewport.SImageButton;
import scene.graph.GameObject;
import scene.graph.SceneGraph;
import scene.graph.components.MaterialComponent;
import scene.graph.components.MeshComponent;
import scene.graph.components.ModelComponent;
import scene.graph.components.TransformComponent;
import sys.Log;

import java.util.HashMap;


public class SceneTree extends STable {

    private SceneGraph sceneGraph;
    protected STree tree;
    float indentSpacing = 20;
    float ySpacing = 4, iconSpacingLeft = 2, iconSpacingRight = 2, paddingLeft, paddingRight;
    GameObjectNode root;
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

        root = new GameObjectNode(sceneGraph.getRootGameObj(),1);
        root.setExpanded(true);
        tree.add(root);

        indentSpacing = tree.getIndentSpacing();


        traverse(sceneGraph.getRootGameObj(),null,1);
        add(tree).grow();
        for (int i = 0; i < 100 ; i++) {

        }
    }

    void traverse(GameObject go, STree.Node parent, int depth) {
        GameObjectNode node = new GameObjectNode(go,1);



        node.setExpanded(true);
        node.getActor().depth = depth;
        nodeMap.put(go,node);
        if(parent!=null) {
            parent.add(node);
        } else {
            tree.add(node);
        }
        Log.info("Adding node: "+go.name+" depth: "+depth);
        if(go.getChildren()!=null) {
            for (GameObject child : go.getChildren()) {
                traverse(child,node, depth+1);
            }
        }
    }

    public void addGameObject(GameObject go) {
        GameObjectNode node = new GameObjectNode(go,1);
        nodeMap.put(go,node);
            if(nodeMap.get(go.getParent())!=null){
                traverse(go,nodeMap.get(go.getParent()),1);
        } else {
            root.add(node);
        }
    }

    public void removeGameObject(GameObject go) {
        GameObjectNode node = nodeMap.get(go);
        if(node==null) return;
        nodeMap.remove(go);
        node.remove();
    }

    public void refresh() {
        nodeMap.clear();
        tree.clearChildren();
        TitleNode title = new TitleNode("Scene Graph");
        root = new GameObjectNode(sceneGraph.getRootGameObj(),1);
        root.setExpanded(true);


        traverse(sceneGraph.getRootGameObj(),null,1);
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
        Table table = new Table();
        boolean mesh=false;
        boolean material=false;
        boolean model=false;
        boolean iconsHidden=false;

        Array<ComponentIcon> componentIcons = new Array<>();
        public GameObjectNodeTable(GameObject go,int depth) {
            super();
            this.depth = depth;
            name = new VisLabel(go.name);

            table.add(name).left();
            table.align(Align.left);
            add(table).growX().left();

            addListener(go);
            parseComponents(go);
            add(visible).right().size(20);

        }

        void hideIcons(){
            iconsHidden=true;
            componentIcons.forEach(c->{
                c.visible=false;
            });
        }
        void showIcons(){
            iconsHidden=false;
            componentIcons.forEach(c->{
                c.visible=true;
            });
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

        void parseComponents(GameObject go){
            for(Component component : go.getComponents()){
                switch (component.getClass().getSimpleName()) {
                    case "TransformComponent":
                        TransformComponent transformComponent = (TransformComponent) component;
                        ComponentIcon icon = new ComponentIcon("transform_icon",component);
                        componentIcons.add(icon);
                        table.add(icon).left().size(20);


                        break;
                        case "MeshComponent":
                            ComponentIcon icon2 = new ComponentIcon("tool-mesh",component);
                            componentIcons.add(icon2);
                            if(!mesh) table.add(icon2).left().size(20);
                            mesh=true;
                            break;
                    case "MaterialComponent":
                        MaterialComponent materialComponent = (MaterialComponent) component;
                        ComponentIcon icon3 = new ComponentIcon("materials_icon",component);
                        componentIcons.add(icon3);
                        componentIcons.add(icon3);
                        if(!material) table.add(icon3).left().size(20);
                        material=true;
                        break;
                    case "ModelComponent":
                        ModelComponent modelComponent = (ModelComponent) component;
                        ComponentIcon icon4 = new ComponentIcon("geometry_icon",component);
                        componentIcons.add(icon4);

                        if(!model) table.add(icon4).left().size(20);
                        model=true;
                        break;
                }


            }
        }






        @Override
        public float getPrefWidth() {
            return SceneTree.this.getWidth()-SceneTree.this.plusMinusWidth()-(indentSpacing*depth)-10;
        }

    }

    abstract class BaseNode<T,K extends Actor> extends STree.Node<STree.Node, T, K> {
        K table;
        T obj;
        GameObject go;
        public BaseNode(K table,T obj,GameObject go) {
            this.table = table;
            this.obj = obj;
        }
    }

    protected class GameObjectNode extends STree.Node<STree.Node, GameObject,GameObjectNodeTable> {

        public GameObjectNode(GameObject value,int depth) {
            super(new GameObjectNodeTable(value,depth));




        }

        public GameObjectNode(GameObjectNodeTable actor) {
            super(actor);
        }

        public GameObjectNode() {
            super();
        }

        void addComponent(Component component) {
            STable table = new STable();

            BaseNode<Component,STable> node = new BaseNode<Component, STable>(table,component,getValue()){
                boolean mesh=false;
                boolean material=false;
                boolean model=false;

                @Override
                public void setExpanded(boolean expanded) {
                    table.clear();
                    switch (component.getClass().getSimpleName()) {
                        case "MeshComponent":
                            if(!mesh) table.add(new ComponentIcon("tool-mesh",component)).left().size(20);
                            mesh=true;
                            break;
                        case "MaterialComponent":
                            MaterialComponent materialComponent = (MaterialComponent) component;
                            SLabel name = new SLabel(((MaterialComponent) component).getObj().id);
                            table.add(name).left();
                            table.add(new ComponentIcon("materials_icon",component)).left().size(20);
                            material=true;
                            break;
                        case "ModelComponent":
                            ModelComponent modelComponent = (ModelComponent) component;
                            if(!model) table.add(new ComponentIcon("geometry_icon",component)).left().size(20).padRight(5);
                            SLabel name2 = new SLabel("Model");
                            go.components.forEach(c->{
                                if(c instanceof MeshComponent){
                                    MeshComponent materialComponent1 = (MeshComponent) c;
                                    table.add(new ComponentIcon("tool-mesh",c)).left().size(20);
                                }
                            }   );
                            model=true;
                            break;
                    }
                }
            };
            add(node);

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

    class ComponentNode extends STree.Node<ComponentNode, Component, ComponentNodeTable> {

        public ComponentNode(Component value,int depth,GameObject gameObject) {
            super(new ComponentNodeTable(value,depth,gameObject));
        }

    }

    class ComponentNodeTable extends STable {
        Table table = new Table();
        GameObject gameObject;
        boolean mesh=false;
        boolean material=false;
        boolean model=false;
        public ComponentNodeTable(Component component,int depth,GameObject gameObject) {
            super();
            this.gameObject = gameObject;
            switch (component.getClass().getSimpleName()) {
                case "MeshComponent":
                    if(!mesh) table.add(new ComponentIcon("tool-mesh",component)).left().size(20);
                    mesh=true;
                    break;
                case "MaterialComponent":
                    MaterialComponent materialComponent = (MaterialComponent) component;
                    SLabel name = new SLabel(((MaterialComponent) component).getObj().id);
                    table.add(name).left();
                    table.add(new ComponentIcon("materials_icon",component)).left().size(20);
                    material=true;
                    break;
                case "ModelComponent":
                    ModelComponent modelComponent = (ModelComponent) component;
                    if(!model) table.add(new ComponentIcon("geometry_icon",component)).left().size(20).padRight(5);
                    SLabel name2 = new SLabel("Model");
                    gameObject.components.forEach(c->{
                        if(c instanceof MeshComponent){
                            MeshComponent materialComponent1 = (MeshComponent) c;
                            table.add(new ComponentIcon("tool-mesh",c)).left().size(20);
                        }
                    }   );
                    model=true;
                    break;
            }

        }

        @Override
        public void act(float delta) {
            if(nodeMap.get(gameObject).isExpanded()){
                if (!nodeMap.get(gameObject).getActor().iconsHidden) nodeMap.get(gameObject).getActor().hideIcons();
            } else {
                if (nodeMap.get(gameObject).getActor().iconsHidden) nodeMap.get(gameObject).getActor().showIcons();
            }
        }
    }
    class ComponentIcon extends ImageButton {

        Component component;
        public boolean visible=true;

        public ComponentIcon(String style,Component component) {
            super(UI.getSkin());
            this.component = component;

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = UI.getSkin().getDrawable(style);
            imageButtonStyle.imageDown = UI.getSkin().getDrawable(style);
            imageButtonStyle.imageOver = UI.getSkin().getDrawable(style);

            setStyle(imageButtonStyle);
        }
    }
}
