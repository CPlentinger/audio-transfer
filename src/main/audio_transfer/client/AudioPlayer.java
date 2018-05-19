package audio_transfer.client;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer extends Thread {

    private final AudioFormat audioFormat = new AudioFormat(44100, 16, 2,
            true, true);
    private InputStream input;
    private AudioInputStream audioStream;
    private SourceDataLine outputLine;


    public AudioPlayer(byte[] audio) throws LineUnavailableException {
        input = new ByteArrayInputStream(audio);
        audioStream = getAudioStream(audio.length);
        outputLine = getOutputLine();
    }

    @Override
    public void run() {
        try {
            outputLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        outputLine.start();
        int bufferSize = (int) audioFormat.getSampleRate()
                * audioFormat.getFrameSize();
        byte buffer[] = new byte[bufferSize];
        int count;
        try {
            while ((count = audioStream.read(buffer, 0, buffer.length)) != -1) {
                if (count > 0) {
                    outputLine.write(buffer, 0, count);
                }
            }
            outputLine.drain();
            outputLine.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AudioInputStream getAudioStream(int length) {
        return new AudioInputStream(input, audioFormat, length / audioFormat.getFrameSize());
    }

    private SourceDataLine getOutputLine() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(
                SourceDataLine.class, audioFormat);
        return (SourceDataLine) AudioSystem.getLine(info);
    }

}
