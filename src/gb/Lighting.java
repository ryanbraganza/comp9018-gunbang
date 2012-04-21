package gb;

import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

public class Lighting {

	private final LightState ls;
	
	public LightState getLightState() {
		return this.ls;
	}

	public Lighting(DisplaySystem display) {
		
		this.ls = display.getRenderer().createLightState();
		PointLight light = new PointLight();
		light.setLocation(new Vector3f(20,100,20));
		light.setDiffuse(new ColorRGBA(1,1,1,1));
		light.setEnabled(true);
		ls.attach(light);
		
		DirectionalLight dlight = new DirectionalLight();
		dlight.setAmbient(ColorRGBA.white.clone());
		dlight.setAttenuate(true);
		dlight.setDirection(new Vector3f(0,1,0));
		dlight.setEnabled(false);
		ls.attach(dlight);
		
	}
	public void attachTo(Node rootNode) {
		rootNode.setRenderState(ls);
		rootNode.updateRenderState();
	}
}
