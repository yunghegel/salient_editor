package tools

import app.App
import app.Salient
import com.badlogic.gdx.math.Matrix4
import events.scene.GameObjectDeselectedEvent
import events.scene.GameObjectSelectedEvent
import org.yunghegel.gdx.gizmo.GizmoManager
import org.yunghegel.gdx.gizmo.core.GizmoType
import org.yunghegel.gdx.gizmo.core.transform.BasicTransformTarget
import org.yunghegel.gdx.gizmo.core.transform.TransformGizmoTarget
import scene.graph.GameObject
import util.Sal

object ToolManager : GameObjectSelectedEvent.Listener,GameObjectDeselectedEvent.Listener {
    var toolMode = ToolMode.NONE
    val gameObjectPicker = GameObjectPicker()
    val gizmoManager = GizmoManager(Salient.inputManager.inputMultiplexer, Salient.projectManager.sceneManager.currentScene.perspectiveCamera, Salient.projectManager.sceneManager.currentScene.viewport)

    init {
        GameObjectPicker.PickerShader.init()
        Salient.registerListener(this)
        gizmoManager.setSharedTarget(TransformGizmoTarget(BasicTransformTarget(Matrix4())))
    }

    fun setMode(mode: ToolMode) {
        toolMode = mode
    }

    fun processActiveTool() {

        when (toolMode) {
            ToolMode.SELECT -> {
                gameObjectPicker.processPicks(Salient.projectManager.sceneManager.currentScene)
                if(Salient.getSelectedGameObject()!=null) {
                    Sal.getSelectedGameObject()!!.flags.clear(util.Flags.INTERACTING)
                }


            }
            ToolMode.TRANSLATE -> {
                if(Salient.getSelectedGameObject()==null) return
                gizmoManager.setIfNotCurrent(GizmoType.TRANSLATE)
                if(App.i.isButtonPressed(0)) {
                    Sal.getSelectedGameObject()!!.flags.set(util.Flags.INTERACTING)

                }
            }

            ToolMode.ROTATE -> {
                if(Salient.getSelectedGameObject()==null) return
                gizmoManager.setIfNotCurrent(GizmoType.ROTATE)
                if(App.i.isButtonPressed(0)) {
                    Sal.getSelectedGameObject()!!.flags.set(util.Flags.INTERACTING)
                }            }

            ToolMode.SCALE -> {
                if(Salient.getSelectedGameObject()==null) return
                gizmoManager.setIfNotCurrent(GizmoType.SCALE)
                if(App.i.isButtonPressed(0)) {
                    Sal.getSelectedGameObject()!!.flags.set(util.Flags.INTERACTING)
                }
            }
            else -> {

                return
            }
        }
        if (toolMode ==ToolMode.TRANSLATE||toolMode ==ToolMode.ROTATE||toolMode ==ToolMode.SCALE){
            gizmoManager.update()

            if(Salient.projectManager.sceneManager.currentScene.selectedGameObjects.size>0){
                gizmoManager.setTarget(TransformGizmoTarget(Salient.projectManager.sceneManager.currentScene.selectedGameObjects.first()))
            }
        }
    }

    fun renderTool() {
        if (toolMode ==ToolMode.TRANSLATE||toolMode ==ToolMode.ROTATE||toolMode ==ToolMode.SCALE){
            gizmoManager.render()
        }
    }

    fun ensureGizmo() {

    }

    override fun onGameObjectDeselected(event: GameObjectDeselectedEvent) {
        gizmoManager.disableAll()
    }

    override fun onGameObjectSelected(event: GameObjectSelectedEvent) {
        gizmoManager.setSharedTarget(TransformGizmoTarget(event.gameObject))
    }



}

inline fun <T:Any> T.transformToolActive(crossinline listener: T.(GameObject) -> Unit) {
    if(ToolManager.toolMode == ToolMode.TRANSLATE||ToolManager.toolMode == ToolMode.ROTATE||ToolManager.toolMode == ToolMode.SCALE) {
        listener(Salient.getSelectedGameObject()!!)
    }
}