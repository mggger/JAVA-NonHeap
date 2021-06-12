package niolearning;


import java.nio.ByteBuffer;

public class DuplicateApp {

    public static void main(String[] args) throws Exception {
        ByteBuffer bb = ByteBuffer.allocateDirect(10_000);

        class TestThread extends Thread {

            private Integer threadNum;

            public TestThread(Integer threadNum) {
                this.threadNum = threadNum;
            }

            @Override
            public void run() {
                ByteBuffer localBB = bb.duplicate();
                String name = Thread.currentThread().getName();
                int start = threadNum * Long.BYTES;
                localBB.position(start);
                localBB.mark();
                while (true) {
                    Long val = Long.valueOf(threadNum + 1 + System.nanoTime());
                    localBB.putLong(val);
                    localBB.reset();
                    Thread.yield();
                }
            }
        }

        int maxThreads = 12;
        for (int t = 0; t < maxThreads; t++) {
            Thread th = new TestThread(t);
            th.start();
        }

        while (true) {
            for (int t = 0; t< maxThreads; t++) {
                bb.position(t * Long.BYTES);
                Long val = bb.getLong();
                System.out.println(val + " ");
            }
            System.out.println();
            Thread.yield();
        }
    }
}
