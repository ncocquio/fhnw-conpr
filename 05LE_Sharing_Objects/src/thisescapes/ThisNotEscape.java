package thisescapes;

@SuppressWarnings("unused")
public class ThisNotEscape {
    public final int i;
    private ThisNotEscape() { i = 42; }
    
    public static ThisNotEscape create(EventSource source) {
        final ThisNotEscape notEscape = new ThisNotEscape();
        source.registerListener(new EventListener() {
            public void objectChanged() {
                notEscape.doSomething();
            }
        });
        return notEscape;
    }

    protected void doSomething() {
        System.out.println(i);
    }

    public static void main(String[] args) {
        EventSource source = new EventSource();
        ThisNotEscape thisEscape = ThisNotEscape.create(source);
    }
}
