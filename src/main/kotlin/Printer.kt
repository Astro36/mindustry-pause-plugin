import arc.util.Log
import mindustry.entities.type.Player
import mindustry.gen.Call

public class Printer {
    companion object {
        public fun info(message: String) {
            Call.sendMessage(message)
            Log.info("[INFO] $message")
        }

        public fun info(receiver: Player, message: String) {
            receiver.sendMessage(message)
            Log.info("[INFO] $message (send to: ${receiver.name})")
        }

        public fun warn(message: String) {
            Call.sendMessage("[orange]$message[]")
            Log.info("[WARN] $message")
        }

        public fun warn(receiver: Player, message: String) {
            receiver.sendMessage("[orange]$message[]")
            Log.info("[WARN] $message (send to: ${receiver.name})")
        }

        public fun error(message: String) {
            Call.sendMessage("[crimson]$message[]")
            Log.info("[ERROR] $message")
        }

        public fun error(receiver: Player, message: String) {
            receiver.sendMessage("[crimson]$message[]")
            Log.info("[ERROR] $message (send to: ${receiver.name})")
        }
    }
}