package gb;

import static gb.Constants.WORLD_SIZE;
import static gb.TextureUtil.*;

import java.util.ArrayList;
import java.util.List;

import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jmex.audio.AudioSystem;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.SimplePhysicsGame;

public class Main extends SimplePhysicsGame {
	private List<Tank> tanks = new ArrayList<Tank>();
	private int turn = 0;
	private Hud hud;
	private static KeyBindingManager kbm;
	private Sound sound;
	private Skybox skybox;
	private Lighting lighting;

	public static void main(String[] args) {
		Main main = new Main();
		// main.setConfigShowMode(ConfigShowMode.AlwaysShow);
		main.start();
	}

	private void addTanks() {
		tanks.add(addTank(new Vector3f(-30, 0, -30), 10, ColorRGBA.red));
		tanks.add(addTank(new Vector3f(-30, 0, 30), 10, ColorRGBA.green));
		tanks.add(addTank(new Vector3f(30, 0, -30), 10, ColorRGBA.yellow));
		tanks.add(addTank(new Vector3f(30, 0, 30), 10, ColorRGBA.blue));
	}

	private Tank addTank(Vector3f position, float dimension, ColorRGBA color) {
		Tank theTank = new Tank(getPhysicsSpace(), position, dimension,
				display, color, lighting);
		theTank.attachTo(rootNode);
		return theTank;
	}

	private void addFloor() {

		StaticPhysicsNode n = getPhysicsSpace().createStaticNode();
		rootNode.attachChild(n);
		Box floorBox = new Box("floor", new Vector3f(0, -0.5f, 0), WORLD_SIZE,
				0.5f, WORLD_SIZE);
		TextureState ts = display.getRenderer().createTextureState();

		Texture t = simpleLoadTexture("checker.png");
		ts.setTexture(t);
		ts.setEnabled(true);

		floorBox.setRenderState(ts);

		floorBox.updateRenderState();
		n.attachChild(floorBox);
		n.generatePhysicsGeometry();
	}

	@Override
	protected void simpleInitGame() {
		this.hud = new Hud(rootNode, display);
		this.skybox = new Sky(rootNode, display);
		this.sound = new Sound(cam);
		this.lighting = new Lighting(display);
		lighting.attachTo(rootNode);
		rootNode.attachChild(skybox);
		rootNode.setCullHint(CullHint.Never);
		addTanks();
		addFloor();
		for (Tank t : tanks) {
			t.off();
		}
		tanks.get(0).on();
		setCommands();

	}

	@Override
	protected void simpleUpdate() {
		Tank tank = tanks.get(turn);
		checkCommands();
		hud.update(turn, tank);
		AudioSystem.getSystem().update();
		skybox.getLocalTranslation().set(cam.getLocation().x,
				cam.getLocation().y, cam.getLocation().z);
		
		for (Tank t : tanks) {
			t.update();
		}
	}

	private void setCommands() {
		kbm.set("fire", KeyInput.KEY_F);
		kbm.set("barrelUp", KeyInput.KEY_K);
		kbm.set("barrelDown", KeyInput.KEY_J);
		kbm.set("goLeft", KeyInput.KEY_Y);
		kbm.set("goRight", KeyInput.KEY_U);
		kbm.set("goahead", KeyInput.KEY_I);
		kbm.set("goback", KeyInput.KEY_M);
		kbm.set("stronger", KeyInput.KEY_RBRACKET);
		kbm.set("weaker", KeyInput.KEY_LBRACKET);
		kbm.set("stopMusic", KeyInput.KEY_SPACE);
	}

	private void checkCommands() {
		Tank tank = tanks.get(turn);
		if (kbm.isValidCommand("fire", false)) {
			tank.fireBullet(rootNode, getPhysicsSpace(), display, sound);
			turn++;
			turn %= 4;
			for (Tank t : tanks) {
				t.off();
			}
			tanks.get(turn).on();
		} else if (kbm.isValidCommand("barrelUp")) {
			tank.barrelUp();
		} else if (kbm.isValidCommand("barrelDown")) {
			tank.barrelDown();
		} else if (kbm.isValidCommand("goRight")) {
			tank.turnRight();
		} else if (kbm.isValidCommand("goLeft")) {
			tank.turnLeft();
		} else if (kbm.isValidCommand("goahead")) {
			tank.forward();
		} else if (kbm.isValidCommand("goback")) {
			tank.backward();
		} else if (kbm.isValidCommand("stronger", false)) {
			tank.stronger();
		} else if (kbm.isValidCommand("weaker", false)) {
			tank.weaker();
		} else if (kbm.isValidCommand("stopMusic", false)) {
			sound.toggle();
		}
		// cam.update();
	}

	@Override
	protected void simpleRender() {

	}

	@Override
	protected void initSystem() {
		super.initSystem();
		getPhysicsSpace().setDirectionalGravity(new Vector3f(0, -50, 0));
		kbm = KeyBindingManager.getKeyBindingManager();
	}
}
