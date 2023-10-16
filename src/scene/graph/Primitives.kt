package scene.graph

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import org.yunghegel.gdx.utils.graphics.model.Primitive
import org.yunghegel.gdx.utils.graphics.model.PrimitiveSupplier


abstract class PrimitiveModel(val primitive: Primitive) {

    companion object {
        fun selector(primitive: Primitive) : PrimitiveModel {
            return when(primitive) {
                Primitive.Box -> Box(1f,1f,1f)
                Primitive.Sphere -> Sphere(1f)
                Primitive.Cylinder -> Cylinder(1f,1f)
                Primitive.Cone -> Cone(1f,1f)
                Primitive.Plane -> Plane(1f,1f)
                Primitive.Capsule -> Capsule(1f,2f)
                Primitive.Cube -> Cube(1f)
            }


        }
    }

    abstract fun getModel(): Model

   abstract fun getRenderable(): ModelInstance

}

class Box(val width:Float,val depth:Float,val height:Float) : PrimitiveModel(Primitive.Box) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getBox(width,depth,height)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return PrimitiveSupplier.getBox(width,depth,height)
    }

}

class Sphere(val radius:Float) : PrimitiveModel(Primitive.Sphere) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getSphere(radius)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}

class Cylinder(val radius:Float,val height:Float) : PrimitiveModel(Primitive.Cylinder) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getCylinder(radius,height)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}

class Cone(val radius:Float,val height:Float) : PrimitiveModel(Primitive.Cone) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getCone(radius,height)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}

class Plane(val scale:Float,val height:Float) : PrimitiveModel(Primitive.Plane) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getPlane(scale)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}

class Capsule(val radius:Float,val height:Float) : PrimitiveModel(Primitive.Capsule) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getCapsule(radius,height)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}

class Cube(val size:Float) : PrimitiveModel(Primitive.Cube) {

    val instance:ModelInstance

    init {
        instance = PrimitiveSupplier.getCube(size)
    }

    override fun getModel(): Model {
        return instance.model
    }

    override fun getRenderable(): ModelInstance {
        return instance
    }

}







