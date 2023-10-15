package graphics

fun interface Renderer {

    fun render(delta: Float)

}

interface BeforeRender {
    fun beforeRender(delta: Float)
}

interface AfterRender {
    fun afterRender(delta: Float)
}