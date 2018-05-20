package audio_transfer.networking;

public class AudioPayload implements PacketPayload {

    private byte[] audio;

    public AudioPayload(byte[] audio) {
        this.audio = audio;
    }

    @Override
    public byte[] getPayloadData() {
        return audio;
    }

    @Override
    public int length() {
        return audio.length;
    }
}
