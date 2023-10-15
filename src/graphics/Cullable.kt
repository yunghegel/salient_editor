package graphics

import com.badlogic.gdx.graphics.Camera

interface Cullable {

    fun shouldCull(cam: Camera): Boolean

}