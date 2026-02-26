package models.player;

public class DeletePlayerRequestDto {

    private int playerId;

    public DeletePlayerRequestDto(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}