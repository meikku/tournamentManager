package tournamentmanager.util;

public class Util {

    public static int findNextPowerOfTwo(int number) throws Exception {
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            int power = (int) Math.pow(2, i);
            if (power >= number) {
                return power;
            }
        }
        throw new Exception("Number too high, cannot find a power of two");
    }
}
