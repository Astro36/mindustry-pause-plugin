import arc.Events
import arc.util.CommandHandler
import arc.util.Log
import arc.util.Time
import arc.util.pooling.Pools
import mindustry.Vars
import mindustry.core.GameState
import mindustry.game.EventType
import mindustry.gen.Call
import mindustry.io.TypeIO
import mindustry.net.Net
import mindustry.net.Net.SendMode
import mindustry.net.Packets.InvokePacket
import mindustry.plugin.Plugin
import java.nio.ByteBuffer


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

    private fun checkGameState(state: GameState.State): Boolean {
        return Vars.state.`is`(state)
    }

    private fun forceGameState(state: GameState.State) {
        reflectedNetActiveField.setBoolean(Vars.net, state != GameState.State.paused)
        Vars.state.set(state)
        gamePaused = (state == GameState.State.paused)
    }

    private fun forceSendMessage(message: String) {
        val buf = ByteBuffer.allocate(4096)
        val packet = Pools.obtain(InvokePacket::class.java) { InvokePacket() } as InvokePacket
        packet.writeBuffer = buf
        packet.priority = 0
        packet.type = 53
        buf.position(0)
        TypeIO.writeString(buf, message)
        packet.writeLength = buf.position()
        Vars.net.send(packet, SendMode.tcp)
    }

    private fun forceSync() {
        Vars.playerGroup.updateEvents()
        Vars.playerGroup.forEach { player ->
            player.info.lastSyncTime = Time.millis()
            Call.onWorldDataBegin(player.con)
            Vars.netServer.sendWorldData(player)
        }
    }

    private fun pauseGame() {
        when {
            checkGameState(GameState.State.playing) -> {
                forceGameState(GameState.State.paused)
            }
            checkGameState(GameState.State.paused) -> {
                throw PauseException("The game is already paused.")
            }
            else -> {
                throw PauseException("The server must be open to use this command.")
            }
        }
    }

    private fun resumeGame() {
        when {
            checkGameState(GameState.State.paused) -> {
                forceGameState(GameState.State.playing)
                forceSync()
            }
            checkGameState(GameState.State.playing) -> {
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
            forceSendMessage("[scarlet]Game paused.")
            Log.info("Game paused.")
        } catch (e: PauseException) {
            forceSendMessage("[orange]${e.message}")
            Log.warn(e.message)
        } catch (e: Exception) {
            forceSendMessage("[crimson]Fail to pause the game.")
            Log.err("Fail to pause the game.")
        }
    }

    private fun executeResumeCommand() {
        try {
            resumeGame()
            forceSendMessage("[green]Game resumed.")
            Log.info("Game resumed.")
        } catch (e: PauseException) {
            forceSendMessage("[orange]${e.message}")
            Log.warn(e.message)
        } catch (e: Exception) {
            forceSendMessage("[crimson]Fail to pause the game.")
            Log.err("Fail to resume the game.")
        }
    }
}

