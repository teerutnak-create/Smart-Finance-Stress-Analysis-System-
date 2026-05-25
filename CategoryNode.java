// คลาสทำหน้าที่เป็น Node สำหรับโครงสร้าง N-ary Tree
public class CategoryNode {

    String categoryName;
    DailyTransactionList transactions; 
    CategoryNode[] children; // เก็บกิ่งย่อย (Branch) แทนการใช้ left/right แบบ Binary Tree

    public CategoryNode(String categoryName){
        this.categoryName = categoryName;
        // สร้าง Linked List ประจำกิ่งนี้เพื่อเก็บธุรกรรม
        this.transactions = new DailyTransactionList();
        // จองพื้นที่ Array ขนาด 6 ช่อง เผื่อไว้ใช้ index 1-5 ให้ตรงกับหมวดหมู่
        this.children = new CategoryNode[6]; 
    }
}