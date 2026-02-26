package api.player;

import core.AbstractRestExecutor;
import io.restassured.response.Response;
import models.player.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerClient extends AbstractRestExecutor {

    private static final String CREATE = "/player/create/{editor}";
    private static final String DELETE = "/player/delete/{editor}";
    private static final String GET = "/player/get";
    private static final String UPDATE = "/player/update/{editor}/{id}";
    private static final String GET_ALL = "/player/get/all";

    public Response createPlayer(Editor editor, PlayerDto player) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("editor", editor.getValue());
        return executeGetWithQueryParams(CREATE, player.buildCreateQuery(), pathParams);
    }

    public Response getPlayer(int playerId) {
        return executePost(GET, new GetPlayerRequestDto(playerId));
    }

    public Response deletePlayer(Editor editor, int playerId) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("editor", editor.getValue());

        return executeDelete(DELETE, new DeletePlayerRequestDto(playerId), pathParams);
    }

    public Response updatePlayer(Editor editor, int id, UpdatePlayerRequestDto update) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("editor", editor.getValue());
        pathParams.put("id", id);

        return executePatch(UPDATE, update, pathParams);
    }

    public Response getAllPlayers() {
        return executeGet(GET_ALL);
    }
}