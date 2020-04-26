import mindustry.Vars
import mindustry.core.GameState

public class PausableGameState : GameState() {
    companion object {
        public fun copyFrom(state: GameState): PausableGameState {
            return PausableGameState().apply {
                wave = state.wave
                wavetime = state.wavetime
                gameOver = state.gameOver
                launched = state.launched
                rules = state.rules
                stats = state.stats
                teams = state.teams
                enemies = state.enemies
                set(state.state)
            }
        }
    }

    override fun isPaused(): Boolean {
        return `is`(State.paused) || (gameOver && !Vars.net.active())
    }
}