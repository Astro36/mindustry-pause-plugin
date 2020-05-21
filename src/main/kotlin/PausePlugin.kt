import arc.Events
import arc.util.CommandHandler
import arc.util.Time
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import mindustry.Vars
import mindustry.core.GameState
import mindustry.entities.type.Player
import mindustry.game.EventType
import mindustry.gen.Call
import mindustry.plugin.Plugin
import java.io.File


class PausePlugin : Plugin() {
    private var lastForceSync = 0L
    var config = loadConfig()

    init {
        Events.on(EventType.Trigger.update) {
            if (Vars.state.isPaused) {
                if (Time.timeSinceMillis(lastForceSync) >= 1000L) {
                    lastForceSync = Time.millis()
                    forceSync()
                }
            }
        }

        Events.on(EventType.PlayerJoin::class.java) {
            if (config.pauseAuto && Vars.playerGroup.isEmpty && Vars.state.isPaused) {
                executeResumeCommand(null)
            }
        }

        Events.on(EventType.PlayerLeave::class.java) {
            if (config.pauseAuto && Vars.playerGroup.size() == 1 && !Vars.state.isPaused) {
                executePauseCommand(null)
            }
        }
    }

    override fun registerClientCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game.") { _, player: Player ->
            if (config.pausePermission == Permission.ALL
                    || (config.pausePermission == Permission.ADMIN_ONLY && player.isAdmin)
            ) {
                executePauseCommand(player)
            } else {
                Printer.warn(player, "You are not authorized to use this command.")
            }
        }

        handler.register("resume", "Resume the game.") { _, player: Player ->
            if (config.pausePermission == Permission.ALL
                    || (config.pausePermission == Permission.ADMIN_ONLY && player.isAdmin)
            ) {
                executeResumeCommand(player)
            } else {
                Printer.warn(player, "You are not authorized to use this command.")
            }
        }
    }

    override fun registerServerCommands(handler: CommandHandler) {
        handler.register("pause", "Pause the game. This command can only be used when the server is open.") {
            executePauseCommand(null)
        }

        handler.register("resume", "Resume the game. This command can only be used when the server is open.") {
            executeResumeCommand(null)
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

    private fun injectPausable() {
        if (Vars.state !is PausableGameState) {
            Vars.state = PausableGameState.copyFrom(Vars.state)
        }
    }

    private fun executePauseCommand(actor: Player?) {
        try {
            pauseGame()
            Printer.info("Game paused by ${actor?.name ?: "host"}.")
        } catch (e: PauseException) {
            if (actor != null) {
                Printer.warn(actor, e.message.toString())
            } else {
                Printer.warn(e.message.toString())
            }
        } catch (e: Exception) {
            if (actor != null) {
                Printer.error(actor, "Fail to pause the game.")
            } else {
                Printer.error("Fail to pause the game.")
            }
        }
    }

    private fun executeResumeCommand(actor: Player?) {
        try {
            resumeGame()
            Printer.info("Game resumed by ${actor?.name ?: "host"}.")
        } catch (e: PauseException) {
            if (actor != null) {
                Printer.warn(actor, e.message.toString())
            } else {
                Printer.warn(e.message.toString())
            }
        } catch (e: Exception) {
            if (actor != null) {
                Printer.error(actor, "Fail to resume the game.")
            } else {
                Printer.error("Fail to resume the game.")
            }
        }
    }

    fun loadConfig(): Config {
        val json = Json(JsonConfiguration.Stable)
        val pluginFileURI = javaClass.protectionDomain.codeSource.location.toURI()
        val pluginDirectory = File(pluginFileURI).parentFile
        val configFile = File(pluginDirectory, "pause.json")
        if (configFile.exists()) {
            return json.parse(Config.serializer(), configFile.readText())
        }
        return Config().also {
            val configString = json.stringify(Config.serializer(), it)
            configFile.writeText(configString)
        }
    }

    fun pauseGame() {
        when (Vars.state.state) {
            GameState.State.playing -> {
                injectPausable()
                forceSync(true)
                forceGameState(GameState.State.paused)
            }
            GameState.State.paused -> {
                throw PauseException("The game is already paused.")
            }
            else -> {
                throw PauseException("This command can only be used when the server is open.")
            }
        }
    }

    fun resumeGame() {
        when (Vars.state.state) {
            GameState.State.paused -> {
                forceGameState(GameState.State.playing)
                forceSync(true)
            }
            GameState.State.playing -> {
                throw PauseException("The game is already resumed.")
            }
            else -> {
                throw PauseException("This command can only be used when the server is open.")
            }
        }
    }
}

