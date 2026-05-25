/**
 * คลาสทำหน้าที่ประมวลผลข้อมูลการเงิน โดยรวบรวมอัลกอริทึมต่างๆ 
 * เพื่อวิเคราะห์พฤติกรรมจากข้อมูลที่ดึงมาจากโครงสร้างข้อมูลหลัก
 */
public class Analysis {

    // คำนวณรายรับ รายจ่าย และยอดคงเหลือสุทธิ (Net Balance) โดยการ Traverse ผ่านคิวข้อมูล
    public static void incomeVsExpense(TransactionQueue queue) {
        double totalIncome = 0;
        double totalExpense = 0;

        DayNode currentDay = queue.front;
        while (currentDay != null) {
            TransactionNode currentTransaction = currentDay.transactions.head;
            while (currentTransaction != null) {
                Transaction t = currentTransaction.data;
                // ตรวจสอบประเภทธุรกรรม: type 1 คือฝาก, 2 คือถอน
                if (t.type == 1) {
                    totalIncome += t.amount;
                } else if (t.type == 2) {
                    totalExpense += t.amount;
                }
                currentTransaction = currentTransaction.next;
            }
            currentDay = currentDay.next;
        }

        System.out.println("\n===== Income vs Expense =====");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expense: " + totalExpense);
        System.out.println("Net Balance: " + (totalIncome - totalExpense));
    }

    // คำนวณสัดส่วนเปอร์เซ็นต์ค่าใช้จ่ายรายหมวดหมู่โดยดึงข้อมูลจาก N-ary Tree
    public static void categoryPercentage(TransactionQueue queue) {
        double[] categoryTotals = new double[6];
        double totalExpense = 0;
        CategoryTree tree = queue.categoryTree;

        // ดึงข้อมูลจากกิ่งหมวดหมู่ (Index 1-5) โดยตรงเพื่อประสิทธิภาพสูงสุด (Direct Access)
        for (int i = 1; i <= 5; i++) {
            CategoryNode branch = tree.root.children[i];
            TransactionNode current = branch.transactions.head;
            while (current != null) {
                categoryTotals[i] += current.data.amount;
                totalExpense += current.data.amount;
                current = current.next;
            }
        }

        String[] names = {"", "Food", "Travel", "Shopping", "Entertainment", "Other"};
        System.out.println("\n===== Category Percentage =====");
        
        // ตรวจสอบข้อมูลก่อนคำนวณเพื่อป้องกันข้อผิดพลาดจากการหารด้วยศูนย์
        if (totalExpense == 0) {
            System.out.println("No expense data");
            return;
        }

        for (int i = 1; i <= 5; i++) {
            double percent = (categoryTotals[i] / totalExpense) * 100;
            System.out.println(names[i] + ": " + String.format("%.2f", percent) + "%");
        }
    }

    // คำนวณความถี่การใช้งาน (จำนวนครั้ง) ต่อหมวดหมู่
    public static void spendingFrequency(TransactionQueue queue) {
        int[] frequency = new int[6];
        CategoryTree tree = queue.categoryTree;

        for (int i = 1; i <= 5; i++) {
            CategoryNode branch = tree.root.children[i];
            TransactionNode current = branch.transactions.head;
            // นับจำนวนโหนดธุรกรรมใน Linked List ของกิ่งนั้นๆ
            while (current != null) {
                frequency[i]++;
                current = current.next;
            }
        }

        String[] names = {"", "Food", "Travel", "Shopping", "Entertainment", "Other"};
        System.out.println("\n===== Spending Frequency =====");
        for (int i = 1; i <= 5; i++) {
            System.out.println(names[i] + ": " + frequency[i] + " times");
        }
    }

    // สร้างแถวลำดับ (Array) ยอดรายจ่ายรายวันเพื่อรองรับการคำนวณความผันผวน
    public static double[] buildDailyExpenseArray(TransactionQueue queue) {
        double[] dailyExpense = new double[queue.size];
        int index = 0;
        DayNode currentDay = queue.front;

        while (currentDay != null) {
            double dayTotal = 0;
            TransactionNode currentTransaction = currentDay.transactions.head;
            while (currentTransaction != null) {
                Transaction t = currentTransaction.data;
                if (t.type == 2) {
                    dayTotal += t.amount;
                }
                currentTransaction = currentTransaction.next;
            }
            dailyExpense[index] = dayTotal;
            index++;
            currentDay = currentDay.next;
        }
        return dailyExpense;
    }

