public class GenSerialNo {
    // 原本是雪花算法，这里只用了每毫秒生成个数的限制
    
    // ==============================Fields===========================================
    /** 开始时间截 (2015-01-01) */
    private final long twepoch = 1009843200000L;

    /** 序列在id中占的位数 */
    private final long sequenceBits = 10L;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
    private static GenSerialNo genSerialNo = null;

    private GenSerialNo() {
    }
    /**
     * 取得PrimaryGenerater的单例实现
     *
     * @return
     */
    public static GenSerialNo getInstance() {
        if (genSerialNo == null) {
            synchronized (GenSerialNo.class) {
                if (genSerialNo == null) {
                    genSerialNo = new GenSerialNo();
                }
            }
        }
        return genSerialNo;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID
     * @return
     */
    public synchronized String nextNo() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        System.out.println(String.format("%04d", sequence));
        int num = (int)((Math.random() * 9 + 1) * 10000);
        return timestamp + String.format("%04d", sequence) + num;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        GenSerialNo genSerialNo = GenSerialNo.getInstance();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        String no = genSerialNo.nextNo();
                        System.out.println(no);
                    }
                }
            }).start();
        }
    }
}
