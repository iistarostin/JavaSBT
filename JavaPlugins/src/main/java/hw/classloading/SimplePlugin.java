package hw.classloading;

public class SimplePlugin implements Plugin{
    SupportClass supportClass = new SupportClass();
    public void doUseful() {
        System.out.println(supportClass.getMessage());
    }
}
