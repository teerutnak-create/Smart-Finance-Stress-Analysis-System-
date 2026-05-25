// คลาสโครงสร้างข้อมูลแบบ Singly Linked List สำหรับจัดการธุรกรรมย่อยในแต่ละวัน
public class DailyTransactionList {
    TransactionNode head; // ตัวชี้โหนดแรกของลิสต์

    // เมธอดเพิ่มโหนดใหม่ต่อท้ายลิสต์ (Append)
    public void addTransaction(Transaction transaction){
        TransactionNode newNode = new TransactionNode(transaction); 
        
        // กรณีลิสต์ว่าง ให้โหนดใหม่เป็น Head ทันที
        if(head == null){
            head = newNode; 
            return;
        }

        // กรณีมีข้อมูล ให้ Traverse หาโหนดสุดท้าย (Tail)
        TransactionNode current = head; 
        while(current.next != null){ 
            current = current.next; 
        }
        
        // เชื่อมโหนดใหม่เข้าที่ส่วนท้าย
        current.next = newNode; 
    }

    // เมธอดสำหรับพิมพ์ข้อมูลทั้งหมดในลิสต์
    public void printTransactions(){
        TransactionNode current = head; 
        
        while(current != null){ 
            Transaction t = current.data; 
            System.out.println("ID: " + t.id);
            System.out.println("Type: " + t.getTypeName());

            if(t.type == 2){
                System.out.println("Category: " + t.getCategoryName());
            }

            System.out.println("Amount: " + t.amount);
            System.out.println("Time: " + t.datetime);
            System.out.println("----------------");
            
            // ขยับพอยน์เตอร์ไปโหนดถัดไป
            current = current.next; 
        }
    }

    // เมธอดค้นหาโหนดด้วย ID (Linear Search)
    public TransactionNode findById(int id){
        TransactionNode current = head;

        while(current != null){
            if(current.data.id == id){
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // เมธอดลบโหนดโดยอ้างอิงจาก ID
    public void removeById(int id){
        if(head == null){
            return;
        }

        // กรณีที่ 1: โหนดที่ต้องการลบคือ Head
        if(head.data.id == id){
            head = head.next; // เลื่อน Head ไปโหนดถัดไป (โหนดเดิมจะหลุดจากลิสต์)
            return;
        }

        // กรณีที่ 2: โหนดที่ต้องการลบอยู่ตรงกลางหรือท้ายลิสต์
        TransactionNode current = head;
        while(current.next != null){
            // ตรวจสอบข้อมูลในโหนดล่วงหน้า 1 สเต็ป (Look ahead)
            if(current.next.data.id == id){
                // บายพาส (Bypass) พอยน์เตอร์ข้ามโหนดเป้าหมาย เพื่อลบออกจากโครงสร้าง
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }
}