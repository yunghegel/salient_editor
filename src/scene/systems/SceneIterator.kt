package scene.systems

import ktx.collections.isNotEmpty
import scene.graph.GameObject
import java.util.*

class SceneIterator(val gameObject: GameObject) : Iterator<GameObject> {

    val stack : Stack<GameObject> = Stack()

    override fun hasNext(): Boolean {
        return stack.isNotEmpty()
    }

    override fun next(): GameObject {
        val next = stack.pop()
        if(next.children.isNotEmpty()){
            next.children.forEach(stack::push)
        }
        return next
    }

}