package ui.fields

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener

class FloatFieldWithLabel : TextFieldWithLabel {

    init {
        addScrollListener()
        addDragListener()
    }

    constructor(labelText: String, width: Int, allowNegative: Boolean) : super(labelText, width) {
        textField.textFieldFilter = FloatOnlyFilter()
    }

    constructor(labelText: String, width: Int) : super(labelText, width) {
        textField.textFieldFilter = FloatOnlyFilter()
    }

    fun isValid(): Boolean {
        // A failsafe catchall to see if its valid
        return textField.text.matches(Regex("-?\\d+(\\.\\d+)?"))
    }

    val float: Float
        get() {
            if (isValid()) {
                return java.lang.Float.parseFloat(textField.text)
            }
            return 0f
        }

    fun addDragListener(){
        textField.addListener(object : DragListener() {
            override fun dragStart(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Gdx.input.isCursorCatched = true
            }

            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.drag(event, x, y, pointer)
                if (isValid()) {
                    val delta = Gdx.input.getDeltaY() * 0.1f
                    textField.text = (java.lang.Float.parseFloat(textField.text) + delta).toString()
                    Gdx.input.setCursorPosition(x.toInt(), y.toInt())
                }
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.dragStop(event, x, y, pointer)
                Gdx.input.isCursorCatched = false
            }
        })
    }
    fun addScrollListener(){
        textField.addListener(object : InputListener() {
            override fun scrolled(event: InputEvent?, x: Float, y: Float, amountX: Float, amountY: Float): Boolean {
                if (isValid()) {
                    textField.text = (java.lang.Float.parseFloat(textField.text) + amountY).toString()
                }
                return super.scrolled(event, x, y, amountX, amountY)
            }
        })
    }
}