    // วิเคราะห์การเพิ่มขึ้นสูงสุดของรายจ่ายในแต่ละวัน (Derivative Analysis)
    public static void derivativeAnalysis(TransactionQueue queue) {
        double[] daily = buildDailyExpenseArray(queue);
            
        if(daily.length < 2){
            System.out.println("Not enough data");
            return;
        }

        double maxIncrease = daily[1] - daily[0];
        int maxDay = 1;

        for (int i = 1; i < daily.length; i++) {
            double diff = daily[i] - daily[i - 1];
            if (diff > maxIncrease) {
                maxIncrease = diff;
                maxDay = i;
            }
        }
        System.out.println("\nHighest Spending Increase: " + maxIncrease);
        System.out.println("Highest Increase Day Index: " + maxDay);
    }

    // ประเมินระดับความเครียดโดยคำนวณจากปัจจัยพฤติกรรมการเงิน (Weighted Scoring Model)
    public static void stressScore(TransactionQueue queue) {
        double totalExpense = 0;
        double shoppingExpense = 0;
        int totalTransactions = 0;
        int nightTransactions = 0;

        DayNode currentDay = queue.front;

        while(currentDay != null){
            TransactionNode currentTransaction = currentDay.transactions.head;
            while(currentTransaction != null){
                Transaction t = currentTransaction.data;
                if(t.type == 2){
                    totalExpense += t.amount;
                    totalTransactions++;
                    if(t.category == 3){ shoppingExpense += t.amount; }
                    int hour = t.datetime.getHour();
                    if(hour >= 22 || hour <= 4){ nightTransactions++; }
                }
                currentTransaction = currentTransaction.next;
            }
            currentDay = currentDay.next;
        }

        if(totalExpense == 0){
            System.out.println("\nNo expense data");
            return;
        }

        // คำนวณปัจจัยย่อย
        double shoppingRatio = shoppingExpense / totalExpense;
        double frequencyScore = totalTransactions / 50.0;
        double nightScore = nightTransactions / 20.0;
        
        // วัดความผันผวนทางการเงิน (Volatility)
        double[] daily = buildDailyExpenseArray(queue);
        double volatility = 0;
        for(int i = 1; i < daily.length; i++){ volatility += Math.abs(daily[i] - daily[i - 1]); }
        double volatilityScore = volatility / 10000.0;

        double baseline = calculateBaseline(queue);
        double latestExpense = daily[daily.length - 1];
        double baselineScore = (baseline > 0) ? (latestExpense / baseline) : 0;

        // รวมคะแนนโดยใช้น้ำหนักที่กำหนด (Weighted Sum)
        double stress = (shoppingRatio * 30) + (frequencyScore * 15) + 
                        (nightScore * 15) + (volatilityScore * 20) + (baselineScore * 20);

        if(stress > 100){ stress = 100; }

        System.out.println("\n===== Stress Analysis =====");
        System.out.println("Stress Score: " + stress);

        // ตัดสินสถานะความเครียด
        if(stress <= 25){
            System.out.println("Stress Level: LOW\nRecommendation: Financial behavior is stable");
        } else if(stress <= 50){
            System.out.println("Stress Level: MODERATE\nRecommendation: Monitor spending habits");
        } else if(stress <= 75){
            System.out.println("Stress Level: HIGH\nRecommendation: Reduce non-essential spending");
        } else {
            System.out.println("Stress Level: CRITICAL\nRecommendation: High impulsive spending detected");
        }
    }

    public static double calculateBaseline(TransactionQueue queue){
        double[] daily = buildDailyExpenseArray(queue);
        if(daily.length == 0){ return 0; }
        double total = 0;
        for(int i = 0; i < daily.length; i++){ total += daily[i]; }
        return total / daily.length;
    }
}