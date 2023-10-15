package app

import assets.AssetService
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT
import com.badlogic.gdx.graphics.Pixmap
import com.crashinvaders.vfx.VfxManager
import com.crashinvaders.vfx.effects.FxaaEffect
import ktx.async.KtxAsync
import project.ProjectManager


class Editor : ApplicationAdapter() {

    lateinit var projectManager: ProjectManager
    lateinit var effectsManager : VfxManager

    override fun create() {
        super.create()
        KtxAsync.initiate()
        projectManager = Salient.inject()
        Salient.initializeProjectContext()

        effectsManager= VfxManager(Pixmap.Format.RGBA8888)
        effectsManager.addEffect(FxaaEffect())

    }


    override fun render() {
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        Gdx.gl.glClearColor(0.1f,0.1f,0.1f,0f)
        projectManager.sceneManager.currentScene?.update(Gdx.graphics.deltaTime)
        AssetService.processQueue(Salient.projectManager.assetManager)

        Salient.ui.render(Gdx.graphics.deltaTime)




    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        projectManager.sceneManager.currentScene?.resize(width, height)
        Salient.ui.resize(width, height)

    }

    inline fun <reified> clearColorAndDepthBuffer() {
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    inline fun <reified> clearColor() {
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f,0f)
    }

}