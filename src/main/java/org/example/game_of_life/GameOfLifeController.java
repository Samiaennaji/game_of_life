package org.example.game_of_life;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameOfLifeController {
    @FXML
    private GridPane gridPane;

    private final int gridSize = GameOfLifeApplication.GRID_SIZE;
    private final int pixelSize = GameOfLifeApplication.PIXEL_SIZE;
    private Rectangle[][] grid;
    private boolean[][] currentGeneration;
    private boolean[][] nextGeneration;
    private boolean isRunning = false;
    private AnimationTimer gameTimer;

    @FXML
    protected void initialize() {
        initializeGrid();
    }

    @FXML
    protected void onStartButtonClick() {
        if (!isRunning) {
            isRunning = true;
            startSimulation();
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        if (isRunning) {
            isRunning = false;
            if (gameTimer != null) gameTimer.stop();
        }
    }

    private void initializeGrid() {
        gridPane.getChildren().clear();
        grid = new Rectangle[gridSize][gridSize];
        currentGeneration = new boolean[gridSize][gridSize];
        nextGeneration = new boolean[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Rectangle cell = new Rectangle(pixelSize, pixelSize, Color.WHITE);
                cell.setStroke(Color.GRAY);
                int finalRow = row;
                int finalCol = col;

                // Toggle cell state on click
                cell.setOnMouseClicked((MouseEvent event) -> {
                    toggleCellState(finalRow, finalCol);
                });

                grid[row][col] = cell;
                gridPane.add(cell, col, row);
            }
        }
    }

    private void toggleCellState(int row, int col) {
        currentGeneration[row][col] = !currentGeneration[row][col];
        grid[row][col].setFill(currentGeneration[row][col] ? Color.BLUE : Color.WHITE);
    }

    private void startSimulation() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGeneration();
            }
        };
        gameTimer.start();
    }

    private void updateGeneration() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);

                // Apply Conway's rules
                if (currentGeneration[row][col]) {
                    nextGeneration[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    nextGeneration[row][col] = liveNeighbors == 3;
                }

                // Update cell color
                grid[row][col].setFill(nextGeneration[row][col] ? Color.BLUE : Color.WHITE);
            }
        }

        // Swap generations
        boolean[][] temp = currentGeneration;
        currentGeneration = nextGeneration;
        nextGeneration = temp;
    }

    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // Skip the current cell
                int neighborRow = row + i;
                int neighborCol = col + j;

                if (neighborRow >= 0 && neighborRow < gridSize && neighborCol >= 0 && neighborCol < gridSize) {
                    if (currentGeneration[neighborRow][neighborCol]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
