import arc.util.Log
import mindustry.entities.type.Player
import mindustry.gen.Call

class Printer {
    companion object {
        fun info(message: String) {
            Call.sendMessage(message)
            Log.info("[INFO] $message")
        }

        fun info(receiver: Player, message: String) {
            receiver.sendMessage(message)
            Log.info("[INFO] $message (send to: ${receiver.name})")
        }

        fun warn(message: String) {
            Call.sendMessage("[orange]$message[]")
            Log.info("[WARN] $message")
        }

        fun warn(receiver: Player, message: String) {
            receiver.sendMessage("[orange]$message[]")
            Log.info("[WARN] $message (send to: ${receiver.name})")
        }

        fun error(message: String) {
            Call.sendMessage("[crimson]$message[]")
            Log.info("[ERROR] $message")
        }

        fun error(receiver: Player, message: String) {
            receiver.sendMessage("[crimson]$message[]")
            Log.info("[ERROR] $message (send to: ${receiver.name})")
        }
    }
}