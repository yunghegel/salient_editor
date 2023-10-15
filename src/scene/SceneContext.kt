package scene

import app.Salient
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.utils.Disposable
import net.mgsx.gltf.scene3d.attributes.FogAttribute
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx
import net.mgsx.gltf.scene3d.scene.SceneSkybox
import net.mgsx.gltf.scene3d.utils.IBLBuilder
import org.yunghegel.gdx.gizmo.core.utility.CompassGizmo
import org.yunghegel.gdx.utils.graphics.Grid
import scene.context.env.EnvironmentData
import util.SceneUtils

class SceneContext(var config: EnvironmentData = EnvironmentData.default) : Disposable {

    var env: Environment

    lateinit var skybox: SceneSkybox
    lateinit var grid: Grid
    lateinit var compass: CompassGizmo

    init {
        env = Environment()
        sharedSetup()
        defaultGlConfig()
        configLighting()
        configFog()
    }

    private fun configLighting(){
        env.set(ColorAttribute(ColorAttribute.AmbientLight, config.globalLighting.ambientLight, config.globalLighting.ambientLight, config.globalLighting.ambientLight, 1f))
        var directionalLight = DirectionalLightEx()
        directionalLight.set(config.globalLighting.directionalLight.x, config.globalLighting.directionalLight.y, config.globalLighting.directionalLight.z, config.globalLighting.directionalLight.r, config.globalLighting.directionalLight.g, config.globalLighting.directionalLight.b)

        env.add(directionalLight)
    }

    private fun configFog(){
        env.set(ColorAttribute(ColorAttribute.Fog, config.fog.r, config.fog.g, config.fog.b, config.fog.a))
        env.set(FogAttribute(FogAttribute.FogEquation).set(config.fog.near,config.fog.far,config.fog.exponent))
    }

    private fun sharedSetup() {
        val customSkyboxCubemap = SceneUtils.createCubemapDirectionFormat("editor/", "editor/")
        skybox = SceneSkybox(customSkyboxCubemap)

        grid = Grid(0.2f, 300, 1f, 0.5f)
        compass = CompassGizmo(Salient.inputManager.inputMultiplexer,Salient.modelBatch,Salient.camera, Salient.viewport,-1)

        val iblBuilder = IBLBuilder.createOutdoor(DirectionalLightEx())
        val brdfLUT = Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"))
        val environmentCubemap = iblBuilder.buildEnvMap(1024)
        val diffuseCubemap = iblBuilder.buildIrradianceMap(256)
        val specularCubemap = iblBuilder.buildRadianceMap(10)

        env.set(
            PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT)
        )
        env.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap))
        env.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap))
    }

    fun defaultGlConfig(){
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL)
        Gdx.gl.glDepthMask(true)

        Gdx.gl.glEnable(GL20.GL_CULL_FACE)
        Gdx.gl.glCullFace(GL20.GL_BACK)

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun dispose() {
        skybox.dispose()
        grid.dispose()
        compass.dispose()

    }

}