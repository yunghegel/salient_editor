package scene.graph

import app.Salient
import com.badlogic.gdx.utils.Array
import events.scene.GameObjectAddedEvent
import org.yunghegel.gdx.scenegraph.scene3d.BaseScene
import scene.Scene
import sys.Log

class SceneGraph(sceneObj: Scene)  {

    val currScene = sceneObj
    var scene: BaseScene? = null
    lateinit var root: GameObject


     fun render(delta: Float) {
//        currScene.sceneRenderer.batch.begin(currScene.perspectiveCamera)
        root.children.forEach{ it.render(delta) }
//        currScene.sceneRenderer.batch.end()
    }

     fun addGameObject(go: GameObject) {
        Salient.postEvent(GameObjectAddedEvent(go ))
        Log.info("Added game object " + go + " to scene graph")
         root.addChild(go)
    }
     fun update(delta: Float) {
        for (go in root.getChildren()) {
            go.update(delta)
        }
    }
     fun getGameObjects(): Array<GameObject> {
        return root.getChildren()
    }

     fun getGameObjectByName(name: String?): GameObject {
        return root.findChildrenByName(name)
    }

    fun getRoot(): GameObject {
        return root
    }


     fun getGameObjectCount(): Int {
        var count = 0
        for (go in root.getChildren()) {
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