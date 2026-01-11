package tictactoe_server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tictactoe_server.ClientRequests.ClientRequest;
import tictactoe_server.ClientRequests.Response;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractClientRequest<T> implements ClientRequest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    protected final T params;

    public AbstractClientRequest(JsonNode jsonParams) {
        this.params = deserialize(jsonParams);
    }

    @SuppressWarnings("unchecked")
    private T deserialize(JsonNode node) {
        try {
            ParameterizedType pType = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> paramClass = (Class<T>) pType.getActualTypeArguments()[0];

            // Deserialize JsonNode → T
            return MAPPER.treeToValue(node, paramClass);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to deserialize params for " + getClass().getSimpleName() + ": " + e.getMessage() +
                "\nReceived: " + (node != null ? node.toPrettyString() : "null"),
                e
            );
        }
    }

    @Override
    public Response run(ClientContext clientContext) {
        return execute(clientContext, params);
    }

    protected abstract Response execute(ClientContext ctx, T params);
}