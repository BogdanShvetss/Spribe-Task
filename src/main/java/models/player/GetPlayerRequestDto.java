package models.player;

public class GetPlayerRequestDto {
    private int playerId;

    public GetPlayerRequestDto() {}

    public GetPlayerRequestDto(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}