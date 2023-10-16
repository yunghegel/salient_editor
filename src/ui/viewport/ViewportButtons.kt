package ui.viewport

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.viewport.SImageButton
import tools.ToolManager
import tools.ToolMode

object ViewportButtons {

    var size = 26f

    class ToolButtons : STable() {
        val VerticalGroup = VerticalGroup()
        val translateButton = SImageButton("translate")
        val rotateButton = SImageButton("rotate")
        val scaleButton = SImageButton("scale")
        val selectButton = SImageButton("select")
        val group = ButtonGroup<SImageButton>()

        init {
            VerticalGroup.addActor(translateButton)
            VerticalGroup.addActor(rotateButton)
            VerticalGroup.addActor(scaleButton)
            VerticalGroup.addActor(selectButton)
            VerticalGroup.align(Align.center)
            add(VerticalGroup).pad(3f)
            setBackground("border")

            group.add(translateButton)
            group.add(rotateButton)
            group.add(scaleButton)
            group.add(selectButton)

            group.uncheckAll()
            group.setMinCheckCount(0)

            selectButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (selectButton.isChecked) {
                        ToolManager.setMode(ToolMode.SELECT)
                    }
                }
            })

            scaleButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (scaleButton.isChecked) {
                        ToolManager.setMode(ToolMode.SCALE)
                    }
                }
            })

            rotateButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (rotateButton.isChecked) {
                        ToolManager.setMode(ToolMode.ROTATE)
                    }
                }
            })

            translateButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (translateButton.isChecked) {
                        ToolManager.setMode(ToolMode.TRANSLATE)
                    }
                }
            })

        }



        override fun act (delta: Float) {
            super.act(delta)
            val selected = group.checked
            if (selected != null) {
                group.uncheckAll()
                selectButton.isChecked = true
            }
        }
    }

    class MeshSelectionButtons : STable() {
        val vertexButton = SImageButton("select-vertex")
        val edgeButton = SImageButton("select-edge")
        val faceButton = SImageButton("select-face")
        init {
            add(vertexButton).pad(1f).size(size)
            add(edgeButton).pad(1f).size(size)
            add(faceButton).pad(1f).size(size)
        }
    }

    class ConfigButtons : STable() {
        val orthoButton = SImageButton("orthographic-view")
        val perspectiveButton = SImageButton("perspective-view")
        val snapButton = SImageButton("snap")

        init {
            add(orthoButton).pad(3f).size(size)
            add(perspectiveButton).pad(3f).size(size)
            add(snapButton).pad(3f).size(size)
        }
    }

    class RenderOptionButtons : STable() {
        val wireframeButton = SImageButton("wireframe")
        val shadedButton = SImageButton("shaded")

        init {
            add(wireframeButton).pad(3f)
            add(shadedButton).pad(3f)
        }
    }


    val toolButtons = ToolButtons()
    val meshSelectionButtons = MeshSelectionButtons()
    val configButtons = ConfigButtons()
    val renderOptionButtons = RenderOptionButtons()





}