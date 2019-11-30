package com.example.phase2.stage1.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.phase2.appcore.user.User;
import com.example.phase2.appcore.user.UserManager;
import com.example.phase2.stage1.view.MazeActivity;
import com.example.phase2.stage1.model.Background;
import com.example.phase2.stage1.model.Hero;
import com.example.phase2.stage1.model.IMazeModel;
import com.example.phase2.stage1.model.MazeObjects;
import com.example.phase2.stage2.TreasureHuntActivity;

import java.util.List;

public class MazeViewPresenter extends SurfaceView implements Runnable{
    /**
     * The maze model for collecting data.
     */
    IMazeModel mazeModel;

    /**
     * The main Thread
     */
    private Thread thread;


    /**
     * @param context
     */
    public MazeViewPresenter(Context context, IMazeModel mazeModel){
        super(context);

        this.mazeModel = mazeModel;

        setCurStage(mazeModel.getCurUser(), 1);
        saveUser();
    }

    /**
     * in where we keep running the methods
     */
    @Override
    public void run() {
        while (mazeModel.getIsPlaying()){
            update();
            draw();
            sleep();
            for (MazeObjects monster : mazeModel.getMyMonsters()) { action(monster);}
        }
    }

    public void setCurStage(User curUser, int stage){
        curUser.getCurPlayer().setCurStage(stage);
    }

    /**
     * method to save the User data
     */
    public void saveUser() {
        mazeModel.getFileSystem().save(UserManager.getInstance().getUsers(), "Users.ser");
    }

    /**
     * Action to move monsters
     */
    public void action(MazeObjects monster){
        double d = Math.random();
        if (d < 0.25){
            monster.setX(monster.getX()+monster.getWidth());
        } else if(0.25 <= d && d < 0.5){
            monster.setX(monster.getX()-monster.getWidth());
        } else if(0.5 <= d && d < 0.75){
            monster.setY(monster.getY()+monster.getHeight());
        } else{
            monster.setY(monster.getY()-monster.getHeight());
        }
        if (monster.getY() < 360)
            monster.setY(360);

        if (monster.getY() >= 1440)
            monster.setY(1440);

        if (monster.getX() < 0)
            monster.setX(0);

        if (monster.getX() >= mazeModel.getScreenX() - monster.getWidth())
            monster.setX(mazeModel.getScreenX() - monster.getWidth());
    }

    /**
     * update the x,y of monster, hte situation where player hit monsters and when to
     * jump to the next activity
     */
    private void update(){
        updateHero();
        updateMonster();
        updateTreasure();
        updateDoor();
    }

    public void updateHero(){
        Hero hero = mazeModel.getHero();
        if (hero.getIsGoingUp()){
            hero.setY(hero.getY()-hero.getHeight());
            hero.setIsGoingUp(false);
        }

        if (hero.getIsGoingDown()){
            hero.setY(hero.getY()+hero.getHeight());
            hero.setIsGoingDown(false);
        }

        if (hero.getIsGoingLeft()){
            hero.setX(hero.getX()-hero.getWidth());
            hero.setIsGoingLeft(false);
        }

        if (hero.getIsGoingRight()){
            hero.setX(hero.getX()+hero.getWidth());
            hero.setIsGoingRight(false);
        }

        if (hero.getY() < 360)
            hero.setY(360);

        if (hero.getY() >= 1440)
            hero.setY(1440);

        if (hero.getX() < 0)
            hero.setX(0);

        if (hero.getX() >= mazeModel.getScreenX() - hero.getWidth())
            hero.setX(mazeModel.getScreenX() - hero.getWidth());
    }

    public void updateMonster(){
        List<MazeObjects> myMonsters = mazeModel.getMyMonsters();
        Hero hero = mazeModel.getHero();
        for (MazeObjects monster : myMonsters) {
            if (monster.getType().equals("Strong")){
                if (hero.getX() == monster.getX() && hero.getY() == monster.getY()) {
                    mazeModel.setLife(mazeModel.getLife() - 5);
                    if (mazeModel.getLife() <= 0) {
                        restartStage1();
                    }
                }
            }
            else if (monster.getType().equals("Weak")){
                if (hero.getX() == monster.getX() && hero.getY() == monster.getY()) {
                    mazeModel.setLife(mazeModel.getLife() - 1);
                    if (mazeModel.getLife() <= 0) {
                        restartStage1();
                    }
                }
            }
        }
    }

