package gb;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class Hud {
	private Node hudRoot;
	private Node topLeft;
	private Node bottomRight;
	private Node topRight;
	private Node bottomLeft;

	private Text text = new Text("text", "the text to write");

	public Hud(Node rootNode, DisplaySystem display) {

		hudRoot = new Node("hudRoot");
		topLeft = new Node("topLeft");
		bottomRight = new Node("bottomRight");
		topRight = new Node("topRight");
		bottomLeft = new Node("bottomLeft");
		rootNode.attachChild(hudRoot);
		hudRoot.attachChild(topLeft);
		hudRoot.attachChild(topRight);
		hudRoot.attachChild(bottomRight);
		hudRoot.attachChild(bottomLeft);

    topLeft.setLightCombineMode(Spatial.LightCombineMode.Off);
		topLeft.setRenderQueueMode(Renderer.QUEUE_ORTHO);
    topLeft.updateRenderState();
		topLeft.attachChild(text);
		text.setTextColor(ColorRGBA.green);
		text.setCullHint(CullHint.Never);
		text.setRenderState(Text.getDefaultFontTextureState());
		text.setRenderState(Text.getFontBlend());

		topLeft.setLocalTranslation(Vector3f.ZERO.clone());
		bottomRight.setLocalTranslation(display.getWidth(),display.getHeight(),0);
		topRight.setLocalTranslation(display.getWidth(),0,0);
		bottomLeft.setLocalTranslation(0,display.getHeight(),0);

		Node backing = new Node("backing");
		Texture t = TextureUtil.simpleLoadTexture("hud.png");
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(t);
		ts.setEnabled(true);
		backing.setRenderState(ts);

		BlendState bs = display.getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunctionAlpha(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.OneMinusSourceAlpha);
        //bs.setBlendEquation(BlendState.BlendEquation.Add);
        bs.setTestEnabled(false);
        bs.setEnabled(true);
        Quad quad = new Quad("backing",800,600);
        quad.setRenderState(bs);
        quad.setRenderState(ts);
        backing.attachChild(quad);
        backing.setRenderState(bs);
        backing.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        backing.setLightCombineMode(LightCombineMode.Off);
        backing.updateRenderState();
        backing.setLocalTranslation(new Vector3f(400,-200,0));
        
        bottomLeft.attachChild(backing);
        backing.setZOrder(0);
        hudRoot.setRenderQueueMode(Renderer.QUEUE_ORTHO);
	}
	
	public void update(int turn, Tank tank) {
		int strength = tank.getStrength();
		float angle = tank.getAngle();
		ColorRGBA color = tank.getColor();
		int score = tank.getScore();
		int numDeaths = tank.getDeaths();
		
		String text = "";
		text += "player " + turn+"'s turn"; 
		text += " - ";
		text += "power: " + strength;
		text += " - ";
		text += "angle: " + Math.round(angle);
		text += " - ";
		text += "score: " + score;
		text += " - ";
		text += "deaths: " + numDeaths;
		
		Text newText = new Text("text", text);
		newText.setTextColor(color);
		this.text.removeFromParent();
		topLeft.attachChild(newText);
		this.text = newText;
		newText.setRenderState(Text.getDefaultFontTextureState());
		newText.setRenderState(Text.getFontBlend());
		newText.updateRenderState();
		newText.setZOrder(10);
	}
}
