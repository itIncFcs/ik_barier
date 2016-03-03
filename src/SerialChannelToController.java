import jssc.*;

public class SerialChannelToController implements ChannelToController {

    //Port config
    private SerialPort serialPort;
    private String comPortName;
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;

    //Port messages
    public final byte BARRIER_BROKEN = 49;
    public final byte OPEN_BARRIER = 50;

    private BarrierListener barrierListener;

    public SerialChannelToController(String comPortName) throws Exception{
       this(comPortName, SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
               SerialPort.PARITY_NONE);
    }
    public SerialChannelToController(String comPortName,
                                     int baudRate,
                                     int dataBits,
                                     int stopBits,
                                     int parity) throws Exception {
        this.comPortName = comPortName;
        serialPort = new SerialPort(this.comPortName);
        setParams(baudRate, dataBits, stopBits, parity);
    }

    public void setBarrierListener(BarrierListener listener){
        this.barrierListener = listener;
    }

    public void setParams(int baudRate,
                          int dataBits,
                          int stopBits,
                          int parity) throws SerialPortException {

        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        if(serialPort.isOpened()) {
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
        }
    }

    @Override
    public boolean open() throws Exception {
        boolean isOpen = serialPort.openPort();
        serialPort.setParams(baudRate, dataBits, stopBits, parity);
        return isOpen;
    }

    @Override
    public boolean close() throws Exception {
        return serialPort.closePort();
    }

    @Override
    public boolean send(byte msg) {
        try {
            serialPort.writeByte(msg);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void startReceive() {
        while (true) {
            try {
                byte t = serialPort.readBytes(1)[0];
                if(t == this.BARRIER_BROKEN){
                    barrierListener.barrierBroken();
                }
                serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR); //очистка буффера порта

            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
