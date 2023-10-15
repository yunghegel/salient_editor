package events.io

import events.IOEvent
import events.Subscribe

class FilesDroppedEvent(vararg val files: String) : IOEvent() {

        interface Listener {
            @Subscribe
            fun onFilesDropped(event: FilesDroppedEvent)
        }
}