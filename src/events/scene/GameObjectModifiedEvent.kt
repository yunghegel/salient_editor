package events.scene

import events.Event
import events.Subscribe
import scene.graph.GameObject

class GameObjectModifiedEvent(val type: ModificationType, val gameObject: GameObject) : Event(){

    interface Listener {
        @Subscribe
        fun gameObjectModified(event: GameObjectModifiedEvent)
    }

}


enum class ModificationType {
    POSITION, ROTATION, SCALE, FLAGS
}