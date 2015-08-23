import org.junit.Test;

/**
 * Created by stms130834 on 2014/3/26.
 */
public class FractalPictureTest {

    @Test
    public void test1(){
        System.out.println("Hello Test");
    }

    @Test
    public void test2(){
        FractalPicture fp = new FractalPicture();

        System.out.println(fp.getLength(-1, 0, 1,53));
    }

    @Test
    public void test3(){
        FractalPicture fp = new FractalPicture();

        System.out.println(fp.getLength(1, 1, 10,10));
    }

    @Test
    public void test4(){
        FractalPicture fp = new FractalPicture();

        System.out.println(fp.getLength(-10, 54, 10,55));
    }

    @Test
    public void test5(){
        FractalPicture fp = new FractalPicture();
        System.out.println(fp.getLength(14, 45, 22,54));
    }
}
