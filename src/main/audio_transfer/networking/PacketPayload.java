package audio_transfer.networking;

public interface PacketPayload {

    byte[] getPayloadData();

    int length();

}
