package simple;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.input.KeyBindingManager;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;

public class Main extends SimpleGame {

	public static void main(String[] args) {
		Main main = new Main();
		main.setConfigShowMode(ConfigShowMode.AlwaysShow);
		main.start();
	}

	@Override
	protected void simpleInitGame() {
		Box box1 = new Box("Box numero uno", new Vector3f(0, 1, 0), 1, 1, 1);
		box1.setModelBound(new BoundingBox());
		box1.updateModelBound();
		rootNode.attachChild(box1);
		Box box2 = new Box("Box numero dos", new Vector3f(0, -1, 0), 10, 0.25f,
				10);
		box2.setModelBound(new BoundingBox());
		box2.updateModelBound();
		rootNode.attachChild(box2);
		Sphere sphere1 = new Sphere("Sphere nummer eins",
				new Vector3f(0, 1, 1), 20, 20, 1);
		sphere1.setModelBound(new BoundingSphere());
		sphere1.updateModelBound();
		rootNode.attachChild(sphere1);
		rootNode.setModelBound(new BoundingBox());
		rootNode.updateModelBound();
	}

	@Override
	protected void simpleUpdate() {
		// every frame before rendering
	}

	@Override
	protected void simpleRender() {
		// every frame after rendering
	}

}
