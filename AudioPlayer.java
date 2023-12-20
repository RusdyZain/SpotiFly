import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class AudioPlayer {
    private Clip clip;
    private FloatControl volumeControl;
    private long lastValidPosition;

    public void load(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public boolean isRunning() {
        return clip != null && clip.isRunning();
    }

    public void setLoop(boolean loop) {
        if (clip != null) {
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.loop(0);
            }
        }
    }

    public void playFromPosition(long position) {
        clip.setMicrosecondPosition(position);
        clip.start();
        lastValidPosition = position;
    }

    public long getLastValidPosition() {
        return lastValidPosition;
    }

    public Clip getClip() {
        return clip;
    }

    public void setVolume(float value) {
        if (volumeControl != null) {
            volumeControl.setValue(20f * (float) Math.log10(value));
        }
    }

    public void stop() {
        if (clip != null) {
            lastValidPosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null) {
            clip.start();
        }
    }

    public void addCompletionListener(CompletionListener listener) {
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                if (event.getFramePosition() == clip.getFrameLength()) {
                    listener.onCompletion();
                }
            }
        });
    }

    public interface CompletionListener {
        void onCompletion();
    }

}
