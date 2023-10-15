package scene.graph;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.yunghegel.gdx.scenegraph.component.Component;
import org.yunghegel.gdx.scenegraph.scene3d.SimpleNode;
import org.yunghegel.gdx.scenegraph.scene3d.Spatial;
import scene.Scene;
import scene.SceneRenderer;
import scene.systems.SceneIterator;
import util.Flags;
import util.RenderFlags;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GameObject extends Spatial<GameObject> implements Iterable<GameObject> {

    public static final String DEFAULT_NAME = "GameObjectBase";

    public String name;
    public boolean active;

    public Array<Component> components=new Array<>();
    private Array<String> tags;

    public SceneGraph sceneGraph;
    public Scene scene;
    public ModelBatch depthBatch, batch;
    public PerspectiveCamera camera;
    public SceneRenderer renderer;

    public Flags flags = new Flags();
    public RenderFlags renderFlags = new RenderFlags();

    public boolean visible = true;

    public GameObject(SceneGraph sceneGraph, String name, int id, Matrix4 transform) {
        super(id);
        this.scene=sceneGraph.getCurrScene();
        this.sceneGraph = sceneGraph;
        this.depthBatch = sceneGraph.getCurrScene().getDepthBatch();
        this.batch = sceneGraph.getCurrScene().getModelBatch();
        this.camera = sceneGraph.getCurrScene().getPerspectiveCamera();
        this.renderer = sceneGraph.getCurrScene().getSceneRenderer();
    }

    public void render(float delta){

        if(!visible) return;
        batch.begin(camera);

        for (Component component : components) {
            component.render(delta);
        }
        if(getChildren()!=null){
            for (GameObject child : getChildren()) {
                child.render(delta);
            }
        }
        batch.end();
    }

    public void update(float delta){
        for (Component component : components) {
            component.update(delta);
        }
        if(getChildren()!=null){
            for (GameObject child : getChildren()) {
                child.update(delta);
            }
        }
    }


    public GameObject findChildrenByName(String name) {
        for (GameObject go : this) {
            if (go.name.equals(name)) {
                return go;
            }
        }

        return null;
    }




    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.removeValue(component, true);
    }

    public void addComponents(Component ... components) {
        for (Component component : components) {
            addComponent(component);
        }
    }

    public boolean hasComponent(Class<? extends Component> component) {
        for (Component c : components) {
            if (c.getClass().equals(component)) {
                return true;
            }
        }

        return false;
    }

    public Array<String> getTags() {
        return this.tags;
    }

    /**
     * Adds a tag.
     *
     * @param tag
     *            tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new Array<String>(2);
        }

        this.tags.add(tag);
    }




    public Array<Component> getComponents() {
        return components;
    }

    public Array<Component> getComponentsByClass(Class<? extends Component> type) {
        Array<Component> components = getComponents();
        Array<Component> tmp = new Array<>();
        for (Component component : components) {
            if (component.getClass().equals(type)) {
                tmp.add(component);
            }
        }
        return tmp;
    }


    @NotNull
    @Override
    public Iterator<GameObject> iterator() {
        return new SceneIterator(this);
    }

    @Override
    public void forEach(Consumer<? super GameObject> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<GameObject> spliterator() {
        return Iterable.super.spliterator();
    }
}
