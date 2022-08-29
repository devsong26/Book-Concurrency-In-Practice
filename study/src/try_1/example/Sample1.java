package try_1.example;

public class Sample1 {

    private Integer cnt = 0;

    private Integer incrementAndGet(int threadN){
//        synchronized(cnt){
//            cnt++;
//        }
        System.out.println("----- Occupy thread : " + threadN + " -----");
        synchronized(cnt){ // warning: synchronization on a non-final field 'cnt'
            increment();
        }
        System.out.println("----- Increment cnt : " + cnt + " by thread : " + threadN + " -----");
        return cnt;
    }

    private void increment(){
        cnt++;
    }

    public static void main(String[] args){
        Sample1 sa = new Sample1();

        Thread[] ths = new Thread[5];
        for(int i=0; i<ths.length; i++){
            int finalI = i;
            ths[i] = new Thread(() -> {
                for(int j=0; j<5; j++){
                    System.out.println("Thread : " + finalI + ", cnt : " + sa.incrementAndGet(finalI));
//                    System.out.println("----- Release thred : " + finalI);
                }
            });

            ths[i].start();
        }
    }

}
