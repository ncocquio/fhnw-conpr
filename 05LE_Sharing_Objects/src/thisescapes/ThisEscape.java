package thisescapes;

@SuppressWarnings("unused")
public class ThisEscape {
    public final int i;

    public ThisEscape(EventSource source) {
    	source.registerListener(new EventListener() {
            //this escapes here
            public void objectChanged() {
                doSomething();
            }
        });
    	i = 42;
    }

    protected void doSomething() {
        System.out.println(i);
    }

	public static void main(String[] args) {
        EventSource source = new EventSource();
        ThisEscape thisEscape = new ThisEscape(source);
    }
}
