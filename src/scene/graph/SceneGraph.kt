package scene.graph

import app.Salient
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array
import events.scene.GameObjectAddedEvent
import org.yunghegel.gdx.scenegraph.scene3d.BaseScene
import scene.Scene
import sys.Log

class SceneGraph(sceneObj: Scene)  {

    val currScene = sceneObj
    var scene: BaseScene? = null
    companion object {
        var count: Int = 0
    }

    var rootGameObj: GameObject = GameObject(this, "ROOT", 0, Matrix4())

    init {
        }


     fun render(delta: Float) {
//        currScene.sceneRenderer.batch.begin(currScene.perspectiveCamera)
        rootGameObj.children.forEach{ it.render(delta) }
//        currScene.sceneRenderer.batch.end()
    }

     fun addGameObject(go: GameObject) {
         rootGameObj.addChild(go)
         Salient.postEvent(GameObjectAddedEvent(go ))
        Log.info("Added game object " + go + " to scene graph")

    }
     fun update(delta: Float) {
        for (go in rootGameObj.getChildren()) {
            go.update(delta)
        }
    }
     fun getGameObjects(): Array<GameObject> {
        return rootGameObj.getChildren()
    }

     fun getGameObjectByName(name: String?): GameObject {
        return rootGameObj.findChildrenByName(name)
    }

    fun getRoot(): GameObject {
        return rootGameObj
    }


     fun getGameObjectCount(): Int {
        var count = 0
        for (go in rootGameObj.getChildren()) {
            count++
            countChildren(go, count)
        }
        return count
    }

     fun countChildren(go: GameObject, count: Int): Int {
        var count = count
        if (go.getChildren() == null) return count
        for (child in go.getChildren()) {
            if (child != null) {
                count++
                countChildren(child, count)
            }
        }
        return count
    }




}