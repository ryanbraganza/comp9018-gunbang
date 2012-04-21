package gb;

import static gb.Constants.AXIS_SAMPLES;
import static gb.Constants.MAX_BARREL_ANGLE;
import static gb.Constants.MIN_BARREL_ANGLE;
import static gb.Constants.RADIAL_SAMPLES;
import static gb.Constants.Z_SAMPLES;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

public class Tank{
	
	private int numDeaths = 0;
	private int score = 0;
	
	private static final long serialVersionUID = 1L;
	
	private final ColorRGBA color;
	
	private final Node node;
	private final DynamicPhysicsNode dpn;
	
	private final float dimension;
	
	private float barrelAngle = 45;
	
	private final float BARREL_RADIUS;
	private final float BARREL_HEIGHT;
	
	private int strength = 500;
	private final float MIN_STRENGTH = 100;
	private final float MAX_STRENGTH = 1000;
	
	private PointLight pl;
	
	public Tank (PhysicsSpace ps, Vector3f center, float dimension, DisplaySystem display, ColorRGBA color, Lighting lighting){
		this.node = new Node();
		this.color = color;
		this.dpn = ps.createDynamicNode();
		this.dimension = dimension;
		this.BARREL_RADIUS = dimension/3f;
		this.BARREL_HEIGHT = dimension*5;
		
		pl = new PointLight();
		pl.setEnabled(true);
		pl.setAmbient(color);
		pl.setDiffuse(color);
		pl.setAttenuate(true);
		pl.setLinear(0.001f);
		pl.setQuadratic(0.005f);
		pl.setLocation(new Vector3f(0,10,0));
		lighting.getLightState().attach(pl);
		
		
		node.attachChild(dpn);
		
		Box box = new Box("box", Vector3f.ZERO.clone(),dimension, dimension, dimension);
		box.setModelBound(new BoundingBox());
		dpn.setLocalTranslation(center.x, dimension,center.z);
		box.updateModelBound();
		Texture boxTexture = TextureUtil.simpleLoadTexture("tank.png");
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(boxTexture);
		ts.setEnabled(true);
		box.setRenderState(ts);
		box.updateRenderState();
		
		dpn.attachChild(box);
		
		Vector3f spherePos = Vector3f.ZERO.clone();
		spherePos.y = spherePos.y + dimension;
		Sphere cap = new Sphere("cap", spherePos, Z_SAMPLES, RADIAL_SAMPLES, dimension);
		TextureState capS = display.getRenderer().createTextureState();
		Texture tex = TextureUtil.simpleLoadTexture("bullet.png");
		capS.setTexture(tex);
		cap.setRenderState(capS);
		cap.updateRenderState();
		dpn.attachChild(cap);
		dpn.generatePhysicsGeometry();  //do not include the barrel in the physics geometry
		
		dpn.setCenterOfMass(new Vector3f(0,-dimension/2f,0));
		
		Node joint = new Node("joint");
		
		dpn.attachChild(joint);
		joint.setLocalRotation(new Quaternion().fromAngles(-FastMath.DEG_TO_RAD*45,0,0));
		joint.setLocalTranslation(0,dimension,0);
		Node barrel = new Node("barrel");
		barrel.setLocalTranslation(0,0,BARREL_HEIGHT/2f+dimension);
		joint.attachChild(barrel);
		
		Cylinder cylinder = new Cylinder("barrel",AXIS_SAMPLES,RADIAL_SAMPLES,BARREL_RADIUS, BARREL_HEIGHT);
		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setAmbient(color);
		ms.setDiffuse(color);
		ms.setShininess(20);
		
		cylinder.setRenderState(ms);
		cylinder.updateRenderState();
		barrel.attachChild(cylinder);
		
	}

