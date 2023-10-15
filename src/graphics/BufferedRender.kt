package graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer

class BufferedRender(val batch: SpriteBatch) {



    fun begin(beforeRender: () -> Unit){
        beforeRender()
    }

    fun end(afterRender: ()-> Unit){
        afterRender()
    }

    fun render(renderer: Renderer, delta: Float){
        ensureBuffer()
        renderToBuffer(renderer, delta)

    }


    var buffer = FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, true)

    fun renderToBuffer(renderer: Renderer,delta: Float){
        with (buffer){
            begin()
            renderer.render(delta)
            end()
        }
    }

    fun renderBuffer(){
        batch.draw(buffer.colorBufferTexture, 0f, 0f)
    }

    fun ensureBuffer(){
        if(buffer.width != Gdx.graphics.width || buffer.height != Gdx.graphics.height){
            buffer = FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, true)
        }
    }

}