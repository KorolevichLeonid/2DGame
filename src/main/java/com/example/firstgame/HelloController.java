package com.example.firstgame;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView bg1, bg2, player, enemy, flyenemy, flyenemyTwo;

    @FXML
    private Label labelPause, labelLose;

    private TranslateTransition enemyTransition;
    private TranslateTransition flyenemyTransition;
    private TranslateTransition flyenemyTwoTransition;
    private final int BG_WIDTH = 760;
    private ParallelTransition parallelTransition;

    public static boolean right = false;
    public static boolean left = false;
    public static boolean isPause = false;

    private float playerSpeed = 3.5f;
    private int playerSpeedS = 2;
    private double jumpHeight = 0;
    private boolean isJumping = false;
    private boolean isSliding = false;
    private double initialPlayerY;

    private final double MAX_JUMP_HEIGHT = 300;
    private int jumpCount = 0;
    private final int MAX_JUMP_COUNT = 2;

    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            // Движение игрока
            if (right && !isSliding) {
                player.setLayoutX(player.getLayoutX() + playerSpeed);
            }
            if (left && !isSliding) {
                player.setLayoutX(player.getLayoutX() - playerSpeed);
            }

            // Логика прыжка
            if (isJumping) {
                if (jumpHeight < MAX_JUMP_HEIGHT) {
                    player.setLayoutY(player.getLayoutY() - (8 - (jumpHeight / 20)));
                    jumpHeight += 6;
                } else {
                    player.setLayoutY(player.getLayoutY() + (jumpHeight / 30));
                    jumpHeight -= 5;
                }

                if (player.getLayoutY() >= initialPlayerY) {
                    player.setLayoutY(initialPlayerY);
                    isJumping = false;
                    jumpHeight = 0;
                    jumpCount = 0;
                }
            }

            // Логика скольжения
            if (isSliding) {
                player.setLayoutY(initialPlayerY + 30);
                player.setLayoutX(player.getLayoutX() + playerSpeedS);
                player.setRotate(-90);
            } else {
                player.setRotate(0);
            }

            // Проверка паузы
            if (isPause && !labelPause.isVisible()) {
                playerSpeed = 0;
                playerSpeedS= 0;
                parallelTransition.pause();
                enemyTransition.pause();
                flyenemyTransition.pause();
                flyenemyTwoTransition.pause();
                labelPause.setVisible(true);

            } else if (!isPause && labelPause.isVisible()) {
                labelPause.setVisible(false);
                playerSpeed = 3.5f;
                playerSpeedS = 2;
                parallelTransition.play();
                enemyTransition.play();
                flyenemyTransition.play();
                flyenemyTwoTransition.play();
                isJumping = true;

            }

            // Проверка на столкновение с врагами
            List<ImageView> enemies = List.of(enemy, flyenemy, flyenemyTwo);
            for (ImageView enemy : enemies) {
                if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    handlePlayerDeath();
                    break; // Если игрок столкнулся с любым врагом, выходим из цикла
                }
            }
        }
    };

    @FXML
    void initialize() {
        player.setLayoutY(270f);
        initialPlayerY = player.getLayoutY();

        // Фоновая анимация
        TranslateTransition bgOneTransition = new TranslateTransition(Duration.millis(5000), bg1);
        bgOneTransition.setFromX(0);
        bgOneTransition.setToX(BG_WIDTH * -1);
        bgOneTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition bgTwoTransition = new TranslateTransition(Duration.millis(5000), bg2);
        bgTwoTransition.setFromX(0);
        bgTwoTransition.setToX(BG_WIDTH * -1);
        bgTwoTransition.setInterpolator(Interpolator.LINEAR);

        enemyTransition = new TranslateTransition(Duration.millis(3500), enemy);
        enemyTransition.setFromX(0);
        enemyTransition.setToX(BG_WIDTH * -1 - 100);
        enemyTransition.setInterpolator(Interpolator.LINEAR);
        enemyTransition.setCycleCount(Animation.INDEFINITE);
        enemyTransition.play();

        flyenemyTransition = new TranslateTransition(Duration.millis(5500), flyenemy);
        flyenemyTransition.setFromX(0);
        flyenemyTransition.setToX(BG_WIDTH * -1 - 500);
        flyenemyTransition.setInterpolator(Interpolator.LINEAR);
        flyenemyTransition.setCycleCount(Animation.INDEFINITE);
        flyenemyTransition.play();

        flyenemyTwoTransition = new TranslateTransition(Duration.millis(6500), flyenemyTwo);
        flyenemyTwoTransition.setFromX(0);
        flyenemyTwoTransition.setToX(BG_WIDTH * -1 - 700);
        flyenemyTwoTransition.setInterpolator(Interpolator.LINEAR);
        flyenemyTwoTransition.setCycleCount(Animation.INDEFINITE);
        flyenemyTwoTransition.play();

        parallelTransition = new ParallelTransition(bgOneTransition, bgTwoTransition);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();

        timer.start();
    }

    private void handlePlayerDeath() {
        labelLose.setVisible(true);
        playerSpeed = 0;
        parallelTransition.pause();
        enemyTransition.pause();
        flyenemyTransition.pause();
        flyenemyTwoTransition.pause();
        isJumping = false;
    }

    public void slide() {
        isSliding = true;
    }

    public void stopSliding() {
        isSliding = false;
        player.setRotate(0);

        if (!isJumping) {
            player.setLayoutY(initialPlayerY);
        }
    }

    public void jump() {
        if (jumpCount < MAX_JUMP_COUNT) {
            isJumping = true;
            jumpHeight = 0;
            jumpCount++;
        }
    }
}