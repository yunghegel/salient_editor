package events.lifecycle

import events.LifecycleEvent
import events.Subscribe

class ShutdownEvent : LifecycleEvent() {

    interface Listener {

        @Subscribe
        fun onShutdown(event: ShutdownEvent)
    }

}