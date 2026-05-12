public class test {
    public static void main(String[] args) {
        try {
            // 适配 5.x 版本驱动的类名
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("? 驱动类加载成功！jar 包引入没问题~");
        } catch (ClassNotFoundException e) {
            System.out.println("? 找不到类，jar 包没加载成功！");
            e.printStackTrace();
        }
    }
}