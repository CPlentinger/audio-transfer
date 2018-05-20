package audio_transfer.networking;

public class StatusPayload implements PacketPayload {

    private String status;

    public StatusPayload(String status) {
        this.status = status;
    }

    @Override
    public byte[] getPayloadData() {
        return status.getBytes();
    }

    @Override
    public int length() {
        return getPayloadData().length;
    }
}
