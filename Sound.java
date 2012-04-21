package gb;

import com.jme.renderer.Camera;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;

public class Sound {
	AudioTrack fireSound;
    AudioTrack bgmusic;
	private Camera cam;
	public Sound(Camera cam){
		this.cam=cam;
		setupSound();
	}
	public void toggle() {
		if (bgmusic.isPlaying()) {
			bgmusic.pause();
		} else {
			bgmusic.play();
		}
	}
	private void setupSound() {
        /** Set the 'ears' for the sound API */
        AudioSystem audio = AudioSystem.getSystem();
        audio.getEar().trackOrientation(cam);
        audio.getEar().trackPosition(cam);
		
		/** Create program sound */
		fireSound = audio.createAudioTrack( getClass().getResource( "fire.ogg" ), false);
        fireSound.setMaxAudibleDistance(10);
        fireSound.setVolume(1.0f);
        

        bgmusic = audio.createAudioTrack(getClass().getResource("lovegrows.ogg"), true);
        bgmusic.setLooping(true);
        //bgmusic.setVolumeChangeRate(1f);
        bgmusic.setVolume(0);
        bgmusic.setTargetVolume(0.2f);
        bgmusic.play();
	}
	public void fireSound() {
	    fireSound.setWorldPosition(cam.getLocation());
        fireSound.play();
	}

}
