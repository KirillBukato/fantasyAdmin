//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        ChGKProcessor processor = new ChGKProcessor();
        processor.process("example.xlsx", ChGKTableType.BASIC);
    }
}