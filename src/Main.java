public final class Main {
    private int total;
    private int passed;

    public static void main(String[] args) {
        if (args.length != 1 || !"--demo".equals(args[0])) {
            System.err.println("Usage: java -cp out Main --demo");
            System.exit(2);
        }
        Main demo = new Main();
        demo.runDemo();
        if (demo.passed != demo.total) {
            System.exit(1);
        }
    }

    private void runDemo() {
        Renderer vector = new VectorRenderer();
        Renderer raster = new RasterRenderer();

        checkResult("T1", "Circle + VectorRenderer",
                new Circle("circle-1", 2, vector), "VECTOR circle radius=2");
        checkResult("T2", "Circle + RasterRenderer",
                new Circle("circle-1", 2, raster), "RASTER circle radius=2");
        checkResult("T3", "Square + VectorRenderer",
                new Square("square-1", 3, vector), "VECTOR square side=3");
        checkResult("T4", "Square + RasterRenderer",
                new Square("square-1", 3, raster), "RASTER square side=3");
        checkRuntimeSwitch();

        System.out.printf("SUMMARY: %d/%d PASS%n", passed, total);
    }

    private void checkResult(String testId, String classes, Shape shape, String expected) {
        String actual = shape.execute();
        record(testId, expected.equals(actual), classes + " | result=" + actual,
                "result=" + expected);
    }

    private void checkRuntimeSwitch() {
        Circle original = new Circle("circle-switch", 2, new VectorRenderer());
        Circle current = original;
        String originalId = original.getId();
        int originalRadius = original.getRadius();
        String before = current.execute();

        current.setImplementation(new RasterRenderer());
        String after = current.execute();

        boolean sameObject = original == current;
        boolean stateUnchanged = originalId.equals(current.getId())
                && originalRadius == current.getRadius();
        boolean correctResults = "VECTOR circle radius=2".equals(before)
                && "RASTER circle radius=2".equals(after);

        record("T5", sameObject && stateUnchanged && correctResults,
                "Circle + VectorRenderer -> RasterRenderer"
                        + " | sameObject=" + sameObject + " | stateUnchanged=" + stateUnchanged,
                "sameObject=true | stateUnchanged=true"
                        + " | before=VECTOR circle radius=2 | after=RASTER circle radius=2"
                        + " | id=circle-switch | radius=2");
        System.out.println("  before=" + before + " | after=" + after);
        System.out.printf("  id=%s -> %s | radius=%d -> %d%n",
                originalId, current.getId(), originalRadius, current.getRadius());
    }

    private void record(String testId, boolean success, String actual, String expected) {
        total++;
        if (success) {
            passed++;
        }
        System.out.printf("%s %s | %s%n", testId, success ? "PASS" : "FAIL", actual);
        if (!success) {
            System.out.println("  expected=" + expected);
        }
    }
}
