package graphics

class RenderSequence {

    fun begin(beforeRender: () -> Unit){
        beforeRender()
    }

    fun end(afterRender: () -> Unit){
        afterRender()
    }

    fun render(renderer: Renderer, delta: Float){
        renderer.render(delta)
    }



}