package tictactoe_server.util;

import tictactoe_server.model.InputPosition;
import tictactoe_server.model.InvalidMove;

public final class InputPositionValidation {

    private InputPositionValidation() {
        // prevent instantiation
    }

    public static void assertValid(InputPosition inputPosition, int max) {
        if (inputPosition == null) {
            throw new IllegalArgumentException("inputPosition cannot be null");
        }
        if (inputPosition.pos() < 0) {
            throw new InvalidMove("Position: " + inputPosition.pos() + " is too small");
        }
        if (inputPosition.pos() >= max) {
            throw new InvalidMove("Position: " + inputPosition.pos() + " is too big");
        }
    }
}