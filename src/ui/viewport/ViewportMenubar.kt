package ui.viewport

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar

class ViewportMenubar: MenuBar("no-bg") {



    init {
     val items =   getTable().getChild(0) as Table
        items.pad(0f,0f,0f,0f)
        val view = addMenu(Menu("View","viewport"))
        val add = addMenu(Menu("Add","viewport"))
        val select = addMenu(Menu("Select","viewport"))
        val obj = addMenu(Menu("Object","viewport"))

        items.cells.forEach { it.pad(3f,3f,0f,3f)
            it.width(60f)
            it.align(Align.center)
        }
    }



}