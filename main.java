import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.Random;

// คลาสหลัก ควบคุม UI และการทำงานของระบบ
public class main {
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        TransactionQueue queue = new TransactionQueue();
        
        // โหลดข้อมูลจำลองสำหรับทดสอบระบบ
        loadDummyData(queue);

        double balance = 25000;
        int choice;

        // ลูปควบคุมเมนูการทำงานหลัก (Console UI)
        do{
            System.out.println("\n==========================");
            System.out.println("      FINANCE SYSTEM");
            System.out.println("==========================");
            System.out.println("Balance: " + balance);
            System.out.println("\n1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Transactions");
            System.out.println("4. Analysis");
            System.out.println("5. What-if Simulation");
            System.out.println("0. Exit");
            System.out.println("==========================");
            System.out.print("Select: ");

            choice = input.nextInt();

            if (choice == 1){
                // รับค่าฝากเงิน
                System.out.print("Enter amount: ");
                double amount = input.nextDouble();
                Transaction t = new Transaction(1,0,amount);
                queue.addTransaction(t);
                balance += amount;
                System.out.println("Deposit Success");
            }
            else if (choice == 2){
                // รับค่าถอนเงินและหมวดหมู่
                System.out.println("1.Food");
                System.out.println("2.Travel");
                System.out.println("3.Shopping");
                System.out.println("4.Entertainment");
                System.out.println("5.Other");
                System.out.print("Select category: ");
                int category = input.nextInt();
                
                System.out.print("Enter amount: ");
                double amount = input.nextDouble();

                // ป้องกันการถอนเงินเกินยอดคงเหลือ
                if(amount > balance){
                        System.out.println("Insufficient Balance");
                }else {
                        Transaction t = new Transaction(2,category,amount);
                        queue.addTransaction(t);
                        balance -= amount;
                        System.out.println("Withdrawn Success");
                }

            }else if (choice == 3){
                queue.printQueue();
                
            }else if (choice == 4){
                // เมนูย่อยสำหรับการวิเคราะห์
                int analysisChoice;
                System.out.println("\n===== ANALYSIS =====");
                System.out.println("1. Income vs Expense");
                System.out.println("2. Category Percentage");
                System.out.println("3. Spending Frequency");
                System.out.println("4. Derivative Analysis");
                System.out.println("5. Stress Score");
                System.out.print("Select: ");

                analysisChoice = input.nextInt();

                if (analysisChoice == 1) {
                    Analysis.incomeVsExpense(queue);
                } else if (analysisChoice == 2) {
                    Analysis.categoryPercentage(queue);
                } else if (analysisChoice == 3) {
                    Analysis.spendingFrequency(queue);
                } else if (analysisChoice == 4) {
                    Analysis.derivativeAnalysis(queue);
                }else if (analysisChoice == 5){
                    Analysis.stressScore(queue);
                }
            }
            else if (choice == 5){
                // โหมดจำลองสถานการณ์ (What-if Simulation)
                queue.printQueue();
                System.out.print("\nEnter Transaction ID to remove: ");
                int removeId = input.nextInt();

                // ทำ Deep Copy ข้อมูลเพื่อไม่ให้กระทบข้อมูลจริง
                TransactionQueue simulation = queue.cloneQueue();

                // ลบโหนดธุรกรรมออกจากระบบจำลอง
                simulation.removeTransactionById(removeId);

                System.out.println("\n===== BEFORE =====");
                Analysis.stressScore(queue);

                System.out.println("\n===== AFTER =====");
                Analysis.stressScore(simulation);
            }
        }while(choice != 0);

    }

    // ฟังก์ชันสำหรับสร้าง Mock Data เพื่อใช้ทดสอบ Algorithm
    public static void loadDummyData(TransactionQueue queue) {
        Random random = new Random();

        // 1. สร้างฐานรายรับ (Baseline Income)
        Transaction deposit = new Transaction(1, 0, 50000);
        deposit.datetime = LocalDateTime.now().minusDays(10);
        queue.addTransaction(deposit);

        // 2. สุ่มสร้างพฤติกรรมใช้งานทั่วไป 25 รายการ ให้อยู่ในช่วงเวลาทำการปกติ
        for (int i = 0; i < 25; i++) {
            int category = random.nextInt(2) + 1; 
            double amount = 100 + random.nextInt(200); 
            Transaction t = new Transaction(2, category, amount);
            
            int randomDay = random.nextInt(10);
            int randomHour = 8 + random.nextInt(10); // 08:00 - 17:00
            
            t.datetime = LocalDateTime.now()
                    .minusDays(randomDay)
                    .withHour(randomHour)
                    .withMinute(random.nextInt(60));
            queue.addTransaction(t);
        }

        // 3. ฝังธุรกรรมเป้าหมาย (Bad Habit) เพื่อใช้ทดสอบฟีเจอร์ What-if Simulation
        // เป็นยอดใช้จ่ายก้อนใหญ่ ตอนตี 2 ซึ่งจะทำให้ Stress Score ของระบบสูงขึ้นทันที
        Transaction badHabit = new Transaction(2, 3, 18500); 
        badHabit.datetime = LocalDateTime.now().withHour(2).withMinute(30);
        badHabit.id = 999; // Fix ID ให้เรียกทดสอบง่ายๆ
        
        queue.addTransaction(badHabit);
    }
}