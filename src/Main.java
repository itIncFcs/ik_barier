import jssc.SerialPortException;

import java.util.Scanner;

/**
 * Created by ����� on 16.02.2016.
 */
public class Main {
    static SerialChannelToController serialChannelToController = null;
    public static void main(String[] args){

        try {
            serialChannelToController = new SerialChannelToController("/dev/ttyUSB0");
            BarrierListener barrierListener = new BarrierListener() {
                @Override
                public void barrierBroken() {
                    System.out.println("barrierBroken");
                    Scanner in = new Scanner(System.in);
                    in.next();
                    serialChannelToController.send(serialChannelToController.OPEN_BARRIER);
                }
            };
            serialChannelToController.setBarrierListener(barrierListener);
            serialChannelToController.open();
            serialChannelToController.startReceive();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                serialChannelToController.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
