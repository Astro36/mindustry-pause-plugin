import arc.Events
import arc.util.CommandHandler
import arc.util.Log
import arc.util.Time
import mindustry.Vars
import mindustry.core.GameState
import mindustry.game.EventType
import mindustry.net.Net
import mindustry.plugin.Plugin
import java.io.IOException


class PausePlugin : Plugin() {
    private val reflectedNetActiveField = Net::class.java.getDeclaredField("active").apply { isAccessible = true }
    private var gamePaused = false
    private var lastForceSync = 0L

    init {
        Events.on(EventType.Trigger.update) {
            if (gamePaused) {
                if (Time.timeSinceMillis(lastForceSync) >= 1000L) {
                    lastForceSync = Time.millis()
                    forceSync()
                }
            }
        }
    }

    override fun registerClientCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game.") {
            try {
                pauseGame()
                Log.info("Game pause")
            } catch (e: PauseException) {
                Log.warn(e.message)
            } catch (e: Exception) {
                Log.err("Fail to pause the game")
            }
        }

        handler.register("resume", "Resume the game.") {
            try {
                resumeGame()
                Log.info("Game resume")
            } catch (e: PauseException) {
                Log.warn(e.message)
            } catch (e: Exception) {
                Log.err("Fail to resume the game")
            }
        }
    }

    override fun registerServerCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game. The server must be open to use this command.") {
            try {
                pauseGame()
                Log.info("Game pause")
            } catch (e: PauseException) {
                Log.warn(e.message)
            } catch (e: Exception) {
                Log.err("Fail to pause the game")
            }
        }

        handler.register("resume", "Resume the game. The server must be open to u se this command.") {
            try {
                resumeGame()
                Log.info("Game resume")
            } catch (e: PauseException) {
                Log.warn(e.message)
            } catch (e: Exception) {
                Log.err("Fail to resume the game")
            }
        }
    }

    private fun checkGameState(state: GameState.State): Boolean {
        return Vars.state.`is`(state)
    }

    private fun forceGameState(state: GameState.State) {
        reflectedNetActiveField.setBoolean(Vars.net, state != GameState.State.paused)
        Vars.state.set(state)
        gamePaused = (state == GameState.State.paused)
    }

    private fun pauseGame() {
        if (checkGameState(GameState.State.playing)) {
            try {
                forceGameState(GameState.State.paused)
            } catch (e: IOException) {
                Log.info(e.message)
            }
        } else {
            throw PauseException("The game is already paused.")
        }
    }

    private fun resumeGame() {
        if (checkGameState(GameState.State.paused)) {
            forceGameState(GameState.State.playing)
        } else {
            throw PauseException("The game is already resumed.")
        }
    }

    private fun forceSync() {
        Vars.playerGroup.updateEvents()
        Vars.playerGroup.forEach { player ->
            player.info.lastSyncTime = Time.millis()
            Vars.netServer.sendWorldData(player)
        }
    }
}

