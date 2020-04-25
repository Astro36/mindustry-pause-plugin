import arc.Events
import arc.util.CommandHandler
import arc.util.Log
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.entities.type.Player
import mindustry.game.EventType.BuildSelectEvent
import mindustry.gen.Call
import mindustry.plugin.Plugin


class ExamplePlugin() : Plugin() {
    init {
        Events.on(BuildSelectEvent::class.java) { event: BuildSelectEvent ->
            if (!event.breaking
                && event.builder != null
                && event.builder.buildRequest() != null
                && event.builder.buildRequest().block === Blocks.thoriumReactor
                && event.builder is Player
            ) {
                Call.sendMessage("[scarlet]ALERT![] " + (event.builder as Player).name + " has begun building a thorium reactor at " + event.tile.x + ", " + event.tile.y)
            }
        }
    }

    override fun registerClientCommands(handler: CommandHandler) {
        handler.register(
            "reply", "<text...>", "A simple ping command that echoes a player's text."
        ) { args: Array<String>, player: Player ->
            player.sendMessage("You said: [accent] " + args[0]);
        }

        handler.register(
            "whisper", "<player> <text...>", "Whisper text to another player."
        ) { args: Array<String>, player: Player ->
            val other = Vars.playerGroup.find {
                it.name.equals(args[0], true)
            }
            other?.sendMessage("[lightgray](whisper) " + player.name.toString() + ":[] " + args[1])
                ?: player.sendMessage("[scarlet]No player by that name found!")
        }
    }

    override fun registerServerCommands(handler: CommandHandler) {
        handler.register("reactors", "List all thorium reactors in the map.") {
            for (x in 0..Vars.world.width()) {
                for (y in 0..Vars.world.height()) {
                    if (Vars.world.tile(x, y).block() === Blocks.thoriumReactor) {
                        Log.info("Thorium Reactor at {0}, {1}", x, y)
                    }
                }
            }
        }
    }
}

