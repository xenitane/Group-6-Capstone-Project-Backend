package mainpackage.socMedApp.util;


import java.time.Instant;

public class Generator {
    public static String IDGen() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
        stringBuilder.append('-');
        for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
        stringBuilder.append('-');
        for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
        stringBuilder.append('-');
        for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
        stringBuilder.append('-');
        for (int i = 0; i < 12; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));

        return stringBuilder.toString();

    }
}