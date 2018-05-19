package audio_transfer.client;

import audio_transfer.exceptions.MixerNotFoundException;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AudioExtractor extends Thread {

    private final String targetMixerInfo = "Stereo-mix";
    private Mixer extractionMixer;
    private final AudioFormat audioFormat = new AudioFormat(44100, 16, 2,
            true, true);
    private TargetDataLine extractionLine;
    private ByteArrayOutputStream outputStream;

    private volatile boolean running = true;

    public AudioExtractor() throws MixerNotFoundException, LineUnavailableException {
        outputStream = new ByteArrayOutputStream();
        extractionMixer = getExtractionMixer();
        extractionLine = getExtractionLine();
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket("192.168.2.23", 9090);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            extractionLine.open(audioFormat);
            extractionLine.start();
            int bufferSize = (int) audioFormat.getSampleRate();
            byte buffer[] = new byte[bufferSize];
            while (running) {
                int count =
                        extractionLine.read(buffer, 0, buffer.length);
                if (count > 0) {
                    out.write(buffer);
                }
            }
            outputStream.close();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        running = false;
    }

    private TargetDataLine getExtractionLine() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, audioFormat);
        return (TargetDataLine) extractionMixer.getLine(dataLineInfo);
    }

    private Mixer getExtractionMixer() throws MixerNotFoundException {
        for (Mixer.Info i : AudioSystem.getMixerInfo())  {
            if (i.getName().startsWith(targetMixerInfo)) {
                return AudioSystem.getMixer(i);
            }
        }
        throw new MixerNotFoundException("Could not find a suitable capture device.");
    }

    public static void main(String[] args) {
        try {
            AudioExtractor audioExtractor = new AudioExtractor();
            audioExtractor.start();
//            sleep(10000);
//            audioExtractor.terminate();
//            System.out.println("Done capturing");
//            Socket s = new Socket("localhost", 9090);
//            DataOutputStream out = new DataOutputStream(s.getOutputStream());
//            out.write(audioExtractor.outputStream.toByteArray());
//            s.close();
        } catch (MixerNotFoundException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
