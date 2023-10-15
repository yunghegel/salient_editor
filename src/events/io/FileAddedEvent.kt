package events.io

import events.IOEvent
import events.Subscribe

class FilesAddedEvent(vararg val files: String) : IOEvent() {

    interface Listener {
        @Subscribe
        fun onFilesAdded(event: FilesAddedEvent)
    }

}