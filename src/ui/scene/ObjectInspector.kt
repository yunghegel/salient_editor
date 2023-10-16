package ui.scene

import com.badlogic.gdx.utils.Align
import org.yunghegel.gdx.ui.UI
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.viewport.SImageButton
import ui.TransformWidget

class ObjectInspector : STable() {

    val globalTransformWidget = TransformWidget()
    val buttonTable: STable = STable()
    val container: STable = STable()
    val sidebarButtons: STable = STable()
    init{
        val transform = SImageButton("transform")
        val material = SImageButton("material")
        val settings = SImageButton("settings")
        val mesh = SImageButton("mesh")
        val light = SImageButton("light")
        val scene = SImageButton("scene")
        align(Align.top)

        sidebarButtons.add(settings).pad(5f,0f,5f,0f).size(22f).row()
        sidebarButtons.add(transform).pad(5f,0f,5f,0f).size(22f).row()
        sidebarButtons.add(scene).pad(5f,0f,5f,0f).size(22f).row()
        sidebarButtons.add(material).pad(5f,0f,5f,0f).size(22f).row()
        sidebarButtons.add(mesh).pad(5f,0f,5f,0f).size(22f).row()
        sidebarButtons.add(light).pad(5f,0f,5f,0f).size(22f).center().row()
        sidebarButtons.align(Align.top)
        setBackground("border")
        UI.tooltip(transform,"Transform")
        UI.tooltip(material,"Material")
        UI.tooltip(settings,"Settings")
        UI.tooltip(mesh,"Mesh")
        UI.tooltip(light,"Light")
        UI.tooltip(scene,"Scene")
        val buttonGroup = com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup<SImageButton>()
        buttonGroup.add(transform)
        buttonGroup.add(material)
        buttonGroup.add(settings)
        buttonGroup.add(mesh)
        buttonGroup.add(light)
        buttonGroup.add(scene)
        buttonGroup.uncheckAll()
        buttonGroup.setMaxCheckCount(1)
        buttonGroup.setMinCheckCount(1)
        buttonGroup.setUncheckLast(true)
        transform.isChecked=true


        buttonTable.add(sidebarButtons).width(26f).fillY().expandY().top().padTop(5f)
        add(buttonTable).fillY().expandY().top()
        add(globalTransformWidget).fill().expand().top().row()




    }



        }




