import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String s1  = "aaa";
        String s = new String ("aaa");
        System.out.println(s == s1);

        String s2 = new String("a")+new String("b");
        s2.intern();
        String s3 = "ab";
        System.out.println(s2 == s3);

        String a = "1";
        String b = "2";
        String c = a + b;
        c.intern();
        System.out.println(c =="12");

        Map m = new HashMap<>();
        String a1 = new String("a");
        String b1 = new String("a");
        m.put(a1,"aaaaa");
        m.put(b1,"bbbbbbb");
        System.out.println(m.get(a1));
    }
}
