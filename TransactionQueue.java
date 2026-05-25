import java.time.LocalDate;

// คลาสโครงสร้างข้อมูล Queue เพื่อควบคุมไทม์ไลน์ข้อมูลย้อนหลัง 10 วัน (Sliding Window)
public class TransactionQueue { 

    DayNode front; // ชี้หัวคิว (วันเก่าสุด)
    DayNode rear;  // ชี้ท้ายคิว (วันล่าสุด)

    int size; // ตัวแปรนับจำนวนวันในคิว
    CategoryTree categoryTree = new CategoryTree(); // ต้นไม้สำหรับจัดหมวดหมู่คู่ขนานกับคิว

    // เมธอดนำโหนดวันใหม่เข้าสู่คิว (Enqueue)
    public void enqueueDay(DayNode newDay){
        // กรณีคิวว่าง (Empty Queue)
        if(front == null){ 
            front = newDay;
            rear = newDay;
        }else { 
            // นำวันใหม่ไปต่อท้ายคิวแล้วอัปเดตตัวชี้ Rear
            rear.next = newDay; 
            rear = newDay; 
        }
        size++;

        // บังคับขนาดคิวให้ไม่เกิน 10 วันล่าสุด (FIFO Rule)
        if(size > 10) {
            dequeueOldestDay(); 
        }
    }

    // เมธอดเพิ่มธุรกรรมเข้าสู่ระบบ ควบคุมการแจกจ่ายข้อมูล
    public void addTransaction(Transaction transaction){
        LocalDate transactionDate = transaction.datetime.toLocalDate(); 

        // 1. Traverse หาว่าในคิวมีโหนดของ "วันนี้" อยู่แล้วหรือไม่
        DayNode current = front; 
        while(current != null){ 
            if(current.date.equals(transactionDate)){ 
                // หากพบ ให้แทรกธุรกรรมลงใน Linked List ของวันนั้น
                current.transactions.addTransaction(transaction);
                // ขนานกัน ให้นำข้อมูลส่งเข้าไปจัดเก็บในกิ่งของ Category Tree ด้วย
                categoryTree.addTransaction(transaction.getCategoryName(), transaction);
                return;
            }
            current = current.next;
        } 

        // 2. หากยังไม่มีโหนดของวันนี้ ให้สร้าง DayNode ใหม่
        DayNode newDay = new DayNode(transactionDate); 
        newDay.transactions.addTransaction(transaction); 
        
        // 3. นำวันใหม่เข้าสู่โครงสร้างคิว
        enqueueDay(newDay); 
    }
    
    // เมธอดแสดงข้อมูลทั้งหมดในคิว
    public void printQueue() {
        DayNode currentDay = front; 
        while(currentDay != null){ 
            System.out.println("==== " + currentDay.date + " =====");
            currentDay.transactions.printTransactions(); 
            System.out.println();
            currentDay = currentDay.next;
        }
   }

   // เมธอดนำโหนดวันเก่าสุดออกจากหัวคิว (Dequeue)
   public void dequeueOldestDay(){
        if(front == null){
            return;
        }
        
        // ตัดโหนดหน้าสุดออกโดยเลื่อน Front ไปโหนดถัดไป
        front = front.next; 
        size--; 

        // ดักจับกรณีคิวว่างหลังจากลบ
        if(front == null){
            rear = null; 
        }
   }

   // เมธอดทำสำเนาโครงสร้างข้อมูลทั้งหมด (Deep Copy) เพื่อใช้สร้างระบบจำลอง (Simulation)
   public TransactionQueue cloneQueue(){
        TransactionQueue copy = new TransactionQueue();
        DayNode currentDay = front;

        // วนลูป 2 ชั้น เพื่อคัดลอกตั้งแต่วันยันธุรกรรม (หลีกเลี่ยง Reference Copy)
        while(currentDay != null){
            TransactionNode currentTransaction = currentDay.transactions.head;

            while(currentTransaction != null){
                Transaction old = currentTransaction.data;
                // สร้าง Object ใหม่ทั้งหมดให้ข้อมูลขาดจากกัน
                Transaction t = new Transaction(old.id, old.type, old.category, old.amount);
                t.datetime = old.datetime; // Copy Timestamp จริง
                
                copy.addTransaction(t);
                currentTransaction = currentTransaction.next;
            }
            currentDay = currentDay.next;
        }
        return copy;
    }

    // เมธอดลบข้อมูลธุรกรรมแบบซับซ้อน (Complex Deletion)
    public void removeTransactionById(int id){
        // 1. Traverse ค้นหาและลบออกจาก Linked List รายวัน
        DayNode currentDay = front;
        while(currentDay != null){
            currentDay.transactions.removeById(id);
            currentDay = currentDay.next;
        }

        // 2. ค้นหาและลบออกจากกิ่งโครงสร้างต้นไม้คู่ขนาน เพื่อให้ Data สอดคล้องกัน
        if (categoryTree != null && categoryTree.root != null) {
            for (int i = 1; i <= 5; i++) {
                CategoryNode branch = categoryTree.root.children[i];
                if (branch != null) {
                    branch.transactions.removeById(id); 
                }
            }
        }
    }
}