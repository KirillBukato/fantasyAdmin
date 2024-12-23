package fantasyadmin;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        MainWindow window = new MainWindow();
        ChGKProcessor processor = new ChGKProcessor();
        processor.preprocess("example.xlsx", ChGKTableType.BASIC);
    }
}