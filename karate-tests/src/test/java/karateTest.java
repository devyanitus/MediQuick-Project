import com.intuit.karate.junit5.Karate;

class KarateTest {

    @Karate.Test
    Karate runAllTests() {
        return Karate.run("features").relativeTo(getClass());
    }
}