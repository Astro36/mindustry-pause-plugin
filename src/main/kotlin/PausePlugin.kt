import arc.util.CommandHandler
import arc.util.Log
import mindustry.Vars
import mindustry.core.GameState
import mindustry.net.Net
import mindustry.plugin.Plugin


class PausePlugin : Plugin() {
    private val serverActiveField = Net::class.java.getDeclaredField("active").apply { isAccessible = true }

    override fun registerClientCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game.") {
            pauseGame()
        }

        handler.register("resume", "Resume the game.") {
            resumeGame()
        }
    }

    override fun registerServerCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game. The server must be open to use this command.") {
            pauseGame()
        }

        handler.register("resume", "Resume the game. The server must be open to use this command.") {
            resumeGame()
        }
    }

    private fun pauseGame() {
        if (Vars.state != null && Vars.net != null) {
            if (!Vars.state.`is`(GameState.State.paused)) {
                serverActiveField.setBoolean(Vars.net, false)
                Vars.state.set(GameState.State.paused)
                Log.info("Game pause")
            } else {
                Log.info("The game is already paused.")
            }
        } else {
            Log.info("Fail to pause the game")
        }
    }

    private fun resumeGame() {
        if (Vars.state != null && Vars.net != null) {
            if (!Vars.state.`is`(GameState.State.playing)) {
                serverActiveField.setBoolean(Vars.net, true)
                Vars.state.set(GameState.State.playing)
                Log.info("Game resume")
            } else {
                Log.info("The game is already resumed.")
            }
        } else {
            Log.info("Fail to resume the game")
        }
    }
}

