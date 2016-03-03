/**
 * Created by andrey on 19.02.16.
 */
public interface ChannelToController {
    boolean open() throws Exception;

    boolean close() throws Exception;

    boolean send(byte msg);

    void startReceive();
}