    public void updateTreasure(){
        List<MazeObjects> myTreasures = mazeModel.getMyTreasures();
        Hero hero = mazeModel.getHero();
        for (MazeObjects treasure : myTreasures){
            if (hero.getX() == treasure.getX() && hero.getY() == treasure.getY()){
                if (treasure.getType().equals("Life")){
                    mazeModel.setLife(mazeModel.getLife() + 5);
                    mazeModel.setGiftLife(mazeModel.getGiftLife() + 5);
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                } else if(treasure.getType().equals("Attack")){
                    mazeModel.setAttack(mazeModel.getAttack() + 5);
                    mazeModel.setGiftAttack(mazeModel.getGiftAttack() + 5);
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                } else if(treasure.getType().equals("Defence")){
                    mazeModel.setDefence(mazeModel.getDefence() + 5);
                    mazeModel.setGiftDefence(mazeModel.getGiftDefence() + 5);
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                } else if(treasure.getType().equals("Flexibility")){
                    mazeModel.setFlexibility(mazeModel.getFlexibility() + 5);
                    mazeModel.setGiftFlexibility(mazeModel.getGiftFlexibility() + 5);
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                } else if(treasure.getType().equals("Luckiness")){
                    mazeModel.setLuckiness(mazeModel.getLuckiness() + 5);
                    mazeModel.setGiftLuckiness(mazeModel.getGiftLuckiness() + 5);
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                }
                else if (treasure.getType().equals("Key")){
                    hero.setKey();
                    mazeModel.setHasKey("Yes");
                    treasure.setType("Empty");
                    myTreasures.remove(treasure);
                    break;
                }
            }
        }
    }

    public void updateDoor(){
        List<MazeObjects> myDoors = mazeModel.getMyDoors();
        Hero hero = mazeModel.getHero();
        for (MazeObjects door : myDoors){
            if (door.getType().equals("True")){
                if (hero.getX() == door.getX() && hero.getY() == door.getY() && hero.getKey()){
                    processToStage2();
                }
            }
            else if (door.getType().equals("False")){
                if (hero.getX() == door.getX() && hero.getY() == door.getY() && hero.getKey()){
                    mazeModel.setLife(mazeModel.getLife() - 5);
                    door.setType("Used");
                    myDoors.remove(door);
                    break;
                }
            }
        }
    }

    public void restartStage1(){
        Intent restartg1Intent = new Intent(getContext(), MazeActivity.class);
        getContext().startActivity(restartg1Intent);
    }

    public void processToStage2(){
        mazeModel.getCurUser().getCurPlayer().getProperty().setAttack(mazeModel.getAttack());
        mazeModel.getCurUser().getCurPlayer().getProperty().setDefence(mazeModel.getDefence());
        mazeModel.getCurUser().getCurPlayer().getProperty().setFlexibility(mazeModel.getFlexibility());
        mazeModel.getCurUser().getCurPlayer().getProperty().setLuckiness(mazeModel.getLuckiness());
        mazeModel.getCurUser().getCurPlayer().setLivesRemain(mazeModel.getLife());
        mazeModel.getCurUser().getCurPlayer().setCurStage(2);
        saveUser();

        Intent tog2Intent = new Intent(getContext(), TreasureHuntActivity.class);
        getContext().startActivity(tog2Intent);
    }