	public void fireBullet(Node rootNode, PhysicsSpace ps, DisplaySystem display, Sound sound) {
		float angle;
		
		float[] angles = new float[3];
		dpn.getLocalRotation().toAngles(angles);
		angle = -angles[1];
		angle -= FastMath.PI/2f;

		DynamicPhysicsNode bullet = ps.createDynamicNode();
		rootNode.attachChild(bullet);
		
		Vector3f worldTranslation = dpn.getLocalTranslation().clone();
		worldTranslation.x += -1.2f*dimension*FastMath.cos(angle);
		worldTranslation.z += -1.2f*dimension*FastMath.sin(angle);
		worldTranslation.y += 1.5f*dimension;
		
		bullet.setLocalTranslation(worldTranslation);
		bullet.setMass(5f);
		
		bullet.attachChild(new Sphere("sphere",Vector3f.ZERO.clone(),Z_SAMPLES,RADIAL_SAMPLES,4));
		bullet.generatePhysicsGeometry();
		Texture t = TextureUtil.simpleLoadTexture("bullet.png");
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(t);
		ts.setEnabled(true);
		bullet.setRenderState(ts);
		bullet.updateRenderState();
		
		
		Vector3f force = new Vector3f(FastMath.cos(angle),-barrelAngle/50f,FastMath.sin(angle)).mult(-100);
		bullet.addForce(force.mult(strength/4f));
		sound.fireSound();
	}

	public void attachTo(Node rootNode) {
		rootNode.attachChild(node);

	}
	
	public void barrelUp() {
		if (barrelAngle< MAX_BARREL_ANGLE) {
			barrelAngle += 0.1f;
			Spatial thingo = dpn.getChild("joint");
			thingo.setLocalRotation(new Quaternion().fromAngles(-FastMath.DEG_TO_RAD*barrelAngle,0,0));
		}
	}
	public void barrelDown() {
		if (barrelAngle>MIN_BARREL_ANGLE) {
			barrelAngle -= 0.1f;
			Spatial thingo = dpn.getChild("joint");
			thingo.setLocalRotation(new Quaternion().fromAngles(-FastMath.DEG_TO_RAD*barrelAngle,0,0));
		}
	}
	public void turnRight() {
		Vector3f torque = new Vector3f(0,-2,0);
		dpn.addTorque(torque);
		dpn.addForce(new Vector3f(0,37,0));
	}
	public void turnLeft() {
		Vector3f torque = new Vector3f(0,2,0);
		dpn.addTorque(torque);
		dpn.addForce(new Vector3f(0,37,0));
	}
	public void forward() {
		float angle;
		
		float[] angles = new float[3];
		dpn.getLocalRotation().toAngles(angles);
		angle = -angles[1];
		angle -= FastMath.PI/2f;
		Vector3f force = new Vector3f(FastMath.cos(angle),-1.4f,FastMath.sin(angle)).mult(-20);
		dpn.addForce(force);
	}
	public void backward() {
		float angle;
		
		float[] angles = new float[3];
		dpn.getLocalRotation().toAngles(angles);
		angle = -angles[1];
		angle -= FastMath.PI/2f;
		Vector3f force =new Vector3f(FastMath.cos(angle),1.4f,FastMath.sin(angle)).mult(20);
		dpn.addForce(force);
	}
	public void stronger() {
		if (strength < MAX_STRENGTH) {
			strength += 100;
		}
	}
	public void weaker() {
		if (strength > MIN_STRENGTH) {
			strength -= 100;
		}
	}

	private float yaw=0;
	public void update() {
		yaw+= 0.2f;
		Vector3f position = dpn.getLocalTranslation().clone();
		position.y += 4*dimension;
		pl.setLocation(position);
	}
	
	public ColorRGBA getColor() {
		return this.color;
	}
	public int getStrength() {
		return this.strength;
	}
	public float getAngle() {
		return this.barrelAngle;
	}
	public void on() {
		this.pl.setEnabled(true);
		
	}
	public void off() {
		this.pl.setEnabled(false);
	}
	public void reset() {
		
	}
	public int getScore() {
		return this.score;
	}
	public void incScore() {
		this.score++;
	}
	public int getDeaths() {
		return this.numDeaths;
	}
}
