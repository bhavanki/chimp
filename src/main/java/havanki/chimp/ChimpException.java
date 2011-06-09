package havanki.chimp;

public class ChimpException extends Exception
{
    public ChimpException() { super(); }
    public ChimpException (String msg) { super (msg); }
    public ChimpException (Throwable cause) { super (cause); }
    public ChimpException (String msg, Throwable cause) { super (msg, cause); }
}
