// คลาสจัดหมวดหมู่ข้อมูลด้วยโครงสร้าง N-ary Tree เพื่อเพิ่มประสิทธิภาพในการค้นหากลุ่มข้อมูล
public class CategoryTree {

    CategoryNode root;

    public CategoryTree() {
        // 1. สร้าง Root Node ระดับบนสุด
        root = new CategoryNode("Category");

        // 2. แตกกิ่งย่อยตายตัว 5 กิ่ง (ใช้ดัชนี 1-5 ตามตัวเลขประเภทธุรกรรมเพื่อให้ Direct Access ได้)
        root.children[1] = new CategoryNode("Food");
        root.children[2] = new CategoryNode("Travel");
        root.children[3] = new CategoryNode("Shopping");
        root.children[4] = new CategoryNode("Entertainment");
        root.children[5] = new CategoryNode("Other");
    }

    // เมธอดนี้ถูกเว้นว่างไว้ เนื่องจากระบบเราออกแบบให้มี 5 หมวดหมู่คงที่ตั้งแต่เริ่มต้น
    public void insertCategory(String name){
    }

    // เมธอดเพิ่มธุรกรรมลงในกิ่งที่ถูกต้อง
    public void addTransaction(String category, Transaction t){
        int index = -1;
        
        // แปลงชื่อหมวดหมู่เป็น Index
        switch(category){
            case "Food": index = 1; break;
            case "Travel": index = 2; break;
            case "Shopping": index = 3; break;
            case "Entertainment": index = 4; break;
            case "Other": index = 5; break;
        }

        // หากพบ Index นำ Transaction ไปต่อท้าย Linked List ของกิ่งนั้นทันที (O(1) Branch Access)
        if(index != -1){
            root.children[index].transactions.addTransaction(t);
        }
    }
}