package graphics;

import app.Editor;
import app.Salient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import events.scene.GameObjectSelectedEvent;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import org.jetbrains.annotations.NotNull;
import org.yunghegel.gdx.scenegraph.component.Component;
import scene.graph.GameObject;
import scene.graph.SceneGraph;

import scene.graph.components.RenderableComponent;
import util.GenFrameBuffer;

public class OutlineRenderer implements GameObjectSelectedEvent.Listener {

    public SpriteBatch batch;
    public GenFrameBuffer buf;
    public RenderableProvider modelInstance;
    public ShaderProgram outlineShaderProgram;
    public ModelBatch modelBatch;
    public Array<ModelInstance> modelInstances = new Array<>();
    public Texture texture;
    public TextureRegion textureRegion;

    public OutlineDepth outlineDepth;
    private FrameBuffer depthBuffer;
    private Texture depthTexture;
    private ModelBatch depthBatch;
    public GameObject gameObject;


    public OutlineRenderer() {
        batch = new SpriteBatch();
        modelBatch = new ModelBatch();
        buf = new GenFrameBuffer(true);
        outlineShaderProgram = new ShaderProgram(Gdx.files.internal("shaders/object_outline.vert").readString(), Gdx.files.internal("shaders/object_outline.frag").readString());
        if (!outlineShaderProgram.isCompiled()) {
            System.out.println(outlineShaderProgram.getLog());
        }
        outlineShaderProgram.bind();
        batch.setShader(outlineShaderProgram);

        outlineDepth = new OutlineDepth(true);
        depthBuffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        depthBatch = new ModelBatch(PBRShaderProvider.createDefaultDepth(144));
        Salient.INSTANCE.registerListener(this);
    }

    public void setModelInstance(ModelInstance modelInstance) {
        modelInstances.clear();
        modelInstances.add(modelInstance);
//        Editor.i().sceneContext.sceneManager.getRenderableProviders().add(modelInstance);
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        for (Component c : gameObject.getComponents()) {
            if (c instanceof RenderableComponent) {
                RenderableComponent component = (RenderableComponent) c;
                modelInstance = component.getObj();
            }
        }
    }

    public void deselectGameObject() {
        gameObject = null;
        modelInstances.clear();
    }

    public void captureFBO(SceneGraph sceneGraph){
        buf.ensureFboSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buf.begin(Salient.INSTANCE.getUi().viewportWidget.viewport);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        modelBatch.render(modelInstance);
        modelBatch.end();
        buf.end();
//        texture = buf.getColorBufferTexture();
//        textureRegion = new TextureRegion(texture);
//        textureRegion.flip(false, true);
    }

    public void renderFBO(SceneGraph sceneGraph) {
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(buf.getFboTexture(),0 , 0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight() );
        batch.end();
    }

    public void render(SceneGraph sceneGraph) {
        if (modelInstances.isEmpty()) return;
        captureFBO(sceneGraph);
        renderFBO(sceneGraph);
    }

    public void captureDepth(SceneGraph sceneGraph){
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        depthBuffer.begin();
        ScreenUtils.clear(.2f,.2f,.2f,0, true);
        depthBatch.begin(sceneGraph.getCurrScene().getPerspectiveCamera());
        depthBatch.render(modelInstance,sceneGraph.getCurrScene().getSceneContext().getEnv());
        depthBatch.end();

        Salient.INSTANCE.getProjectManager().sceneManager.currentScene.getSceneRenderer().renderDepth();
        depthBuffer.end();
        depthTexture = depthBuffer.getColorBufferTexture();
    }

    public void renderDepthOutline(SceneGraph sceneGraph) {
        captureDepth(sceneGraph);
        outlineDepth.render(batch,depthTexture,sceneGraph.getCurrScene().getPerspectiveCamera());
    }


    @Override
    public void onGameObjectSelected(@NotNull GameObjectSelectedEvent event) {
        setGameObject(event.getGameObject());
    }
}


