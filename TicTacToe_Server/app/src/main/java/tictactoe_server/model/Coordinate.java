package tictactoe_server.model;

import tictactoe_server.util.InputPositionValidation;

public record Coordinate(InputPosition x, InputPosition y) {
    public static Coordinate makeCoordinate(int xNumber, int yNumber, int max) {
        InputPosition xIp = new InputPosition(xNumber);
        InputPositionValidation.assertValid(xIp, max);
        InputPosition yIp = new InputPosition(yNumber);
        InputPositionValidation.assertValid(yIp, max);
        return new Coordinate(xIp, yIp);
    }
}