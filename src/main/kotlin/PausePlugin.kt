import arc.Events
import arc.util.CommandHandler
import arc.util.Log
import arc.util.Time
import mindustry.Vars
import mindustry.core.GameState
import mindustry.game.EventType
import mindustry.gen.Call
import mindustry.plugin.Plugin


public class PausePlugin : Plugin() {
    private var lastForceSync = 0L

    init {
        Events.on(EventType.Trigger.update) {
            if (Vars.state.isPaused) {
                if (Time.timeSinceMillis(lastForceSync) >= 1000L) {
                    lastForceSync = Time.millis()
                    forceSync()
                }
            }
        }
    }

    override fun registerClientCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game.") {
            executePauseCommand()
        }

        handler.register("resume", "Resume the game.") {
            executeResumeCommand()
        }
    }

    override fun registerServerCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game. The server must be open to use this command.") {
            executePauseCommand()
        }

        handler.register("resume", "Resume the game. The server must be open to use this command.") {
            executeResumeCommand()
        }
    }

    private fun forceGameState(state: GameState.State) {
        Vars.state.set(state)
    }

    private fun forceSync(loadingFragment: Boolean = false) {
        Vars.playerGroup.updateEvents()
        Vars.playerGroup.forEach { player ->
            player.info.lastSyncTime = Time.millis()
            if (loadingFragment) {
                Call.onWorldDataBegin(player.con)
            }
            Vars.netServer.sendWorldData(player)
        }
    }

    private fun injectPausableGameState() {
        if (Vars.state !is PausableGameState) {
            Log.info("copy and change")
            Vars.state = PausableGameState.copyFrom(Vars.state)
        }
    }

    private fun pauseGame() {
        when (Vars.state.state) {
            GameState.State.playing -> {
                injectPausableGameState()
                forceGameState(GameState.State.paused)
            }
            GameState.State.paused -> {
                throw PauseException("The game is already paused.")
            }
            else -> {
                throw PauseException("The server must be open to use this command.")
            }
        }
    }

    private fun resumeGame() {
        when (Vars.state.state) {
            GameState.State.paused -> {
                forceGameState(GameState.State.playing)
                forceSync(true)
            }
            GameState.State.playing -> {
                throw PauseException("The game is already resumed.")
            }
            else -> {
                throw PauseException("The server must be open to use this command.")
            }
        }
    }

    private fun executePauseCommand() {
        try {
            pauseGame()
            Call.sendMessage("[scarlet]Game paused.")
            Log.info("Game paused.")
        } catch (e: PauseException) {
            Call.sendMessage("[orange]${e.message}")
            Log.warn(e.message)
        } catch (e: Exception) {
            Call.sendMessage("[crimson]Fail to pause the game.")
            Log.err("Fail to pause the game.")
        }
    }

    private fun executeResumeCommand() {
        try {
            resumeGame()
            Call.sendMessage("[green]Game resumed.")
            Log.info("Game resumed.")
        } catch (e: PauseException) {
            Call.sendMessage("[orange]${e.message}")
            Log.warn(e.message)
        } catch (e: Exception) {
            Call.sendMessage("[crimson]Fail to pause the game.")
            Log.err("Fail to resume the game.")
        }
    }
}

