package events.lifecycle

import events.LifecycleEvent
import events.Subscribe

class StartupEvent : LifecycleEvent() {

    interface Listener {
        @Subscribe
        fun  onStartup(event: StartupEvent)
    }

}