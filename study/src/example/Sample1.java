package example;

public class Sample1 {

    private int cnt = 0;

    public static void main(String[] args){
        Sample1 sa = new Sample1();

        Thread[] ths = new Thread[5];
        for(int i=0; i<ths.length; i++){
            int finalI = i;
            ths[i] = new Thread(() -> {
                for(int j=0; j<2; j++)
                    System.out.println("Thread : " + finalI + ", cnt : " + sa.cnt++);
            });

            ths[i].start();
        }
    }

}
