package audio_transfer.networking;

public class ConfigPayload implements PacketPayload {

    public static final int PAYLOAD_LENGTH = 9;

    public static final int SAMPLE_RATE_LENGTH = 4;

    public static final int SAMPLE_SIZE_LENGTH = 1;

    public static final int PACKET_SIZE_LENGTH = 4;

    private int sampleRate;

    private byte sampleSize;

    private int packetSize;

    public ConfigPayload(int sampleRate, int sampleSize, int packetSize) {
        this.sampleRate = sampleRate;
        this.sampleSize = (byte) sampleSize;
        this.packetSize = packetSize;
    }

    @Override
    public byte[] getPayloadData() {
        byte[] packet = new byte[PAYLOAD_LENGTH];

        packet[0] = (byte) (this.sampleRate >>> 24);
        packet[1] = (byte) (this.sampleRate >>> 16);
        packet[2] = (byte) (this.sampleRate >>> 8);
        packet[3] = (byte) (this.sampleRate);

        packet[4] = this.sampleSize;

        packet[5] = (byte) (this.packetSize >>> 24);
        packet[6] = (byte) (this.packetSize >>> 16);
        packet[7] = (byte) (this.packetSize >>> 8);
        packet[8] = (byte) (this.packetSize);

        return packet;
    }

    @Override
    public int length() {
        return PAYLOAD_LENGTH;
    }
}
