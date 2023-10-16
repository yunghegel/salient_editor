package ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Separator
import org.yunghegel.gdx.ui.widgets.SLabel
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.viewport.SImageButton


class CollapsibleWidget(val titleText: String,val content: Actor) :CustomWidget() {


    val titleTable = object: STable() {
        override fun getPrefWidth(): Float {
            return parent.width
        }
    }
    val title = SLabel(titleText)
    val expandIcon: SImageButton = SImageButton("expand-collapse")

    init {
        setBackground("list-background-dark")
        align(Align.left)
        titleTable.add(expandIcon).size(20f, 20f).padRight(5f)
        titleTable.add(title).padRight(5f).expandX().fillX()
        titleTable.align(Align.left)
        add(titleTable).padBottom(5f).left().expandX().fillX().row()
        expandIcon.addListener(object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                toggleContent()
            }
        })
        add(Separator()).growX().row()
        }


    fun showContent() {
        add(content).padLeft(10f).padBottom(5f).row()
    }

    fun hideContent() {
        removeActor(content)
    }

    fun toggleContent() {
        if(content.parent == null) {
            showContent()
        } else {
            hideContent()
        }
    }

    fun isContentVisible(): Boolean {
        return content.parent != null
    }

    fun addButtonListener(listener: () -> Unit) {
        expandIcon.addListener {
            listener()
            true
        }
    }





}