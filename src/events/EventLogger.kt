package events

import com.badlogic.gdx.files.FileHandle
import com.google.common.base.Charsets
import com.google.common.base.Utf8
import events.lifecycle.ShutdownEvent
import io.FileService
import org.yunghegel.gdx.utils.StringUtils
import org.yunghegel.gdx.utils.console.ConsoleUtils
import sys.Log
import util.Format
import java.beans.Encoder
import java.io.ByteArrayOutputStream
import java.io.Writer
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.function.Consumer
import java.util.logging.LogRecord
import java.util.stream.Collectors
import java.util.stream.Stream


open class EventLogger  {










    init {

    }




    enum class State {
        CREATED, POSTED, HANDLED
    }


    data class  EventLog(val event: Event, val state: State,var desc: String = "") {

         var postTime: Instant = Instant.now()
         var handleTime: Instant = Instant.now()


        init {
            var msg = when(state) {
                State.CREATED -> "[${event::class.java.simpleName} ${state.name}]\n${event.record}}"
                State.POSTED -> "[${event::class.java.simpleName} ${state.name}]\nMSG: ${event.msg}"
                State.HANDLED -> handled()
            }
            msg+="DESC: ${event.desc}"

        }

        override fun toString(): String {


            return when(state) {
                State.CREATED -> "[${event::class.java.simpleName} ${state.name}]\n${event.record}}"
                State.POSTED -> "[${event::class.java.simpleName} ${state.name}]\nMSG: ${event.msg}"
                State.HANDLED -> handled()}
        }

        fun  time() : String {
            return (SimpleDateFormat("HH:mm:ss").format(Date()).toString())
        }

        fun head():String {
            return "[${event::class.java.simpleName} ${state.name}]"
        }

        fun stack(strBuilder:StringBuilder?) :StringBuilder {

            val builder = StringBuilder().append("\n")
            val startIdx = 10

          var list =  StackWalker.getInstance().walk { s: Stream<StackWalker.StackFrame?> ->
                s.skip(
                    6
                )
                    .collect(Collectors.toList()).takeWhile { it?.className!!.contains("Editor") }


            }
            val endIdx  = builder.length - 1

            return if(strBuilder != null) {
                strBuilder.appendLine(builder.toString())
                strBuilder
            } else {
                builder
            }

        }

        fun created(): String {
            val record = StringBuilder()







            return head()
        }

        fun ansiToUnicode(str:String) : String {
//            validate that the string is ansi


            val arr = str.byteInputStream(Charsets.UTF_8).readBytes()
            var utf= String(arr, Charsets.UTF_8)
            while (utf.contains(Format.ANSI_RESET)) {
                utf = utf.replace(Format.ANSI_RESET, "")
            }
            return utf

        }


        fun posted():String {
            postTime = Instant.now()

            val record = event.record
            return "[${event::class.java.simpleName} POSTED] from SOURCE: $record\n"
        }

        fun handled():String {
            handleTime = Instant.now()
            return "[${event::class.java.simpleName} HANDLED] ${time()}"
        }

    }

    companion object {


    }




}

open class DefaultLogger:EventLogger(), Event.Listener, ShutdownEvent.Listener {

    val stack = Stack<EventLogger.EventLog>()
    val logFile : FileHandle
    val logFileWriter : Writer
    val bytesOut = ByteArrayOutputStream()

    init {

        logFile = FileService.getLogFile()
        logFileWriter = logFile.writer(true)

    }

    fun checkLineCount() {
        val lines = logFile.reader().readLines()
        if(lines.size > 2000) {
            val newLines = lines.subList(1000, lines.size)
            logFileWriter.write(newLines.joinToString("\n"))
            logFileWriter.flush()
        }
    }

    fun log(event: Event, state: EventLogger.State) {
        checkLineCount()
        val log = EventLogger.EventLog(event , state)
        println(log)

        logFileWriter.write((log.toString()))
        logFileWriter.flush()



    }

    override fun onShutdown(event: ShutdownEvent) {
        logFileWriter.write("Event Log: \n")


        logFileWriter.close()
        logFileWriter.flush()
    }

    override fun eventPosted(event:Event) {
         log(event, EventLogger.State.POSTED)
    }

    override fun eventHandled(event: Event): Boolean {
    log(event, EventLogger.State.HANDLED)
        return true
    }

    override fun eventCreated(event: Event) {
        log(event, EventLogger.State.CREATED)
    }


}