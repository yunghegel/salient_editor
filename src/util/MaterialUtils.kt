package util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Attribute
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.*

object MaterialUtils {

    fun getAttributes(material: Material): Array<Attribute?> {
        val attributes = arrayOfNulls<Attribute>(material.size())
        for (i in 0 until material.size()) {
            attributes[i] = material[i.toLong()]
        }
        return attributes
    }

    fun getTextureAttributes(material: Material): Array<TextureAttribute?> {
        val attributes = arrayOfNulls<TextureAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is TextureAttribute) {
                attributes[i] = material[i.toLong()] as TextureAttribute
            }
        }
        return attributes
    }

    fun getColorAttributes(material: Material): Array<ColorAttribute?> {
        val attributes = arrayOfNulls<ColorAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is ColorAttribute) {
                attributes[i] = material[i.toLong()] as ColorAttribute
            }
        }
        return attributes
    }

    fun getBlendingAttributes(material: Material): Array<BlendingAttribute?> {
        val attributes = arrayOfNulls<BlendingAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is BlendingAttribute) {
                attributes[i] = material[i.toLong()] as BlendingAttribute
            }
        }
        return attributes
    }

    fun getDepthTestAttributes(material: Material): Array<DepthTestAttribute?> {
        val attributes = arrayOfNulls<DepthTestAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is DepthTestAttribute) {
                attributes[i] = material[i.toLong()] as DepthTestAttribute
            }
        }
        return attributes
    }

    fun getIntAttributes(material: Material): Array<IntAttribute?> {
        val attributes = arrayOfNulls<IntAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is IntAttribute) {
                attributes[i] = material[i.toLong()] as IntAttribute
            }
        }
        return attributes
    }

    fun getFloatAttributes(material: Material): Array<FloatAttribute?> {
        val attributes = arrayOfNulls<FloatAttribute>(material.size())
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is FloatAttribute) {
                attributes[i] = material[i.toLong()] as FloatAttribute
            }
        }
        return attributes
    }

    fun getTexturesFromMaterial(material: Material): Map<TextureAttribute,Texture> {
        val textureMap = mutableMapOf<TextureAttribute,Texture>()
        for (i in 0 until material.size()) {
            if (material[i.toLong()] is TextureAttribute) {
                val textureAttribute = material[i.toLong()] as TextureAttribute
                textureMap[textureAttribute] = textureAttribute.textureDescription.texture
            }
        }
        return textureMap
    }

}