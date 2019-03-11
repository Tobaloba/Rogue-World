package com.dat055.model.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.dat055.controller.MenuController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This Class is responsible for creating the settings menu.
 *
 * @author Pontus Johansson
 * @version 2019-03-07
 */
public class DebugSettingsMenu extends Menu {
    private MenuController controller;
    private TextButton back, apply, resolution, fullscreen;
    private ArrayList<Vector2> resolutions;
    private int resIndex, tmpResIndex;

    /**
     * The constructor for the settingsMenu
     * @param ctrl a menucontroller
     */
    public DebugSettingsMenu(MenuController ctrl) {
        super(ctrl, false, "UI/Delta.jpg");
        resolutions = new ArrayList<Vector2>();
        initResoltions();

        controller = ctrl;
        createTable();
    }

    private void initResoltions() {
        resolutions.add(new Vector2(1024,576));
        resolutions.add(new Vector2(1280,720));
        resolutions.add(new Vector2(1536,864));
        resolutions.add(new Vector2(1664,936));
        resolutions.add(new Vector2(1792,1008));
        resolutions.add(new Vector2(1920,1080));
    }

    /**
     * overrides the method
     * Creates the buttons and text
     */
    @Override
    protected void createTable() {
        //creates the Buttons in the menu
        apply = createButton("Apply");
        back = createButton("Back");
        resolution = createButton(resToString(resolutions.get(resIndex)));
        fullscreen = createButton("Fullscreen", checkedStyle);

        layoutTable(width,  height);

        addListeners();
    }

    private String resToString (Vector2 vec) {
        return (int) vec.x + "x" + (int) vec.y;
    }

    private void layoutTable(int butX, int butY) {
        table.setSize(controller.getWidth(),controller.getHeight());
        table.setPosition(0, 0);
        table.bottom();

        table.add(settingsTable(butX, butY)).expand().colspan(2).row();
        table.add(back).size(butX>>1, butY).pad(butY>>1).expandX().right();
        table.add(apply).size(butX>>1, butY).pad(butY>>1).expandX().left();
    }

    public Table settingsTable(int butX, int butY) {
        Table subTable = new Table();

        subTable.add(resolution).size(butX, butY).padBottom(butY>>1).row();
        subTable.add(fullscreen).size(butX, butY);
        return subTable;
    }

    /**
     * Adds listeners for the buttons
     */
    private void addListeners() {
        back.addListener(new ClickListener() {
            /**
             * Overrides the method so that {@link MenuController} swaps to the .
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(controller.getCtrl().isRunning()) controller.swapMenu("Pause");
                else controller.swapMenu("Main");
                super.touchUp(event, x, y, pointer, button);
            }

            /**
             * Overrides the method so that the button changes to the style when the pointer is above it.
             */
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                back.setStyle(hoverStyle);
                super.enter(event, x, y, pointer, fromActor);
            }

            /**
             * Overrides the method so that the button changes to its original style when the pointer leaves.
             */
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                back.setStyle(txtBtnStyle);
                super.enter(event, x, y, pointer, toActor);
            }
        });

        apply.addListener(new ClickListener() {
            /**
             * Overrides the method so that {@link MenuController} swaps to the .
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resIndex = tmpResIndex;
                Vector2 vec = resolutions.get(resIndex);
                Gdx.graphics.setWindowedMode((int) vec.x, (int) vec.y);
                if(fullscreen.isChecked())
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                controller.resize((int)vec.x,(int)vec.y);
                super.touchUp(event, x, y, pointer, button);
            }

            /**
             * Overrides the method so that the button changes to the style when the pointer is above it.
             */
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                apply.setStyle(hoverStyle);
                super.enter(event, x, y, pointer, fromActor);
            }

            /**
             * Overrides the method so that the button changes to its original style when the pointer leaves.
             */
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                apply.setStyle(txtBtnStyle);
                super.enter(event, x, y, pointer, toActor);
            }
        });

        resolution.addListener(new ClickListener() {
            /**
             * Overrides the method so that {@link MenuController} swaps to the .
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(++tmpResIndex==resolutions.size()) tmpResIndex = 0;

                resolution.setText(resToString(resolutions.get(tmpResIndex)));
                super.touchUp(event, x, y, pointer, button);
            }

            /**
             * Overrides the method so that the button changes to the style when the pointer is above it.
             */
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                resolution.setStyle(hoverStyle);
                super.enter(event, x, y, pointer, fromActor);
            }

            /**
             * Overrides the method so that the button changes to its original style when the pointer leaves.
             */
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                resolution.setStyle(txtBtnStyle);
                super.enter(event, x, y, pointer, toActor);
            }
        });
    }
}