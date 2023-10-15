package events.scene

import events.GameObjectEvent
import scene.graph.GameObject

class GameObjectTransformedEvent(gameObject: GameObject) : GameObjectEvent(gameObject) {

        init {
            msg = "GameObject ${gameObject.name} transformed; { id: ${gameObject.id} }"
        }

        interface Listener {
            fun onGameObjectTransformed(event: GameObjectTransformedEvent)
        }
}