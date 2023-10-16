package util

import app.App.log
import com.badlogic.gdx.utils.BooleanArray
import scene.graph.GameObject


open class Flags(var flags: Long= VISIBLE or SELECTABLE, val amount: Int = 8) : BooleanArray(amount),Iterable<Boolean> {

    companion object {
        const val VISIBLE = 1L shl 0
        const val HIDDEN = 1L shl 1
        const val HOVER = 1L shl 2
        const val PRESSED = 1L shl 3
        const val FOCUSED = 1L shl 4
        const val SELECTED = 1L shl 5
        const val LOCKED = 1L shl 6
        const val SELECTABLE = 1L shl 7
        const val INTERACTING = 1L shl 8
    }


    override fun get(index: Int): Boolean {
        return flags and (1L shl index) == (1L shl index)
    }

    override fun set(index: Int, value: Boolean) {
        if (value) {
            flags = flags or (1L shl index)
        } else {
            flags = flags and (1L shl index).inv()
        }
    }

    override fun iterator(): kotlin.collections.Iterator<Boolean> {
        return object : kotlin.collections.Iterator<Boolean> {
            var i = 0
            override fun hasNext(): Boolean {
                return i < 8
            }

            override fun next(): Boolean {
                val flag = 1L shl i
                i++
                return flags and flag == flag
            }
        }
    }
    fun set(flag: Long) {
        val idx= flag.toInt()
        flags = flags or flag
    }

    fun clear(flag: Long) {
        flags = flags and flag.inv()
    }

    fun has(flag: Long): Boolean {
        return flags and flag == flag
    }

    fun add(flags: Long) {
        this.flags = this.flags or flags
    }

    fun unset(flags: Long) {
        this.flags = this.flags and flags.inv()
    }

    fun removeFlags(flags: Long) {
        this.flags = this.flags and flags.inv()
    }

    infix fun Flags.and(flags: Long): Boolean {
        return this.flags and flags == flags
    }

    infix fun <T: Flags> Flags.or(other: T): Boolean {
        return this.flags or other.flags == other.flags
    }

    infix fun Flags.xor(flags: Long): Boolean {
        return this.flags xor flags == flags
    }



    override fun toString(): String {
        val sb = StringBuilder()
        if(has(VISIBLE)){
            sb.append("Visible/")
        }
        if(has(HIDDEN)){
            sb.append("Hidden/")
        }
        if(has(HOVER)){
            sb.append("Hover/")
        }
        if(has(PRESSED)){
            sb.append("Pressed/")
        }
        if(has(FOCUSED)){
            sb.append("Focused/")
        }
        if(has(SELECTED)){
            sb.append("Selected/")
        }
        if(has(LOCKED)){
            sb.append("Locked/")
        }
        if(has(SELECTABLE)){
            sb.append("Selectable/")
        }
        if(has(INTERACTING)){
            sb.append("Interacting/")
        }
        return sb.toString()
    }







    infix fun or(set: Flags): Flags {
        return Flags(flags or set.flags)
    }
    
    infix fun and(set: Flags): Flags {
        return Flags(flags and set.flags)
    }
    
    infix fun xor(set: Flags): Flags {
        return Flags(flags xor set.flags)
    }
    
    infix fun has(set: Flags): Boolean {
        return flags and set.flags == set.flags
    }
    
    infix fun not(set: Flags): Flags {
        return Flags(flags and set.flags.inv())
    }

    fun Flags.toLongArray(): LongArray {
        var i = 0;
        val array = LongArray(amount)
        while (i < 8) {
            val flag = 1L shl i
            if (has(flag)) {
                array[i] = flag
            }
            i++
        }
        return array
    }






    class Iterator(val flags: Flags) : kotlin.collections.Iterator<Long> {
        var i = 0
        override fun hasNext(): Boolean {
            return i < 8
        }

        override fun next(): Long {
            val flag = 1L shl i
            i++
            return flag
        }
    }
    

}

class RenderFlags : Flags(RENDER_SHADED or RENDER_SHADOWS) {

    companion object {
        const val RENDER_SHADED = 1L shl 0
        const val RENDER_NONE = 1L shl 1
        const val RENDER_DEPTH = 1L shl 2
        const val RENDER_COLOR = 1L shl 3
        const val RENDER_WIREFRAME = 1L shl 4
        const val RENDER_SHADOWS = 1L shl 5
        const val RENDER_OUTLINE = 1L shl 6
        const val RENDER_BOUNDS = 1L shl 7
    }


}


inline fun GameObject.has(flag: Long): Boolean {
    return flags.has(flag)
}

inline fun GameObject.setFlag(flag: Long) {
    flags.set(flag)
}

inline fun GameObject.clearFlag(flag: Long) {
    flags.clear(flag)
}

inline fun GameObject.flags(flags: () -> Long) {
    this.flags.set(flags())
}

inline fun GameObject.setFlags(block: () -> Array<Long>) {
    var flags = 0L
    block().forEach {
        flags = flags or it
    }
    this.flags.set(flags)
}

inline fun GameObject.ifFlag(flag: Long, block: () -> Unit) {
    if (flags.has(flag)) {
        block()
    } else {
        log {
            "Flag $flag not set"
        }
    }
}

inline fun GameObject.forEachFlag(block: (Long) -> Unit) {
    var i = 0
    while (i < flags.amount) {
        val flag = 1L shl i
        if (flags.has(flag)) {
            block(flag)
        } else {
            log {
                "Flag $flag not set"
            }
        }
        i++
    }
}

