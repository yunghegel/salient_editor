package ui

import app.Salient
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Align
import events.gameObjectModified
import events.gameObjectSelected
import ktx.actors.onChange
import org.yunghegel.gdx.ui.widgets.SLabel
import org.yunghegel.gdx.ui.widgets.STable
import scene.graph.GameObject
import ui.fields.FloatFieldWithLabel

class TransformWidget(val mat4: Matrix4= Matrix4()) :STable() {

    val content: STable = STable()
    val collapsibleWidget = CollapsibleWidget("Transform", content)

    companion object {
        private val tempV3 = Vector3()
        private val tempQuat = Quaternion()
    }

    private val FIELD_SIZE = 70
    private val posX = FloatFieldWithLabel("x", FIELD_SIZE)
    private val posY = FloatFieldWithLabel("y", FIELD_SIZE)
    private val posZ = FloatFieldWithLabel("z", FIELD_SIZE)

    private val rotX = FloatFieldWithLabel("x", FIELD_SIZE)
    private val rotY = FloatFieldWithLabel("y", FIELD_SIZE)
    private val rotZ = FloatFieldWithLabel("z", FIELD_SIZE)

    private val sclX = FloatFieldWithLabel("x", FIELD_SIZE)
    private val sclY = FloatFieldWithLabel("y", FIELD_SIZE)
    private val sclZ = FloatFieldWithLabel("z", FIELD_SIZE)

    init {
        align(Align.top)

        posX.text="0.00"
        posY.text="0.00"
        posZ.text="0.00"
        rotX.text="0.00"
        rotY.text="0.00"
        rotZ.text="0.00"
        sclX.text="1.00"
        sclY.text="1.00"
        sclZ.text="1.00"

    setBackground("button-down")

        content.add(SLabel("Position: ")).pad(5f).row()
        content.add(posX).padRight(2f).padLeft(2f)
        content.add(posY).padRight(2f).padLeft(2f)
        content.add(posZ).padRight(2f).padLeft(2f).row()

        content.add(SLabel("Rotation: ")).pad(5f).row()
        content.add(rotX).padRight(2f).padLeft(2f)
        content.add(rotY).padRight(2f).padLeft(2f)
        content.add(rotZ).padRight(2f).padLeft(2f).padTop(5f).row()

        content.add(SLabel("Scale: ")).pad(5f).row()
        content.add(sclX).padRight(2f).padLeft(2f)
        content.add(sclY).padRight(2f).padLeft(2f)
        content.add(sclZ).padRight(2f).padLeft(2f).padTop(5f).row()

        collapsibleWidget.showContent()

        add(collapsibleWidget).padBottom(5f).top().growX().row()

        buildListeners()

        gameObjectSelected {
            setValues(it)
        }

        gameObjectModified {
            setValues(it)
        }


    }

    fun buildListeners() {
        rotX.onChange {
            val go = getGameObj() ?: return@onChange
            val rot = go.getLocalRotation(tempQuat)
            go.setLocalRotation(rot.yaw, rotX.float, rot.roll)
        }
        rotY.onChange {
            val go = getGameObj() ?: return@onChange
            val rot = go.getLocalRotation(tempQuat)
            go.setLocalRotation(rotX.float, rot.yaw, rot.roll)
        }
        rotZ.onChange {
            val go = getGameObj() ?: return@onChange
            val rot = go.getLocalRotation(tempQuat)
            go.setLocalRotation(rotX.float, rot.yaw, rot.roll)
        }
        posX.onChange {
            val go = getGameObj() ?: return@onChange
            val pos = go.getLocalPosition(tempV3)
            go.setLocalPosition(posX.float, pos.y, pos.z)

        }
        posY.onChange {
            val go = getGameObj() ?: return@onChange
            val pos = go.getLocalPosition(tempV3)
            go.setLocalPosition(pos.x, posY.float, pos.z)
        }
        posZ.onChange {
            val go = getGameObj() ?: return@onChange
            val pos = go.getLocalPosition(tempV3)
            go.setLocalPosition(pos.x, pos.y, posZ.float)
        }
        sclX.onChange {
            val go = getGameObj() ?: return@onChange
            val scale = go.getLocalScale(tempV3)
            go.setLocalScale(sclX.float, scale.y, scale.z)
        }
        sclY.onChange {
            val go = getGameObj() ?: return@onChange
            val scale = go.getLocalScale(tempV3)
            go.setLocalScale(scale.x, sclY.float, scale.z)
        }
        sclZ.onChange {
            val go = getGameObj() ?: return@onChange
            val scale = go.getLocalScale(tempV3)
            go.setLocalScale(scale.x, scale.y, sclZ.float)
        }
    }

    fun setValues(go: GameObject) {
        val pos = go.getLocalPosition(tempV3)
        posX.text = pos.x.toString()
        posY.text = formatFloat(pos.y, 2)
        posZ.text = formatFloat(pos.z, 2)

        val rot = go.getLocalRotation(tempQuat)
        rotX.text = formatFloat(rot.pitch, 2)
        rotY.text = formatFloat(rot.yaw, 2)
        rotZ.text = formatFloat(rot.roll, 2)

        val scl = go.getLocalScale(tempV3)
        sclX.text = formatFloat(scl.x, 2)
        sclY.text = formatFloat(scl.y, 2)
        sclZ.text = formatFloat(scl.z, 2)
    }

    override fun act(delta: Float) {
        super.act(delta)

        val go = getGameObj()
        if (go != null) {
            if(go.flags.has(util.Flags.INTERACTING)) setValues(go)

        }
    }

    fun getGameObj(): GameObject? {
        return Salient.getSelectedGameObject()
    }

    fun formatFloat(float: Float,places:Int):String{
        return String.format("%.${places}f",float)
    }

}