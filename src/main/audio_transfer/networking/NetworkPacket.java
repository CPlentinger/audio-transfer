package audio_transfer.networking;

import java.net.DatagramPacket;

public class NetworkPacket {

    public static final int HEADER_LENGTH = 6;

    public static final int TYPE_LENGTH = 1;

    public static final int SEQUENCE_LENGTH = 2;

    public static final int CHECKSUM_LENGTH = 1;

    private PacketType type;

    private short sequenceNumber;

    private byte checksum;

    private PacketPayload payload;

    public NetworkPacket(PacketType type, int sequenceNumber) {
        this.type = type;
        this.sequenceNumber = (short) sequenceNumber;
    }

    public NetworkPacket(PacketType type, int sequenceNumber, PacketPayload payload) {
        this.type = type;
        this.sequenceNumber = (short) sequenceNumber;
        this.payload = payload;
    }

    public DatagramPacket getDatagramPacket() {
        byte[] packet = new byte[HEADER_LENGTH + payload.length()];


        packet[0] = (byte) this.type.ordinal();

        packet[1] = (byte)(this.sequenceNumber & 0xff);
        packet[2] = (byte)((this.sequenceNumber >> 8) & 0xff);

        packet[3] = this.checksum;

        int i = 4;
        for (byte b : this.payload.getPayloadData()) {
            packet[i] = b;
            i++;
        }

        return new DatagramPacket(packet, packet.length);
    }

    public boolean hasPayload() {
        return payload != null;
    }

    public byte[] getData() {
        return null;
    }

    public byte getChecksum(byte[] input) {
        byte checksum = 0;
        for (byte cur_byte : input) {
            checksum = (byte) (((checksum & 0xFF) >>> 1) + ((checksum & 0x1) << 7)); // Rotate the accumulator
            checksum = (byte) ((checksum + cur_byte) & 0xFF);                        // Add the next chunk
        }
        return checksum;
    }

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = (short) sequenceNumber;
    }

    public byte getChecksum() {
        return checksum;
    }

    public void setChecksum(byte checksum) {
        this.checksum = checksum;
    }

    public PacketPayload getPayload() {
        return payload;
    }

    public void setPayload(PacketPayload payload) {
        this.payload = payload;
    }

}
