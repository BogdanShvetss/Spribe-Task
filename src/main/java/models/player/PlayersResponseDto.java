package models.player;

import java.util.List;
import java.util.Objects;

public class PlayersResponseDto {

    private List<PlayerShortDto> players;

    public PlayersResponseDto() {
    }

    public PlayersResponseDto(List<PlayerShortDto> players) {
        this.players = players;
    }

    public List<PlayerShortDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerShortDto> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayersResponseDto that)) return false;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }

    @Override
    public String toString() {
        return "PlayersResponseDto{" +
                "players=" + players +
                '}';
    }
}