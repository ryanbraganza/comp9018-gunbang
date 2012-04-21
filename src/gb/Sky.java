package gb;

//import static com.jme.scene.Node.logger;

import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


public class Sky extends Skybox{
	private static final long serialVersionUID = 1L;
	private final Node rootNode;
	private final DisplaySystem display;
	
	public Sky(Node rootNode, DisplaySystem display) {
		super( "skybox", 200, 200, 200 );
		this.rootNode = rootNode;
		this.display = display;
		setupSky();
	}
    private void setupSky() {
        
//
//        try {
//            ResourceLocatorTool.addResourceLocator(
//                    ResourceLocatorTool.TYPE_TEXTURE,
//                    new SimpleResourceLocator(getClass().getResource(
//                            "/jmetest/data/texture/")));
//        } catch (Exception e) {
//     //       logger.warning("Unable to access texture directory.");
//            e.printStackTrace();
//        }

//        setTexture( Skybox.Face.North, TextureManager.loadTexture("north.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
//        setTexture( Skybox.Face.West, TextureManager.loadTexture("west.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
//        setTexture( Skybox.Face.South, TextureManager.loadTexture("south.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
//        setTexture( Skybox.Face.East, TextureManager.loadTexture("east.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
//        setTexture( Skybox.Face.Up, TextureManager.loadTexture("top.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
//        setTexture( Skybox.Face.Down, TextureManager.loadTexture("bottom.jpg", Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear ) );
        setTexture(Skybox.Face.North, TextureManager.loadTexture(getClass().getResource("clouds.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        setTexture(Skybox.Face.South, TextureManager.loadTexture(getClass().getResource("clouds.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        setTexture(Skybox.Face.East, TextureManager.loadTexture(getClass().getResource("clouds.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        setTexture(Skybox.Face.West, TextureManager.loadTexture(getClass().getResource("clouds.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        setTexture(Skybox.Face.Up, TextureManager.loadTexture(getClass().getResource("sun.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        setTexture(Skybox.Face.Down, TextureManager.loadTexture(getClass().getResource("fire.png"),Texture.MinificationFilter.BilinearNoMipMaps,Texture.MagnificationFilter.Bilinear));
        preloadTextures();
        
       
//        CullState cullState = display.getRenderer().createCullState();
//        cullState.setCullFace(CullState.Face.Back);
//        cullState.setEnabled( true );
//        setRenderState( cullState );
////        rootNode.setRenderState(cullState);
        updateRenderState();
    }


}
