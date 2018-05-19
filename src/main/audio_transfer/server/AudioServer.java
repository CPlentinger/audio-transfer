package audio_transfer.server;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServer extends Thread {

    private ServerSocket socket;
    private volatile boolean running = true;
    private final AudioFormat audioFormat = new AudioFormat(44100, 16, 2,
            true, true);
    private SourceDataLine audioOutput;

    public AudioServer(int portNumber) throws IOException, LineUnavailableException {
        socket = new ServerSocket(portNumber);
        audioOutput = getOutputLine();
    }

    @Override
    public void run() {
        try {
            audioOutput.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        audioOutput.start();
        while (running) {
            try {
                Socket connectedSocket = socket.accept();
                DataInputStream in = new DataInputStream(new BufferedInputStream(connectedSocket.getInputStream()));
                int count;
                int bufferSize = (int) audioFormat.getSampleRate();
                byte buffer[] = new byte[bufferSize];
                in.readFully(buffer);
                while (buffer.length > 0) {
                    audioOutput.write(buffer, 0, bufferSize);
                    in.readFully(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        running = false;
    }

    private SourceDataLine getOutputLine() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(
                SourceDataLine.class, audioFormat);
        return (SourceDataLine) AudioSystem.getLine(info);
    }


    public static void main(String[] args) {
        try {
            new AudioServer(9090).start();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
