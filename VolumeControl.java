import javax.swing.JSlider;

public class VolumeControl {
    private JSlider volumeSlider;
    private AudioPlayer audioPlayer;

    public VolumeControl(JSlider slider, AudioPlayer player) {
        volumeSlider = slider;
        audioPlayer = player;

        volumeSlider.addChangeListener(e -> updateVolume());
    }

    public void updateVolume() {
        if (!volumeSlider.getValueIsAdjusting()) {
            float volume = (float) volumeSlider.getValue() / 100;
            audioPlayer.setVolume(volume);
        }
    }
}
