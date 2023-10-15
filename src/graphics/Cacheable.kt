package graphics

import com.badlogic.gdx.graphics.g3d.ModelCache

interface Cacheable {

    fun shouldCache(): Boolean

    fun addToCache(cache: ModelCache)

}