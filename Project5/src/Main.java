import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.List;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Main extends Application{

    Image gameoverImage = new Image(getClass().getResourceAsStream("Sprite/gameover.png"));
    Image explosionImage = new Image(getClass().getResourceAsStream("Sprite/explosion.png"));
    Image healthImage = new Image(getClass().getResourceAsStream("Sprite/heart2.png"));
    Image playerImage = new Image(getClass().getResourceAsStream("Sprite/player3.png"));
    Image playerImage2 = new Image(getClass().getResourceAsStream("Sprite/player4.png"));
    Image playerImage3 = new Image(getClass().getResourceAsStream("Sprite/player5.png"));
    Image bulletImage = new Image(getClass().getResourceAsStream("Sprite/bullet4.png"));
    Image bulletImage2 = new Image(getClass().getResourceAsStream("Sprite/bullet5.png"));
    Image bulletImage3 = new Image(getClass().getResourceAsStream("Sprite/bullet6.png"));
    Image alienImage = new Image(getClass().getResourceAsStream("Sprite/alien1.png"));
    Image alienImage2 = new Image(getClass().getResourceAsStream("Sprite/alien4.png"));
    Image alienImage3 = new Image(getClass().getResourceAsStream("Sprite/alien5.png"));
    Image alienImage4 = new Image(getClass().getResourceAsStream("Sprite/alien6.png"));
    Image alienImage5 = new Image(getClass().getResourceAsStream("Sprite/alien7.png"));
    Image background = new Image(getClass().getResourceAsStream("Sprite/background.png"));

    int healthShip = 3;
    int princecessShip = 4;
    double time = 0;

    boolean moveLeft;
    boolean moveRight;
    boolean moveUp;
    boolean moveDown;
    boolean readyShoot;
    boolean rightShot = false;
    boolean doneOnce = true;

    boolean pinkRightMove = true;
    boolean pinkLeftMove = false;

    Pane root = new Pane();
    private Sprite health = new Sprite(0, 700, 0, 0, "health", healthImage);
    private Sprite health2 = new Sprite(35, 700, 0, 0, "health", healthImage);
    private Sprite health3 = new Sprite(70, 700, 0, 0, "health", healthImage);
    private Sprite enemy;
    private Sprite player = new Sprite(300, 730, 60, 60, "player", playerImage);
    private Sprite[] enemyList = new Sprite[7];
    private Sprite[] enemyList2 = new Sprite[1];

    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    Background background2 = new Background(backgroundImage);

    private Parent createContent(){
        root.setPrefSize(600, 800);
        root.setBackground(background2);
        root.getChildren().addAll(player, health, health2, health3);

        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                update();
            }
        };
        timer.start();
        nextLevel();
        return root;
    }

    private List<Sprite> sprites()
    {
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }

    private void update(){
        time += 0.016;

        if(player.getTranslateX() >= 540){
            player.moveLeft(2);
        }
        if(player.getTranslateX() <= 0){
            player.moveRight(2);
        }
        if(player.getTranslateY() <= 650){
            player.moveDown(2);
        }
        if(player.getTranslateY() >= 740){
            player.moveUp(2);
        }
        if(pinkRightMove){
            enemyList2[0].moveRight(2);
        }
        if(enemyList2[0].getTranslateX() >= 540){
            pinkRightMove = false;
            pinkLeftMove = true;
        }
        if(pinkLeftMove){
            enemyList2[0].moveLeft(2);
        }
        if(enemyList2[0].getTranslateX() <= 0){
            pinkRightMove = true;
            pinkLeftMove = false;
        }
        if(healthShip == 2){
            player.setImage(playerImage2);
            root.getChildren().remove(health3);
        }
        if(healthShip == 1){
            player.setImage(playerImage3);
            root.getChildren().remove(health2);
        }
        if(healthShip <= 0){
            root.getChildren().remove(health);
            Sprite explosion = new Sprite(player.getTranslateX(), player.getTranslateY(), 0, 0, "explosion", explosionImage);
            Sprite gameover = new Sprite(100, 300, 0, 0, "explosion", gameoverImage);
            root.getChildren().addAll(explosion, gameover);
            player.dead = true;
        }
        if(princecessShip == 3){
            enemyList2[0].setImage(alienImage5);
        }
        if(princecessShip == 0){
            enemy.dead = true;
        }
        if(player.dead){
            moveLeft = false;
            moveRight = false;
            moveDown = false;
            moveUp = false;
            readyShoot = false;
        }
        if(moveLeft){
            player.moveLeft(2);
        }
        if(moveRight){
            player.moveRight(2);
        }
        if(moveUp){
            player.moveUp(2);
        }
        if(moveDown){
            player.moveDown(2);
        }
        if(readyShoot) {
            shoot(player);
            readyShoot = false;
        }

        for(Sprite s : sprites()){
            switch(s.type){
                case "playerbullet":
                    s.moveUp(2);
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy ->
                    {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent()))
                        {
                            enemy.dead = true;
                            s.dead = true;
                        }
                        if(s.getTranslateY() < 0){
                            root.getChildren().remove(s);
                        }
                    });
                    sprites().stream().filter(e -> e.type.equals("enemyPink")).forEach(enemy ->
                    {
                        if (s.getBoundsInParent().intersects(enemy.getBoundsInParent()))
                        {
                            princecessShip = princecessShip - 1;
                            s.dead = true;
                        }
                    });
                    break;
                case "enemybullet":
                    s.moveDown(2);
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent()))
                    {
                        healthShip = healthShip - 1;
                        s.dead = true;
                    }
                    if(s.getTranslateY() > 800){
                        root.getChildren().remove(s);
                    }
                    break;
                case "enemyPinkbullet":
                    s.moveDown(3);
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent()))
                    {
                        healthShip = healthShip - 1;
                        s.dead = true;
                    }
                    if(s.getTranslateY() > 800){
                        root.getChildren().remove(s);
                    }
                    break;
                case "enemy":
                    if(time > 1) {
                        if(Math.random() < 0.025)
                        {
                            shoot(s);
                        }
                    }
                    break;
                case "enemyPink":
                    if(time > 1) {
                        if (Math.random() < 0.2) {
                            shoot(s);
                        }
                    }
                    break;
            }
        }
        if(time >= 1) {
            time = 0;
        }
        root.getChildren().removeIf(n -> { Sprite s = (Sprite) n;return s.dead; });
    }

    private void nextLevel(){
        for(int i = 0; i < 7; i++)
        {
            enemy = new Sprite(30 + i*80, 220, 60, 60, "enemy", alienImage);
            enemyList[i] = enemy;
            root.getChildren().add(enemyList[i]);
        }

        for(int i = 0; i < 7; i++)
        {
            enemy = new Sprite(30 + i*80, 145, 60, 60, "enemy", alienImage3);
            enemyList[i] = enemy;
            root.getChildren().add(enemyList[i]);
        }

        for(int i = 0; i < 7; i++)
        {
            enemy = new Sprite(30 + i*80, 70, 60, 60, "enemy", alienImage2);
            enemyList[i] = enemy;
            root.getChildren().add(enemyList[i]);
        }

        for(int i = 0; i < 1; i++)
        {
            enemy = new Sprite(0 + i*80, 0, 60, 60, "enemyPink", alienImage4);
            enemyList2[i] = enemy;
            root.getChildren().add(enemyList2[i]);
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case A:
                        moveLeft = true;
                        break;
                    case D:
                        moveRight = true;
                        break;
                    case W:
                        moveUp = true;
                        break;
                    case S:
                        moveDown = true;
                        break;
                    case SPACE:
                        readyShoot = true;
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case A:
                        moveLeft = false;
                        break;
                    case D:
                        moveRight = false;
                        break;
                    case W:
                        moveUp = false;
                        break;
                    case S:
                        moveDown = false;
                        break;
                    case SPACE:
                        readyShoot = false;
                        break;
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private void shoot(Sprite who)
    {
        if (rightShot) {
            if(who.type =="player"){
                Sprite autoAttack = new Sprite((int)who.getTranslateX() + 30, (int) who.getTranslateY(), 4, 29, who.type + "bullet", bulletImage);
                root.getChildren().add(autoAttack);
                rightShot=false;
            }
        }
        else {
            if (who.type == "player") {
                Sprite autoAttack = new Sprite((int) who.getTranslateX(), (int) who.getTranslateY(), 4, 29, who.type + "bullet", bulletImage);
                root.getChildren().add(autoAttack);
                rightShot = true;
            }
        }
        if(who.type=="enemy"){
            Sprite autoAttack = new Sprite((int)who.getTranslateX(), (int) who.getTranslateY(), 4, 29, who.type + "bullet", bulletImage2);
            root.getChildren().add(autoAttack);
        }
        if(who.type=="enemyPink"){
            Sprite autoAttack = new Sprite((int)who.getTranslateX(), (int) who.getTranslateY(), 4, 29, who.type + "bullet", bulletImage3);
            root.getChildren().add(autoAttack);
        }
    }

    public static void main(String[] args){

        launch(args);
    }
}
