package at.walternative.demo.inheritance;

import at.walternative.demo.inheritance.fixtures.Child;
import at.walternative.demo.inheritance.fixtures.Parent;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InheritanceTests {

    @Test
    public void testThatInheritanceWorks() {
        Parent parent = new Parent();
        Child child = new Child();

        assertNotEquals(parent.doSomething(), child.doSomething());
    }

    @Test
    public void testThatIdentityIsNotEquality() {
        String a = "This is a test";
        String b = "This is a test";

        assertTrue(a.equals(b));
    }

    @Test
    public void testThatAutoboxingIsUnpredictable() {
        Parent parent = new Parent();

        int bla = parent.calculate(1);
        int blu = parent.calculate(1);

        assertEquals(bla, blu);
    }
}
