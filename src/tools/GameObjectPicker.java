package tools;

import app.App;
import app.Salient;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import events.scene.GameObjectDeselectedEvent;
import events.scene.GameObjectSelectedEvent;
import org.yunghegel.gdx.picking.Pickable;
import org.yunghegel.gdx.picking.PickerColorEncoder;

import org.yunghegel.gdx.picking.PickerShader;
import org.yunghegel.gdx.scenegraph.component.Component;
import scene.Scene;
import scene.graph.GameObject;
import scene.graph.SceneGraph;
import scene.graph.components.RenderableComponent;
import sys.Log;
import sys.reflection.ClassScanner;
import util.Flags;

public class GameObjectPicker extends BasePicker {

    public GameObjectPicker() {
        super();

    }

    public static PickerShader PickerShader = new PickerShader();

    public GameObject pick(Scene scene, int screenX, int screenY) {
        begin(scene.getViewport());
        renderPickableScene(scene.getSceneGraph());
        end();
        Pixmap pm = getFrameBufferPixmap(scene.getViewport());

        int x = screenX - scene.getViewport().getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.getViewport().getScreenY() + scene.getViewport().getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        System.out.println("ID: " + id);
        for (GameObject go : scene.getSceneGraph().getGameObjects()) {
            if (id == go.id)
            {
                go.flags.set(Flags.SELECTED);
                return go;
            }
            if (go.getChildren() != null) {
                for (GameObject child : go.getChildren()) {
                    if (id == child.id) {
                        child.flags.set(Flags.SELECTED);
                        Log.info("Picked child: " + child.name + " with id: " + child.id);
                        return child;
                    }
                }
            } else {
                go.flags.clear(Flags.SELECTED);
            }

        }
        return null;
    }



    private void renderPickableScene(SceneGraph sceneGraph) {
        sceneGraph.getCurrScene().getModelBatch().begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        for (GameObject go: sceneGraph.getGameObjects()) {
            var gameObj = (GameObject) go;
            renderPickableGameObject(gameObj, sceneGraph.getCurrScene().getModelBatch());
        }
        sceneGraph.getCurrScene().getModelBatch().end();
    }

    private void renderPickableGameObject(GameObject go, ModelBatch batch) {
        for (Component c : go.components) {
            if (c instanceof RenderableComponent) {
                ((RenderableComponent) c).renderPick(batch);
            }
        }

        if (go.getChildren() != null) {
            for (GameObject goc : go.getChildren()) {

                renderPickableGameObject( goc, batch);
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

    public void processPicks(Scene scene) {
        if(App.INSTANCE.getI().isButtonJustPressed(0)){
            GameObject go = pick(scene, Gdx.input.getX(), Gdx.input.getY());
            if (go != null) {
                for(GameObject obj : scene.getSelectedGameObjects()) {
                    if (obj.id == go.id) {
                        Log.info("Already selected: " + go.name + " with id: " + go.id, " deselcting instead");
                        Salient.INSTANCE.postEvent(new GameObjectDeselectedEvent(go));
                        return;
                    }
                }
                Log.info("Picked: " + go.name + " with id: " + go.id);

                if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    Salient.INSTANCE.postEvent(new GameObjectSelectedEvent(go,true));

                } else {
                    Salient.INSTANCE.postEvent(new GameObjectSelectedEvent(go,false));
                }

            }
        }
    }
}
