import java.time.LocalDate;

// คลาสตัวแทนโหนดของโครงสร้าง Queue ทำหน้าที่เก็บข้อมูลรายวัน
public class DayNode {
     LocalDate date; // ระบุวันที่ของโหนดนี้
     DailyTransactionList transactions; // ห่อหุ้ม Linked List ของธุรกรรมไว้ภายในโหนดคิว
     DayNode next; // พอยน์เตอร์ชี้ไปยังวันถัดไปในคิว

     public DayNode(LocalDate date){
        this.date = date; 
        this.transactions = new DailyTransactionList(); // สร้างลิงก์ลิสต์ว่างรอรับข้อมูลทันที
        this.next = null; 
     }
}