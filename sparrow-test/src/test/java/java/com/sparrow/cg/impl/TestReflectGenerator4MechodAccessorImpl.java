package java.com.sparrow.cg.impl;

import com.sparrow.cg.Generator4MethodAccessor;
import com.sparrow.cg.MethodAccessor;
import com.sparrow.cg.impl.ReflectGenerator4MechodAccessorImpl;
import com.sparrow.protocol.dto.AuthorDTO;

public class TestReflectGenerator4MechodAccessorImpl {

    public static void main(String[] args) {

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.getAvatar();
        authorDTO.getCent();

        Generator4MethodAccessor generator4MethodAccessor = new ReflectGenerator4MechodAccessorImpl();

        final MethodAccessor methodAccessor = generator4MethodAccessor.newMethodAccessor(authorDTO.getClass());


        long start;

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            authorDTO.setAvatar("哈哈" + i);
//            final Object getAvatar = methodAccessor.get(authorDTO, "getAvatar");
//            long temp = i;
//            methodAccessor.set(authorDTO, "setCent",temp);
//            final Object getCent = methodAccessor.get(authorDTO, "getCent");
        }
        System.out.println("原生：" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            methodAccessor.set(authorDTO, "setAvatar", "哈哈" + i);
//            final Object getAvatar = methodAccessor.get(authorDTO, "getAvatar");
//            long temp = i;
//            methodAccessor.set(authorDTO, "setCent",temp);
//            final Object getCent = methodAccessor.get(authorDTO, "getCent");
        }
        System.out.println("反射：" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            authorDTO.setAvatar("哈哈" + i);
//            final Object getAvatar = methodAccessor.get(authorDTO, "getAvatar");
//            long temp = i;
//            methodAccessor.set(authorDTO, "setCent",temp);
//            final Object getCent = methodAccessor.get(authorDTO, "getCent");
        }
        System.out.println("原生2：" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            methodAccessor.set(authorDTO, "setAvatar", "哈哈" + i);
//            final Object getAvatar = methodAccessor.get(authorDTO, "getAvatar");
//            long temp = i;
//            methodAccessor.set(authorDTO, "setCent",temp);
//            final Object getCent = methodAccessor.get(authorDTO, "getCent");
        }
        System.out.println("反射2：" + (System.currentTimeMillis() - start));
        System.out.println();

    }
}