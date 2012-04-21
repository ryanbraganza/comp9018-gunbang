package simple;

import java.nio.FloatBuffer;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;


/** see http://www.jmonkeyengine.com/wiki/doku.php?id=displaying_dynamic_values_in_the_hud */
public class HudTut3 extends SimpleGame {
    private static final float MAXIMUM = 100f;
    private Quaternion rotQuat = new Quaternion();
    private Vector3f axis = new Vector3f(1, 1, 0);
    private Cylinder cylinder;
    private float angle = 0;
   
    private Quad gauge;
   
    private int textureWidth;
    private int textureHeight;

    
    private float getUForPixel(int xPixel) {
        return (float) xPixel / textureWidth;
    }

    private float getVForPixel(int yPixel) {
        return 1f - (float) yPixel / textureHeight;
    }

    
    

    private Node hudNode;
    
    public static void main(String[] args) {
        HudTut3 app = new HudTut3();
        app.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
        app.start();
    }
    protected void simpleInitGame() {
        display.setTitle("HUD Tutorial 1");
       
        /* create a rotating cylinder so we have something in the background */
        cylinder = new Cylinder("Cylinder", 6, 18, 5, 10);
        cylinder.setModelBound(new BoundingBox());
        cylinder.updateModelBound();

        MaterialState ms = display.getRenderer().createMaterialState();
        ms.setAmbient(new ColorRGBA(1f, 0f, 0f, 1f));
        ms.setDiffuse(new ColorRGBA(1f, 0f, 0f, 1f));
        
        /* has been depricated */
        //ms.setAlpha(1f);

        ms.setEnabled(true);
        cylinder.setRenderState(ms);
        cylinder.updateRenderState();
        rootNode.attachChild(cylinder);
        
        hudNode = new Node("hudNode");
        Quad hudQuad = new Quad("hud", 34f, 10f);
        gauge = new Quad("gauge",32f,8f);
        
        // create the texture state to handle the texture
        final TextureState ts = display.getRenderer().createTextureState();
        // load the image bs a texture (the image should be placed in the same directory bs this class)
        final Texture texture = TextureManager.loadTexture(
                getClass().getResource("dynhud.png"),
                Texture.MinificationFilter.Trilinear, // of no use for the quad
                Texture.MagnificationFilter.Bilinear, // of no use for the quad
                1.0f,
                true);
        // set the texture for this texture state
        ts.setTexture(texture);
        // initialize texture width
        textureWidth = ts.getTexture().getImage().getWidth();
        // initialize texture height
        textureHeight = ts.getTexture().getImage().getHeight();
        // activate the texture state
        ts.setEnabled(true);
        
        
        // correct texture application:
        final FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
        // coordinate (0,0) for vertex 0
        texCoords.put(getUForPixel(0)).put(getVForPixel(0));
        // coordinate (0,40) for vertex 1
        texCoords.put(getUForPixel(0)).put(getVForPixel(10));
        // coordinate (40,40) for vertex 2
        texCoords.put(getUForPixel(34)).put(getVForPixel(10));
        // coordinate (40,0) for vertex 3
        texCoords.put(getUForPixel(34)).put(getVForPixel(0));
        // assign texture coordinates to the quad
        hudQuad.setTextureCoords(new TexCoords(texCoords));
        // apply the texture state to the quad
        hudQuad.setRenderState(ts);

        // to handle texture transparency:
        // create a blend state
        final BlendState bs = display.getRenderer().createBlendState();
        // activate blending
        bs.setBlendEnabled(true);
        // set the source function
        bs.setSourceFunctionAlpha(BlendState.SourceFunction.SourceAlpha);
        // set the destination function
        bs.setDestinationFunctionAlpha(BlendState.DestinationFunction.OneMinusSourceAlpha);
        // set the blend equation between source and destination
        bs.setBlendEquation(BlendState.BlendEquation.Add);
        bs.setTestEnabled(false);
        // activate the blend state
        bs.setEnabled(true);
        // assign the blender state to the quad
        hudQuad.setRenderState(bs);
        

        hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);        

        hudNode.setLocalTranslation(new Vector3f(display.getWidth()/2,display.getHeight()/2,0));

       
        hudNode.setLightCombineMode(Spatial.LightCombineMode.Off);        
        
 
        hudNode.attachChild(hudQuad);
        hudNode.attachChild(gauge);
       
        hudNode.setRenderState(ts);
        hudNode.setRenderState(bs);
        hudNode.updateRenderState();
        setGauge(0);
        hudNode.updateRenderState();
        rootNode.attachChild(hudNode);


    }
    protected void simpleUpdate() {
        /* recalculate rotation for the cylinder */
        if (timer.getTimePerFrame() < 1) {
            angle = angle + timer.getTimePerFrame();
        }

        rotQuat.fromAngleAxis(angle, axis);
        setGauge((int)cam.getLocation().length());

        cylinder.setLocalRotation(rotQuat);
    }
    private void setGauge(int value) {
        value %= (int)MAXIMUM;
        FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
        
        float relCoord = 0.5f - ((float)value / MAXIMUM) * 0.5f;
        texCoords.put(relCoord).put(getVForPixel(56));
        texCoords.put(relCoord).put(getVForPixel(63));
        texCoords.put(relCoord + 0.5f).put(getVForPixel(63));
        texCoords.put(relCoord + 0.5f).put(getVForPixel(56));     
        gauge.setTextureCoords(new TexCoords(texCoords));
   }

}