    /**
     * Where to draw the bitmap background, player, treasure and monsters
     */
    private void draw(){
        if (getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();

            drawBackground(canvas, mazeModel.getCurBackground());
            drawHero(canvas, mazeModel.getHero());
            drawMonsters(canvas, mazeModel.getMyMonsters());
            drawTreasures(canvas, mazeModel.getMyTreasures());
            drawDoors(canvas, mazeModel.getMyDoors());
            drawText(canvas, mazeModel.getTextPaint());

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void drawBackground(Canvas canvas, Background background){
        canvas.drawBitmap(background.getBackgroundView(), background.x, background.y, mazeModel.getPaint());
    }

    private void drawHero(Canvas canvas, Hero hero){
        canvas.drawBitmap(hero.getHeroView(), hero.getX(), hero.getY(), mazeModel.getPaint());
    }

    private void drawMonsters(Canvas canvas, List<MazeObjects> myMonsters){
        for (MazeObjects monster : myMonsters) {
            canvas.drawBitmap(monster.getView(), monster.getX(), monster.getY(), mazeModel.getPaint());
        }
    }

    private void drawTreasures(Canvas canvas, List<MazeObjects> myTreasures){
        for (MazeObjects treasure : myTreasures) {
            canvas.drawBitmap(treasure.getView(), treasure.getX(), treasure.getY(), mazeModel.getPaint());
        }
    }

    private void drawDoors(Canvas canvas, List<MazeObjects> myDoors){
        for (MazeObjects door : myDoors) {
            canvas.drawBitmap(door.getView(), door.getX(), door.getY(), mazeModel.getPaint());
        }
    }

    private void drawText(Canvas canvas, Paint textPaint){
        canvas.drawText("Life: " + mazeModel.getLife(), 20, 60, textPaint);
        canvas.drawText("Key: " + mazeModel.getHasKey(), 500, 60, textPaint);
        canvas.drawText("Attack: " + mazeModel.getAttack(), 20, 180, textPaint);
        canvas.drawText("Defence: " + mazeModel.getDefence(), 500, 180, textPaint);
        canvas.drawText("Flexibility: " + mazeModel.getFlexibility(), 20, 320, textPaint);
        canvas.drawText("Luckiness: " + mazeModel.getLuckiness(), 500, 320, textPaint);

        canvas.drawText("Life from the treasure: " + mazeModel.getGiftLife(), 100, 1600, textPaint);
        canvas.drawText("Attack from the treasure: " + mazeModel.getGiftAttack(), 100, 1680, textPaint);
        canvas.drawText("Defence from the treasure: " + mazeModel.getGiftDefence(), 100, 1760, textPaint);
        canvas.drawText("Flexibility from the treasure: " + mazeModel.getGiftFlexibility(), 100, 1840, textPaint);
        canvas.drawText("Luckiness from the treasure: " + mazeModel.getGiftLuckiness(), 100, 1920, textPaint);
    }

    /**
     * we suspend the program for 200 millis
     */
    private void sleep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * resume the thread
     */
    public void resume(){
        mazeModel.setIsPlaying(true);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * pause the thread
     */
    public void pause(){
        try {
            mazeModel.setIsPlaying(false);
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Where we move the player
     * click the upper screen to move player upwards
     * click the lower screen to move player downwards
     * click the left screen to move player leftwards
     * click the right screen to move player rightwards
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (event.getX() > 0 && event.getX() < mazeModel.getScreenX() &&
                    event.getY() > 0 && event.getY() < mazeModel.getScreenY() / 3){
                mazeModel.getHero().setIsGoingUp(true);
            }
            if (event.getX() > 0 && event.getX() < mazeModel.getScreenX() / 2 &&
                    event.getY() > mazeModel.getScreenY() /3 && event.getY() < mazeModel.getScreenY() * 2 /3){
                mazeModel.getHero().setIsGoingLeft(true);
            }
            if (event.getX() > mazeModel.getScreenX() / 2 && event.getX() < mazeModel.getScreenX() &&
                    event.getY() > mazeModel.getScreenY() /3 && event.getY() < mazeModel.getScreenY() * 2 /3){
                mazeModel.getHero().setIsGoingRight(true);
            }
            if (event.getX() > 0 && event.getX() < mazeModel.getScreenX() &&
                    event.getY() > mazeModel.getScreenY() * 2 / 3 && event.getY() < mazeModel.getScreenY()){
                mazeModel.getHero().setIsGoingDown(true);
            }
        }

        return true;

    }
}