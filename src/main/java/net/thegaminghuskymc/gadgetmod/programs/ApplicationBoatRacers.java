package net.thegaminghuskymc.gadgetmod.programs;

import net.minecraft.nbt.NBTTagCompound;
import net.thegaminghuskymc.gadgetmod.api.app.Application;
import net.thegaminghuskymc.gadgetmod.api.app.Icons;
import net.thegaminghuskymc.gadgetmod.api.app.Layout;
import net.thegaminghuskymc.gadgetmod.api.app.component.Button;
import net.thegaminghuskymc.gadgetmod.api.app.component.CheckBox;
import net.thegaminghuskymc.gadgetmod.api.app.component.Label;
import net.thegaminghuskymc.gadgetmod.object.Game;
import net.thegaminghuskymc.gadgetmod.object.TileGrid;
import net.thegaminghuskymc.gadgetmod.object.tiles.Tile;

public class ApplicationBoatRacers extends Application {

    private Layout layoutLevelEditor;
    private Game game;
    private TileGrid tileGrid;
    private Label labelLayer;
    private Button btnNextLayer;
    private Button btnPrevLayer;
    private CheckBox checkBoxForeground;
    private CheckBox checkBoxBackground;
    private CheckBox checkBoxPlayer;

    public ApplicationBoatRacers() {
        this.setDefaultWidth(320);
        this.setDefaultHeight(160);
    }

    @Override
    public void init() {
        layoutLevelEditor = new Layout(364, 178);

        try {
            game = new Game(4, 4, 256, 144);
            game.setEditorMode(true);
            game.setRenderPlayer(false);
            game.fill(Tile.grass);
            layoutLevelEditor.addComponent(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tileGrid = new TileGrid(266, 3, game);
        layoutLevelEditor.addComponent(tileGrid);

        labelLayer = new Label("1", 280, 108);
        layoutLevelEditor.addComponent(labelLayer);

        btnNextLayer = new Button(266, 106, Icons.CHEVRON_RIGHT);
        btnNextLayer.setClickListener((mouseX, mouseY, mouseButton) -> {
            game.nextLayer();
            labelLayer.setText(Integer.toString(game.getCurrentLayer().layer + 1));
        });
        layoutLevelEditor.addComponent(btnNextLayer);

        btnPrevLayer = new Button(314, 106, Icons.CHEVRON_LEFT);
        btnPrevLayer.setClickListener((mouseX, mouseY, mouseButton) -> {
            game.prevLayer();
            labelLayer.setText(Integer.toString(game.getCurrentLayer().layer + 1));
        });
        layoutLevelEditor.addComponent(btnPrevLayer);

        checkBoxBackground = new CheckBox("Background", 3, 151);
        checkBoxBackground.setClickListener((mouseX, mouseY, mouseButton) -> game.setRenderBackground(checkBoxBackground.isSelected()));
        checkBoxBackground.setSelected(true);
        layoutLevelEditor.addComponent(checkBoxBackground);

        checkBoxForeground = new CheckBox("Foreground", 80, 151);
        checkBoxForeground.setClickListener((mouseX, mouseY, mouseButton) -> game.setRenderForeground(checkBoxForeground.isSelected()));
        checkBoxForeground.setSelected(true);
        layoutLevelEditor.addComponent(checkBoxForeground);

        checkBoxPlayer = new CheckBox("Player", 160, 151);
        checkBoxPlayer.setClickListener((mouseX, mouseY, mouseButton) -> game.setRenderPlayer(checkBoxPlayer.isSelected()));
        layoutLevelEditor.addComponent(checkBoxPlayer);

        setCurrentLayout(layoutLevelEditor);
    }

    @Override
    public void load(NBTTagCompound tagCompound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(NBTTagCompound tagCompound) {
        // TODO Auto-generated method stub

    }


}
