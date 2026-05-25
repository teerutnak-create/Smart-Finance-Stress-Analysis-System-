// คลาสทำหน้าที่เป็นโหนด (Node) พื้นฐานของโครงสร้าง Singly Linked List
public class TransactionNode {
    Transaction data; // เก็บข้อมูล Payload ออบเจกต์ Transaction
    TransactionNode next; // พอยน์เตอร์ Edge สำหรับชี้ไปยังโหนดถัดไป (เริ่มแรกจะเป็น null)

    public TransactionNode(Transaction data){ 
        this.data = data; 
        this.next = null; 
    }
}