import java.time.Instant;

public class Timestamp {
    public static void main(String[] args) {
        Instant instant = Instant.now();
        System.out.println(instant.getEpochSecond());
    }
}
