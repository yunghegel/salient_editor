package ui.viewport

import app.Salient
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.MenuItem
import org.yunghegel.gdx.utils.graphics.model.Primitive
import scene.graph.PrimitiveImporter
import scene.graph.PrimitiveModel

class ViewportMenubar: MenuBar("no-bg") {

    val view = (Menu("View","viewport"))
    val add = (Menu("Add","viewport"))
    val select = (Menu("Select","viewport"))
    val obj = (Menu("Object","viewport"))

    init {
     val items =   getTable().getChild(0) as Table
        items.pad(0f,0f,0f,0f)
        items.cells.forEach { it.pad(3f,3f,0f,3f)
            it.width(60f)
            it.align(Align.center)
        }
        addMenu(view)
        addMenu(add)
        addMenu(select)
        addMenu(obj)
        addMenuItems()
    }

    fun addMenuItems(){
        for(primitive in Primitive.values()){
            val item = MenuItem(primitive.name)
            add.addItem(item)
            item.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: com.badlogic.gdx.scenes.scene2d.Actor) {
                    val go = PrimitiveImporter(PrimitiveModel.selector(primitive),Salient.projectManager.sceneManager.currentScene.sceneGraph).buildGo()
                    Salient.projectManager.sceneManager.currentScene.sceneGraph.addGameObject(go)
                }
            })

        }
    }



}