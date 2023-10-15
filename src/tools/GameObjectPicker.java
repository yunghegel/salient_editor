package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.yunghegel.gdx.picking.Pickable;
import org.yunghegel.gdx.picking.PickerColorEncoder;

import org.yunghegel.gdx.scenegraph.component.Component;
import scene.Scene;
import scene.graph.GameObject;
import scene.graph.SceneGraph;
import sys.Log;
import sys.reflection.ClassScanner;

public class GameObjectPicker extends BasePicker {

    public GameObjectPicker() {
        super();
    }

    public GameObjectBase pick(Scene scene, int screenX, int screenY) {
        begin(scene.getViewport());
        renderPickableScene(scene.getSceneGraph());
        end();
        Pixmap pm = getFrameBufferPixmap(scene.getViewport());

        int x = screenX - scene.getViewport().getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.getViewport().getScreenY() + scene.getViewport().getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        System.out.println("ID: " + id);
        for (GameObjectBase go : scene.getSceneGraph().getGameObjects()) {
            if (id == go.id) return go;
            if (go.getChildren() != null) {
                for (GameObjectBase child : go.getChildren()) {
                    if (id == child.id) {
                        Log.info("Picked child: " + child.name + " with id: " + child.id);
                        return child;
                    }
                }
            }

        }

        return null;
    }



    private void renderPickableScene(SceneGraph sceneGraph) {
        sceneGraph.getCurrScene().getModelBatch().begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        for (GameObjectBase go: sceneGraph.getGameObjects()) {
            var gameObj = (GameObject) go;
            renderPickableGameObject(gameObj, sceneGraph.getCurrScene().getModelBatch());
        }
        sceneGraph.getCurrScene().getModelBatch().end();
    }

    private void renderPickableGameObject(GameObject go, ModelBatch batch) {
        for (Component c : go.components) {
            if(ClassScanner.INSTANCE.classImplementsInterface(c.getClass(), Pickable.class)){
                ((Pickable)c).renderPick(batch);
            }
        }

        if (go.getChildren() != null) {
            for (GameObjectBase goc : go.getChildren()) {

                renderPickableGameObject((GameObject) goc, batch);
            }
        }
    }

    private void renderPickableGameObjects(GameObject[] objects, SceneGraph sceneGraph) {
        sceneGraph.getCurrScene().getModelBatch().begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        for (GameObject go : objects) {
            renderPickableGameObject(go, sceneGraph.getCurrScene().getModelBatch());
        }
        sceneGraph.getCurrScene().getModelBatch().end();
    }

    public void renderPickables(Pickable[] pickables, SceneGraph sceneGraph) {
        sceneGraph.getCurrScene().getModelBatch().begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        for (int i = 0; i < pickables.length; i++) {
            pickables[i].renderPick(sceneGraph.getCurrScene().getModelBatch());
        }
        sceneGraph.getCurrScene().getModelBatch().end();
    }
}
